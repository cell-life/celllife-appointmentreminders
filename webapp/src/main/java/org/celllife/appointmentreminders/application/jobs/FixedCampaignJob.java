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

        log.info("Now sending message with ID " + messageId);

        Appointment appointment = appointmentService.get(message.getAppointmentId());
        Patient patient = patientService.get(appointment.getPatientId());

        if (!patient.isSubscribed()) {
            log.debug("Not sending message to patient " + patient.getId() + " because patient is not subscribed.");
            return;
        } else if (message.getMessageState() == MessageState.SENT) {
            log.debug("Not sending message to patient " + patient.getId() + " because message has already been sent.");
            return;
        }

        try {

            Long communicateId = communicateService.sendOneSms(message);
            message.setMessageState(MessageState.SENT);
            message.setCommunicateId(communicateId);
            message.setMessageSent(new Date());
            try {
                messageService.save(message);
            } catch (RequiredFieldIsNullException e) {
                log.warn("Could not save message with ID " + message.getId() + ". Reason: " + e.getMessage());
            }

        } catch (Exception e1) {

            message.setMessageState(MessageState.FAILED);
            log.warn("Could not send message with ID " + messageId + ". Reason: " + e1.getLocalizedMessage());
            try {
                messageService.save(message);
            } catch (RequiredFieldIsNullException e2) {
                log.warn("Could not save message with ID " + message.getId() + ". Reason: " + e2.getMessage());
            }
        }

    }

}
