package org.celllife.appointmentreminders.domain.exception;

/**
 * Exception thrown when trying to create a Patient with a patient code that already
 * exists. (Note that patient codes are only unique for a clinic.)
 */
public class PatientCodeExistsException extends AppointmentRemindersException {

    private static final long serialVersionUID = 2008554760172512071L;

    public PatientCodeExistsException(String message) {
        super(message);
    }
}
