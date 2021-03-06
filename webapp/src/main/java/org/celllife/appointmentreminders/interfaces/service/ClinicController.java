package org.celllife.appointmentreminders.interfaces.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.celllife.appointmentreminders.application.clinic.ClinicService;
import org.celllife.appointmentreminders.domain.clinic.Clinic;
import org.celllife.appointmentreminders.domain.clinic.ClinicDto;
import org.celllife.appointmentreminders.domain.exception.ClinicCodeExistsException;
import org.celllife.security.utils.SecurityServiceUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClinicController {

    private Logger log = LoggerFactory.getLogger(this.getClass().getName().toString());

    @Autowired
    ClinicService clinicService;

    @Value("${external.base.url}")
    String baseUrl;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value= "/service/clinic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ClinicDto createClinic(@RequestBody ClinicDto clinicDto, HttpServletResponse response) {

        //Create new clinic and encrypt the password
        Clinic clinic = new Clinic(clinicDto.getName(), clinicDto.getCode());
        SecurityServiceUtils.setPasswordAndSalt(clinic, clinicDto.getPassword());
        try {
            clinic = clinicService.save(clinic);
        } catch (ClinicCodeExistsException e) {
            log.warn(e.getMessage());
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return null;
        }

        //Create data transfer object and send it back to the client
        //response.setStatus(HttpServletResponse.SC_CREATED); //FIXME: the Mobilisr client (in iDart) throws an exception when it gets 201 Created
        response.addHeader("Link", baseUrl + "/service/clinic/" + clinic.getId());
        return clinic.getClinicDto();

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT,value = "/service/clinic/{clinicId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ClinicDto updateClinic(@RequestBody ClinicDto clinicDto, @PathVariable Long clinicId, HttpServletResponse response) {

        // Retrieve the clinic entity with id clinicId
        Clinic clinic = clinicService.get(clinicId);
        if (clinic == null) {
            log.warn("A clinic with this identifier does not exist. Please check the url.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        // Update the clinic where necessary and save
        if (clinicDto.getName() != null)
            clinic.setName(clinicDto.getName());
        if (clinicDto.getCode() != null)
            clinic.setCode(clinicDto.getCode());

        try {
            clinic = clinicService.save(clinic);
        } catch (ClinicCodeExistsException e) {
            // this exception should never occur since the check here is if the id is null and there is already a clinic
            // with the specified code. since we retrieve the clinic a couple lines above, the id should never be null.
            log.warn(e.getMessage());
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return null;
        }

        return clinic.getClinicDto();

    }

    @ResponseBody
    @RequestMapping(value = "/service/clinics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ClinicDto> getAllClinics() {

        List<Clinic> clinics = clinicService.getAllClinics();
        List<ClinicDto> clinicDtos = new ArrayList<>();

        for (Clinic clinic : clinics) {
            clinicDtos.add(clinic.getClinicDto());
        }

        return clinicDtos;

    }

    @ResponseBody
    @RequestMapping(value = "/service/clinic/{clinicId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ClinicDto getClinic(@PathVariable Long clinicId, HttpServletResponse response) {

        Clinic clinic = clinicService.get(clinicId);

        if (clinic == null) {
            log.warn("Could not find clinic with id " + clinicId);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        return clinic.getClinicDto();

    }

}
