package org.celllife.appointmentreminders.domain.patient;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface PatientRepository extends PagingAndSortingRepository<Patient, Long> {

    Iterable<Patient> findByClinicId(Long clinicId);

}
