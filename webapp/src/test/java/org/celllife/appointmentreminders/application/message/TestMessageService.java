package org.celllife.appointmentreminders.application.message;

import junit.framework.Assert;
import org.celllife.appointmentreminders.application.quartz.QuartzService;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.domain.message.Message;
import org.celllife.appointmentreminders.domain.message.MessageType;
import org.celllife.appointmentreminders.test.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestMessageService extends TestConfiguration{

    @Autowired
    MessageService messageService;

    @Autowired
    QuartzService quartzService;

    @Test
    public void testSaveMessage() throws RequiredFieldIsNullException {

        Date hourLater = getHourLater();
        Date threeHoursLater = getThreeHoursLater();

        Message message = new Message();
        message.setAppointmentId(1L);
        message.setMessageDate(hourLater);
        message.setMessageTime(hourLater);
        message.setMessageText("Hello there!");
        message.setMessageType(MessageType.REMINDER);
        messageService.save(message);

        List<Trigger> triggers = quartzService.getTriggers(message.getIdentifierString());

        Assert.assertEquals(1,triggers.size());
        Assert.assertEquals(hourLater,triggers.get(0).getNextFireTime());
        Assert.assertEquals(message.getId(), triggers.get(0).getJobDataMap().get("messageId") );

        message.setMessageText("Hello there again!");
        message.setMessageTime(threeHoursLater);
        messageService.save(message);

        triggers = quartzService.getTriggers(message.getIdentifierString());

        Assert.assertEquals(1,triggers.size());
        Assert.assertEquals(threeHoursLater, triggers.get(0).getNextFireTime());

    }

    private Date getHourLater() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,1);
        return calendar.getTime();
    }

    private Date getThreeHoursLater() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,3);
        return calendar.getTime();
    }
}
