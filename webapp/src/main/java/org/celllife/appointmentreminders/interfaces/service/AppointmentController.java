package org.celllife.appointmentreminders.interfaces.service;

import org.celllife.appointmentreminders.application.appointment.AppointmentService;
import org.celllife.appointmentreminders.application.message.MessageService;
import org.celllife.appointmentreminders.application.patient.PatientService;
import org.celllife.appointmentreminders.domain.appointment.Appointment;
import org.celllife.appointmentreminders.domain.appointment.AppointmentDto;
import org.celllife.appointmentreminders.domain.appointment.AppointmentWithMessagesDto;
import org.celllife.appointmentreminders.domain.message.Message;
import org.celllife.appointmentreminders.domain.message.MessageDto;
import org.celllife.appointmentreminders.domain.patient.Patient;
import org.celllife.appointmentreminders.framework.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
public class AppointmentController {

    @Autowired
    PatientService patientService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    MessageService messageService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value= "/service/appointment", produces = MediaType.APPLICATION_JSON_VALUE)
    public AppointmentDto createAppointment(@RequestBody AppointmentWithMessagesDto appointmentDto, @RequestParam(required = true) String clinicCode, @RequestParam(required = true) String patientCode, HttpServletResponse response) throws Exception{

        Patient patient = patientService.findByPatientCodeAndClinicCode(clinicCode,patientCode);

        Appointment appointment = new Appointment(patient.getId(),DateUtil.getDateFromString(appointmentDto.getAppointmentDate()),DateUtil.getTimeFromString(appointmentDto.getAppointmentTime()));
        appointment = appointmentService.save(appointment);

        for (MessageDto messageDto : appointmentDto.getMessages()) {
            Message message = new Message(appointment.getId(),DateUtil.getDateFromString(messageDto.getMessageDate()),DateUtil.getTimeFromString(messageDto.getMessageTime()),messageDto.getMessageText());
            messageService.save(message);
        }

        //Create data transfer object and send it back to the client
        response.setStatus(HttpServletResponse.SC_CREATED);
        return appointment.getAppointmentDto();

    }

}
