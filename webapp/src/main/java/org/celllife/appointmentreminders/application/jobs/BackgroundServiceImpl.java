package org.celllife.appointmentreminders.application.jobs;

import org.celllife.appointmentreminders.application.message.MessageService;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.domain.message.Message;
import org.celllife.appointmentreminders.domain.message.MessageState;
import org.celllife.appointmentreminders.integration.CommunicateService;
import org.celllife.mobilisr.client.exception.RestCommandException;
import org.celllife.mobilisr.constants.SmsStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("backgroundServices")
public class BackgroundServiceImpl implements BackgroundService {

    private static Logger log = LoggerFactory.getLogger(BackgroundServiceImpl.class);

    @Autowired
    MessageService messageService;

    @Autowired
    private CommunicateService communicateService;

    @Override
    public void retryFailedMessages() {

        log.debug("Checking for failed messages to retry.");

        List<Message> messagesToRetry = messageService.findByMessageStateAndMessageDate(MessageState.FAILED_SENDING_TO_COMMUNICATE, new Date());

        for (Message message : messagesToRetry) {

            log.debug("Retrying message with id " + message.getId() + ". Number of previous attempts: " + message.getRetryAttempts());

            try {
                Long returnedId = communicateService.sendOneSms(message);
                message.setCommunicateId(returnedId);
                message.setMessageState(MessageState.SENT_TO_COMMUNICATE);
                message.setMessageSent(new Date());
            } catch (RestCommandException e) {
                message.setMessageState(MessageState.FAILED_SENDING_TO_COMMUNICATE);
                log.warn("Could not send message with id " + message.getId() + ". Reason: " + e.getMessage(), e);
            }
            finally {
                message.increaseRetryCount();
                try {
                    messageService.save(message);
                } catch (RequiredFieldIsNullException e) {
                    log.warn("Could save message with id " + message.getId() + ". Reason: " + e.getMessage(), e);
                }
            }

        }

    }

    @Override
    public void updateMessageStatuses() {

        log.debug("Updating message statuses.");

        List<Message> messageList = messageService.findByMessageState(MessageState.SENT_TO_COMMUNICATE);

        for (Message message : messageList) {
            try {
                SmsStatus smsStatus = communicateService.getMessageLogStatus(message.getCommunicateId());
                if (smsStatus == SmsStatus.TX_FAIL) {
                    message.setMessageState(MessageState.COMMUNICATE_FAILED);
                } else if (smsStatus == SmsStatus.TX_SUCCESS) {
                    message.setMessageState(MessageState.COMMUNICATE_DELIVERED);
                } else {
                    log.debug("Message " + message.getId() + "'s status on Communicate is " + smsStatus);
                }
            } catch (RestCommandException e) {
                log.warn("Could not get message status from Communicate.",e);
            }

        }

    }

}
