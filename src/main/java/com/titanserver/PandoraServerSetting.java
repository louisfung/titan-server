package com.c2.pandoraserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.io.IOUtils;

import com.c2.pandoraserver.structure.PandoraServerDefinition;
import com.thoughtworks.xstream.XStream;

public class PandoraServerSetting {
	private static PandoraServerSetting setting = null;
	public String id;
	public Vector<PandoraServerDefinition> pandoraServers = new Vector<PandoraServerDefinition>();
	//	public String novaOsServiceToken;
	public String novaOsUsername;
	public String novaOsPassword;
	public String novaOsTenantName;
	public String novaOsAuthUrl;
	public String novaOsService_endpoint;
	public String novaOsServiceToken;
	public String novaAdminURL;
	public String s3AdminURL;
	public String glanceAdminURL;
	public String cinderAdminURL;
	public String ec2AdminURL;
	public String keystoneAdminURL;

	//	public String novaOsService_endpoint;
	public HashMap<String, String> novaCommands = new HashMap<String, String>();

	public static PandoraServerSetting getInstance() {
		if (setting == null) {
			setting = load();
		}
		return setting;
	}

	public void save() {
		XStream xstream = new XStream();
		xstream.alias("pandoraServer", PandoraServerDefinition.class);
		String xml = xstream.toXML(this);
		try {
			IOUtils.write(xml, new FileOutputStream(new File("pandora-server.xml")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static PandoraServerSetting load() {
		try {
			XStream xstream = new XStream();
			xstream.alias("pandoraServer", PandoraServerDefinition.class);
			PandoraServerSetting setting = (PandoraServerSetting) xstream.fromXML(new FileInputStream(new File("pandora-server.xml")));
			return setting;
		} catch (Exception ex) {
			//JOptionPane.showMessageDialog(null, "Loading pandora-server.xml error.", "Error", JOptionPane.ERROR_MESSAGE);
			new File("pandora-server.xml").delete();
			PandoraServerSetting pandoraServerSetting = new PandoraServerSetting();
			pandoraServerSetting.save();
			return pandoraServerSetting;
		}
	}

	public static void main(String args[]) {
		System.out.println(PandoraServerSetting.getInstance().novaOsService_endpoint);
	}
}
