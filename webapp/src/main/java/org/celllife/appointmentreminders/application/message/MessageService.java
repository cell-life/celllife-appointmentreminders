package org.celllife.appointmentreminders.application.message;

import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.domain.message.Message;
import org.celllife.appointmentreminders.domain.message.MessageState;

import java.util.Date;
import java.util.List;

public interface MessageService {

    /**
     * Saves a new or existing message.
     * @param message
     * @return
     * @throws RequiredFieldIsNullException If a required property is null.
     */
    Message save(Message message) throws RequiredFieldIsNullException;

    /**
     * Gets a message by id.
     * @param id
     * @return
     */
    Message getMessage(Long id);

    /**
     * Returns true if a message with this date and time already exists for this appointment.
     * @param appointmentId
     * @param messageDate
     * @param messageTime
     * @return
     */
    boolean messageExists(Long appointmentId, Date messageDate, Date messageTime);

    /**
     * Finds message by appointment, date and time.
     * @param appointmentId
     * @param messageDate
     * @param messageTime
     * @return
     */
    List<Message> findByAppointmentIdAndDateTimeStamp(Long appointmentId, Date messageDate, Date messageTime);

    /**
     * Finds messages by message state and message date.
     * @param messageState
     * @param messageDate
     * @return
     */
    List<Message> findByMessageStateAndMessageDate(MessageState messageState, Date messageDate);

    /**
     * Finds messages by message state.
     * @param messageState
     * @return
     */
    List<Message> findByMessageState(MessageState messageState);

    /**
     * Deletes all messages.
     */
    void deleteAll();
}
