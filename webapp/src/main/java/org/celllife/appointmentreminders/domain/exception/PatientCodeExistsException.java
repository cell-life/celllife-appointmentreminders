package org.celllife.appointmentreminders.domain.exception;

public class PatientCodeExistsException extends AppointmentRemindersException {

    public PatientCodeExistsException(String message) {
        super(message);
    }
}
