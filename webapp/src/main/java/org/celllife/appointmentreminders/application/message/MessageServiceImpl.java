package org.celllife.appointmentreminders.application.message;

import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.domain.message.Message;
import org.celllife.appointmentreminders.domain.message.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Override
    public Message save(Message message) throws RequiredFieldIsNullException {

        if ((message.getAppointmentId() == null) || (message.getMessageDate() == null) || (message.getMessageTime() == null)) {
            throw new RequiredFieldIsNullException("AppointmentId, MessageDate and MessageTime must all be not null.");
        }
        return messageRepository.save(message);
    }

}
