package org.celllife.appointmentreminders.domain.message;

/**
 * An Enumeration of the various Message states.
 * 
 * SCHEDULED is when the message is in a queue to be sent
 * UNSCHEDULED is when the message has not been sent but is not in a queue
 * SENT is went the message has been successfully sent
 * FAILED is when there was an error sending the message and the message is being retried. 
 */
public enum MessageState {

    SCHEDULED,
    UNSCHEDULED,
    SENT,
    FAILED

}
