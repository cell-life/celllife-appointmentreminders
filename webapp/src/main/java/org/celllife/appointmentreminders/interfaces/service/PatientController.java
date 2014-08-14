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

@Controller
public class PatientController {

    @Autowired
    PatientService patientService;

    @Autowired
    ClinicService clinicService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value= "/service/patient", produces = MediaType.APPLICATION_JSON_VALUE)
    public PatientDto createPatient(@RequestBody PatientDto patientDto, @RequestParam(required = true) String clinicCode, HttpServletResponse response) throws Exception {

        Clinic clinic = clinicService.findClinicByCode(clinicCode);
        if (clinic == null) {
            throw new Exception("Sorry, no clinic with code " + clinicCode + " exists.");
        }

        //Create new patient
        Patient patient = new Patient(clinic.getId(), patientDto.getPatientCode(), patientDto.getMsisdn(), patientDto.getSubscribed());
        patient = patientService.save(patient);

        //Create data transfer object and send it back to the client
        response.setStatus(HttpServletResponse.SC_CREATED);
        return patient.getPatientDto();

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, value= "/service/patient", produces = MediaType.APPLICATION_JSON_VALUE)
    public PatientDto updatePatient(@RequestBody PatientDto patientDto, @RequestParam(required = true) String clinicCode, @RequestParam(required = true) String patientCode, HttpServletResponse response) throws Exception {

        //Create new patient
        Patient patient = patientService.findByPatientCodeAndClinicCode(patientCode,clinicCode);
        patient.setMsisdn(patientDto.getMsisdn());
        patient.setSubscribed(patientDto.getSubscribed());
        patient = patientService.save(patient);

        //Create data transfer object and send it back to the client
        response.setStatus(HttpServletResponse.SC_CREATED);
        return patient.getPatientDto();

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/service/patient",  produces = MediaType.APPLICATION_JSON_VALUE)
    public PatientDto getPatient(@RequestParam(required = true)  String patientCode, @RequestParam(required = true)  String clinicCode) throws Exception {

        Patient patient = patientService.findByPatientCodeAndClinicCode(patientCode, clinicCode);
        return patient.getPatientDto();

    }

}
