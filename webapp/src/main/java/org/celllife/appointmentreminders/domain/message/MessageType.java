package org.celllife.appointmentreminders.domain.message;

/**
 * An Enumeration of the Message types.
 * 
 * REMINDER is a message containing details of an upcoming Appointment
 * MISSED is a message regarding an unattended Appointment in the recent past.
 */
public enum MessageType {

    REMINDER,
    MISSED

}

