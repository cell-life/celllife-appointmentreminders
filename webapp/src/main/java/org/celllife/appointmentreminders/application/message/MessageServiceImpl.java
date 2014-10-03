package org.celllife.appointmentreminders.application.message;

import org.apache.commons.collections.IteratorUtils;
import org.celllife.appointmentreminders.application.quartz.QuartzService;
import org.celllife.appointmentreminders.domain.exception.AppointmentRemindersException;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.domain.message.Message;
import org.celllife.appointmentreminders.domain.message.MessageRepository;
import org.celllife.appointmentreminders.domain.message.MessageState;
import org.celllife.appointmentreminders.framework.util.DateUtil;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
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
        if ((message.getAppointmentId() == null) || (message.getMessageDate() == null) || (message.getMessageTime() == null) || (message.getMessageText() == null) || (message.getMessageText().length() < 1)) {
            throw new RequiredFieldIsNullException("AppointmentId, MessageDate and MessageTime and Message Text must all be not null.");
        }
        // if a message with this date and time exists for this appointment, simply update it
        else if ((message.getId() == null) && messageExists(message.getAppointmentId(), message.getMessageDate(), message.getMessageTime())) {
            Message oldMessage = findByAppointmentIdAndDateTimeStamp(message.getAppointmentId(), message.getMessageDate(), message.getMessageTime()).get(0);
            oldMessage.setMessageText(message.getMessageText());
            oldMessage.setMessageType(message.getMessageType());
            message = messageRepository.save(oldMessage);
        }
        // if this is a new message, but the time is in the past, then send it in five minutes' time
        else if ((message.getId() == null) && (message.getMessageDateTime().before(new Date())) && (DateUtil.isToday(message.getMessageDate()))) {
            log.debug("Message time " + message.getMessageDateTime().toString() + " has already passed. Will send message five minutes from now.");
            message.setMessageDate(fiveMinutesLater());
            message.setMessageTime(fiveMinutesLater());
        }

        message = messageRepository.save(message);

        if (message.getMessageDateTime().after(new Date()) ) {
            try {
                quartzService.scheduleOrUpdateMessageJob(message);
                message.setMessageState(MessageState.SCHEDULED);
                message = messageRepository.save(message);
            } catch (SchedulerException | AppointmentRemindersException e) {
                log.error("Could not schedule message with id " + message.getId() + ". Reason: " + e.getMessage(), e);
            }

        }

        return message;

    }

    @Override
    public List<Message> getAllMessages() {
        return IteratorUtils.toList(messageRepository.findAll().iterator());
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

    private Date fiveMinutesLater() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,5);
        return calendar.getTime();
    }

}
