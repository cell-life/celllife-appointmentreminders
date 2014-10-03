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
    public void testSaveMessage_MessageDateTimeExists() throws RequiredFieldIsNullException {

        Date hourLater = getHourLater();

        Message message = new Message();
        message.setAppointmentId(1L);
        message.setMessageDate(hourLater);
        message.setMessageTime(hourLater);
        message.setMessageText("Hello there!");
        message.setMessageType(MessageType.REMINDER);
        messageService.save(message);

        Message message1 = new Message();
        message1.setAppointmentId(1L);
        message1.setMessageDate(hourLater);
        message1.setMessageTime(hourLater);
        message1.setMessageText("Hello there again!");
        message1.setMessageType(MessageType.REMINDER);
        message1 = messageService.save(message1);

        Assert.assertEquals(1,messageService.getAllMessages().size());
        Assert.assertEquals("Hello there again!",messageService.getAllMessages().get(0).getMessageText());

        List<Trigger> triggers = quartzService.getTriggers(message1.getIdentifierString());

        Assert.assertEquals(1,triggers.size());

    }

    @Test
    public void testSaveMessage_MessageInPast() throws RequiredFieldIsNullException {

        Message message = new Message();
        message.setAppointmentId(1L);
        message.setMessageDate(getHourBefore());
        message.setMessageTime(getHourBefore());
        message.setMessageText("Hello there!");
        message.setMessageType(MessageType.REMINDER);
        message = messageService.save(message);

        List<Trigger> triggers = quartzService.getTriggers(message.getIdentifierString());

        Assert.assertEquals(1,triggers.size());
        Assert.assertEquals(message.getId(), triggers.get(0).getJobDataMap().get("messageId"));
        Assert.assertTrue(triggers.get(0).getNextFireTime().after(new Date()));

        message = new Message();
        message.setAppointmentId(1L);
        message.setMessageDate(getHourAndDayBefore());
        message.setMessageTime(getHourAndDayBefore());
        message.setMessageText("Hello there again!");
        message.setMessageType(MessageType.REMINDER);
        message = messageService.save(message);

        triggers = quartzService.getTriggers(message.getIdentifierString());

        Assert.assertEquals(0,triggers.size());

    }

    @Test (expected=RequiredFieldIsNullException.class)
    public void testSaveMessage_EmptyTime() throws RequiredFieldIsNullException {

        Date hourLater = getHourLater();

        Message message = new Message();
        message.setAppointmentId(1L);
        message.setMessageDate(hourLater);
        message.setMessageText("Hello there!");
        message.setMessageType(MessageType.REMINDER);
        messageService.save(message);

    }

    @Test (expected = RequiredFieldIsNullException.class)
    public void testSaveMessage_EmptyDate() throws RequiredFieldIsNullException {

        Date hourLater = getHourLater();

        Message message = new Message();
        message.setAppointmentId(1L);
        message.setMessageTime(hourLater);
        message.setMessageText("Hello there!");
        message.setMessageType(MessageType.REMINDER);
        messageService.save(message);

    }

    @Test (expected = RequiredFieldIsNullException.class)
    public void testSaveMessage_EmptyText() throws RequiredFieldIsNullException {

        Date hourLater = getHourLater();

        Message message = new Message();
        message.setAppointmentId(1L);
        message.setMessageTime(hourLater);
        message.setMessageDate(hourLater);
        message.setMessageText("");
        message.setMessageType(MessageType.REMINDER);
        messageService.save(message);

    }

    @Test
    public void testFindByMessageState() throws RequiredFieldIsNullException {

        Message message = new Message();
        message.setAppointmentId(1L);
        message.setMessageDate(getHourAndDayBefore());
        message.setMessageTime(getHourAndDayBefore());
        message.setMessageText("Hello there!");
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

    private Date getHourBefore() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,-1);
        return calendar.getTime();
    }

    private Date getHourAndDayBefore() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,-1);
        calendar.add(Calendar.DATE,-1);
        return calendar.getTime();
    }

    private Date getThreeHoursLater() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,3);
        return calendar.getTime();
    }
}
