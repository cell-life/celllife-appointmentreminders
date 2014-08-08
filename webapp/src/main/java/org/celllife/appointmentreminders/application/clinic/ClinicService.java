package org.celllife.appointmentreminders.application.clinic;

import org.celllife.appointmentreminders.domain.clinic.Clinic;

import java.util.List;

public interface ClinicService {

    Clinic save(Clinic clinic);

    Clinic get(Long clinicId);

    List<Clinic> getAllClinics();

}
