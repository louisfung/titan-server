package com.titanserver.quartz;

import java.util.Date;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.titanserver.Command;
import com.titanserver.HibernateUtil;
import com.titanserver.ReturnCommand;
import com.titanserver.TitanServerCommonLib;
import com.titanserver.table.InstanceDiagnostics;

public class InstanceStatJob implements Job {
	private static Logger logger = Logger.getLogger(InstanceStatJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("executing schedule job : " + arg0.getJobInstance().toString());
		Session session = HibernateUtil.openSession();
		Object result = null;
		try {
			Command command = new Command();
			command.command = "from titan: nova list";
			ReturnCommand r = TitanServerCommonLib.execute(command);
			result = r.map.get("result");
			JSONArray servers = JSONObject.fromObject(result).getJSONArray("servers");
			for (int x = 0; x < servers.size(); x++) {
				JSONObject obj = servers.getJSONObject(x);
				String instanceID = TitanServerCommonLib.getJSONString(obj, "id", "");

				command.command = "from titan: nova diagnostics";
				HashMap<String, String> parameters = new HashMap<String, String>();
				parameters.put("$InstanceId", instanceID);
				command.parameters.add(parameters);
				ReturnCommand r2 = TitanServerCommonLib.execute(command);
				Date currentTime = new Date();

				JSONObject jsonObj = JSONObject.fromObject(r2.map.get("result"));

				Transaction tx = session.beginTransaction();
				InstanceDiagnostics d = new InstanceDiagnostics();
				d.setInstanceID(instanceID);
				d.setDate(currentTime);
				d.setCpu0_time(TitanServerCommonLib.getJSONInt(jsonObj, "cpu", 0));
				d.setHdd_errors(TitanServerCommonLib.getJSONInt(jsonObj, "hdd_errors", 0));
				d.setHdd_read(TitanServerCommonLib.getJSONInt(jsonObj, "hdd_read", 0));
				d.setHdd_read_req(TitanServerCommonLib.getJSONInt(jsonObj, "hdd_read_req", 0));
				d.setHdd_write(TitanServerCommonLib.getJSONInt(jsonObj, "hdd_write", 0));
				d.setHdd_write_req(TitanServerCommonLib.getJSONInt(jsonObj, "hdd_write_req", 0));
				d.setMemory(TitanServerCommonLib.getJSONInt(jsonObj, "memory", 0));
				d.setVda_errors(TitanServerCommonLib.getJSONInt(jsonObj, "vda_errors", 0));
				d.setVda_read(TitanServerCommonLib.getJSONInt(jsonObj, "vda_read", 0));
				d.setVda_read_req(TitanServerCommonLib.getJSONInt(jsonObj, "vda_read_req", 0));
				d.setVda_write(TitanServerCommonLib.getJSONInt(jsonObj, "vda_write", 0));
				d.setHdd_write_req(TitanServerCommonLib.getJSONInt(jsonObj, "hdd_write_req", 0));
				d.setVnet7_rx(TitanServerCommonLib.getJSONInt(jsonObj, "vnet7_rx", 0));
				d.setVnet7_rx_drop(TitanServerCommonLib.getJSONInt(jsonObj, "vnet7_rx_drop", 0));
				d.setVnet7_rx_errors(TitanServerCommonLib.getJSONInt(jsonObj, "vnet7_rx_errors", 0));
				d.setVnet7_rx_packets(TitanServerCommonLib.getJSONInt(jsonObj, "vnet7_rx_packets", 0));
				d.setVnet7_tx(TitanServerCommonLib.getJSONInt(jsonObj, "vnet7_tx", 0));
				d.setVnet7_tx_drop(TitanServerCommonLib.getJSONInt(jsonObj, "vnet7_tx_drop", 0));
				d.setVnet7_tx_errors(TitanServerCommonLib.getJSONInt(jsonObj, "vnet7_tx_errors", 0));
				d.setVnet7_tx_packets(TitanServerCommonLib.getJSONInt(jsonObj, "vnet7_tx_packets", 0));
				session.save(d);
				tx.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("result=" + result);
		} finally {
			if (session != null && session.isConnected()) {
				session.close();
			}
		}
	}
}
