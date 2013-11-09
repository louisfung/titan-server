package com.titanserver.quartz;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.DiskUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Tcp;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.titanserver.HibernateUtil;
import com.titanserver.table.ServerDiagnostics;

public class CPUMemoryStatJob implements Job {
	private static Logger logger = Logger.getLogger(CPUMemoryStatJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("executing schedule job : " + arg0.getJobInstance().toString());
		Session session = HibernateUtil.openSession();
		try {
			Sigar sigar = new Sigar();
			CpuPerc cpuPerc = sigar.getCpuPerc();
			Mem mem = sigar.getMem();
			DiskUsage diskUsage = sigar.getDiskUsage("/");
			Tcp tcp = sigar.getTcp();

			Transaction tx = session.beginTransaction();
			ServerDiagnostics d = new ServerDiagnostics();
			d.setCpu(cpuPerc.getCombined());
			d.setMemory(mem.getUsed());
			d.setDisk(diskUsage.getReads() + diskUsage.getWrites());
			d.setNetwork(tcp.getInSegs() + tcp.getOutSegs());
			session.save(d);
			tx.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (session != null && session.isConnected()) {
				session.close();
			}
		}
	}
}
