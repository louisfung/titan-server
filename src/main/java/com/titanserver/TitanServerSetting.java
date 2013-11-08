package com.titanserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.io.IOUtils;

import com.thoughtworks.xstream.XStream;
import com.titanserver.structure.TitanServerDefinition;

public class TitanServerSetting {
	private static TitanServerSetting setting = null;
	public String id;
	public Vector<TitanServerDefinition> titanServers = new Vector<TitanServerDefinition>();
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
	public int updateInterval=30;

	//	public String novaOsService_endpoint;
	public HashMap<String, String> novaCommands = new HashMap<String, String>();

	public static TitanServerSetting getInstance() {
		if (setting == null) {
			setting = load();
		}
		return setting;
	}

	public void save() {
		XStream xstream = new XStream();
		xstream.alias("titanServer", TitanServerDefinition.class);
		String xml = xstream.toXML(this);
		try {
			IOUtils.write(xml, new FileOutputStream(new File("titan-server.xml")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static TitanServerSetting load() {
		try {
			XStream xstream = new XStream();
			xstream.alias("titanServer", TitanServerDefinition.class);
			TitanServerSetting setting = (TitanServerSetting) xstream.fromXML(new FileInputStream(new File("titan-server.xml")));
			return setting;
		} catch (Exception ex) {
			//JOptionPane.showMessageDialog(null, "Loading titan-server.xml error.", "Error", JOptionPane.ERROR_MESSAGE);
			new File("titan-server.xml").delete();
			TitanServerSetting titanServerSetting = new TitanServerSetting();
			titanServerSetting.save();
			return titanServerSetting;
		}
	}

	public static void main(String args[]) {
		System.out.println(TitanServerSetting.getInstance().novaOsService_endpoint);
	}
}
