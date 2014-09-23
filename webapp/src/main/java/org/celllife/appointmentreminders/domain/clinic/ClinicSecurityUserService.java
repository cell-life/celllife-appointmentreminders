package org.celllife.appointmentreminders.domain.clinic;

import java.util.Iterator;

import org.celllife.security.domain.SecurityUser;
import org.celllife.security.service.AbstractSecurityUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration with the security dependency service used to set and reset passwords
 */
public class ClinicSecurityUserService extends AbstractSecurityUserServiceImpl {
    
    @Autowired
    ClinicRepository clinicRepository;

    @Override
    public SecurityUser findOneByLogin(String login) {
        return getClinic(login);
    }

    @Override
    public void updateEncryptedPasswordAndSalt(SecurityUser user) {
        Clinic clinic = getClinic(user.getLogin());
        if (clinic != null) {
            clinic.setEncryptedPassword(user.getEncryptedPassword());
            clinic.setSalt(user.getSalt());
            clinicRepository.save(clinic);
        }
    }
    
    private Clinic getClinic(String code) {
        Iterable<Clinic> clinics = clinicRepository.findByCode(code);
        Iterator<Clinic> it = clinics.iterator();
        if (it.hasNext())  {
            return it.next();
        } else {
            return null;
        }
    }
}