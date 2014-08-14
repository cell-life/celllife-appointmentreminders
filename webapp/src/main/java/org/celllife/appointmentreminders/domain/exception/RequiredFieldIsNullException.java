package org.celllife.appointmentreminders.domain.exception;

public class RequiredFieldIsNullException extends AppointmentRemindersException {

    public RequiredFieldIsNullException(String message) {
        super(message);
    }
}
