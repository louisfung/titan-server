package com.c2.pandoraserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.c2.pandoraserver.openstack_communication.OpenstackComm;
import com.c2.pandoraserver.table.InstancePermission;
import com.c2.pandoraserver.table.InstancePermissionGroup;
import com.c2.pandoraserver.table.ScreenPermission;
import com.c2.pandoraserver.table.ScreenPermissionGroup;
import com.c2.pandoraserver.table.User;

public class PandoraServerCommonLib {
	private static Logger logger = Logger.getLogger(PandoraServerCommonLib.class);

	public static void initDB() {
		logger.info("initDB()");
		Session session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		try {
			ScreenPermissionGroup adminGroup = new ScreenPermissionGroup();
			adminGroup.setName("Administrator");
			ScreenPermissionGroup managerGroup = new ScreenPermissionGroup();
			managerGroup.setName("Manager");
			ScreenPermissionGroup operatorGroup = new ScreenPermissionGroup();
			operatorGroup.setName("Operator");

			ScreenPermission screenPermission;
			screenPermission = new ScreenPermission();
			screenPermission.setName("Dasboard");
			screenPermission.setDescription("Monitor the overall performance of openstack cloud");
			session.save(screenPermission);
			adminGroup.getScreenPermissions().add(screenPermission);
			managerGroup.getScreenPermissions().add(screenPermission);

			screenPermission = new ScreenPermission();
			screenPermission.setName("Server");
			screenPermission.setDescription("Monitor openstack servers performance");
			session.save(screenPermission);
			adminGroup.getScreenPermissions().add(screenPermission);
			managerGroup.getScreenPermissions().add(screenPermission);

			screenPermission = new ScreenPermission();
			screenPermission.setName("Instances");
			screenPermission.setDescription("Manage vm instance");
			session.save(screenPermission);
			adminGroup.getScreenPermissions().add(screenPermission);
			managerGroup.getScreenPermissions().add(screenPermission);
			operatorGroup.getScreenPermissions().add(screenPermission);

			screenPermission = new ScreenPermission();
			screenPermission.setName("Keystone");
			screenPermission.setDescription("Manage keystone service of a specific openstack server");
			session.save(screenPermission);
			adminGroup.getScreenPermissions().add(screenPermission);
			managerGroup.getScreenPermissions().add(screenPermission);
			operatorGroup.getScreenPermissions().add(screenPermission);

			screenPermission = new ScreenPermission();
			screenPermission.setName("Flavors");
			screenPermission.setDescription("Manage vm flavor plans");
			session.save(screenPermission);
			adminGroup.getScreenPermissions().add(screenPermission);
			managerGroup.getScreenPermissions().add(screenPermission);

			screenPermission = new ScreenPermission();
			screenPermission.setName("Images");
			screenPermission.setDescription("Manage vm disk image");
			session.save(screenPermission);
			adminGroup.getScreenPermissions().add(screenPermission);

			screenPermission = new ScreenPermission();
			screenPermission.setName("Settings");
			screenPermission.setDescription("General settings");
			session.save(screenPermission);
			adminGroup.getScreenPermissions().add(screenPermission);

			session.save(adminGroup);
			session.save(managerGroup);
			session.save(operatorGroup);

			InstancePermissionGroup adminInstancePermissionGroup = new InstancePermissionGroup();
			adminInstancePermissionGroup.setName("Administrator");
			InstancePermissionGroup operatorInstancePermissionGroup = new InstancePermissionGroup();
			operatorInstancePermissionGroup.setName("Operator");

			InstancePermission instancePermission;
			instancePermission = new InstancePermission();
			instancePermission.setName("Launch VM");
			instancePermission.setDescription("Create a new vm");
			adminInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			session.save(instancePermission);

			instancePermission = new InstancePermission();
			instancePermission.setName("Stop");
			instancePermission.setDescription("Remove vm from nova");
			adminInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			session.save(instancePermission);

			instancePermission = new InstancePermission();
			instancePermission.setName("Delete");
			instancePermission.setDescription("Delete vm");
			adminInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			session.save(instancePermission);

			instancePermission = new InstancePermission();
			instancePermission.setName("Remote");
			instancePermission.setDescription("Remote control");
			adminInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			session.save(instancePermission);

			instancePermission = new InstancePermission();
			instancePermission.setName("Pause");
			instancePermission.setDescription("Pause the vm, all status will be saved in ram");
			adminInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			operatorInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			session.save(instancePermission);

			instancePermission = new InstancePermission();
			instancePermission.setName("Unpause");
			instancePermission.setDescription("Unpause the vm, all status will be resumed from ram");
			adminInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			operatorInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			session.save(instancePermission);

			instancePermission = new InstancePermission();
			instancePermission.setName("Suspend");
			instancePermission.setDescription("Suspend vm to disk");
			adminInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			operatorInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			session.save(instancePermission);

			instancePermission = new InstancePermission();
			instancePermission.setName("Resume");
			instancePermission.setDescription("Resume vm from disk");
			adminInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			operatorInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			session.save(instancePermission);

			instancePermission = new InstancePermission();
			instancePermission.setName("Log");
			instancePermission.setDescription("View the log information");
			adminInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			session.save(instancePermission);

			instancePermission = new InstancePermission();
			instancePermission.setName("Create snapshot");
			instancePermission.setDescription("Take a snapshot");
			adminInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			session.save(instancePermission);

			instancePermission = new InstancePermission();
			instancePermission.setName("Soft reboot");
			instancePermission.setDescription("Ctrl - alt - del");
			adminInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			operatorInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			session.save(instancePermission);

			instancePermission = new InstancePermission();
			instancePermission.setName("Hard reboot");
			instancePermission.setDescription("Press the reset button");
			adminInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			operatorInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			session.save(instancePermission);

			instancePermission = new InstancePermission();
			instancePermission.setName("Advance");
			instancePermission.setDescription("Advance settings");
			adminInstancePermissionGroup.getInstancePermissions().add(instancePermission);
			session.save(instancePermission);

			session.save(adminInstancePermissionGroup);
			session.save(operatorInstancePermissionGroup);

			User user;
			user = new User();
			user.setUsername("admin");
			user.setPassword("1234");
			user.setEnable(true);
			user.getScreenPermissionGroups().add(adminGroup);
			user.getInstancePermissionGroups().add(adminInstancePermissionGroup);
			session.save(user);

			user = new User();
			user.setUsername("user1");
			user.setPassword("1234");
			user.setEnable(true);
			user.getScreenPermissionGroups().add(operatorGroup);
			user.getInstancePermissionGroups().add(operatorInstancePermissionGroup);
			session.save(user);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			tx.commit();
			session.close();
		}
	}

