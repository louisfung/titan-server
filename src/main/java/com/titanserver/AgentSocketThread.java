package com.c2.pandoraserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hyperic.sigar.Sigar;

import com.c2.pandoraserver.table.Status;

public class AgentSocketThread implements Runnable {
	private String name;
	private Socket socket;
	private BufferedReader in = null;
	private PrintStream out = null;
	private static Logger logger = Logger.getLogger(PandoraServerCommonLib.class);
	private Sigar sigar = new Sigar();

	public AgentSocketThread(Socket socket, String name) {
		this.socket = socket;
		this.name = name;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintStream(socket.getOutputStream());
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Override
	public void run() {
		try {
			if (in != null && out != null) {
				String result = "";
				String command = "";
				while (!command.equals("-+-")) {
					command = in.readLine();
					result += command + "\n";
				}
				Session session = HibernateUtil.openSession();
				Transaction tx = session.beginTransaction();
				Status status = new Status();
				status.setIp(socket.getInetAddress().getHostAddress());
				status.setDate(new Date());
				status.setMessage(result);
				session.save(status);
				tx.commit();
				session.close();
			} else {
				logger.error("Can't start thread because in/out stream are null, thread name : " + name);
			}
			socket.close();
		} catch (Exception e) {
			logger.error(e);
			ReturnCommand r = new ReturnCommand();
			r.isError = true;
			r.message = e.getMessage();
			out.close();
		}
	}
}
