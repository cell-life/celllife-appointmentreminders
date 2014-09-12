package org.celllife.appointmentreminders.domain.exception;

/**
 * Exception thrown when a required field has not been provided by the client.
 */
public class RequiredFieldIsNullException extends AppointmentRemindersException {

    private static final long serialVersionUID = -6542510128034867719L;

    public RequiredFieldIsNullException(String message) {
        super(message);
    }
}
