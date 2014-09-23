package org.celllife.appointmentreminders.domain.application;

import java.util.Date;
import java.util.List;

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

@RunWith(SpringJUnit4ClassRunner.class)
public class TestAppointmentRepository extends TestConfiguration {

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Test
    public void testFindByPatientIdAndDate() throws RequiredFieldIsNullException {

        Date date = new Date();

        Appointment appointment = new Appointment();
        appointment.setPatientId(1L);
        appointment.setAppointmentDate(date);
        appointment.setAppointmentTime(date);
        appointmentService.save(appointment);

        List<Appointment> appointments = appointmentRepository.findByPatientIdAndAppointmentDateAndAppointmentTime(1L, date, date);

        Assert.assertNotNull(appointments);
        Assert.assertEquals(1, appointments.size());
    }

    @Test
    public void testExistsfindByPatientIdAndDate() throws RequiredFieldIsNullException {

        Date date = new Date();

        Appointment appointment = new Appointment();
        appointment.setPatientId(1L);
        appointment.setAppointmentDate(date);
        appointment.setAppointmentTime(date);
        appointmentService.save(appointment);

        Assert.assertTrue(appointmentRepository.exists(1L, date, date));
    }
}
