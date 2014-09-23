package org.celllife.appointmentreminders.domain.appointment;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends PagingAndSortingRepository<Appointment, Long> {

    List<Appointment> findByPatientIdAndAppointmentDateAndAppointmentTime(Long patientId, Date appointmentDate, Date appointmentTime);

    @Query("select case when count(a) > 0 then true else false end "
            + "from Appointment a where a.patientId = :patientId "
            + "and a.appointmentDate = :appointmentDate "
            + "and a.appointmentTime = :appointmentTime")
    Boolean exists(@Param("patientId") Long patientId, @Param("appointmentDate") Date appointmentDate, @Param("appointmentTime") Date appointmentTime);

}