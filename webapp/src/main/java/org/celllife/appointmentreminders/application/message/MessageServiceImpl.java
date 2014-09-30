package org.celllife.appointmentreminders.application.message;

import org.celllife.appointmentreminders.application.quartz.QuartzService;
import org.celllife.appointmentreminders.domain.exception.AppointmentRemindersException;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.domain.message.Message;
import org.celllife.appointmentreminders.domain.message.MessageRepository;
import org.celllife.appointmentreminders.domain.message.MessageState;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private static Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    QuartzService quartzService;

    @Override
    public Message save(Message message) throws RequiredFieldIsNullException {

        // if the required fields are null, throw an error
        if ((message.getAppointmentId() == null) || (message.getMessageDate() == null) || (message.getMessageTime() == null)) {
            throw new RequiredFieldIsNullException("AppointmentId, MessageDate and MessageTime must all be not null.");
        }

        // if a message with this date and time exists for this appointment, simply update it
        if ((message.getId() == null) && messageExists(message.getAppointmentId(), message.getMessageDate(), message.getMessageTime())) {
            Message oldMessage = findByAppointmentIdAndDateTimeStamp(message.getAppointmentId(), message.getMessageDate(), message.getMessageTime()).get(0);
            oldMessage.setMessageText(message.getMessageText());
            oldMessage.setMessageType(message.getMessageType());
        } 

        if (message.getMessageState() == null) {
            message.setMessageState(MessageState.SCHEDULED);
        }
        if (message.getMessageText() == null) {
            message.setMessageText("Empty Message");
        }

        message = messageRepository.save(message);

        if (message.getMessageDateTime().after(new Date())) {

            try {
                quartzService.scheduleOrUpdateMessageJob(message);
            } catch (SchedulerException | AppointmentRemindersException e) {
                log.error("Could not schedule message with id " + message.getId() + ". Reason: " + e.getMessage(), e);
            }

        }

        return message;

    }

    @Override
    public Message getMessage(Long messageId) {
        return messageRepository.findOne(messageId);
    }

    @Override
    public boolean messageExists(Long appointmentId, Date messageDate, Date messageTime) {
        return messageRepository.exists(appointmentId, messageDate, messageTime);
    }

    @Override
    public List<Message> findByAppointmentIdAndDateTimeStamp(Long appointmentId, Date messageDate, Date messageTime) {
        return messageRepository.findByAppointmentIdAndMessageDateAndMessageTime(appointmentId, messageDate, messageTime);
    }

    @Override
    public List<Message> findByMessageStateAndMessageDate(MessageState messageState, Date messageDate) {
        return messageRepository.findByMessageStateAndMessageDate(messageState, messageDate);
    }

    @Override
    public List<Message> findByMessageState(MessageState messageState) {
        return messageRepository.findByMessageState(messageState);
    }

    @Override
    public void deleteAll() {
        messageRepository.deleteAll();
    }

}
