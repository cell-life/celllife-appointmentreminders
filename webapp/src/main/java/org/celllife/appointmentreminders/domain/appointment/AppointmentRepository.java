package org.celllife.appointmentreminders.domain.appointment;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface AppointmentRepository extends PagingAndSortingRepository<Appointment,Long> {

}
