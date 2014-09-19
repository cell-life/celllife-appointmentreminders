package org.celllife.appointmentreminders.interfaces.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
            patient = patientService.findByPatientCodeAndClinicCode(patientCode, clinicCode);
        } catch (ClinicCodeNonexistentException | PatientCodeNonexistentException e) {
            log.warn(e.getLocalizedMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        Appointment appointment = null;
        try {
            Date appointmentDate = DateUtil.getDateFromString(appointmentDto.getAppointmentDate());
            Date appointmentTime = DateUtil.getTimeFromString(appointmentDto.getAppointmentTime());
            
            if (appointmentService.appointmentExists(patient.getId(), appointmentDate, appointmentTime)) {
                log.warn("The appointment already exists, so cannot create a new appointment." + appointment);
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                return null;
            }
            appointment = new Appointment(patient.getId(), appointmentDate, appointmentTime);
            appointment.setAttended(appointmentDto.getAttended());
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

        if (appointmentDto.getMessages() != null) {
            for (MessageDto messageDto : appointmentDto.getMessages()) {
                Message message = null;
                try {
                    message = new Message(appointment.getId(),
                            DateUtil.getDateFromString(messageDto.getMessageDate()),
                            DateUtil.getTimeFromString(messageDto.getMessageTime()),
                            messageDto.getMessageText(),
                            messageDto.getMessageType(),
                            messageDto.getMessageSlot());
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

        Appointment appointment = null;
        try {
            Date appointmentDate = DateUtil.getDateFromString(appointmentDto.getAppointmentDate());
            Date appointmentTime = DateUtil.getTimeFromString(appointmentDto.getAppointmentTime());
            
            if (appointmentService.appointmentExists(patient.getId(), appointmentDate, appointmentTime)) {
                List<Appointment> appointments = appointmentService.findByPatientIdAndDateTimeStamp(patient.getId(), appointmentDate, appointmentTime);
                appointment = appointments.get(0);
            } else {
                // create a new appointment if one doesn't exist
                appointment = new Appointment(patient.getId(), DateUtil.getDateFromString(appointmentDto.getAppointmentDate()), DateUtil.getTimeFromString(appointmentDto.getAppointmentTime()));
            }
            appointment.setAttended(appointmentDto.getAttended());
            appointment = appointmentService.save(appointment);
        } catch (InvalidDateException | RequiredFieldIsNullException e) {
            log.warn(e.getLocalizedMessage());
            response.setStatus(SC_UNPROCESSABLE_ENTITY);
            return null;
        }

        log.debug("Saved appointment with id " + appointment.getId() + ", date " + appointmentDto.getAppointmentDate() + ", time " + appointmentDto.getAppointmentTime());

        // append the specified messages
        if (appointmentDto.getMessages() != null) {
            for (MessageDto messageDto : appointmentDto.getMessages()) {
                Message message;
                try {
                    message = new Message(appointment.getId(),
                            DateUtil.getDateFromString(messageDto.getMessageDate()),
                            DateUtil.getTimeFromString(messageDto.getMessageTime()),
                            messageDto.getMessageText(),
                            messageDto.getMessageType(),
                            messageDto.getMessageSlot());
                    message = messageService.save(message);
                } catch (InvalidDateException | RequiredFieldIsNullException e) {
                    log.warn(e.getLocalizedMessage());
                    response.setStatus(SC_UNPROCESSABLE_ENTITY);
                    return null;
                }
    
                log.debug("Saved message with id " + message.getId() + ", date " + messageDto.getMessageDate() + ", time " + messageDto.getMessageTime());
    
            }
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
    
    @ResponseBody
    @RequestMapping(value = "/service/appointment/{appointmentId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AppointmentDto deleteAppointmentWithId(@PathVariable Long appointmentId, HttpServletResponse response) {

       Appointment appointment = appointmentService.delete(appointmentId);
       log.info("Deleted appointment "+appointment);

        if (appointment == null) {
            log.warn("Could not find appointment with id " + appointmentId);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        return appointment.getAppointmentDto();
    }
    
    @ResponseBody
    @RequestMapping(value = "/service/appointment", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AppointmentDto deleteAppointment(@RequestBody AppointmentDto appointmentDto, @RequestParam(required = true) String clinicCode, @RequestParam(required = true) String patientCode, HttpServletResponse response) {
        
        Patient patient = null;
        try {
            patient = patientService.findByPatientCodeAndClinicCode(patientCode,clinicCode);
        } catch (ClinicCodeNonexistentException | PatientCodeNonexistentException e) {
            log.warn(e.getLocalizedMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        
        try {
            Date appointmentDate = DateUtil.getDateFromString(appointmentDto.getAppointmentDate());
            Date appointmentTime = DateUtil.getTimeFromString(appointmentDto.getAppointmentTime());
    
            Appointment appointment = null;
            List<Appointment> appointments = appointmentService.findByPatientIdAndDateTimeStamp(patient.getId(), appointmentDate, appointmentTime);
            if (appointments == null || appointments.isEmpty()) {
                log.warn("Could not find appointment for patient " + patientCode + " at clinic " + clinicCode + " on " + appointmentDto.getAppointmentDate());
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            }
            appointment = appointments.get(0);
            log.info("Deleting appointment "+appointment);
            appointmentService.delete(appointment.getId());

            return appointment.getAppointmentDto();

        } catch (InvalidDateException e) {
            log.warn(e.getLocalizedMessage());
            response.setStatus(SC_UNPROCESSABLE_ENTITY);
            return null;
        }
    }
}
