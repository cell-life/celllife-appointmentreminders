package org.celllife.appointmentreminders.domain.clinic;

import java.io.Serializable;

/**
 * Data Transfer Object for the Clinic entity. Used by the REST interface.
 */
public class ClinicDto implements Serializable {

    private static final long serialVersionUID = -3466580491561864280L;

    private Long id;

    private String name;

    private String code;

    private String encryptedPassword;

    private String salt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "ClinicDto [id=" + id + ", name=" + name + ", code=" + code + ", encryptedPassword=" + encryptedPassword
                + ", salt=" + salt + "]";
    }
}
