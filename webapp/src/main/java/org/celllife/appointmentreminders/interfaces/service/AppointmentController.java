package org.celllife.appointmentreminders.interfaces.service;

import org.celllife.appointmentreminders.application.appointment.AppointmentService;
import org.celllife.appointmentreminders.application.message.MessageService;
import org.celllife.appointmentreminders.application.patient.PatientService;
import org.celllife.appointmentreminders.domain.appointment.Appointment;
import org.celllife.appointmentreminders.domain.appointment.AppointmentDto;
import org.celllife.appointmentreminders.domain.appointment.AppointmentWithMessagesDto;
import org.celllife.appointmentreminders.domain.exception.ClinicCodeNonexistentException;
import org.celllife.appointmentreminders.domain.exception.InvalidDateException;
import org.celllife.appointmentreminders.domain.exception.PatientCodeNonexistentException;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.domain.message.Message;
import org.celllife.appointmentreminders.domain.message.MessageDto;
import org.celllife.appointmentreminders.domain.patient.Patient;
import org.celllife.appointmentreminders.framework.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
public class AppointmentController {

    private Logger log = LoggerFactory.getLogger(this.getClass().getName().toString());

    public static final int SC_UNPROCESSABLE_ENTITY = 422;

    @Value("${external.base.url}")
    String baseUrl;

    @Autowired
    PatientService patientService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    MessageService messageService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value= "/service/appointment", produces = MediaType.APPLICATION_JSON_VALUE)
    public AppointmentDto createAppointment(@RequestBody AppointmentWithMessagesDto appointmentDto, @RequestParam(required = true) String clinicCode, @RequestParam(required = true) String patientCode, HttpServletResponse response) {

        Patient patient;
        try {
            patient = patientService.findByPatientCodeAndClinicCode(clinicCode,patientCode);
        } catch (ClinicCodeNonexistentException | PatientCodeNonexistentException e) {
            log.warn(e.getLocalizedMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        Appointment appointment;
        try {
            appointment = new Appointment(patient.getId(), DateUtil.getDateFromString(appointmentDto.getAppointmentDate()), DateUtil.getTimeFromString(appointmentDto.getAppointmentTime()));
        } catch (InvalidDateException e) {
            log.warn(e.getLocalizedMessage());
            response.setStatus(SC_UNPROCESSABLE_ENTITY);
            return null;
        }
        try {
            appointment = appointmentService.save(appointment);
        } catch (RequiredFieldIsNullException e) {
            log.warn(e.getLocalizedMessage());
            response.setStatus(SC_UNPROCESSABLE_ENTITY);
            return null;
        }

        log.debug("Saving appointment with id " + appointment.getId() + ", date " + appointmentDto.getAppointmentDate() + ", time " + appointmentDto.getAppointmentTime());

        for (MessageDto messageDto : appointmentDto.getMessages()) {
            Message message = null;
            try {
                message = new Message(appointment.getId(),
                        DateUtil.getDateFromString(messageDto.getMessageDate()),
                        DateUtil.getTimeFromString(messageDto.getMessageTime()),
                        messageDto.getMessageText(),
                        messageDto.getMessageType());
            } catch (InvalidDateException e) {
                log.warn(e.getLocalizedMessage());
                response.setStatus(SC_UNPROCESSABLE_ENTITY);
                return null;
            }
            try {
                messageService.save(message);
            } catch (RequiredFieldIsNullException e) {
                log.warn(e.getLocalizedMessage());
                response.setStatus(SC_UNPROCESSABLE_ENTITY);
                return null;
            }

            log.debug("Saving message with id " + message.getId() + ", date " + appointmentDto.getAppointmentDate() + ", time " + appointmentDto.getAppointmentTime());

        }

        //response.setStatus(HttpServletResponse.SC_CREATED); //FIXME: the Mobilisr client (in iDart) throws an exception when it gets 201 Created
        response.addHeader("Link", baseUrl + "/service/appointment/" + appointment.getId());
        return appointment.getAppointmentDto();

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, value= "/service/appointment", produces = MediaType.APPLICATION_JSON_VALUE)
    public AppointmentDto updateAppointment(@RequestBody AppointmentWithMessagesDto appointmentDto, @RequestParam(required = true) String clinicCode, @RequestParam(required = true) String patientCode, HttpServletResponse response) {

        Patient patient = null;
        try {
            patient = patientService.findByPatientCodeAndClinicCode(patientCode,clinicCode);
        } catch (ClinicCodeNonexistentException | PatientCodeNonexistentException e) {
            log.warn(e.getLocalizedMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        Appointment appointment;
        try {
            appointment = new Appointment(patient.getId(), DateUtil.getDateFromString(appointmentDto.getAppointmentDate()), DateUtil.getTimeFromString(appointmentDto.getAppointmentTime()));
            appointment = appointmentService.save(appointment);
        } catch (InvalidDateException | RequiredFieldIsNullException e) {
            log.warn(e.getLocalizedMessage());
            response.setStatus(SC_UNPROCESSABLE_ENTITY);
            return null;
        }

        log.debug("Saved appointment with id " + appointment.getId() + ", date " + appointmentDto.getAppointmentDate() + ", time " + appointmentDto.getAppointmentTime());

        for (MessageDto messageDto : appointmentDto.getMessages()) {
            Message message;
            try {
                message = new Message(appointment.getId(),
                        DateUtil.getDateFromString(messageDto.getMessageDate()),
                        DateUtil.getTimeFromString(messageDto.getMessageTime()),
                        messageDto.getMessageText(),
                        messageDto.getMessageType());
                message = messageService.save(message);
            } catch (InvalidDateException | RequiredFieldIsNullException e) {
                log.warn(e.getLocalizedMessage());
                response.setStatus(SC_UNPROCESSABLE_ENTITY);
                return null;
            }

            log.debug("Saved message with id " + message.getId() + ", date " + messageDto.getMessageDate() + ", time " + messageDto.getMessageTime());

        }

        //response.setStatus(HttpServletResponse.SC_CREATED); //FIXME: the Mobilisr client (in iDart) throws an exception when it gets 201 Created
        response.addHeader("Link", baseUrl + "/service/appointment/" + appointment.getId());
        return appointment.getAppointmentDto();

    }

    @ResponseBody
    @RequestMapping(value = "/service/appointment/{appointmentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public AppointmentDto getAppointment(@PathVariable Long appointmentId, HttpServletResponse response) {

       Appointment appointment = appointmentService.get(appointmentId);

        if (appointment == null) {
            log.warn("Could not find appointment with id " + appointmentId);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        return appointment.getAppointmentDto();

    }

}
