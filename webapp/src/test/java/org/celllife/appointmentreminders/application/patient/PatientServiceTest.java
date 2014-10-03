package org.celllife.appointmentreminders.application.patient;


import java.util.List;

import junit.framework.Assert;

import org.celllife.appointmentreminders.application.clinic.ClinicService;
import org.celllife.appointmentreminders.domain.clinic.Clinic;
import org.celllife.appointmentreminders.domain.clinic.ClinicRepository;
import org.celllife.appointmentreminders.domain.exception.ClinicCodeNonexistentException;
import org.celllife.appointmentreminders.domain.exception.PatientCodeNonexistentException;
import org.celllife.appointmentreminders.domain.patient.Patient;
import org.celllife.appointmentreminders.domain.patient.PatientRepository;
import org.celllife.appointmentreminders.test.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class PatientServiceTest extends TestConfiguration {

    @Autowired
    PatientService patientService;

    @Autowired
    PatientRepository patientRepository;
    
    @Autowired
    ClinicService clinicService;
    
    @Autowired
    ClinicRepository clinicRepository;

    @Test
    public void testCreatePatient() throws Exception {
        
        Long clinicId = null;
        Long patientId = null;
        
        try {
            Clinic clinic = new Clinic("Demo Clinic #1", "0000");
            clinic = clinicService.save(clinic);
            clinicId = clinic.getId();
            
            Patient patient = new Patient(clinicId, "123", "27768198075", Boolean.TRUE);
            patient = patientService.save(patient);
            patientId = patient.getId();
            
            Assert.assertNotNull(patient);
            Assert.assertNotNull(patient.getId());
            
            Patient savedPatient = patientService.findByPatientCodeAndClinicCode("123", "0000");
            Assert.assertNotNull(savedPatient);
            Assert.assertEquals(patient, savedPatient);
            
            
        } finally {
            if (patientId != null) {
                patientRepository.delete(patientId);
            }
            if (clinicId != null) {
                clinicRepository.delete(clinicId);
            }
        }
    }

    @Test
    public void testFindByClinicCode() throws Exception {
        Long clinicId = null;
        Long patient1Id = null;
        Long patient2Id = null;
        
        try {
            Clinic clinic = new Clinic("Demo Clinic #1", "0000");
            clinic = clinicService.save(clinic);
            clinicId = clinic.getId();
            
            Patient patient1 = new Patient(clinicId, "123", "27768198075", Boolean.TRUE);
            patient1 = patientService.save(patient1);
            patient1Id = patient1.getId();
            
            Assert.assertNotNull(patient1);
            Assert.assertNotNull(patient1.getId());
            
            Patient patient2 = new Patient(clinicId, "124", "27768198076", Boolean.TRUE);
            patient2 = patientService.save(patient2);
            patient1Id = patient2.getId();
            
            Assert.assertNotNull(patient2);
            Assert.assertNotNull(patient2.getId());
            
            List<Patient> clinicPatients = patientService.findByClinicCode("0000");
            Assert.assertNotNull(clinicPatients);
            Assert.assertFalse(clinicPatients.isEmpty());
            Assert.assertEquals(2, clinicPatients.size());
            Assert.assertTrue(clinicPatients.contains(patient1));
            Assert.assertTrue(clinicPatients.contains(patient2));
            
        } finally {
            if (patient1Id != null) {
                patientRepository.delete(patient1Id);
            }
            if (patient2Id != null) {
                patientRepository.delete(patient2Id);
            }
            if (clinicId != null) {
                clinicRepository.delete(clinicId);
            }
        }
    }
    
    @Test(expected = ClinicCodeNonexistentException.class)
    public void testFindNonexistentClinic() throws Exception {
        patientService.findByPatientCodeAndClinicCode("123", "000000000");
    }
    
    @Test(expected = PatientCodeNonexistentException.class)
    public void testFindNonexistentPatient() throws Exception {
        
        Long clinicId = null;
        
        try {
            Clinic clinic = new Clinic("Demo Clinic #1", "0000");
            clinic = clinicService.save(clinic);
            clinicId = clinic.getId();
            
            patientService.findByPatientCodeAndClinicCode("123", "0000");
            
        } finally {
            if (clinicId != null) {
                clinicRepository.delete(clinicId);
            }
        }
    }
    
}
