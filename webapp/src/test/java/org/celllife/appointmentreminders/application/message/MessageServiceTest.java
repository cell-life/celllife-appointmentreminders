package org.celllife.appointmentreminders.application.message;

import junit.framework.Assert;
import org.celllife.appointmentreminders.application.quartz.QuartzService;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.domain.message.Message;
import org.celllife.appointmentreminders.domain.message.MessageState;
import org.celllife.appointmentreminders.domain.message.MessageType;
import org.celllife.appointmentreminders.test.TestConfiguration;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class MessageServiceTest extends TestConfiguration{

    @Autowired
    MessageService messageService;

    @Autowired
    QuartzService quartzService;

    @Before
    public void cleanup() {
        messageService.deleteAll();
    }

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
        Assert.assertEquals(message.getId(), triggers.get(0).getJobDataMap().get("messageId") );

        message.setMessageText("Hello there again!");
        message.setMessageTime(threeHoursLater);
        messageService.save(message);

        triggers = quartzService.getTriggers(message.getIdentifierString());

        Assert.assertEquals(1,triggers.size());
        Assert.assertEquals(message.getId(), triggers.get(0).getJobDataMap().get("messageId") );

    }

    @Test
    public void testFindByMessageState() throws RequiredFieldIsNullException {

        Message message = new Message();
        message.setAppointmentId(1L);
        message.setMessageDate(getHourLater());
        message.setMessageTime(getHourLater());
        message.setMessageText("Hello there!");
        message.setMessageType(MessageType.REMINDER);
        message.setMessageState(MessageState.SENT_TO_COMMUNICATE);
        messageService.save(message);

        List<Message> messages = messageService.findByMessageState(MessageState.SENT_TO_COMMUNICATE);

        Assert.assertEquals(1,messages.size());

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
