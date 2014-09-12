package org.celllife.appointmentreminders.domain.exception;

/**
 * Exception thrown when an invalid date is encountered
 */
public class InvalidDateException extends AppointmentRemindersException {

    private static final long serialVersionUID = 2285741467477584510L;

    public InvalidDateException(String message) {
        super(message);
    }
}
