package com.titanserver.quartz;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CPUMemoryJob implements Job {
	private static Logger logger = Logger.getLogger(CPUMemoryJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("exe");

	}
}
