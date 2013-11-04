package com.titanserver.quartz;

import org.apache.log4j.Logger;

public class CPUMemoryJob {
	private static Logger logger = Logger.getLogger(CPUMemoryJob.class);

	protected void execute() {
		logger.info("exe");
	}
}
