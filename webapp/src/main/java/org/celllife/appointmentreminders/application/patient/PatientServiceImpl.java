package org.celllife.appointmentreminders.application.patient;

import org.apache.commons.collections.IteratorUtils;
import org.celllife.appointmentreminders.domain.patient.Patient;
import org.celllife.appointmentreminders.domain.patient.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional("transactionManager")
public class PatientServiceImpl implements PatientService {

    @Autowired
    PatientRepository patientRepository;

    @Override
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient get(Long patientId) {
        return patientRepository.findOne(patientId);
    }

    @Override
    public List<Patient> findPatientByClinic(Long clinicId) {
        List<Patient> patients = IteratorUtils.toList(patientRepository.findByClinicId(clinicId).iterator());
        return patients;
    }

}
