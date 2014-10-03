package org.celllife.appointmentreminders.application.quartz;

import org.celllife.appointmentreminders.domain.exception.AppointmentRemindersException;
import org.celllife.appointmentreminders.domain.message.Message;
import org.quartz.*;

import java.util.Date;
import java.util.List;

public interface QuartzService {

    /**
     * Clears all the quartz triggers in a group;
     * @param triggerGroup Name of the quartz trigger group.
     */
    void clearTriggersForGroup(String triggerGroup);

    /**
     * Gets all the quartz trigger names in a group.
     * @param triggerGroup Name of the quartz trigger group.
     * @return
     */
    List<String> getTriggerNames(String triggerGroup);

    /**
     * Gets all triggers in a group.
     * @param triggerGroup
     * @return
     */
    List<Trigger> getTriggers(String triggerGroup);

    /**
     * Get the scheduler for the quartz service.
     * @return Scheduler for the quartz service.
     */
    Scheduler getScheduler();

    /**
     * Find the quartz trigger by job name and group name
     * @param jobName
     * @param jobGroup
     * @return
     * @throws SchedulerException
     */
    List<CronTrigger> findTriggerByJobNameAndJobGroup(String jobName, String jobGroup) throws SchedulerException;

    /**
     * Schedule or updates the job for this message. At the end there will only be one trigger for each message.
     * @param message
     * @throws AppointmentRemindersException
     * @throws SchedulerException
     */
    void scheduleOrUpdateMessageJob(Message message) throws AppointmentRemindersException, SchedulerException;

}
