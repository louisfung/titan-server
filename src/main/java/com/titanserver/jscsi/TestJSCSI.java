package com.titanserver.jscsi;

import java.io.File;

import org.jscsi.exception.ConfigurationException;
import org.jscsi.exception.NoSuchSessionException;
import org.jscsi.exception.TaskExecutionException;
import org.jscsi.initiator.Configuration;
import org.jscsi.initiator.Initiator;

public class TestJSCSI {
	public static void main(String[] args) throws NoSuchSessionException, TaskExecutionException, ConfigurationException {
		//init of the target
		String target = "peterdisk";
		Initiator initiator = new Initiator(Configuration.create());
		initiator.createSession(target);
		System.out.println(initiator.getBlockSize(target));
		//creating session, performing login on target
		//		initiator.createSession(target);
		//closing the session
		//		initiator.closeSession(target);
	}
}
