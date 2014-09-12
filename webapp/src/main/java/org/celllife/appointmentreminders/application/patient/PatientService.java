package org.celllife.appointmentreminders.application.patient;

import org.celllife.appointmentreminders.domain.exception.ClinicCodeNonexistentException;
import org.celllife.appointmentreminders.domain.exception.PatientCodeExistsException;
import org.celllife.appointmentreminders.domain.exception.PatientCodeNonexistentException;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.domain.patient.Patient;

import java.util.List;

public interface PatientService {

    /**
     * Saves a new or existing patient.
     * @param patient
     * @return
     * @throws PatientCodeExistsException If a patient with this code already exists in the clinic.
     * @throws RequiredFieldIsNullException If a required field is null.
     */
    Patient save(Patient patient) throws PatientCodeExistsException, RequiredFieldIsNullException;

    /**
     * Gets patient by database id.
     * @param patientId
     * @return
     */
    Patient get(Long patientId);

    /**
     * Finds all patients in a clinic.
     * @param clinicCode
     * @return
     * @throws ClinicCodeNonexistentException
     */
    List<Patient> findByClinicCode(String clinicCode) throws ClinicCodeNonexistentException;

    /**
     * Finds a patient by patient code and clinic code.
     * @param patientCode
     * @param clinicCode
     * @return
     * @throws ClinicCodeNonexistentException
     * @throws PatientCodeNonexistentException
     */
    Patient findByPatientCodeAndClinicCode(String patientCode, String clinicCode)
            throws ClinicCodeNonexistentException, PatientCodeNonexistentException;

}
