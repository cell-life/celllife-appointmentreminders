package org.celllife.appointmentreminders.domain.message;

import java.io.Serializable;

/**
 * Data Transfer Object for the Message entity. Used by the REST interface.
 */
public class MessageDto implements Serializable {

    private static final long serialVersionUID = -8149760854829497786L;

    private Long id;

    private Long appointmentId;

    private String messageDate;

    private String messageTime;

    private String messageText;

    private MessageType messageType;

    private MessageState messageState;
    
    private Integer messageSlot;

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

    /**
     * Retrieve the date that the Message is scheduled.
     * @return String message date in format dd/MM/yyyy
     */
    public String getMessageDate() {
        return messageDate;
    }

    /**
     * Set the date the Message is to be scheduled.  Must be today or in the future.
     * @param messageDate String containing a date in format dd/MM/yyyy
     */
    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    /**
     * Get the time the Message is scheduled.
     * @return String containing a time in 24 hr format HH:mm
     */
    public String getMessageTime() {
        return messageTime;
    }

    /**
     * Set the time the Message is to be scheduled.
     * @param messageTime String containing the time in 24 hr format HH:mm
     */
    public void setMessageTime(String messageTime) {
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

    public MessageState getMessageState() {
        return messageState;
    }

    public void setMessageState(MessageState messageState) {
        this.messageState = messageState;
    }

    public Integer getMessageSlot() {
        return messageSlot;
    }

    public void setMessageSlot(Integer messageSlot) {
        this.messageSlot = messageSlot;
    }

    @Override
    public String toString() {
        return "MessageDto [id=" + id + ", appointmentId=" + appointmentId + ", messageDate=" + messageDate
                + ", messageTime=" + messageTime + ", messageText=" + messageText + ", messageType=" + messageType
                + ", messageState=" + messageState + ", messageSlot=" + messageSlot + "]";
    }
}
