package org.celllife.appointmentreminders.application.jobs;

import org.celllife.appointmentreminders.application.appointment.AppointmentService;
import org.celllife.appointmentreminders.application.clinic.ClinicService;
import org.celllife.appointmentreminders.application.message.MessageService;
import org.celllife.appointmentreminders.application.patient.PatientService;
import org.celllife.appointmentreminders.domain.appointment.Appointment;
import org.celllife.appointmentreminders.domain.clinic.Clinic;
import org.celllife.appointmentreminders.domain.exception.ClinicCodeExistsException;
import org.celllife.appointmentreminders.domain.exception.InvalidMsisdnException;
import org.celllife.appointmentreminders.domain.exception.PatientCodeExistsException;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.domain.message.Message;
import org.celllife.appointmentreminders.domain.patient.Patient;
import org.celllife.appointmentreminders.test.TestConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestFixedCampaignJob extends TestConfiguration {

    @Autowired
    private FixedCampaignJob fixedCampaignJob;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private MessageService messageService;

    @Ignore
    @Test
    public void testSendMessagesForCampaign() throws ClinicCodeExistsException, InvalidMsisdnException, PatientCodeExistsException, RequiredFieldIsNullException {

        Clinic clinic = new Clinic();
        clinic.setName("Test Clinic");
        clinic.setCode("911");
        clinic.setEncryptedPassword("abcd");
        clinic.setSalt("xxxx");
        clinic = clinicService.save(clinic);

        Patient patient = new Patient();
        patient.setPatientCode("0001");
        patient.setSubscribed(true);
        patient.setClinicId(clinic.getId());
        patient.setMsisdn("27724194158");
        patient = patientService.save(patient);

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(getThreeDaysLater());
        appointment.setAppointmentTime(new Date());
        appointment.setPatientId(patient.getId());
        appointment = appointmentService.save(appointment);

        Message message = new Message();
        message.setMessageDate(getTwoMinutesLater());
        message.setMessageTime(getTwoMinutesLater());
        message.setMessageText("Testing AR.");
        message.setAppointmentId(appointment.getId());
        message = messageService.save(message);

        fixedCampaignJob.sendMessages(message.getId());

    }

    private Date getThreeDaysLater() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,3);
        return calendar.getTime();
    }

    private Date getTwoMinutesLater() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,2);
        return calendar.getTime();
    }

}
