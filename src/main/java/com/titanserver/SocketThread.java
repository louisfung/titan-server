package com.titanserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Tcp;

import com.titanserver.openstack_communication.OpenstackComm;
import com.titanserver.table.InstancePermission;
import com.titanserver.table.InstancePermissionGroup;
import com.titanserver.table.ScreenPermission;
import com.titanserver.table.ScreenPermissionGroup;
import com.titanserver.table.ServerDiagnostics;
import com.titanserver.table.User;

public class SocketThread implements Runnable {
	private String name;
	private Socket socket;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	private static Logger logger = Logger.getLogger(TitanServerCommonLib.class);
	private Sigar sigar = new Sigar();

	public SocketThread(Socket socket, String name) {
		this.socket = socket;
		this.name = name;
		try {
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Override
	public void run() {
		try {
			if (in != null && out != null) {
				Command command = (Command) in.readObject();

				Global.clients.put(command.id, socket);

				if (!command.command.equals("updateStatus")) {
					logger.info(socket.getInetAddress().getHostAddress() + ", " + command);
				}
				ReturnCommand r = new ReturnCommand();

				if (command.command.equals("login") && command.parameters.size() == 2) {
					Session session = HibernateUtil.openSession();
					Criteria criteria = session.createCriteria(User.class);
					criteria.add(Restrictions.eq("username", command.parameters.get(0)));
					criteria.add(Restrictions.eq("password", command.parameters.get(1)));
					criteria.add(Restrictions.eq("enable", true));
					List<User> users = criteria.list();
					if (users.size() > 0) {
						r.message = "yes";
					} else {
						r.message = "no";
					}
					session.close();
				} else if (command.command.equals("updateStatus")) {
					Mem mem = sigar.getMem();
					r.map.put("maxMemory", mem.getTotal());
					r.map.put("allocatedMemory", mem.getActualUsed());
					r.map.put("freeMemory", mem.getFree());

					CpuPerc cpuPerc = sigar.getCpuPerc();
					r.map.put("cpu_combined", CpuPerc.format(cpuPerc.getCombined()).replace("%", ""));
					r.map.put("cpu_sys", CpuPerc.format(cpuPerc.getSys()).replace("%", ""));
					r.map.put("cpu_user", CpuPerc.format(cpuPerc.getUser()).replace("%", ""));

					Tcp tcp = sigar.getTcp();
					r.map.put("inSegs", tcp.getInSegs());
					r.map.put("outSegs", tcp.getOutSegs());

					r.map.put("os", System.getProperty("os.name"));
				} else if (command.command.equals("getServerDiagnostics")) {
					Session session = HibernateUtil.openSession();
					Query query = session.createQuery("from ServerDiagnostics");
					Criteria criteria = session.createCriteria(ServerDiagnostics.class);
					criteria.add(Restrictions.ge("date", command.parameters.get(0)));
					criteria.add(Restrictions.le("date", command.parameters.get(1)));
					criteria.add(Restrictions.eq("enable", true));
					criteria.addOrder(Order.asc("date"));
					List<ServerDiagnostics> list = criteria.list();
					r.map.put("result", list);
					session.close();
				} else if (command.command.equals("getID")) {
					r.message = TitanServerSetting.getInstance().id;
				} else if (command.command.equals("getTitanServerInfo")) {
					r.map.put("cpu", sigar.getCpu());
					r.map.put("cpuInfoList", sigar.getCpuInfoList());
					r.map.put("cpuPerc", sigar.getCpuPerc());
					r.map.put("fileSystemList", sigar.getFileSystemList());
					r.map.put("fqdn", sigar.getFQDN());
					r.map.put("loadAverage", sigar.getLoadAverage());
					r.map.put("mem", sigar.getMem());
					r.map.put("nativeLibrary", sigar.getNativeLibrary());
					r.map.put("netInfo", sigar.getNetInfo());
					r.map.put("netInterfaceConfig", sigar.getNetInterfaceConfig());
				} else if (command.command.startsWith("from titan:")) {
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
				} else if (command.command.equals("send file")) {
					if (!new File("vmImage").exists()) {
						new File("vmImage").mkdirs();
					}

					long fileSize = in.readLong();

					FileOutputStream f = new FileOutputStream(new File("vmImage") + "/" + command.filename);

					byte[] buff = new byte[1024];
					int k = -1;
					int complete = 0;
					while (complete < fileSize) {
						k = in.read(buff, 0, buff.length);
						f.write(buff, 0, k);
						complete += k;
					}
					f.close();

					r.map.put("result", "ok");
				} else if (command.command.equals("proxy")) {
					int port = (Integer) command.parameters.get(0);
					System.out.println("proxy port=" + port);

					Socket server = new Socket("127.0.0.1", port);

					System.out.println("running -2");
					final InputStream streamFromServer = server.getInputStream();
					final OutputStream streamToServer = server.getOutputStream();

					final ObjectInputStream streamFromClient = in;
					final ObjectOutputStream streamToClient = out;

					System.out.println("running");
					Thread t = new Thread() {
						public void run() {
							int bytesRead;
							try {
								byte bs[] = new byte[1024];
								while ((bytesRead = streamFromClient.read(bs)) > 0) {
									streamToServer.write(bs, 0, bytesRead);
									streamToServer.flush();
								}
							} catch (IOException e) {
							}

							try {
								streamToServer.close();
							} catch (IOException e) {
							}
						}
					};

					t.start();

					int bytesRead;
					try {
						byte bb[] = new byte[1024];
						while ((bytesRead = streamFromServer.read(bb)) > 0) {
							streamToClient.write(bb, 0, bytesRead);
							streamToClient.flush();
						}
					} catch (IOException e) {
					}

					streamToClient.close();
				} else if (command.command.equals("get vnc port")) {
					String instanceName = (String) command.parameters.get(0);
					String result = TitanServerCommonLib.runCommand("ps aux", 0);
					boolean bingo = false;
					for (String s : result.split("\n")) {
						if (s.contains("-name " + instanceName)) {
							bingo = true;
							String vncPort = s.replaceAll("^.*-vnc", "");
							vncPort = vncPort.replaceAll("-.*", "");
							vncPort = vncPort.trim();
							r.map.put("port", Integer.parseInt(vncPort.split(":")[1]));
						}
					}

					if (!bingo) {
						r.map.put("port", -1);
					}
				} else if (command.command.equals("qmp command")) {
					HashMap<String, String> map = (HashMap<String, String>) command.parameters.get(0);
					String instanceName = map.get("instanceName");
					String commandStr = map.get("commandStr");
					String result = TitanServerCommonLib.runCommand("virsh qemu-monitor-command --hmp " + instanceName + " " + commandStr, 0);
					System.out.println("/usr/bin/virsh qemu-monitor-command --hmp " + instanceName + " '" + commandStr + "'");
					System.out.println(result);
					r.map.put("result", result);
				} else if (command.command.equals("get screen permissions")) {
					Session session = HibernateUtil.openSession();
					Query query = session.createQuery("from ScreenPermission");
					List<ScreenPermission> list = query.list();
					r.map.put("result", list);
					session.close();
				} else if (command.command.equals("get instance permissions")) {
					Session session = HibernateUtil.openSession();
					Query query = session.createQuery("from InstancePermission");
					List<InstancePermission> list = query.list();
					r.map.put("result", list);
					session.close();
				} else if (command.command.equals("get users")) {
					Session session = HibernateUtil.openSession();
					Query query = session.createQuery("from User");
					List<User> list = query.list();
					r.map.put("result", list);
					session.close();
				} else if (command.command.equals("get screen permission groups")) {
					Session session = HibernateUtil.openSession();
					Query query = session.createQuery("from ScreenPermissionGroup");
					List<ScreenPermissionGroup> list = query.list();
					r.map.put("result", list);
					session.close();
				} else if (command.command.equals("get screen permission group")) {
					Session session = HibernateUtil.openSession();
					Query query = session.createQuery("from ScreenPermissionGroup where screenPermissionGroupId=:groupId");
					query.setInteger("groupId", (Integer) command.parameters.get(0));
					List<ScreenPermissionGroup> list = query.list();
					r.map.put("result", list.get(0));
					session.close();
				} else if (command.command.equals("get instance permission groups")) {
					Session session = HibernateUtil.openSession();
					Query query = session.createQuery("from InstancePermissionGroup");
					List<InstancePermissionGroup> list = query.list();
					r.map.put("result", list);
					session.close();
				}
				out.writeObject(r);
				out.flush();
				if (!command.command.equals("updateStatus")) {
					logger.info("Command replied to client");
				}
			} else {
				logger.error("Can't start thread because in/out stream are null, thread name : " + name);
			}
		} catch (Exception e) {
			logger.error(e);
			ReturnCommand r = new ReturnCommand();
			r.isError = true;
			r.message = e.getMessage();
			try {
				out.writeObject(r);
				out.flush();
			} catch (IOException e1) {
				logger.error("Triple fault");
			}
		}
	}
}
