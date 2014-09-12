package org.celllife.appointmentreminders.domain.appointment;

import org.celllife.appointmentreminders.domain.message.MessageDto;

import java.io.Serializable;
import java.util.Collection;

/**
 * Data Transfer Object (DTO) for an Appointment with Messages. Used by the REST interface.
 */
public class AppointmentWithMessagesDto implements Serializable {

    private static final long serialVersionUID = -3965132092171605079L;

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

    /**
     * Gets the date of the Appointment
     * @return String containing a date in format dd/MM/yyyy
     */
    public String getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * Sets the Appointment Date in format dd/MM/yyyy
     * @param appointmentDate String appointment date
     */
    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    /**
     * Gets the time of the Appointment 
     * @return String containing the time in 24 hour format HH:mm
     */
    public String getAppointmentTime() {
        return appointmentTime;
    }

    /**
     * Sets the Appointment Time in 24 hour format HH:mm
     * @param appointmentTime String appointment time
     */
    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Collection<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(Collection<MessageDto> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "AppointmentWithMessagesDto [id=" + id + ", patientId=" + patientId + ", appointmentDate="
                + appointmentDate + ", appointmentTime=" + appointmentTime + ", messages=" + messages + "]";
    }
}
