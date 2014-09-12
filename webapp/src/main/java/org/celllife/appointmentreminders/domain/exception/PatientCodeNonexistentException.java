package org.celllife.appointmentreminders.domain.exception;

/**
 * Thrown when a Patient does not exist (i.e. a patient code is given, but there is no patient
 * with the specified code for the specified clinic).
 */
public class PatientCodeNonexistentException extends AppointmentRemindersException {

    private static final long serialVersionUID = 6815005712645522435L;

    public PatientCodeNonexistentException(String message) {
        super(message);
    }

}
