package org.celllife.appointmentreminders.application.jobs;

import org.celllife.appointmentreminders.application.appointment.AppointmentService;
import org.celllife.appointmentreminders.application.message.MessageService;
import org.celllife.appointmentreminders.application.patient.PatientService;
import org.celllife.appointmentreminders.domain.appointment.Appointment;
import org.celllife.appointmentreminders.domain.exception.RequiredFieldIsNullException;
import org.celllife.appointmentreminders.domain.message.Message;
import org.celllife.appointmentreminders.domain.message.MessageState;
import org.celllife.appointmentreminders.domain.patient.Patient;
import org.celllife.appointmentreminders.integration.CommunicateService;
import org.celllife.mobilisr.api.rest.CampaignDto;
import org.celllife.mobilisr.api.rest.ContactDto;
import org.celllife.mobilisr.api.rest.MessageDto;
import org.celllife.mobilisr.client.MobilisrClient;
import org.celllife.mobilisr.client.exception.RestCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("fixedCampaignJob")
public class FixedCampaignJob {

	public static final String NAME = "fixedCampaignJob";
	
	private static final Logger log = LoggerFactory.getLogger(FixedCampaignJob.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private CommunicateService communicateService;

	protected void sendMessages(Long messageId) {

        Message message = messageService.getMessage(messageId);

        log.info("Now sending message with ID " + messageId);

        try {
            communicateService.sendOneSms(message);
        } catch (RestCommandException e1) {
            message.setMessageState(MessageState.ERROR);
            try {
                messageService.save(message);
            } catch (RequiredFieldIsNullException e2) {
                log.warn("Could not save message with ID " + message.getId() + ". Reason: " + e2.getMessage());
            }
        }

        message.setMessageState(MessageState.SENT);
        try {
            messageService.save(message);
        } catch (RequiredFieldIsNullException e) {
            log.warn("Could not save message with ID " + message.getId() + ". Reason: " + e.getMessage());
        }

    }

}
