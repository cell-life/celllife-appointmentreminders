package org.celllife.appointmentreminders.domain.appointment;

import org.celllife.appointmentreminders.framework.util.DateUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Defines the domain entity for an Appointment. A Patient is given a date to come to the Clinic
 * to see a Doctor, Nurse or receive a package of Drugs.
 */
@Entity
public class Appointment implements Serializable {

    private static final long serialVersionUID = 67160938585426828L;

    @Id
    @TableGenerator(
            name="applicationIdGen",
            table="hibernate_sequences",
            pkColumnName="sequence_name",
            valueColumnName="sequence_next_hi_value",
            pkColumnValue="appointment",
            initialValue=1,
            allocationSize=1)
    @GeneratedValue(strategy= GenerationType.TABLE, generator="applicationIdGen")
    private Long id;

    @Basic(optional=false)
    private Long patientId;

    @Temporal(TemporalType.DATE)
    @Basic(optional=false)
    private Date appointmentDate;

    @Temporal(TemporalType.TIME)
    @Basic(optional=false)
    private Date appointmentTime;

    /**
     * Default constructor
     */
    public Appointment() {
    }

    /**
     * Creates an Appointment and specifies the patient and appointment details
     * @param patientId Long database identifier of the Patient
     * @param appointmentDate Date date information of the Appointment
     * @param appointmentTime Date time information of the Appointment
     */
    public Appointment(Long patientId, Date appointmentDate, Date appointmentTime) {
        this.patientId = patientId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    /**
     * Converts this Appointment entity into a DTO entity for use in client facing interfaces.
     * @return AppointmentDto
     */
    public AppointmentDto getAppointmentDto() {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(this.getId());
        appointmentDto.setPatientId(this.getPatientId());
        appointmentDto.setAppointmentDate(DateUtil.DateToString(this.getAppointmentDate()));
        appointmentDto.setAppointmentTime(DateUtil.TimeToString(this.getAppointmentTime()));
        return appointmentDto;
    }

    /**
     * The database identifier for this Appointment
     * @return Long identifier, can be null if the Appointment is not yet persisted
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the database identifier for this Appointment. It is not usual to manually set the id attribute as it is automatically
     * set by the ORM (Hibernate).
     * @param id Long identifier, can be null if the Appointment is not yet persisted
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * The database identifier for the Patient whose Appointment this is.
     * @return Long identifier, must not be null
     */
    public Long getPatientId() {
        return patientId;
    }

    /**
     * Set the associated Patient's database identifier
     * @param patientId Long identifier, should not be null
     */
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    /**
     * The date of the Appointment
     * @return Date, containing no time information
     */
    public Date getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * Sets the date of the Appointment (time information will be ignored)
     * @param appointmentdate Date
     */
    public void setAppointmentDate(Date appointmentdate) {
        this.appointmentDate = appointmentdate;
    }

    /**
     * The time of the Appointment
     * @return Date, containing no date information
     */
    public Date getAppointmentTime() {
        return appointmentTime;
    }

    /**
     * Sets the time of the Appointment (date information will be ignored)
     * @param appointmentTime Date
     */
    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
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
        Appointment other = (Appointment) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Appointment [id=" + id + ", patientId=" + patientId + ", appointmentDate=" + appointmentDate
                + ", appointmentTime=" + appointmentTime + "]";
    }
}
