package org.celllife.appointmentreminders.application.appointment;

import org.celllife.appointmentreminders.domain.appointment.Appointment;
import org.celllife.appointmentreminders.domain.appointment.AppointmentRepository;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService{

    @Autowired
    AppointmentRepository appointmentRepository;

    @Override
    public Appointment save(Appointment appointment) throws RequiredFieldIsNullException {

        if ((appointment.getPatientId() == null) || (appointment.getAppointmentDate() == null) || (appointment.getAppointmentTime() == null)) {
            throw new RequiredFieldIsNullException("PatientId, AppointmentDate and AppointmentTime must all be not null");
        }
        return appointmentRepository.save(appointment);

    }


}