	public static boolean isUserTableEmpty() {
		Session session = HibernateUtil.openSession();
		try {
			List<User> tables = session.createQuery("from User").list();
			return tables.size() == 0;
		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		} finally {
			session.close();
		}
	}

	public static void dumpJSonObject(JSONObject obj) {
		if (obj instanceof JSONObject) {
			System.out.println(obj);
		}
	}

	public static String runCommand(String command, int skipLine) {
		StringBuffer sb = new StringBuffer(4096);
		try {
			int x = 0;
			String s;
			Process p = Runtime.getRuntime().exec(command);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			// read the output from the command
			while ((s = stdInput.readLine()) != null) {
				if (x >= skipLine) {
					sb.append(s);
					sb.append(System.getProperty("line.separator"));
				}
				x++;
			}
			stdInput.close();
		} catch (Exception ex) {
			//			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
		return sb.toString();
	}

	public static String getTenantId(String tenantName) {
		Command command = new Command();
		command.command = "from pandora: keystone tenant-list";
		ReturnCommand r = execute(command);

		String msg = (String) r.map.get("result");
		if (msg.contains("error")) {
			return null;
		}
		JSONArray flavors = JSONObject.fromObject(msg).getJSONArray("tenants");

		for (int x = 0; x < flavors.size(); x++) {
			JSONObject obj = flavors.getJSONObject(x);
			if (obj.getString("name").equals(tenantName)) {
				obj.getString("id");
			}
		}
		return null;
	}

	private static ReturnCommand execute(Command command) {
		ReturnCommand r = new ReturnCommand();

		ParameterTableModel parameterTableModel = new ParameterTableModel();
		if (command.parameters.size() > 0) {
			HashMap<String, Object> parameters = (HashMap<String, Object>) command.parameters.get(0);
			Iterator<String> iterator = (Iterator<String>) parameters.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				parameterTableModel.parameters.add(key);
				parameterTableModel.values.add(parameters.get(key));
			}
		}
		String result = OpenstackComm.execute(command.command, parameterTableModel);
		r.map.put("result", result);
		return r;
	}
}
