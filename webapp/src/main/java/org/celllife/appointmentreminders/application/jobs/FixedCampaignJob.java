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
import org.celllife.mobilisr.client.exception.RestCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("fixedCampaignJob")
public class FixedCampaignJob {

	public static final String NAME = "fixedCampaignJob";
	
	private static final Logger log = LoggerFactory.getLogger(FixedCampaignJob.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private CommunicateService communicateService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

	protected void sendMessages(Long messageId) {

        Message message = messageService.getMessage(messageId);

        log.info("Now sending message with id " + messageId);

        Appointment appointment;
        try {
            appointment = appointmentService.get(message.getAppointmentId());
        } catch (NullPointerException e) {
            log.warn("Could not find appointment. Cannot send message with id " + messageId);
            return;
        }

        Patient patient;
        try {
            patient = patientService.get(appointment.getPatientId());
        } catch (NullPointerException e) {
            log.warn("Could not find patient with id. Cannot send message with id " + messageId);
            return;
        }

        if (!patient.isSubscribed()) {
            log.debug("Not sending message to patient " + patient.getId() + " because patient is not subscribed.");
            return;
        } else if (message.getMessageState() == MessageState.SENT_TO_COMMUNICATE) {
            log.debug("Not sending message to patient " + patient.getId() + " because message has already been sent.");
            return;
        }

        try {

            Long communicateId = communicateService.sendOneSms(message);
            message.setMessageState(MessageState.SENT_TO_COMMUNICATE);
            message.setCommunicateId(communicateId);
            message.setMessageSent(new Date());
            try {
                messageService.save(message);
            } catch (RequiredFieldIsNullException e) {
                log.warn("Could not save message with ID " + message.getId() + ". Reason: " + e.getMessage(), e);
            }

        } catch (RestCommandException e1) {

            message.setMessageState(MessageState.FAILED_SENDING_TO_COMMUNICATE);
            log.warn("Could not send message with ID " + messageId + ". Reason: " + e1.getMessage(), e1);
            try {
                messageService.save(message);
            } catch (RequiredFieldIsNullException e2) {
                log.warn("Could not save message with ID " + message.getId() + ". Reason: " + e2.getMessage(), e2);
            }
        }

    }

}
