package org.celllife.appointmentreminders.application.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class FixedCampaignJobRunner extends QuartzJobBean {
	
	public static final String PROP_CAMPAIGN_ID = "messageId";

	private Long messageId;

	private ApplicationContext applicationContext;

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext)throws JobExecutionException {
		FixedCampaignJob job = (FixedCampaignJob) applicationContext.getBean(FixedCampaignJob.NAME);
		job.sendMessages(messageId);
	}
	
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
