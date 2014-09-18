package org.celllife.appointmentreminders.interfaces.service;

import javax.servlet.http.HttpServletResponse;

import org.celllife.appointmentreminders.application.clinic.ClinicService;
import org.celllife.appointmentreminders.application.patient.PatientService;
import org.celllife.appointmentreminders.domain.clinic.Clinic;
import org.celllife.appointmentreminders.domain.exception.ClinicCodeNonexistentException;
import org.celllife.appointmentreminders.domain.exception.InvalidMsisdnException;
import org.celllife.appointmentreminders.domain.exception.PatientCodeExistsException;
import org.celllife.appointmentreminders.domain.exception.PatientCodeNonexistentException;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.domain.patient.Patient;
import org.celllife.appointmentreminders.domain.patient.PatientDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PatientController {

    private Logger log = LoggerFactory.getLogger(this.getClass().getName().toString());

    public static final int SC_UNPROCESSABLE_ENTITY = 422;

    @Value("${external.base.url}")
    String baseUrl;

    @Autowired
    PatientService patientService;

    @Autowired
    ClinicService clinicService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value= "/service/patient", produces = MediaType.APPLICATION_JSON_VALUE)
    public PatientDto createPatient(@RequestBody PatientDto patientDto, @RequestParam(required = true) String clinicCode, HttpServletResponse response) {

        Clinic clinic = null;
        try {
            clinic = clinicService.findClinicByCode(clinicCode);
        } catch (ClinicCodeNonexistentException e) {
            log.warn("Sorry, no clinic with code " + clinicCode + " exists.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        //Create new patient
        Patient patient = null;
        try {
            patient = new Patient(clinic.getId(), patientDto.getPatientCode(), patientDto.getMsisdn(), patientDto.getSubscribed());
        } catch (InvalidMsisdnException  e) {
            log.warn(e.getMessage());
            response.setStatus(SC_UNPROCESSABLE_ENTITY);
            return null;
        }
        try {
            patient = patientService.save(patient);
        } catch (PatientCodeExistsException e) {
            log.warn(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (RequiredFieldIsNullException e) {
            log.warn(e.getMessage());
            response.setStatus(SC_UNPROCESSABLE_ENTITY);
            return null;
        }

        //response.setStatus(HttpServletResponse.SC_CREATED); //FIXME: the Mobilisr client (in iDart) throws an exception when it gets 201 Created
        response.addHeader("Link", baseUrl + "/service/patient/" + clinic.getId());
        return patient.getPatientDto();

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, value= "/service/patient", produces = MediaType.APPLICATION_JSON_VALUE)
    public PatientDto updatePatient(@RequestBody PatientDto patientDto, @RequestParam(required = true) String clinicCode, HttpServletResponse response) {

        Patient patient = null;
        // try and fetch an existing patient
        try {
            patient = patientService.findByPatientCodeAndClinicCode(patientDto.getPatientCode(),clinicCode);
        } catch (ClinicCodeNonexistentException | PatientCodeNonexistentException e) { // If the patient does not exist, create a new one.

            log.info("A patient with clinic code " + clinicCode + " and patient code " + patientDto.getPatientCode() + " does not exist. System will create a new one.");
            Clinic clinic = null;
            try {
                clinic = clinicService.findClinicByCode(clinicCode);
            } catch (ClinicCodeNonexistentException ex) {
                log.warn("Sorry, no clinic with code " + clinicCode + " exists.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            }
            try {
                patient =  new Patient(clinic.getId(), patientDto.getPatientCode(), patientDto.getMsisdn(), patientDto.getSubscribed());
            } catch (InvalidMsisdnException e1) {
                log.warn(e.getMessage());
                response.setStatus(SC_UNPROCESSABLE_ENTITY);
                return null;
            }
        }

        // try and save the updated patient
        try {
            patient.setMsisdn(patientDto.getMsisdn());
            patient.setSubscribed(patientDto.getSubscribed());
            patient = patientService.save(patient);
        } catch (InvalidMsisdnException | RequiredFieldIsNullException e) {
            log.warn(e.getMessage());
            response.setStatus(SC_UNPROCESSABLE_ENTITY);
            return null;
        } catch (PatientCodeExistsException e) {
            log.warn(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }

        //Create data transfer object and send it back to the client
        return patient.getPatientDto();

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/service/patient",  produces = MediaType.APPLICATION_JSON_VALUE)
    public PatientDto getPatient(@RequestParam(required = true)  String patientCode, @RequestParam(required = true)  String clinicCode, HttpServletResponse response) {

        Patient patient = null;
        try {
            patient = patientService.findByPatientCodeAndClinicCode(patientCode, clinicCode);
        } catch (ClinicCodeNonexistentException | PatientCodeNonexistentException e) {
           log.warn(e.getMessage());
           response.setStatus(HttpServletResponse.SC_NOT_FOUND);
           return null;
        }

        return patient.getPatientDto();

    }

}
