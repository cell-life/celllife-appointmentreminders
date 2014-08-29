package org.celllife.appointmentreminders.application.clinic;

import org.apache.commons.collections.IteratorUtils;
import org.celllife.appointmentreminders.domain.clinic.Clinic;
import org.celllife.appointmentreminders.domain.clinic.ClinicRepository;
import org.celllife.appointmentreminders.domain.exception.ClinicCodeExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional("transactionManager")
public class ClinicServiceImpl implements ClinicService{

    @Autowired
    ClinicRepository clinicRepository;

    @Override
    public Clinic save(Clinic clinic) throws ClinicCodeExistsException {
        if ( (clinic.getId() == null) && (clinicRepository.findByCode(clinic.getCode()).iterator().hasNext() )) {
            throw new ClinicCodeExistsException("A clinic with code " + clinic.getCode() + " already exists");
        }
        return clinicRepository.save(clinic);
    }

    @Override
    public Clinic get(Long clinicId) {
        return clinicRepository.findOne(clinicId);
    }

    @Override
    public List<Clinic> getAllClinics() {
        List<Clinic> clinics = IteratorUtils.toList(clinicRepository.findAll().iterator());
        return clinics;
    }

    @Override
    public Clinic findClinicByCode(String code) {

        List<Clinic> clinics =  IteratorUtils.toList(clinicRepository.findByCode(code).iterator());

        if (clinics.size() == 0)  {
            return  null;
        } else {
            return clinics.get(0);
        }

    }

}
