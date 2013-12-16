package com.titanserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import javax.swing.UIManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DateType;
import org.hibernate.type.Type;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.titanserver.quartz.CPUMemoryStatJob;
import com.titanserver.quartz.InstanceStatJob;
import com.titanserver.structure.TitanServerDefinition;
import com.titanserver.table.ServerDiagnostics;

public class TitanServer {
	private static CommandLine cmd;
	private boolean isStopped = false;
	private ServerSocket serverSocket;
	private ServerSocket agentSocket;

	private static Logger logger = Logger.getLogger(TitanServer.class);

	public static void main(String[] args) {
		Session session = HibernateUtil.openSession();
		Query query = session.createQuery("from ServerDiagnostics");
		Criteria criteria = session.createCriteria(ServerDiagnostics.class);
		Date fromDate = new Date(2013, 11, 16);
		Date toDate = new Date(2013, 11, 16, 23, 59, 59);
		String period = "minute";
		System.out.println(fromDate + "," + toDate);
		criteria.add(Restrictions.ge("date", fromDate));
		criteria.add(Restrictions.le("date", toDate));
		criteria.addOrder(Order.asc("date"));
		//		criteria.setProjection(Projections.projectionList()
		//				.add(Projections.sqlGroupProjection("hour(date) as x", "hour(date)", new String[] { "x" }, new Type[] { DateType.INSTANCE })).add(Projections.avg("cpu")));
		List<ServerDiagnostics> list = criteria.list();
		//System.exit(1);

		Sigar sigar = new Sigar();
		try {
			for (String ni : sigar.getNetInterfaceList()) {
				// System.out.println(ni);
				NetInterfaceStat netStat = sigar.getNetInterfaceStat(ni);
				NetInterfaceConfig ifConfig = sigar.getNetInterfaceConfig(ni);
				String hwaddr = null;
				if (!NetFlags.NULL_HWADDR.equals(ifConfig.getHwaddr())) {
					hwaddr = ifConfig.getHwaddr();
				}
				if (hwaddr != null) {
					long rxCurrenttmp = netStat.getRxBytes();
					long txCurrenttmp = netStat.getTxBytes();
					//					netStat.get
					System.out.println(ni + "=" + rxCurrenttmp);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			UIManager.setLookAndFeel("com.peterswing.white.PeterSwingWhiteLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		CommandLineParser parser = new PosixParser();
		Options options = new Options();
		try {
			options.addOption("v", "version", false, "display version info");
			options.addOption("t", "test", false, "test database connection");
			options.addOption("s", "sample_setting", false, "create sample titan-server.xml");
			options.addOption("g", "gui", false, "launch the gui, it can do logging or some testings to nova/cinder/keystone/etc...");
			cmd = parser.parse(options, args);
		} catch (ParseException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		if (cmd.hasOption("version") || cmd.hasOption("v")) {
			System.out.println("version : " + Global.version);
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("run : java -jar titan-server.jar [OPTION]", options);
			System.exit(0);
		}

		if (cmd.hasOption("t") || cmd.hasOption("test")) {
			if (TitanServerCommonLib.isUserTableEmpty()) {
				TitanServerCommonLib.initDB();
			}
			System.exit(0);
		}

		if (cmd.hasOption("sample_setting") || cmd.hasOption("s")) {
			try {
				IOUtils.copy(TitanServer.class.getResourceAsStream("/hibernate.cfg.xml"), new FileOutputStream("hibernate.cfg.xml"));
				System.out.println("Created hibernate.cfg.xml");
				IOUtils.copy(TitanServer.class.getResourceAsStream("/log4j.properties"), new FileOutputStream("log4j.properties"));
				System.out.println("Created log4j.properties");

				TitanServerSetting setting = TitanServerSetting.getInstance();
				try {
					setting.id = InetAddress.getLocalHost().getHostName();
				} catch (Exception ex) {
					setting.id = "openstack server";
				}

				setting.novaOsUsername = "admin";
				setting.novaOsPassword = "123456";
				setting.novaOsTenantName = "admin";
				setting.novaOsAuthUrl = "http://localhost:5000/v2.0/";
				setting.novaOsService_endpoint = "http://localhost:35357/v2.0/";
				setting.novaOsServiceToken = "123456";

				setting.novaAdminURL = "http://localhost:8774/v2/";
				setting.s3AdminURL = "http://localhost:3333/";
				setting.glanceAdminURL = "http://localhost:9292/";
				setting.cinderAdminURL = "http://localhost:8776/v1/";
				setting.ec2AdminURL = "http://localhost:8773/services/Admin/";
				setting.keystoneAdminURL = "http://localhost:35357/v2.0/";

				setting.titanServers.clear();
				TitanServerDefinition titanServerDefinition = new TitanServerDefinition();
				titanServerDefinition.id = "sample titan server 1";
				titanServerDefinition.ip = "192.168.0.2";
				setting.titanServers.add(titanServerDefinition);

				titanServerDefinition = new TitanServerDefinition();
				titanServerDefinition.id = "sample titan server 2";
				titanServerDefinition.ip = "192.168.0.3";
				setting.titanServers.add(titanServerDefinition);

				setting.novaCommands.clear();
				setting.novaCommands.put("nova endpoints", "curl -s " + setting.keystoneAdminURL + "/tokens " + " -X POST " + " -H \"Content-Type: application/json\" "
						+ " -d '{\"auth\": {\"tenantName\": \"$Tenant_name\", \"passwordCredentials\": {\"username\": \"$Username\", \"password\": \"$Password\"}}}'");

				setting.novaCommands.put("nova agent-list", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/os-agents " + " -X GET  "
						+ " -H \"X-Auth-Project-Id: $Project_name\"  " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\"");

				setting.novaCommands.put("nova aggregate-list", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/os-aggregates " + " -X GET " + " -H \"X-Auth-Project-Id: admin\" "
						+ " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\"");

				setting.novaCommands.put("nova cloudpipe-list", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/os-cloudpipe " + " -X GET " + " -H \"X-Auth-Project-Id: admin\" "
						+ " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\"");

				setting.novaCommands.put("nova host-list", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/os-hosts " + " -X GET " + " -H \"X-Auth-Project-Id: admin\" "
						+ " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\"");

				setting.novaCommands.put("nova hypervisor-list", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/os-hypervisors " + " -X GET "
						+ " -H \"X-Auth-Project-Id: admin\" " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token:$Token\"");

				setting.novaCommands.put("nova image-list", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/images/detail " + " -X GET " + " -H \"X-Auth-Project-Id: admin\" "
						+ " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\"");

				setting.novaCommands.put("nova flavor-list", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/flavors/detail " + " -X GET " + " -H \"X-Auth-Project-Id: admin\" "
						+ " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\"");

				setting.novaCommands.put("nova list", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/servers/detail " + " -X GET " + " -H \"X-Auth-Project-Id: admin\" "
						+ " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\"");

				setting.novaCommands
						.put("nova create-flavor",
								"curl -s "
										+ setting.novaAdminURL
										+ "/$Tenant_Id/flavors "
										+ " -X POST "
										+ " -H \"X-Auth-Project-Id: admin\" "
										+ " -H \"Content-Type: application/json\" "
										+ " -H \"Accept: application/json\" "
										+ " -H \"X-Auth-Token: $Token\" "
										+ " -d '{\"flavor\": {\"vcpus\": \"$vcpu\", \"disk\": \"$root\", \"name\": \"$name\", \"os-flavor-access:is_public\": true, \"rxtx_factor\": \"1.0\", \"OS-FLV-EXT-DATA:ephemeral\": \"0\", \"ram\": \"$ram\",  \"swap\": \"$swap\"}}'");

				setting.novaCommands.put("nova delete-flavor", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/flavors/$flavorId " + " -X DELETE "
						+ " -H \"X-Auth-Project-Id: admin\" " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\"");

				setting.novaCommands.put("glance image-list", "curl -s " + setting.glanceAdminURL + "/v1/images/detail " + " -X GET " + " -H \"Accept: application/json\" "
						+ " -H \"X-Auth-Token: $Token\"");

				setting.novaCommands.put("glance image-show", "curl -s " + setting.glanceAdminURL + "/v1/images/$imageId " + " -X GET "
						+ " -H \"Content-Type: application/octet-stream\" " + " -H \"X-Auth-Token: $Token\"" + " -H \"CUSTOM: NOCONTENT\"");

				setting.novaCommands.put("glance image-create", "curl -s " + setting.glanceAdminURL + "/v1/images " + " -X POST " + " -H \"Accept: application/json\" "
						+ " -H \"Content-Type: application/octet-stream\" " + " -H \"X-Auth-Token: $Token\"" + " -H \"x-image-meta-name: $x-image-meta-name\" "
						+ " -H \"x-image-meta-container_format: bare\" " + " -H \"x-image-meta-disk_format: $x-image-meta-disk_format\" "
						+ " -H \"x-image-meta-min-ram: $x-image-meta-min-ram\" " + " -H \"x-image-meta-min-disk: $x-image-meta-min-disk\" "
						+ " -H \"x-image-meta-is-public: $x-image-meta-is-public\" " + "-POSTDATA");

				setting.novaCommands.put("glance image-delete", "curl -s " + setting.glanceAdminURL + "/v1/images/$imageId " + " -X DELETE " + " -H \"Accept: application/json\" "
						+ " -H \"X-Auth-Token: $Token\"");

				setting.novaCommands.put("nova boot", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/servers " + " -X POST " + " -H \"X-Auth-Project-Id: admin\" "
						+ " -H \"Content-Type: application/json\" " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" "
						+ " -d '{\"server\": { \"min_count\": \"$min_count\", \"flavorRef\": \"$flavorRef\", \"name\": \"$name\", \"imageRef\": \"$imageRef\"}}'");

				setting.novaCommands.put("nova delete", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/servers/$InstanceId " + " -X DELETE "
						+ " -H \"X-Auth-Project-Id: admin\" " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\"");

				setting.novaCommands.put("nova stop", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/servers/$InstanceId/action " + " -X POST \" "
						+ " -H \"X-Auth-Project-Id: admin\" " + " -H \"Content-Type: application/json\" " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" "
						+ " -d '{\"os-stop\": null}' ");

				setting.novaCommands.put("nova pause", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/servers/$InstanceId/action " + " -X POST "
						+ " -H \"X-Auth-Project-Id: admin\" " + " -H \"Content-Type: application/json\" " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" "
						+ " -d '{\"pause\": null}'");

				setting.novaCommands.put("nova unpause", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/servers/$InstanceId/action " + " -X POST "
						+ " -H \"X-Auth-Project-Id: admin\" " + " -H \"Content-Type: application/json\" " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" "
						+ " -d '{\"unpause\": null}'");

				setting.novaCommands.put("nova suspend", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/servers/$InstanceId/action " + " -X POST "
						+ " -H \"X-Auth-Project-Id: admin\" " + " -H \"Content-Type: application/json\" " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" "
						+ " -d '{\"suspend\": null}'");

				setting.novaCommands.put("nova resume", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/servers/$InstanceId/action " + " -X POST "
						+ " -H \"X-Auth-Project-Id: admin\" " + " -H \"Content-Type: application/json\" " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" "
						+ " -d '{\"resume\": null}'");

				setting.novaCommands.put("nova rename", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/servers/$InstanceId " + " -X PUT " + " -H \"X-Auth-Project-Id: admin\" "
						+ " -H \"Content-Type: application/json\" " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" "
						+ " -d '{\"server\": {\"name\": \"$name\"}}'");

				setting.novaCommands.put("nova soft-reboot", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/servers/$InstanceId/action " + " -X POST "
						+ " -H \"X-Auth-Project-Id: admin\" " + " -H \"Content-Type: application/json\" " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" "
						+ " -d '{\"reboot\": {\"type\": \"SOFT\"}}'");

				setting.novaCommands.put("nova hard-reboot", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/servers/$InstanceId/action " + " -X POST "
						+ " -H \"X-Auth-Project-Id: admin\" " + " -H \"Content-Type: application/json\" " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" "
						+ " -d '{\"reboot\": {\"type\": \"HARD\"}}'");

				setting.novaCommands.put("nova diagnostics", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/servers/$InstanceId/diagnostics " + " -X GET "
						+ " -H \"X-Auth-Project-Id: admin\" " + " -H \"Content-Type: application/json\" " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" ");

				setting.novaCommands.put("keystone role-create", "curl -s " + setting.keystoneAdminURL + "/OS-KSADM/roles " + " -X POST " + " -H \"Accept: application/json\" "
						+ " -H \"X-Auth-Token: $Token\" " + " -H \"Content-Type: application/json\" " + " -d '{\"role\": {\"name\": \"$roleName\"}}'");

				setting.novaCommands.put("keystone user-create", "curl -s " + setting.keystoneAdminURL + "/users " + " -X POST " + " -H \"Accept: application/json\" "
						+ " -H \"X-Auth-Token: $Token\" " + " -H \"Content-Type: application/json\" "
						+ " -d '{\"user\": {\"email\": \"$email\", \"password\": \"$password\", \"enabled\": $enabled, \"name\": \"$name\", \"tenantId\": \"$tenantId\"}}'");

				setting.novaCommands.put("keystone role-list", "curl -s " + setting.keystoneAdminURL + "/OS-KSADM/roles " + " -X GET " + " -H \"Accept: application/json\" "
						+ " -H \"X-Auth-Token: $Token\" ");

				setting.novaCommands.put("keystone user-list", "curl -s " + setting.keystoneAdminURL + "/users " + " -X GET " + " -H \"Accept: application/json\" "
						+ " -H \"X-Auth-Token: $Token\" ");

				setting.novaCommands.put("keystone user-role-list", "curl -s " + setting.keystoneAdminURL + "/tenants/$Tenant_Id/users/$userId/roles" + " -X GET "
						+ " -H \"X-Auth-Token: $Token\" ");

				setting.novaCommands.put("keystone role-delete", "curl -s " + setting.keystoneAdminURL + "/OS-KSADM/roles/$roleId " + " -X DELETE "
						+ " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" ");

				setting.novaCommands.put("keystone role-create", "curl -s " + setting.keystoneAdminURL + "/OS-KSADM/roles " + " -X POST " + " -H \"Accept: application/json\" "
						+ " -H \"X-Auth-Token: $Token\" " + " -H \"Content-Type: application/json\" " + " -d '{\"role\": {\"name\": \"$roleName\"}}'");

				setting.novaCommands.put("keystone tenant-list", "curl -s " + setting.keystoneAdminURL + "/tenants " + " -X GET " + " -H \"Accept: application/json\" "
						+ " -H \"X-Auth-Token: $Token\" ");

				setting.novaCommands.put("keystone tenant-delete", "curl -s " + setting.keystoneAdminURL + "/tenants/$tenantId " + " -X DELETE "
						+ " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" ");

				setting.novaCommands.put("keystone tenant-create", "curl -s " + setting.keystoneAdminURL + "/tenants " + " -X POST " + " -H \"X-Auth-Token: $Token\" "
						+ " -H \"Content-Type: application/json\" " + " -d '{\"tenant\": {\"enabled\": $enabled, \"name\": \"$name\", \"description\":\"$description\"}}'");

				setting.novaCommands.put("keystone endpoint-list", "curl -s " + setting.keystoneAdminURL + "/endpoints " + " -X GET " + " -H \"Accept: application/json\" "
						+ " -H \"X-Auth-Token: $Token\" ");

				setting.novaCommands.put("cinder list", "curl -s " + setting.cinderAdminURL + "/$Tenant_Id/volumes/detail " + " -X GET " + " -H \"Accept: application/json\" "
						+ " -H \"X-Auth-Token: $Token\" ");

				setting.novaCommands
						.put("cinder create",
								"curl -s "
										+ setting.cinderAdminURL
										+ "/$Tenant_Id/volumes "
										+ " -X POST "
										+ " -H \"X-Auth-Token: $Token\" "
										+ " -H \"Content-Type: application/json\" "
										+ " -d '{\"volume\": {\"status\": \"creating\", \"availability_zone\": null, \"source_volid\": null, \"display_description\": \"$displayDescription\", \"snapshot_id\": null, \"user_id\": null, \"size\": $size, \"display_name\": \"$displayName\", \"imageRef\": null, \"attach_status\": \"detached\", \"volume_type\": \"$volumeType\", \"project_id\": null, \"metadata\": {}}}'");

				setting.novaCommands.put("cinder delete", "curl -s " + setting.cinderAdminURL + "/$Tenant_Id/volumes/$volumeId " + " -X DELETE "
						+ " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" ");

				setting.novaCommands.put("cinder type-list", "curl -s " + setting.cinderAdminURL + "/$Tenant_Id/types " + " -X GET " + " -H \"Accept: application/json\" "
						+ " -H \"X-Auth-Token: $Token\" ");

				setting.novaCommands.put("cinder type-create", "curl -s " + setting.cinderAdminURL + "/$Tenant_Id/types " + " -X POST " + " -H \"X-Auth-Token: $Token\" "
						+ " -H \"Content-Type: application/json\" " + " -d '{\"volume_type\": {\"name\": \"$name\"}}'");

				setting.novaCommands.put("cinder type-delete", "curl -s " + setting.cinderAdminURL + "/$Tenant_Id/types/$volumeTypeId " + " -X DELETE "
						+ " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" ");

				setting.novaCommands.put("nova quota-defaults", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/os-quota-sets/$Tenant_Id/defaults " + " -X GET "
						+ " -H \"X-Auth-Project-Id: $tenantName\"  " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" ");

				setting.novaCommands.put("nova quota-show", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/os-quota-sets/$Tenant_Id " + " -X GET "
						+ " -H \"X-Auth-Project-Id: $tenantName\"  " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" ");

				setting.novaCommands.put("nova quota-update", "curl -s " + setting.novaAdminURL + "/$Tenant_Id/os-quota-sets/$Tenant_Id " + " -X PUT "
						+ " -H \"X-Auth-Project-Id: $tenantName\"  " + " -H \"Accept: application/json\" " + " -H \"X-Auth-Token: $Token\" "
						+ " -H \"Content-Type: application/json\" " + " -d '{\"quota_set\": {\"tenant_id\": \"$Tenant_Id\", \"$type\": $value}}'");

				System.out.println("Created titan-server.xml");
				setting.save();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.exit(0);
		} else {
			// refresh the latest setting
			TitanServerSetting.getInstance().save();
		}

		try {
			PropertyConfigurator.configure(new FileInputStream("log4j.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		if (TitanServerCommonLib.isUserTableEmpty()) {
			TitanServerCommonLib.initDB();
		}

		testSigar();

		if (cmd.hasOption("gui") || cmd.hasOption("g")) {
			new MainFrame().setVisible(true);
		}

		startQuartz();

		new TitanServer();
	}

	private static void startQuartz() {
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();

			logger.info(scheduler.getSchedulerName() + " started");

			JobDetail job = JobBuilder.newJob(InstanceStatJob.class).withIdentity("Instance Stat Job").build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startNow()
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(TitanServerSetting.getInstance().updateInterval).repeatForever()).build();
			scheduler.scheduleJob(job, trigger);

			job = JobBuilder.newJob(CPUMemoryStatJob.class).withIdentity("CPU And Memory Stat Job").build();
			trigger = TriggerBuilder.newTrigger().withIdentity("trigger2", "group1").startNow()
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(TitanServerSetting.getInstance().updateInterval).repeatForever()).build();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException se) {
			se.printStackTrace();
		}
	}

	private static void testSigar() {
		try {
			Sigar sigar = new Sigar();
			sigar.getMem();
		} catch (UnsatisfiedLinkError ex) {
			logger.error("Unable to load sigar, system exit");
			System.exit(1);
		} catch (SigarException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public TitanServer() {
		new Thread() {
			public void run() {
				commandServer();
			}
		}.start();
		new Thread() {
			public void run() {
				agentserver();
			}
		}.start();
	}

	private void commandServer() {
		try {
			serverSocket = new ServerSocket(4444);
			logger.info("Titan server started at port 4444");
			while (!isStopped) {
				Socket clientSocket = null;
				clientSocket = serverSocket.accept();
				new Thread(new SocketThread(clientSocket, "")).start();
			}
		} catch (Exception e) {
			if (isStopped) {
				System.out.println("Server Stopped.");
				return;
			}
			e.printStackTrace();
		}
	}

	private void agentserver() {
		try {
			agentSocket = new ServerSocket(4445);
			logger.info("Agent server started at port 4445");
			while (!isStopped) {
				Socket clientSocket = null;
				clientSocket = agentSocket.accept();
				new Thread(new AgentSocketThread(clientSocket, "")).start();
			}
		} catch (Exception e) {
			if (isStopped) {
				System.out.println("Server Stopped.");
				return;
			}
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
