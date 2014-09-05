package org.celllife.appointmentreminders.application.jobs;

import org.celllife.appointmentreminders.application.message.MessageService;
import org.celllife.appointmentreminders.domain.exception.InvalidDateException;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.domain.message.Message;
import org.celllife.appointmentreminders.domain.message.MessageState;
import org.celllife.appointmentreminders.framework.util.DateUtil;
import org.celllife.appointmentreminders.integration.CommunicateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("backgroundServices")
public class BackgroundServiceImpl implements BackgroundService {

    private static Logger log = LoggerFactory.getLogger(BackgroundServiceImpl.class);

    @Autowired
    MessageService messageService;

    @Autowired
    private CommunicateService communicateService;

    @Value("${latest_message_time}")
    private String latestMessageTime;

    @Override
    public void retryFailedMessages() {

        log.debug("Checking for failed messages to retry.");

        List<Message> messagesToRetry = messageService.findByMessageStateAndMessageDate(MessageState.FAILED, new Date());

        // if it is too late to retry the message, return
        if (isTooLate())
            return;

        for (Message message : messagesToRetry) {

            log.debug("Retrying message with id " + message.getId() + ". Number of previous attempts: " + message.getRetryAttempts());

            try {
                Long returnedId = communicateService.sendOneSms(message);
                message.setCommunicateId(returnedId);
                message.setMessageState(MessageState.SENT);
                message.setMessageSent(new Date());
            } catch (Exception e) {
                message.setMessageState(MessageState.FAILED);
                log.warn("Could not send message with id " + message.getId() + ". Reason: " + e.getLocalizedMessage());
            }
            finally {
                message.setRetryAttempts(message.getRetryAttempts() + 1);
                try {
                    messageService.save(message);
                } catch (RequiredFieldIsNullException e) {
                    log.warn("Could save message with id " + message.getId() + ". Reason: " + e.getMessage());
                }
            }

        }

    }

    private boolean isTooLate() {

        Date dateLatestTime = null;
        try {
            dateLatestTime = DateUtil.getTimeFromString(latestMessageTime);
        } catch (InvalidDateException e) {
            log.warn("Could not convert latestMessageTime " + dateLatestTime.getTime() + " to a time.");
            return false;
        }

        Calendar now = Calendar.getInstance();

        Calendar latestTime = Calendar.getInstance();
        latestTime.setTime(dateLatestTime);
        latestTime.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));

        if (now.getTime().after(latestTime.getTime()))
            return true;
        else
            return false;

    }

}
