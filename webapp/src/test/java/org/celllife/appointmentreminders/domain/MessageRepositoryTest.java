package org.celllife.appointmentreminders.domain;

import junit.framework.Assert;
import org.celllife.appointmentreminders.domain.message.Message;
import org.celllife.appointmentreminders.domain.message.MessageRepository;
import org.celllife.appointmentreminders.domain.message.MessageState;
import org.celllife.appointmentreminders.domain.message.MessageType;
import org.celllife.appointmentreminders.test.TestConfiguration;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class MessageRepositoryTest extends TestConfiguration {

    @Autowired
    MessageRepository messageRepository;

    private Date date = new Date();
    private Date fiveMinutesLater = getFiveMinutesLater();
    private Date oneDayLater = getOneDayLater();

    @Test
    public void testFindByAppointmentIdAndMessageDateAndMessageTime() {

        Message message = new Message();
        message.setMessageDate(date);
        message.setMessageState(MessageState.NEW);
        message.setMessageText("Test Message One");
        message.setAppointmentId(1L);
        message.setMessageTime(date);
        message.setMessageType(MessageType.REMINDER);
        messageRepository.save(message);

        message = new Message();
        message.setMessageDate(date);
        message.setMessageState(MessageState.NEW);
        message.setMessageText("Test Message Two");
        message.setAppointmentId(1L);
        message.setMessageTime(fiveMinutesLater);
        message.setMessageType(MessageType.REMINDER);
        messageRepository.save(message);

        List<Message> messages = messageRepository.findByAppointmentIdAndMessageDateAndMessageTime(1L,date,date);

        Assert.assertEquals(1,messages.size());
        Assert.assertEquals("Test Message One",messages.get(0).getMessageText());

    }

    @Test
    public void testFindByMessageStateAndMessageDate() {

        Message message = new Message();
        message.setMessageDate(date);
        message.setMessageState(MessageState.NEW);
        message.setMessageText("Test Message One");
        message.setAppointmentId(1L);
        message.setMessageTime(date);
        message.setMessageType(MessageType.REMINDER);
        messageRepository.save(message);

        message = new Message();
        message.setMessageDate(oneDayLater);
        message.setMessageState(MessageState.NEW);
        message.setMessageText("Test Message Two");
        message.setAppointmentId(1L);
        message.setMessageTime(oneDayLater);
        message.setMessageType(MessageType.REMINDER);
        messageRepository.save(message);

        message = new Message();
        message.setMessageDate(date);
        message.setMessageState(MessageState.NEW);
        message.setMessageText("Test Message Three");
        message.setAppointmentId(1L);
        message.setMessageTime(date);
        message.setMessageType(MessageType.REMINDER);
        messageRepository.save(message);

        List<Message> messages = messageRepository.findByMessageStateAndMessageDate(MessageState.NEW,date);
        Iterable<Message> messages2 = messageRepository.findAll();
        Assert.assertEquals(2,messages.size());

    }

    @After
    public void cleaunUp() {

        messageRepository.deleteAll();

    }

    private Date getFiveMinutesLater() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,5);
        return calendar.getTime();
    }

    private Date getOneDayLater() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        return calendar.getTime();
    }

}
