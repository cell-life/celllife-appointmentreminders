package org.celllife.appointmentreminders.domain.appointment;

import org.celllife.appointmentreminders.domain.message.MessageDto;

import java.io.Serializable;
import java.util.Collection;

public class AppointmentWithMessagesDto implements Serializable {

    private Long id;

    private Long patientId;

    private String appointmentDate;

    private String appointmentTime;

    private Collection<MessageDto> messages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Collection<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(Collection<MessageDto> messages) {
        this.messages = messages;
    }
}
