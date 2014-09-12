package org.celllife.appointmentreminders.domain.exception;

/**
 * Parent class for all exceptions thrown by the AppointmentReminder service 
 */
public class AppointmentRemindersException extends Exception {

    private static final long serialVersionUID = 6467927276803324097L;

    public AppointmentRemindersException(String message) {
        super(message);
    }

}
