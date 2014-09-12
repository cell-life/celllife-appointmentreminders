package org.celllife.appointmentreminders.domain.clinic;

import javax.persistence.*;

import java.io.Serializable;

/**
 * Defines the Clinic domain entity. A Clinic is a medical facility with Patients who have Appointments. The Clinic entity is
 * used to provide authentication credentials for the facility using the service to send Appointment related messages.   
 */
@Entity
public class Clinic implements Serializable {

    private static final long serialVersionUID = 3718726486523331541L;

    @Id
    @TableGenerator(
            name="ClinicIdGen",
            table="hibernate_sequences",
            pkColumnName="sequence_name",
            valueColumnName="sequence_next_hi_value",
            pkColumnValue="alert",
            initialValue=1,
            allocationSize=1)
    @GeneratedValue(strategy= GenerationType.TABLE, generator="ClinicIdGen")
    private Long id;

    private String name;

    private String code;

    private String encryptedPassword;

    private String salt;

    /**
     * Default constructor
     */
    public Clinic() {

    }
    
    /**
     * Creates a Clinic given the name and unique code
     * @param name String name of the clinic
     * @param code String code for the clinic (must be unique)
     */
    public Clinic(String name, String code) {
        this.name = name;
        this.code = code;
    }

    /**
     * Creates a Clinic given identifying and authentication information
     * @param name String name of the clinic
     * @param code String code for the clinic (must be unique)
     * @param encryptedPassword String encrypted password
     * @param salt String salt used to encrypt the password
     */
    public Clinic(String name, String code, String encryptedPassword, String salt) {
        this.name = name;
        this.code = code;
        this.encryptedPassword = encryptedPassword;
        this.salt = salt;
    }

    /**
     * Converts this Clinic entity into a DTO entity (for use in client facing interfaces)
     * @return new ClinicDto
     */
    public ClinicDto getClinicDto() {
        ClinicDto clinicDto = new ClinicDto();
        clinicDto.setId(this.getId());
        clinicDto.setName(this.getName());
        clinicDto.setCode(this.getCode());
        clinicDto.setEncryptedPassword(this.getEncryptedPassword());
        clinicDto.setSalt(this.getSalt());
        return clinicDto;
    }
    
    /**
     * The database identifier for this Clinic
     * @return Long identifier, can be null if the Clinic is not yet persisted
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the database identifier for this Clinic. It is not usual to manually set the id attribute as it is automatically
     * set by the ORM (Hibernate).
     * @param id Long identifier, can be null if the Clinic is not yet persisted
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves a descriptive name of the Clinic 
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a descriptive name for this Clinic
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the unique code used by this Clinic. The clinic codes should be set by the Cell-Life Clinic Service.
     * @return String code (e.g. 0000 for Demo Clinic #1).
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the unique code used by this Clinic. The clinic codes should be set by the Cell-Life Clinic Service.
     * @param code String code (e.g. 0000 for Demo Clinic #1).
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets the encrypted password for this Clinic. Used for authentication.
     * @return String password (encrypted)
     */
    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    /**
     * Sets the encrypted password for this Clinic. Used for authentication.
     * @param encryptedPassword String encrypted password (using the salt)
     */
    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    /**
     * Gets the Clinic's password salt (the string used to encrypt the password)
     * @return String salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Sets the Clinic's password salt (the string used to encrypt the password)
     * @param salt String, should be randomly generated in order to be secure
     */
    public void setSalt(String salt) {
        this.salt = salt;
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
        Clinic other = (Clinic) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Clinic [id=" + id + ", name=" + name + ", code=" + code + ", encryptedPassword=" + encryptedPassword
                + ", salt=" + salt + "]";
    }
}
