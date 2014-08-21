package org.celllife.appointmentreminders.application.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("relativeCampaignJob")
public class RelativeCampaignJob {

    public static final String NAME = "relativeCampaignJob";

    private static final Logger log = LoggerFactory.getLogger(RelativeCampaignJob.class);

    public void sendMessagesForCampaign(Long campaignId, Date msgTime) {

        /*Campaign campaign = campaignService.getCampaign(campaignId);

        try {
            processCampaignMessagesToSend(campaignId, msgSlot, msgTime);
        } catch (Exception e) {
            log.error("Error running relative campaign job. [campaign=" + campaignId + "]", e);
        } */

    }

    private void processCampaignMessagesToSend(Long campaignId, Integer messageSlot, Date messageTime) {

        /*Campaign campaign = campaignService.getCampaign(campaignId);

        List<CampaignMessage> campaignMessages = campaignMessageService.findMessagesForTimeSlot(campaignId, messageTime, messageSlot);
        List<Contact> campaignContacts = contactService.findNonVoidedContactsInCampaign(campaignId);

        log.info("sending messages for relative campaign: [id={}], [msgSlot={}], [msgTime={}]]",
                new Object[]{campaignId, messageSlot, messageTime});

        for (Contact campaignContact : campaignContacts) {

            CampaignMessage campaignMessage = getMessageForContact(campaignContact, campaignMessages);

            if (campaignMessage != null) {

                String response;
                try {
                    response = verboiceApplicationService.enqueueCallForMsisdn(campaign, campaignContact.getMsisdn(), campaignMessage.getVerboiceMessageNumber(), campaignContact.getPassword());
                } catch (Exception e) {
                    response = null;
                    log.error("Error enqueuing call to Verboice. Call will be logged as 'waiting'. [campaign=" + campaignId + "]", e);
                }

                updateCallLog(response, campaignContact, campaignMessage.getVerboiceMessageNumber(), campaign);

                campaignContact.setProgress(campaignMessage.getVerboiceMessageNumber());

                try {
                    contactService.saveContact(campaignContact);
                } catch (ContactExistsException e) {
                    log.warn("Error saving contact with id " + campaignContact.getId() + " Reason: " + e.getMessage());
                }
            }

        }            */

    }

    /*protected void updateCallLog(String response, Contact contact, Integer messageNumber, Campaign campaign) {

        //if no response from Verboice, log the call as "waiting"
        if (response == null) {
            log.warn("No response from Verboice server.");
            CallLog callLog = new CallLog(new Date(), null, contact.getMsisdn(),
                    campaign.getChannelName(), campaign.getCallFlowName(), campaign.getScheduleName(), "waiting", messageNumber, contact.getPassword(), campaign.getVerboiceProjectId().intValue(), 1, false, campaign.getId());
            callLogService.saveCallLog(callLog);
            return;
        }

        Map<String, String> responseVariables = new HashMap<String,String>();
        try {
            responseVariables = jsonUtils.extractJsonVariables("{\"response\":" + response + "}");
        } catch (JSONException e) {
            log.warn("Unrecognized Response from Verboice Server. Response: " + response, e.getMessage());
        }

        // log the call with response variables from Verboice
        if (responseVariables.containsKey("call_id")) {
            CallLog callLog = new CallLog(new Date(), Long.parseLong(responseVariables.get("call_id")), contact.getMsisdn(),
                    campaign.getChannelName(), campaign.getCallFlowName(), campaign.getScheduleName(), responseVariables.get("state"), messageNumber, contact.getPassword(), campaign.getVerboiceProjectId().intValue(), 1, false, campaign.getId());
            callLogService.saveCallLog(callLog);
        }
        //if no response from Verboice, log the call as "waiting"
        else {
            log.warn("No call ID returned from Verboice server.");
            CallLog callLog = new CallLog(new Date(), null, contact.getMsisdn(),
                    campaign.getChannelName(), campaign.getCallFlowName(), campaign.getScheduleName(), "waiting", messageNumber, contact.getPassword(), campaign.getVerboiceProjectId().intValue(), 1, false, campaign.getId());
            callLogService.saveCallLog(callLog);
        }

    }*/

    /*protected CampaignMessage getMessageForContact(Contact campaignContact, List<CampaignMessage> campaignMessages) {

        for (CampaignMessage campaignMessage : campaignMessages) {
            if ((campaignContact.getProgress() + 1) ==  campaignMessage.getSequenceNumber()) {
                return campaignMessage;
            }
        }

        return null;
    }       */

}
