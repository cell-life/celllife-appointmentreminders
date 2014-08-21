package org.celllife.appointmentreminders.application.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class RelativeCampaignJobRunner extends QuartzJobBean {
	
	public static final String PROP_MESSAGE_ID = "campaignId";

	public static final String PROP_MSGTIME = "msgTime";

	private Long campaignId;

	private Date msgTime;

	private ApplicationContext applicationContext;
	
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		RelativeCampaignJob job = (RelativeCampaignJob) applicationContext.getBean(RelativeCampaignJob.NAME);
		job.sendMessagesForCampaign(campaignId, msgTime);
	}
	
	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	public void setMsgTime(Date msgTime) {
		this.msgTime = msgTime;
	}
}
