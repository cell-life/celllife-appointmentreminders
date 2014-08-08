package org.celllife.appointmentreminders.interfaces.service;

import org.celllife.appointmentreminders.application.clinic.ClinicService;
import org.celllife.appointmentreminders.application.patient.PatientService;
import org.celllife.appointmentreminders.domain.clinic.Clinic;
import org.celllife.appointmentreminders.domain.patient.Patient;
import org.celllife.appointmentreminders.domain.patient.PatientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class PatientController {

    @Autowired
    PatientService patientService;

    @Autowired
    ClinicService clinicService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value= "/service/clinic/{clinicId}/patient", produces = MediaType.APPLICATION_JSON_VALUE)
    public PatientDto createPatient(@RequestBody List<PatientDto> patientDtos, @PathVariable Long clinicId, HttpServletResponse response) throws Exception {

        if (patientDtos.size() > 1) {
            throw new Exception("Sorry, unfortunately you can only add one patient at a time.");
        }

        Clinic clinic = clinicService.get(clinicId);
        if (clinic == null) {
            throw new Exception("Sorry, no clinic with id " + clinicId + " exists.");
        }

        //Create new patient
        PatientDto patientDto = patientDtos.get(0);
        Patient patient = new Patient(clinicId, patientDto.getMsisdn(), patientDto.getSubscribed());
        patient = patientService.save(patient);

        //Create data transfer object and send it back to the client
        response.setStatus(HttpServletResponse.SC_CREATED);
        return patient.getPatientDto();

    }

}
