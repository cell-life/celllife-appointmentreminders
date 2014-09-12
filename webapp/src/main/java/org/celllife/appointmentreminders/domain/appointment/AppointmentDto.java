package org.celllife.appointmentreminders.domain.appointment;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) for the Appointment entity. Used by the REST interface.
 */
public class AppointmentDto implements Serializable {
    
    private static final long serialVersionUID = 3887503096070499815L;

    private Long id;

    private Long patientId;

    private String appointmentDate;

    private String appointmentTime;

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

    @Override
    public String toString() {
        return "AppointmentDto [id=" + id + ", patientId=" + patientId + ", appointmentDate=" + appointmentDate
                + ", appointmentTime=" + appointmentTime + "]";
    }
}
