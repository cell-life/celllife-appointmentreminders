package org.celllife.appointmentreminders.domain.exception;

/**
 * Exception thrown when creating a Clinic and the specific clinic code is already
 * used by another clinic
 */
public class ClinicCodeExistsException extends AppointmentRemindersException {

    private static final long serialVersionUID = 4494807335924752350L;

    public ClinicCodeExistsException(String message) {
        super(message);
    }
}
