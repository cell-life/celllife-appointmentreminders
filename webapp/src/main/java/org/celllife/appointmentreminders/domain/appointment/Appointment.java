package org.celllife.appointmentreminders.domain.appointment;

import org.celllife.appointmentreminders.framework.util.DateUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Appointment implements Serializable {

    private static final long serialVersionUID = 67160938585426828L;

    @Id
    @TableGenerator(
            name="applicationIdGen",
            table="hibernate_sequences",
            pkColumnName="sequence_name",
            valueColumnName="sequence_next_hi_value",
            pkColumnValue="alert",
            initialValue=1,
            allocationSize=1)
    @GeneratedValue(strategy= GenerationType.TABLE, generator="applicationIdGen")
    private Long id;

    @Basic(optional=false)
    private Long patientId;

    @Basic(optional=false)
    private Date appointmentDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition="TIME")
    @Basic(optional=false)
    private Date appointmentTime;

    public Appointment() {
    }

    public Appointment(Long patientId, Date appointmentDate, Date appointmentTime) {
        this.patientId = patientId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    public AppointmentDto getAppointmentDto() {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(this.getId());
        appointmentDto.setPatientId(this.getPatientId());
        appointmentDto.setAppointmentDate(DateUtil.DateToString(this.getAppointmentDate()));
        appointmentDto.setAppointmentTime(DateUtil.TimeToString(this.getAppointmentTime()));
        return appointmentDto;
    }

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

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentdate) {
        this.appointmentDate = appointmentdate;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

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

}
