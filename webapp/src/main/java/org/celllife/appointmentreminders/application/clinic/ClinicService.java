package org.celllife.appointmentreminders.application.clinic;

import org.celllife.appointmentreminders.domain.clinic.Clinic;
import org.celllife.appointmentreminders.domain.exception.ClinicCodeExistsException;

import java.util.List;

public interface ClinicService {

    /**
     * Saves a clinic.
     * @param clinic
     * @return The new or updated clinic.
     * @throws ClinicCodeExistsException If the clinic is new but the clinic code is already present.
     */
    Clinic save(Clinic clinic) throws ClinicCodeExistsException;

    /**
     * Gets a clinic by id.
     * @param clinicId
     * @return
     */
    Clinic get(Long clinicId);

    /**
     * Gets all the clinics in the database.
     * @return
     */
    List<Clinic> getAllClinics();

    /**
     * Finds a clinic by code.
     * @param code
     * @return
     */
    Clinic findClinicByCode(String code);

}
