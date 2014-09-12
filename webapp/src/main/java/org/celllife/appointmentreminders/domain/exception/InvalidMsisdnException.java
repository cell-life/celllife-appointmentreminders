package org.celllife.appointmentreminders.domain.exception;

/**
 * Exception thrown when an invalid MSISDN (cellphone number) is encountered.
 */
public class InvalidMsisdnException extends AppointmentRemindersException {

    private static final long serialVersionUID = -1933701837596388045L;

    public InvalidMsisdnException(String message) {
        super(message);
    }
}
