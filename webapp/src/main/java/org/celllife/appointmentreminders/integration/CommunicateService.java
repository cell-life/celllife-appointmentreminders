package org.celllife.appointmentreminders.integration;

import org.celllife.appointmentreminders.domain.message.Message;
import org.celllife.mobilisr.client.exception.RestCommandException;
import org.celllife.mobilisr.constants.SmsStatus;

public interface CommunicateService {

    public Long sendOneSms(Message message) throws RestCommandException;

    public SmsStatus getMessageLogStatus(Long id) throws RestCommandException;

}
