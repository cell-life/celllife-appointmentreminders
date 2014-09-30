package org.celllife.appointmentreminders.application.jobs;

public interface BackgroundService {

    /**
     * This function will retry all failed messages from the current day.
     */
    void retryFailedMessages();

    /**
     * This function will poll Communicate and update the messages statuses accordingly.
     */
    void updateMessageStatuses();

}
