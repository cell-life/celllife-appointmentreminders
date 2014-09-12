package org.celllife.appointmentreminders.domain.patient;

import org.celllife.appointmentreminders.domain.exception.InvalidMsisdnException;
import org.celllife.appointmentreminders.framework.util.MsisdnUtils;

import javax.persistence.*;

import java.io.Serializable;

/**
 * Defines the Patient domain entity. A Patient is a person being treated at a Clinic.
 */
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

    /**
     * Default constructor
     */
    public Patient() {

    }

    /**
     * Create a Patient
     * @param clinicId Long database identifier of the Clinic
     * @param patientCode String clinic specific patient code
     * @param msisdn String cellphone number
     * @param subscribed Boolean subscription to Appointment reminders
     * @throws InvalidMsisdnException if the specifided MSISDN is invalid.
     */
    public Patient(Long clinicId, String patientCode, String msisdn, Boolean subscribed) throws InvalidMsisdnException {

        if (!MsisdnUtils.isValidMsisdn(msisdn)) {
            throw new InvalidMsisdnException("The msisdn " + msisdn + " does not match the prescribed format.");
        }

        this.clinicId = clinicId;
        this.patientCode = patientCode;
        this.msisdn = msisdn;
        this.subscribed = subscribed;
    }

    /**
     * Converts this Patient into a DTO object to be used by clinic facing interaces (e.g. REST).
     * @return PatientDto
     */
    public PatientDto getPatientDto() {
        PatientDto patientDto = new PatientDto();
        patientDto.setId(this.getId());
        patientDto.setPatientCode(this.getPatientCode());
        patientDto.setClinicId(this.getClinicId());
        patientDto.setMsisdn(this.getMsisdn());
        patientDto.setSubscribed(subscribed);
        return patientDto;
    }

    /**
     * The database identifier for this Patient
     * @return Long identifier, can be null if the Patient is not yet persisted
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the database identifier for this Patient. It is not usual to manually set the id attribute as it is automatically
     * set by the ORM (Hibernate).
     * @param id Long identifier, can be null if the Patient is not yet persisted
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieve the Clinic's Patient specific code.
     * @return String patient code
     */
    public String getPatientCode() {
        return patientCode;
    }

    /**
     * Set the Clinic's Patient specific code (must be unique for a Clinic).
     * @param patientCode String code
     */
    public void setPatientCode(String patientCode) {
        this.patientCode = patientCode;
    }

    /**
     * Retrieve the database identifier for the associated Clinic.
     * @return Long identifier
     */
    public Long getClinicId() {
        return clinicId;
    }

    /**
     * Set the database idenfifier for the associated Clinic.
     * @param clinicId Long identifier (must not be null)
     */
    public void setClinicId(Long clinicId) {
        this.clinicId = clinicId;
    }

    /**
     * Retrieve the Patient's mobile number.
     * @return String msisdn
     */
    public String getMsisdn() {
        return msisdn;
    }

    /**
     * Sets the Patient's cellphone number
     * @param msisdn String
     * @throws InvalidMsisdnException if there is a validation issue with the specified MSISDN
     */
    public void setMsisdn(String msisdn) throws InvalidMsisdnException {
        this.msisdn = msisdn;
    }

    /**
     * Check if the Patient is subscribed to Appointment Reminder messages. If the value is false, then no messages will be sent.
     * @return Boolean, true if the patient wishes to receive messages
     */
    public Boolean isSubscribed() {
        return subscribed;
    }

    /**
     * Indicate that the Patient wishes to receive Appointment Reminder messages.
     * @param subscribed Boolean, true if the patient wishes to receive messages
     */
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

    @Override
    public String toString() {
        return "Patient [id=" + id + ", patientCode=" + patientCode + ", clinicId=" + clinicId + ", msisdn=" + msisdn
                + ", subscribed=" + subscribed + "]";
    }
}
