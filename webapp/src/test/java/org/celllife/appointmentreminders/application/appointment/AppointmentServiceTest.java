package org.celllife.appointmentreminders.application.appointment;

import junit.framework.Assert;
import org.celllife.appointmentreminders.domain.appointment.Appointment;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.test.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class AppointmentServiceTest extends TestConfiguration {

    @Autowired
    AppointmentService appointmentService;

    @Test
    public void testDelete() throws RequiredFieldIsNullException {

        Appointment appointment = new Appointment();
        appointment.setPatientId(1L);
        appointment.setAppointmentDate(new Date());
        appointment.setAppointmentTime(new Date());
        appointment = appointmentService.save(appointment);

        Long id = appointment.getId();

        appointmentService.delete(id);

        Assert.assertNull(appointmentService.get(id));

    }

    @Test
    public void testAppointmentExists() throws RequiredFieldIsNullException {

        Date date = new Date();
        Date time = new Date();

        Appointment appointment = new Appointment();
        appointment.setPatientId(1L);
        appointment.setAppointmentDate(date);
        appointment.setAppointmentTime(time);
        appointmentService.save(appointment);

        Assert.assertTrue(appointmentService.appointmentExists(1L,date,time));

    }

    @Test
    public void testFindByPatientIdAndDateTimeStamp() throws RequiredFieldIsNullException {

        Date date = new Date();
        Date time = new Date();

        Appointment appointment = new Appointment();
        appointment.setPatientId(1L);
        appointment.setAppointmentDate(date);
        appointment.setAppointmentTime(time);
        appointmentService.save(appointment);

        List<Appointment> appointmentList = appointmentService.findByPatientIdAndDateTimeStamp(1L,date,time);

        Assert.assertEquals(1L,appointmentList.size());

    }

}
