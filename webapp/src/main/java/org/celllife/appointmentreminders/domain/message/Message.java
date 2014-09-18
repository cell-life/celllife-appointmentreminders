package org.celllife.appointmentreminders.domain.message;

import org.celllife.appointmentreminders.framework.util.DateUtil;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Defines the Message domain entity. Messages can either be reminders of an upcoming Appointment, or reminders of a missed
 * Appointment. Messages will be sent to the Patient at the specified date & time. 
 */
@Entity
public class Message implements Serializable {

    private static final long serialVersionUID = -8625546880551237209L;

    @Id
    @TableGenerator(
            name="MessageIdGen",
            table="hibernate_sequences",
            pkColumnName="sequence_name",
            valueColumnName="sequence_next_hi_value",
            pkColumnValue="message",
            initialValue=1,
            allocationSize=1)
    @GeneratedValue(strategy= GenerationType.TABLE, generator="MessageIdGen")
    private Long id;

    @Basic(optional=false)
    private Long appointmentId;

    @Temporal(TemporalType.DATE)
    @Basic(optional=false)
    private Date messageDate;

    @Temporal(TemporalType.TIME)
    @Basic(optional=false)
    private Date messageTime;

    private String messageText;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Enumerated(EnumType.STRING)
    private MessageState messageState = MessageState.NEW;

    private Integer retryAttempts = 0;

    private Long communicateId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date messageSent;

    /**
     * Default contstructor
     */
    public Message() {

    }

    /**
     * Create a Message entity
     * @param appointmentId Long database identifier of the Appointment
     * @param messageDate Date the date the message should be sent
     * @param messageTime Date the time the message should be sent
     * @param messageText String the message contents. Note: An SMS is 160 characters
     * @param messageType MessageType the type of the message (reminder or missed)
     */
    public Message(Long appointmentId, Date messageDate, Date messageTime, String messageText, MessageType messageType) {
        this.appointmentId = appointmentId;
        this.messageDate = messageDate;
        this.messageTime = messageTime;
        this.messageText = messageText;
        this.messageType = messageType;
    }

    /**
     * Converts this Message into a DTO for use in a clinic facing interface (e.g. REST)
     * @return MessageDto
     */
    public MessageDto getMessageDto() {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(this.getId());
        messageDto.setAppointmentId(this.getAppointmentId());
        messageDto.setMessageDate(DateUtil.DateToString(this.getMessageDate()));
        messageDto.setMessageTime(DateUtil.TimeToString(this.getMessageTime()));
        messageDto.setMessageType(this.getMessageType());
        messageDto.setMessageState(this.messageState);
        return messageDto;
    }

    /**
     * The database identifier for this Message
     * @return Long identifier, can be null if the Message is not yet persisted
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the database identifier for this Message. It is not usual to manually set the id attribute as it is automatically
     * set by the ORM (Hibernate).
     * @param id Long identifier, can be null if the Message is not yet persisted
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the associated Appointment identifier.
     * @return Long database identifier
     */
    public Long getAppointmentId() {
        return appointmentId;
    }

    /**
     * Sets the associated Appointment identifier
     * @param appointmentId Long database identifier of the Appointment, must not be null
     */
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Retrieve the date that the Message should be sent
     * @return Date containing only date information (no time)
     */
    public Date getMessageDate() {
        return messageDate;
    }

    /**
     * Sets the date that the Message should be sent.
     * @param messageDate Date containing only date information (no time)
     */
    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    /**
     * Retrieve the time that the Message should be sent.
     * @return Date containing only time information (no date)
     */
    public Date getMessageTime() {
        return messageTime;
    }

    /**
     * Set the time the Message should be sent.
     * @param messageTime Date containing the time information (no date)
     */
    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    /**
     * Retrieve the contents of the Message.
     * @return String message text
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * Set the content of the Message to be sent to the Patient.  Note: an SMS is 160 characters.
     * @param messageText String message text
     */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    /**
     * Retrieve the type of the Message (either a reminder message or a missed appointment message)
     * @return MessageType
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Set the type of the Message (either a reminder message or a missed appointment message)
     * @param messageType MessageType, should not be null
     */
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * Retrieve the current status of the message which indicates if the message was sent successfully or not.
     * @return MessageState
     */
    public MessageState getMessageState() {
        return messageState;
    }

    /**
     * Set the current status of the message which indicates if the message was sent successfully or not.
     * @param messageState MessageState
     */
    public void setMessageState(MessageState messageState) {
        this.messageState = messageState;
    }

    /**
     * Retrieve the associated Communicate identifier of this Message.
     * @return Long identifier of the message, can be null if the Message has not been scheduled.
     */
    public Long getCommunicateId() {
        return communicateId;
    }

    /**
     * Sets the associated Communicate identifier of this Message. This will be done when the message is scheduled.
     * @param communicateId Long Communicate specific identifier
     */
    public void setCommunicateId(Long communicateId) {
        this.communicateId = communicateId;
    }

    /**
     * Retrieve the number of times this message has been sent and re-sent (to Communicate)
     * @return Integer schedule retry times
     */
    public Integer getRetryAttempts() {
        return retryAttempts;
    }

    /**
     * Set the number of times this message has been sent and resent (to Communicate)
     * @param retryAttempts Integer
     */
    public void setRetryAttempts(Integer retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    /**
     * Retrieve the date that the Message was sent.
     * @return Date that the message was sent (can be null if the Message has not yet been sent)
     */
    public Date getMessageSent() {
        return messageSent;
    }

    /**
     * Set the date that the Message was sent.
     * @param messageSent Date that the message was sent
     */
    public void setMessageSent(Date messageSent) {
        this.messageSent = messageSent;
    }

    public void increaseRetryCount() {
        if (this.getRetryAttempts() == null) {
            this.setRetryAttempts(1);
        }
        else {
            this.setRetryAttempts(getRetryAttempts() + 1);
        }
    }

    /**
     * Retrieve the date and time that the Message should be sent. This combines the messageDate and messageTime
     * attributes of this Message.
     * 
     * @return Date containing message schedule date and time
     */
    public Date getMessageDateTime() {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime(getMessageTime());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getMessageDate());
        calendar.set(Calendar.HOUR_OF_DAY,timeCalendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,timeCalendar.get(Calendar.MINUTE));

        return calendar.getTime();
    }

    /**
     * Generate a unique identifier for this Message using the database identifier. This is used by Quartz to schedule
     * a task to schedule or update this Message.
     *
     * @return String identifier
     */
    public String getIdentifierString(){
        return this.getClass().getName() + ":" + this.id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Message other = (Message) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Message [id=" + id + ", appointmentId=" + appointmentId + ", messageDate=" + messageDate
                + ", messageTime=" + messageTime + ", messageText=" + messageText + ", messageType=" + messageType
                + ", messageState=" + messageState + ", retryAttempts=" + retryAttempts + ", communicateId="
                + communicateId + ", messageSent=" + messageSent + "]";
    }
}
