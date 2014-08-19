package org.celllife.appointmentreminders.domain.message;

import org.celllife.appointmentreminders.framework.util.DateUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Message implements Serializable {

    @Id
    @TableGenerator(
            name="MessageIdGen",
            table="hibernate_sequences",
            pkColumnName="sequence_name",
            valueColumnName="sequence_next_hi_value",
            pkColumnValue="alert",
            initialValue=1,
            allocationSize=1)
    @GeneratedValue(strategy= GenerationType.TABLE, generator="MessageIdGen")
    private Long id;

    @Basic(optional=false)
    private Long appointmentId;

    @Basic(optional=false)
    private Date messageDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition="TIME")
    @Basic(optional=false)
    private Date messageTime;

    private String messageText;

    private MessageType messageType;

    public Message() {

    }

    public Message(Long appointmentId, Date messageDate, Date messageTime, String messageText, MessageType messageType) {
        this.appointmentId = appointmentId;
        this.messageDate = messageDate;
        this.messageTime = messageTime;
        this.messageText = messageText;
        this.messageType = messageType;
    }

    public MessageDto getMessageDto() {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(this.getId());
        messageDto.setAppointmentId(this.getAppointmentId());
        messageDto.setMessageDate(DateUtil.DateToString(this.getMessageDate()));
        messageDto.setMessageTime(DateUtil.TimeToString(this.getMessageTime()));
        messageDto.setMessageType(this.getMessageType());
        return messageDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
