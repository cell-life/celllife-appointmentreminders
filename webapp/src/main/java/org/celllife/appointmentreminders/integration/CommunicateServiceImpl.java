package org.celllife.appointmentreminders.integration;

import org.celllife.appointmentreminders.application.appointment.AppointmentService;
import org.celllife.appointmentreminders.application.patient.PatientService;
import org.celllife.appointmentreminders.domain.appointment.Appointment;
import org.celllife.appointmentreminders.domain.message.Message;
import org.celllife.appointmentreminders.domain.patient.Patient;
import org.celllife.mobilisr.api.rest.CampaignDto;
import org.celllife.mobilisr.api.rest.ContactDto;
import org.celllife.mobilisr.api.rest.MessageDto;
import org.celllife.mobilisr.client.MobilisrClient;
import org.celllife.mobilisr.client.exception.RestCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommunicateServiceImpl implements CommunicateService {

    private Logger log = LoggerFactory.getLogger(this.getClass().getName().toString());

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private MobilisrClient communicateClient;

    @Override
    public Long sendOneSms(Message message) throws RestCommandException {

        List<MessageDto> messageDtoList = new ArrayList<>();
        MessageDto messageDto = new MessageDto();
        messageDto.setDate(message.getMessageDate());
        messageDto.setTime(message.getMessageTime());
        messageDto.setMsgDay(0);
        messageDto.setText(message.getMessageText());
        messageDtoList.add(messageDto);

        Appointment appointment = appointmentService.get(message.getAppointmentId());
        Patient patient = patientService.get(appointment.getPatientId());

        List<ContactDto> contactDtoList = new ArrayList<>();
        ContactDto contactDto = new ContactDto();
        contactDto.setMsisdn(patient.getMsisdn());
        contactDtoList.add(contactDto);

        CampaignDto campaignDto = new CampaignDto();
        campaignDto.setType("FIXED");
        campaignDto.setName("Appointment Reminder " + new Date());
        campaignDto.setMessages(messageDtoList);
        campaignDto.setContacts(contactDtoList);
        campaignDto.setDuration(1);
        campaignDto.setTimesPerDay(1);

        return communicateClient.getCampaignService().createNewCampaign(campaignDto);

    }

}
