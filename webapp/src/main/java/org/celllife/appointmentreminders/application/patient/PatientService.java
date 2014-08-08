package org.celllife.appointmentreminders.application.patient;

import org.celllife.appointmentreminders.domain.patient.Patient;

import java.util.List;

public interface PatientService {

    Patient save(Patient patient);

    public Patient get(Long patientId);

    public List<Patient> findPatientByClinic(Long clinicId);

}
