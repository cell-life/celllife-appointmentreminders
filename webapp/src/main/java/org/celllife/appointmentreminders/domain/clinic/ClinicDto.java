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

    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ClinicDto [id=" + id + ", name=" + name + ", code=" + code + ", password=" + password + "]";
    }
}
