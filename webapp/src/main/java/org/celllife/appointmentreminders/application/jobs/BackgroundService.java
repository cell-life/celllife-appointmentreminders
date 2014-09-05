package org.celllife.appointmentreminders.application.jobs;

public interface BackgroundService {

    /**
     * This function will retry all failed messages from the current day.
     */
    void retryFailedMessages();

}
