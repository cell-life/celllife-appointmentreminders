package org.celllife.appointmentreminders.application.quartz;

import org.celllife.appointmentreminders.application.jobs.FixedCampaignJobRunner;
import org.celllife.appointmentreminders.domain.exception.AppointmentRemindersException;
import org.celllife.appointmentreminders.domain.message.Message;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.Calendar;

@Service
public class QuartzServiceImpl implements QuartzService {

    private static final String DAILY_CRONEXPR = "{0} {1} {2} ? * *";

    private static Logger log = LoggerFactory.getLogger(QuartzServiceImpl.class);

    @Autowired
    @Qualifier("qrtzScheduler")
    private Scheduler scheduler;

    public String generateCronExprForDailyOccurence(Date msgDateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(msgDateTime);

        String cronExpr = MessageFormat.format(DAILY_CRONEXPR, new Object[]{
                calendar.get(Calendar.SECOND), calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.HOUR_OF_DAY)});

        return cronExpr;
    }

    @Override
    public void clearTriggersForGroup(String triggerGroup) {

        try {
            List<String> triggers = getTriggerNames(triggerGroup);
            for (String trigger : triggers) {
                TriggerKey triggerKey = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(trigger)).iterator().next();
                boolean unscheduleJob = scheduler.unscheduleJob(triggerKey);
                if (unscheduleJob) {
                    log.debug("Trigger : [{}] deleted for group : [{}]", trigger, triggerGroup);
                } else {
                    log.error("Trigger : [{}] NOT deleted for group : [{}]", trigger, triggerGroup);
                }
            }
        } catch (SchedulerException e) {
            log.error("Error clearing triggers for group " + triggerGroup, e);
        }

    }

    @Override
    public List<String> getTriggerNames(String triggerGroup) {

        ArrayList<String> list = new ArrayList<String>();

        try {
            Set<TriggerKey> triggers = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroup));
            for (TriggerKey triggerKey : triggers) {
                list.add(triggerKey.getName());
            }
            return list;
        } catch (SchedulerException e) {
            log.error("Error checking triggers [group=" + triggerGroup + "]", e);
        }
        return list;
    }

    @Override
    public List<Trigger> getTriggers(String triggerGroup) {

        ArrayList<Trigger> list = new ArrayList<>();

        try {
            Set<TriggerKey> triggers = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroup));
            for (TriggerKey triggerKey : triggers) {
                list.add(scheduler.getTrigger(triggerKey));
            }
            return list;
        } catch (SchedulerException e) {
            log.error("Error checking triggers [group=" + triggerGroup + "]", e);
        }
        return list;
    }

    private boolean doesTriggerExist(String triggerGroup, String triggerName) {

        try {
            TriggerKey triggerKey = findByTriggerNameAndTriggerGroup(triggerName, triggerGroup);
            if (triggerKey != null)
                return true;
            else
                return false;
        } catch (SchedulerException e) {
            log.warn("Could not find trigger " + triggerName + " in group " + triggerGroup, e);
            return false;
        }
    }

    @Override
    public void removeTrigger(String triggerName, String triggerGroup) throws SchedulerException {
        TriggerKey triggerKey = findByTriggerNameAndTriggerGroup(triggerName, triggerGroup);
        if (triggerKey != null) {
            scheduler.unscheduleJob(triggerKey);
        } else {
            log.warn("Could not remove trigger " + triggerName + " in group " + triggerGroup + " because it was not found.");
        }
    }

    @Override
    public void addTrigger(Trigger trigger) throws SchedulerException {
        scheduler.scheduleJob(trigger);

    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

    protected TriggerKey findByTriggerNameAndTriggerGroup(String triggerName, String triggerGroup) throws SchedulerException {
        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroup));
        for (TriggerKey triggerKey : triggerKeys) {
            if (triggerKey.getName().equals(triggerName)) {
                return triggerKey;
            }
        }
        return null;
    }

    public List<CronTrigger> findTriggerByJobNameAndJobGroup(String jobName, String jobGroup) throws SchedulerException {

        Set<JobKey> jobkeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(jobGroup));
        for (JobKey jobKey : jobkeys) {
            if (jobKey.getName().equals(jobName)) {
                return (List<CronTrigger>) getScheduler().getTriggersOfJob(jobKey);
            }
        }

        return null;

    }

    @Override
    public void scheduleOrUpdateMessageJob(Message message) throws AppointmentRemindersException, SchedulerException {

        Date dateToSend = message.getMessageDateTime();

        String triggerGroup = message.getIdentifierString();
        String name = MessageFormat.format("message{0}-[datetime={1,date,medium}]", message.getId(), dateToSend);

        // Unschedule current triggers for this message
        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroup));
        for (TriggerKey triggerKey : triggerKeys) {
            scheduler.unscheduleJob(triggerKey);
        }

        // Create new trigger for this message
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(FixedCampaignJobRunner.PROP_CAMPAIGN_ID, message.getId());

        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity(name, message.getIdentifierString())
                .startAt(dateToSend)
                .forJob("fixedCampaignJobRunner", "campaignJobs")
                .usingJobData(jobDataMap)
                .build();

        // Schedule new trigger for this message
        try {
            Date scheduledDate = scheduler.scheduleJob(trigger);
            log.debug("Message: {} scheduled to run at {}", message.getId(), scheduledDate);
        }
        catch (SchedulerException e) {
            throw new AppointmentRemindersException("Error scheduling campaign. Cause: " + e.getMessage());
        }
    }

}
