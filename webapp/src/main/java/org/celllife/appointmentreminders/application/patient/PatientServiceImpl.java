package org.celllife.appointmentreminders.application.patient;

import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.celllife.appointmentreminders.domain.clinic.Clinic;
import org.celllife.appointmentreminders.domain.clinic.ClinicRepository;
import org.celllife.appointmentreminders.domain.exception.ClinicCodeNonexistentException;
import org.celllife.appointmentreminders.domain.exception.PatientCodeExistsException;
import org.celllife.appointmentreminders.domain.exception.PatientCodeNonexistentException;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.domain.patient.Patient;
import org.celllife.appointmentreminders.domain.patient.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional("transactionManager")
public class PatientServiceImpl implements PatientService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    ClinicRepository clinicRepository;

    @Override
    public Patient save(Patient patient) throws PatientCodeExistsException, RequiredFieldIsNullException {

        if ((patient.getId() == null) && (patientRepository.findByPatientCodeAndClinicId(patient.getPatientCode(),patient.getClinicId()).iterator().hasNext())) {
            throw new PatientCodeExistsException("A patient with the code " + patient.getPatientCode() + " already exists.");
        }

        if ((patient.getPatientCode() == null) || (patient.getMsisdn() == null)) {
            throw new RequiredFieldIsNullException("Patient code and msisdn cannot be null.");
        }

        return patientRepository.save(patient);
    }

    @Override
    public Patient get(Long patientId) {
        return patientRepository.findOne(patientId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Patient> findByClinicCode(String clinicCode) throws ClinicCodeNonexistentException {

        List<Clinic> clinics = IteratorUtils.toList(clinicRepository.findByCode(clinicCode).iterator());
        if (clinics.size() == 0) {
            throw new ClinicCodeNonexistentException("No clinic with code " + clinicCode + "could be found.");
        }
        Clinic clinic = clinics.get(0);

        List<Patient> patients = IteratorUtils.toList(patientRepository.findByClinicId(clinic.getId()).iterator());
        return patients;

    }

    @SuppressWarnings("unchecked")
    @Override
    public Patient findByPatientCodeAndClinicCode(String patientCode, String clinicCode)
            throws ClinicCodeNonexistentException, PatientCodeNonexistentException {

        List<Clinic> clinics = IteratorUtils.toList(clinicRepository.findByCode(clinicCode).iterator());
        if (clinics.size() == 0) {
            throw new ClinicCodeNonexistentException("No clinic with code " + clinicCode + " could be found.");
        }
        Clinic clinic = clinics.get(0);

        Iterable<Patient> patients = patientRepository.findByPatientCodeAndClinicId(patientCode, clinic.getId());
        
        if (patients == null || !patients.iterator().hasNext()) {
            throw new PatientCodeNonexistentException("No patient with code "+ patientCode + " could be found.");
        }
        
        return patients.iterator().next();

    }

}
