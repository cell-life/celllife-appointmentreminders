package org.celllife.appointmentreminders.application.appointment;

import org.apache.commons.collections.IteratorUtils;
import org.celllife.appointmentreminders.domain.appointment.Appointment;
import org.celllife.appointmentreminders.domain.appointment.AppointmentRepository;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Override
    public Appointment save(Appointment appointment) throws RequiredFieldIsNullException {

        if ((appointment.getPatientId() == null) || (appointment.getAppointmentDate() == null) || (appointment.getAppointmentTime() == null)) {
            throw new RequiredFieldIsNullException("PatientId, AppointmentDate and AppointmentTime must all be not null");
        } else if ((appointment.getId() == null) && (appointmentExists(appointment.getPatientId(),appointment.getAppointmentDate(),appointment.getAppointmentTime()))) {
            return findByPatientIdAndDateTimeStamp(appointment.getPatientId(),appointment.getAppointmentDate(),appointment.getAppointmentTime()).get(0);
        } else
            return appointmentRepository.save(appointment);

    }

    @Override
    public Appointment get(Long appointmentId) {
        return appointmentRepository.findOne(appointmentId);
    }

    @Override
    public boolean appointmentExists(Long patientId, Date appointmentDate, Date appointmentTime) {
        return (findByPatientIdAndDateTimeStamp(patientId, appointmentDate, appointmentTime).size() > 0);
    }

    @Override
    public List<Appointment> findByPatientIdAndDateTimeStamp(Long patientId, Date appointmentDate, Date appointmentTime) {
        return IteratorUtils.toList(appointmentRepository.findByPatientIdAndAppointmentDateAndAppointmentTime(patientId, appointmentDate, appointmentTime).iterator());
    }

}
