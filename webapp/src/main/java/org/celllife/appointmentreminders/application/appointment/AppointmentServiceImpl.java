package org.celllife.appointmentreminders.application.appointment;

import java.util.Date;
import java.util.List;

import org.celllife.appointmentreminders.domain.appointment.Appointment;
import org.celllife.appointmentreminders.domain.appointment.AppointmentRepository;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    @Autowired
    AppointmentRepository appointmentRepository;

    @Override
    public Appointment save(Appointment appointment) throws RequiredFieldIsNullException {

        if ((appointment.getPatientId() == null) || (appointment.getAppointmentDate() == null) || (appointment.getAppointmentTime() == null)) {
            throw new RequiredFieldIsNullException("PatientId, AppointmentDate and AppointmentTime must all be not null");
        } else {
            return appointmentRepository.save(appointment);
        }
    }

    @Override
    public Appointment get(Long appointmentId) {
        return appointmentRepository.findOne(appointmentId);
    }

    @Override
    public Appointment delete(Long appointmentId) {
        Appointment appointment = get(appointmentId);
        if (appointment != null) {
            appointmentRepository.delete(appointmentId);
        }
        return appointment;
    }

    @Override
    public boolean appointmentExists(Long patientId, Date appointmentDate, Date appointmentTime) {
        return appointmentRepository.exists(patientId, appointmentDate, appointmentTime);
    }

    @Override
    public List<Appointment> findByPatientIdAndDateTimeStamp(Long patientId, Date appointmentDate, Date appointmentTime) {
        return appointmentRepository.findByPatientIdAndAppointmentDateAndAppointmentTime(patientId, appointmentDate, appointmentTime);
    }

}
