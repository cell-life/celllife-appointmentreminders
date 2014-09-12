package org.celllife.appointmentreminders.domain.patient;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) for the Patient entity. Used by the REST interface.
 */
public class PatientDto implements Serializable {

    private static final long serialVersionUID = 2239616278983798243L;

    private Long id;

    private String patientCode;

    private Long clinicId;

    private String msisdn;

    private Boolean subscribed;

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

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    @Override
    public String toString() {
        return "PatientDto [id=" + id + ", patientCode=" + patientCode + ", clinicId=" + clinicId + ", msisdn="
                + msisdn + ", subscribed=" + subscribed + "]";
    }
}
