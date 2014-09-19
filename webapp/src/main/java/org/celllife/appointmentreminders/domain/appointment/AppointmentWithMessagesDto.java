package org.celllife.appointmentreminders.domain.appointment;

import java.util.Collection;

import org.celllife.appointmentreminders.domain.message.MessageDto;

/**
 * Data Transfer Object (DTO) for an Appointment with Messages. Used by the REST interface.
 */
public class AppointmentWithMessagesDto extends AppointmentDto {

    private static final long serialVersionUID = -3965132092171605079L;

    private Collection<MessageDto> messages;

    public Collection<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(Collection<MessageDto> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "AppointmentWithMessagesDto [id=" + getId() + ", patientId=" + getPatientId() + ", appointmentDate="
                + getAppointmentDate() + ", appointmentTime=" +getAppointmentTime() + ", messages=" + messages + "]";
    }
}
