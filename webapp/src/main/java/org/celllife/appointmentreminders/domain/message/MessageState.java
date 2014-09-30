package org.celllife.appointmentreminders.domain.message;

/**
 * An Enumeration of the various Message states.
 *
 * NEW is when the message has just been created and is not yet scheduled.
 * SCHEDULED is when the message is in a queue to be sent
 * UNSCHEDULED is when the message has not been sent but is not in a queue
 * SENT_TO_COMMUNICATE is went the message has been successfully sent to Communicate
 * FAILED_SENDING_TO_COMMUNICATE is when there was an error sending the message to Communicate, and the message is being retried
 * COMMUNICATE_DELIVERED is when Communicate has successfully delivered the SMS to the patient
 */
public enum MessageState {

    /* NEW is when the message has just been created and is not yet scheduled. */
    NEW,

    /*  SCHEDULED is when the message is in a queue to be sent. */
    SCHEDULED,

    /* UNSCHEDULED is when the message has not been sent but is not in a queue */
    UNSCHEDULED,

    /* SENT_TO_COMMUNICATE is went the message has been successfully sent to Communicate */
    SENT_TO_COMMUNICATE,

    /* FAILED_SENDING_TO_COMMUNICATE is when there was an error sending the message to Communicate, and the message is being retried. */
    FAILED_SENDING_TO_COMMUNICATE,

    /* COMMUNICATE_DELIVERED is when Communicate has successfully delivered the SMS to the patient */
    COMMUNICATE_DELIVERED,

    /* COMMUNICATE_FAILED is when Communicate failed permanently to deliver the SMS to the patient. */
    COMMUNICATE_FAILED

}
