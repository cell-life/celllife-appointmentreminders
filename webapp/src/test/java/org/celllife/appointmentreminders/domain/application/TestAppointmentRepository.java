package org.celllife.appointmentreminders.domain.application;

import junit.framework.Assert;
import org.celllife.appointmentreminders.application.appointment.AppointmentService;
import org.celllife.appointmentreminders.domain.appointment.Appointment;
import org.celllife.appointmentreminders.domain.appointment.AppointmentRepository;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.test.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestAppointmentRepository extends TestConfiguration {

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Test
    public void testFindfindByPatientIdAndDate() throws RequiredFieldIsNullException {

        Date date = new Date();

        Appointment appointment = new Appointment();
        appointment.setPatientId(1L);
        appointment.setAppointmentDate(date);
        appointment.setAppointmentTime(date);
        appointmentService.save(appointment);

        Iterable<Appointment> appointments = appointmentRepository.findByPatientIdAndAppointmentDateAndAppointmentTime(1L, date, date);

        Assert.assertTrue(appointments.iterator().hasNext());
    }

}
