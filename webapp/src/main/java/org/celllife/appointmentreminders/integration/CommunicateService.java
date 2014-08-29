package org.celllife.appointmentreminders.integration;

import org.celllife.appointmentreminders.domain.message.Message;
import org.celllife.mobilisr.client.exception.RestCommandException;

public interface CommunicateService {

    public Long sendOneSms(Message message) throws RestCommandException;

}
