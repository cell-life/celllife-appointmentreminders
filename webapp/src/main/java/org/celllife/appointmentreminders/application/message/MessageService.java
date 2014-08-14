package org.celllife.appointmentreminders.application.message;

import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.domain.message.Message;

public interface MessageService {

    /**
     * Saves a new or existing message.
     * @param message
     * @return
     * @throws RequiredFieldIsNullException If a required property is null.
     */
    Message save(Message message) throws RequiredFieldIsNullException;

}
