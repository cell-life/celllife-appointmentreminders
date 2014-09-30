package org.celllife.appointmentreminders.integration;

import org.celllife.appointmentreminders.test.TestConfiguration;
import org.celllife.mobilisr.client.exception.RestCommandException;
import org.celllife.mobilisr.constants.SmsStatus;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class CommunicateServiceTest extends TestConfiguration {

    @Autowired
    CommunicateService communicateService;

    @Ignore
    @Test
    public void testGetMessageLogStatus() throws RestCommandException {
        SmsStatus smsStatus = communicateService.getMessageLogStatus(1L);
        System.out.println();
    }


}
