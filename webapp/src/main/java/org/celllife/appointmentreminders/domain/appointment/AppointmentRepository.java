package org.celllife.appointmentreminders.domain.appointment;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;

public interface AppointmentRepository extends PagingAndSortingRepository<Appointment,Long> {

    Iterable<Appointment> findByPatientIdAndAppointmentDateAndAppointmentTime(Long patientId, Date appointmentDate, Date appointmentTime);

}
