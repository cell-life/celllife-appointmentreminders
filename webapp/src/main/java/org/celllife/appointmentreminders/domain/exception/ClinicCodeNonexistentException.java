package org.celllife.appointmentreminders.domain.exception;

/**
 * Thrown when a Clinic does not exist (i.e. a clinic code is given, but there is no clinic
 * with the associated code).
 */
public class ClinicCodeNonexistentException extends AppointmentRemindersException {

    private static final long serialVersionUID = -1466398498394412995L;

    public ClinicCodeNonexistentException(String message) {
        super(message);
    }

}
