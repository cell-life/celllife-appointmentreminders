package org.celllife.appointmentreminders.domain.clinic;

import javax.persistence.*;
import java.io.Serializable;

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

    public Clinic() {

    }

    public Clinic(String name, String code, String encryptedPassword, String salt) {
        this.name = name;
        this.code = code;
        this.encryptedPassword = encryptedPassword;
        this.salt = salt;
    }

    public ClinicDto getClinicDto() {
        ClinicDto clinicDto = new ClinicDto();
        clinicDto.setId(this.getId());
        clinicDto.setName(this.getName());
        clinicDto.setCode(this.getCode());
        clinicDto.setEncryptedPassword(this.getEncryptedPassword());
        clinicDto.setSalt(this.getSalt());
        return clinicDto;
    }

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

}
