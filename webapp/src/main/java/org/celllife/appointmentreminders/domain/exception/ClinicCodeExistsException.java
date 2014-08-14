package org.celllife.appointmentreminders.domain.exception;

public class ClinicCodeExistsException extends AppointmentRemindersException {

    public ClinicCodeExistsException(String message) {
        super(message);
    }
}
