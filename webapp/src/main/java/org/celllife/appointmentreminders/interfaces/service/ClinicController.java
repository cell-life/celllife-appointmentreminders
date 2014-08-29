package org.celllife.appointmentreminders.interfaces.service;

import org.celllife.appointmentreminders.application.clinic.ClinicService;
import org.celllife.appointmentreminders.domain.clinic.Clinic;
import org.celllife.appointmentreminders.domain.clinic.ClinicDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ClinicController {

    @Autowired
    ClinicService clinicService;

    @Value("${external.base.url}")
    String baseUrl;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value= "/service/clinic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ClinicDto createClinic(@RequestBody ClinicDto clinicDto, HttpServletResponse response) throws Exception{

        //Create new clinic
        Clinic clinic = new Clinic(clinicDto.getName(), clinicDto.getCode(), clinicDto.getEncryptedPassword(), clinicDto.getSalt());
        clinic = clinicService.save(clinic);

        //Create data transfer object and send it back to the client
        //response.setStatus(HttpServletResponse.SC_CREATED); //FIXME: the Mobilisr client (in iDart) throws an exception when it gets 201 Created
        response.addHeader("Link", baseUrl + "/service/clinic/" + clinic.getId());
        return clinic.getClinicDto();

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT,value = "/service/clinic/{clinicId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ClinicDto updateClinic(@RequestBody ClinicDto clinicDto, @PathVariable Long clinicId) throws Exception {

        // Retrieve the clinic entity with id clinicId
        Clinic clinic = clinicService.get(clinicId);
        if (clinic == null) {
            throw new Exception("A clinic with this identifier does not exist. Please check the url.");
        }

        // Update the clinic where necessary and save
        if (clinicDto.getName() != null)
            clinic.setName(clinicDto.getName());
        if (clinicDto.getCode() != null)
            clinic.setCode(clinicDto.getCode());
        if (clinicDto.getEncryptedPassword() != null)
            clinic.setEncryptedPassword(clinicDto.getEncryptedPassword());
        if (clinicDto.getSalt() != null)
            clinic.setSalt(clinicDto.getSalt());

        clinic = clinicService.save(clinic);

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
    public ClinicDto getClinic(@PathVariable Long clinicId) throws Exception {

        Clinic clinic = clinicService.get(clinicId);

        if (clinic == null) {
            throw new Exception("A clinic with this identifier does not exist. Please check the url.");
        }

        return clinic.getClinicDto();

    }

}
