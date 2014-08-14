package org.celllife.appointmentreminders.domain.patient;

import org.celllife.appointmentreminders.domain.exception.InvalidMsisdnException;
import org.celllife.appointmentreminders.framework.util.MsisdnUtils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Patient implements Serializable {

    private static final long serialVersionUID = 6697232176219145622L;

    @Id
    @TableGenerator(
            name="PatientIdGen",
            table="hibernate_sequences",
            pkColumnName="sequence_name",
            valueColumnName="sequence_next_hi_value",
            pkColumnValue="alert",
            initialValue=1,
            allocationSize=1)
    @GeneratedValue(strategy= GenerationType.TABLE, generator="PatientIdGen")
    private Long id;

    @Basic(optional = false)
    private String patientCode;

    @Basic(optional = false)
    private Long clinicId;

    @Basic(optional = false)
    private String msisdn;

    @Column(columnDefinition = "BIT", length = 1)
    private Boolean subscribed;

    public Patient() {

    }

    public Patient(Long clinicId, String patientCode, String msisdn, Boolean subscribed) throws InvalidMsisdnException {

        if (!MsisdnUtils.isValidMsisdn(msisdn)) {
            throw new InvalidMsisdnException("The msisdn " + msisdn + " does not match the prescribed format.");
        }

        this.clinicId = clinicId;
        this.patientCode = patientCode;
        this.msisdn = msisdn;
        this.subscribed = subscribed;
    }

    public PatientDto getPatientDto() {
        PatientDto patientDto = new PatientDto();
        patientDto.setId(this.getId());
        patientDto.setPatientCode(this.getPatientCode());
        patientDto.setClinicId(this.getClinicId());
        patientDto.setMsisdn(this.getMsisdn());
        patientDto.setSubscribed(subscribed);
        return patientDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientCode() {
        return patientCode;
    }

    public void setPatientCode(String patientCode) {
        this.patientCode = patientCode;
    }

    public Long getClinicId() {
        return clinicId;
    }

    public void setClinicId(Long clinicId) {
        this.clinicId = clinicId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) throws InvalidMsisdnException {
        this.msisdn = msisdn;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Patient other = (Patient) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
}
