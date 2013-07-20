package com.c2.pandoraserver.openstack_communication;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.c2.pandoraserver.PandoraServerCommonLib;
import com.c2.pandoraserver.PandoraServerSetting;
import com.c2.pandoraserver.ParameterTableModel;
import com.peterswing.advancedswing.enhancedtextarea.EnhancedTextArea;

public class OpenstackComm {

	private static Logger logger = Logger.getLogger(PandoraServerCommonLib.class);

	static String url = "http://127.0.0.1:35357/v2.0/tokens";
	//static String url = "http://127.0.0.1:8774/v2/b721f5b1f7cd43dd83ee573f6d4e6c74/os-cloudpipe";
	//static String tenantId = "b721f5b1f7cd43dd83ee573f6d4e6c74";
	static String tenantId;

	public static String tenantID;
	public static String token;

	public static void main(String args[]) {
		initToken();

		/*
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		//		System.out.println(send(url, headers, "{\"auth\": {\"tenantName\": \"admin\", \"passwordCredentials\": {\"username\": \"admin\", \"password\": \"123456\"}}}"));
		//		headers.put("X-Auth-Project-Id", PandoraServerSetting.getInstance().novaOsTenantName);
		headers.put("Accept", "application/json");
		headers.put(
				"X-Auth-Token",
				"MIIKjQYJKoZIhvcNAQcCoIIKfjCCCnoCAQExCTAHBgUrDgMCGjCCCWYGCSqGSIb3DQEHAaCCCVcEgglTeyJhY2Nlc3MiOiB7InRva2VuIjogeyJpc3N1ZWRfYXQiOiAiMjAxMy0wNS0wOFQwMjowMzo0Ni4wMDI4MzAiLCAiZXhwaXJlcyI6ICIyMDEzLTA1LTA5VDAyOjAzOjQ1WiIsICJpZCI6ICJwbGFjZWhvbGRlciIsICJ0ZW5hbnQiOiB7ImRlc2NyaXB0aW9uIjogbnVsbCwgImVuYWJsZWQiOiB0cnVlLCAiaWQiOiAiYjcyMWY1YjFmN2NkNDNkZDgzZWU1NzNmNmQ0ZTZjNzQiLCAibmFtZSI6ICJhZG1pbiJ9fSwgInNlcnZpY2VDYXRhbG9nIjogW3siZW5kcG9pbnRzIjogW3siYWRtaW5VUkwiOiAiaHR0cDovLzE5Mi4xNjguMTAwLjE3MDo4Nzc0L3YyL2I3MjFmNWIxZjdjZDQzZGQ4M2VlNTczZjZkNGU2Yzc0IiwgInJlZ2lvbiI6ICJSZWdpb25PbmUiLCAiaW50ZXJuYWxVUkwiOiAiaHR0cDovLzE5Mi4xNjguMTAwLjE3MDo4Nzc0L3YyL2I3MjFmNWIxZjdjZDQzZGQ4M2VlNTczZjZkNGU2Yzc0IiwgImlkIjogIjNmNWMxYjhiOTM5YjQ2OWM4MWUwMmQ4MmFmMjQ1NDM3IiwgInB1YmxpY1VSTCI6ICJodHRwOi8vMTkyLjE2OC4xMDAuMTcwOjg3NzQvdjIvYjcyMWY1YjFmN2NkNDNkZDgzZWU1NzNmNmQ0ZTZjNzQifV0sICJlbmRwb2ludHNfbGlua3MiOiBbXSwgInR5cGUiOiAiY29tcHV0ZSIsICJuYW1lIjogIm5vdmEifSwgeyJlbmRwb2ludHMiOiBbeyJhZG1pblVSTCI6ICJodHRwOi8vMTkyLjE2OC4xMDAuMTcwOjMzMzMiLCAicmVnaW9uIjogIlJlZ2lvbk9uZSIsICJpbnRlcm5hbFVSTCI6ICJodHRwOi8vMTkyLjE2OC4xMDAuMTcwOjMzMzMiLCAiaWQiOiAiOTlhYWRkMTU5Zjg5NGZkZjljYjUxYzU4MzU3NWI3MjYiLCAicHVibGljVVJMIjogImh0dHA6Ly8xOTIuMTY4LjEwMC4xNzA6MzMzMyJ9XSwgImVuZHBvaW50c19saW5rcyI6IFtdLCAidHlwZSI6ICJzMyIsICJuYW1lIjogInMzIn0sIHsiZW5kcG9pbnRzIjogW3siYWRtaW5VUkwiOiAiaHR0cDovLzE5Mi4xNjguMTAwLjE3MDo5MjkyIiwgInJlZ2lvbiI6ICJSZWdpb25PbmUiLCAiaW50ZXJuYWxVUkwiOiAiaHR0cDovLzE5Mi4xNjguMTAwLjE3MDo5MjkyIiwgImlkIjogIjg1NWRiYmE1MTNiYzQzZTRiZTk0ZjY4ZTI0OTk2MTA3IiwgInB1YmxpY1VSTCI6ICJodHRwOi8vMTkyLjE2OC4xMDAuMTcwOjkyOTIifV0sICJlbmRwb2ludHNfbGlua3MiOiBbXSwgInR5cGUiOiAiaW1hZ2UiLCAibmFtZSI6ICJnbGFuY2UifSwgeyJlbmRwb2ludHMiOiBbeyJhZG1pblVSTCI6ICJodHRwOi8vMTkyLjE2OC4xMDAuMTcwOjg3NzYvdjEvYjcyMWY1YjFmN2NkNDNkZDgzZWU1NzNmNmQ0ZTZjNzQiLCAicmVnaW9uIjogIlJlZ2lvbk9uZSIsICJpbnRlcm5hbFVSTCI6ICJodHRwOi8vMTkyLjE2OC4xMDAuMTcwOjg3NzYvdjEvYjcyMWY1YjFmN2NkNDNkZDgzZWU1NzNmNmQ0ZTZjNzQiLCAiaWQiOiAiM2Q5Y2UwZGM3MzliNGVjYTk5YmM5NTA1ZGMxYzk3YWMiLCAicHVibGljVVJMIjogImh0dHA6Ly8xOTIuMTY4LjEwMC4xNzA6ODc3Ni92MS9iNzIxZjViMWY3Y2Q0M2RkODNlZTU3M2Y2ZDRlNmM3NCJ9XSwgImVuZHBvaW50c19saW5rcyI6IFtdLCAidHlwZSI6ICJ2b2x1bWUiLCAibmFtZSI6ICJjaW5kZXIifSwgeyJlbmRwb2ludHMiOiBbeyJhZG1pblVSTCI6ICJodHRwOi8vMTkyLjE2OC4xMDAuMTcwOjg3NzMvc2VydmljZXMvQWRtaW4iLCAicmVnaW9uIjogIlJlZ2lvbk9uZSIsICJpbnRlcm5hbFVSTCI6ICJodHRwOi8vMTkyLjE2OC4xMDAuMTcwOjg3NzMvc2VydmljZXMvQ2xvdWQiLCAiaWQiOiAiMzYwZWFkYTUxNDk0NDg4ZDlmNDdlNzVkYTAxMTU2MzgiLCAicHVibGljVVJMIjogImh0dHA6Ly8xOTIuMTY4LjEwMC4xNzA6ODc3My9zZXJ2aWNlcy9DbG91ZCJ9XSwgImVuZHBvaW50c19saW5rcyI6IFtdLCAidHlwZSI6ICJlYzIiLCAibmFtZSI6ICJlYzIifSwgeyJlbmRwb2ludHMiOiBbeyJhZG1pblVSTCI6ICJodHRwOi8vMTkyLjE2OC4xMDAuMTcwOjM1MzU3L3YyLjAiLCAicmVnaW9uIjogIlJlZ2lvbk9uZSIsICJpbnRlcm5hbFVSTCI6ICJodHRwOi8vMTkyLjE2OC4xMDAuMTcwOjUwMDAvdjIuMCIsICJpZCI6ICJiYjM4MWNiOGU3ZTM0MmJmYjQ5MmY0OGM3YmU0MmUwYiIsICJwdWJsaWNVUkwiOiAiaHR0cDovLzE5Mi4xNjguMTAwLjE3MDo1MDAwL3YyLjAifV0sICJlbmRwb2ludHNfbGlua3MiOiBbXSwgInR5cGUiOiAiaWRlbnRpdHkiLCAibmFtZSI6ICJrZXlzdG9uZSJ9XSwgInVzZXIiOiB7InVzZXJuYW1lIjogImFkbWluIiwgInJvbGVzX2xpbmtzIjogW10sICJpZCI6ICI5YTFmOGMxMTRlMmY0NGY4YTVlNDg4OTk5N2YyY2FjMSIsICJyb2xlcyI6IFt7Im5hbWUiOiAiYWRtaW4ifV0sICJuYW1lIjogImFkbWluIn0sICJtZXRhZGF0YSI6IHsiaXNfYWRtaW4iOiAwLCAicm9sZXMiOiBbIjMwYWQ1NTUzOGMxZDQwNWY5NWUzNTRjMjY1MTU2NTEwIl19fX0xgf8wgfwCAQEwXDBXMQswCQYDVQQGEwJVUzEOMAwGA1UECBMFVW5zZXQxDjAMBgNVBAcTBVVuc2V0MQ4wDAYDVQQKEwVVbnNldDEYMBYGA1UEAxMPd3d3LmV4YW1wbGUuY29tAgEBMAcGBSsOAwIaMA0GCSqGSIb3DQEBAQUABIGAgUZkE4kxPKZuClaI5gVOCfuhxAEt66Bz4k2QAUc0qUoYOdRriHwUtjUqpMOPeNxzD2Nz2Iv54j0v52I2N55QNvEIjvkalElgPv7MW4ya4M6+5ZswvZ87wAzR9mQ9zf2oGSVQf+n5H-CitOqJp6jmjKTCpaZLIXxQU+L35d7w1Hs=");
		//		System.out.println(get(url, headers));

		System.out.println(post(url, headers, "{\"auth\": {\"tenantName\": \"admin\", \"passwordCredentials\": {\"username\": \"admin\", \"password\": \"123456\"}}}"));
		//		System.out.println(send(url, headers, "{}"));
		 */
	}

	public static String post(String url, HashMap<String, String> headers, String entity, boolean checkToken) {
		if (token == null && checkToken) {
			initToken();
		}
		logger.info("post(), url=" + url + ", headers=" + headers + ", entity=" + entity.replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\""));
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpPost request = new HttpPost(url);
			Iterator<String> iterator = headers.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				request.setHeader(key, headers.get(key));
			}
			if (entity != null) {
				request.setEntity(new StringEntity(entity, "UTF-8"));
			}

			HttpResponse response = httpClient.execute(request);
			HttpEntity responseEntity = response.getEntity();
			InputStream is = responseEntity.getContent();
			String myString = IOUtils.toString(is, "UTF-8");
			is.close();
			return myString;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String postBytes(String url, HashMap<String, String> headers, byte bytes[], boolean checkToken) {
		if (token == null && checkToken) {
			initToken();
		}
		logger.info("postBytes(), url=" + url + ", headers=" + headers + ", bytes length=" + bytes.length);
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpPost request = new HttpPost(url);
			Iterator<String> iterator = headers.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				request.setHeader(key, headers.get(key));
			}
			if (bytes != null) {
				request.setEntity(new ByteArrayEntity(bytes));
			}

			HttpResponse response = httpClient.execute(request);
			HttpEntity responseEntity = response.getEntity();
			InputStream is = responseEntity.getContent();
			String myString = IOUtils.toString(is, "UTF-8");
			is.close();
			return myString;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String get(String url, HashMap<String, String> headers, boolean checkToken) {
		if (token == null && checkToken) {
			initToken();
		}
		logger.info("get(), url=" + url + ", headers=" + headers + "checkToken=" + checkToken);
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpGet request = new HttpGet(url);
			Iterator<String> iterator = headers.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				request.setHeader(key, headers.get(key));
			}
			HttpResponse response = httpClient.execute(request);
			HttpEntity responseEntity = response.getEntity();
			InputStream is = responseEntity.getContent();
			String myString = IOUtils.toString(is, "UTF-8");
			is.close();

			return myString;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String delete(String url, HashMap<String, String> headers, boolean checkToken) {
		if (token == null && checkToken) {
			initToken();
		}
		logger.info("delete(), url=" + url + ", headers=" + headers + "checkToken=" + checkToken);
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpDelete request = new HttpDelete(url);
			Iterator<String> iterator = headers.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				request.setHeader(key, headers.get(key));
			}
			HttpResponse response = httpClient.execute(request);
			HttpEntity responseEntity = response.getEntity();
			InputStream is = responseEntity.getContent();
			String myString = IOUtils.toString(is, "UTF-8");
			is.close();

			return myString;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private static void initToken() {
		String url = PandoraServerSetting.getInstance().novaOsService_endpoint + "/tokens";
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		String result = post(url, headers, "{\"auth\": {\"tenantName\": \"admin\", \"passwordCredentials\": {\"username\": \"" + PandoraServerSetting.getInstance().novaOsUsername
				+ "\", \"password\": \"" + PandoraServerSetting.getInstance().novaOsPassword + "\"}}}", false);

		JSONObject json = JSONObject.fromObject(result);
		token = json.getJSONObject("access").getJSONObject("token").getString("id");
		tenantId = json.getJSONObject("access").getJSONObject("token").getJSONObject("tenant").getString("id");
	}

	public static String execute(String command, ParameterTableModel parameterTableModel) {
		return execute(command, null, parameterTableModel);
	}

	public static String execute(String command, EnhancedTextArea enhancedTextArea, ParameterTableModel parameterTableModel) {
		return execute(command, parameterTableModel, enhancedTextArea);
	}

	public static String execute(String command, ParameterTableModel parameterTableModel, EnhancedTextArea enhancedTextArea) {
		command = initCommand(command);
		if (command == null) {
			return "command not found";
		}
		initParameters(command, parameterTableModel);
		Pattern pattern = Pattern.compile("-H \"[^\"]+\"");
		Matcher matcher = pattern.matcher(command);
		boolean matchFound = matcher.find();
		HashMap<String, String> headers = new HashMap<String, String>();
		log(enhancedTextArea, "Constructing header...");
		while (matchFound) {
			for (int i = 0; i <= matcher.groupCount(); i++) {
				String groupStr = matcher.group(i);
				groupStr = groupStr.replace("-H \"", "");
				groupStr = groupStr.substring(0, groupStr.length() - 1);
				String temp[] = groupStr.split(":");
				temp[1] = temp[1].trim();
				if (temp[1].contains("$")) {
					temp[1] = parameterTableModel.getValue(temp[1]).toString();
				}
				headers.put(temp[0], temp[1]);

				log(enhancedTextArea, temp[0] + "=" + temp[1]);
			}
			if (matcher.end() + 1 <= command.length()) {
				matchFound = matcher.find(matcher.end());
			} else {
				break;
			}
		}

		// parse url
		pattern = Pattern.compile("-s [a-zA-Z0-9:/\\.\\$_-]+");
		matcher = pattern.matcher(command);
		String url = null;
		matchFound = matcher.find();
		if (matchFound) {
			url = matcher.group(0);
			url = url.replace("-s ", "");
		}
		// end parse url

		// reconstruct -d
		url = filter(url, parameterTableModel);
		// end reconstruct -d

		// parse -d
		String entity = null;
		boolean postBytes = false;
		byte bytes[] = null;

		if (command.contains("-X POST")) {
			if (command.contains("-POSTDATA")) {
				File file = new File("vmImage/" + parameterTableModel.getValue("$POSTDATA").toString());
				bytes = new byte[(int) file.length()];
				try {
					IOUtils.readFully(new FileInputStream(file), bytes);
				} catch (Exception e) {
					e.printStackTrace();
				}
				postBytes = true;
			} else {
				pattern = Pattern.compile("-d '[^']+'");
				matcher = pattern.matcher(command);

				matchFound = matcher.find();
				if (matchFound) {
					entity = matcher.group(0);
					entity = entity.replace("-d '", "");
					entity = entity.substring(0, entity.length() - 1);
				}
				// reconstruct -d
				entity = filter(entity, parameterTableModel);
				// end reconstruct -d
			}
		}
		// end parse -d

		if (url != null) {
			log(enhancedTextArea, "url=" + url);
			String result = null;
			if (command.contains("-X POST")) {
				log(enhancedTextArea, "POST()");
				if (postBytes) {
					result = OpenstackComm.postBytes(url, headers, bytes, true);
				} else {
					log(enhancedTextArea, "entity=" + entity);
					result = OpenstackComm.post(url, headers, entity, true);
				}
			} else if (command.contains("-X DELETE")) {
				log(enhancedTextArea, "delete()");
				result = OpenstackComm.delete(url, headers, true);
			} else {
				log(enhancedTextArea, "GET()");
				result = OpenstackComm.get(url, headers, true);
			}
			log(enhancedTextArea, "result=" + result);
			return result;
		} else {
			log(enhancedTextArea, "url error");
			return null;
		}
	}

	private static String initCommand(String command) {
		if (command.startsWith("from pandora:")) {
			command = command.substring("from pandora:".length()).trim();
			for (String key : PandoraServerSetting.getInstance().novaCommands.keySet()) {
				if (command.equals(key)) {
					command = PandoraServerSetting.getInstance().novaCommands.get(key);
					return command;
				}
			}
		} else {
			return command;
		}
		return null;
	}

	private static String filter(String entity, ParameterTableModel parameterTableModel) {
		if (entity == null) {
			return null;
		}

		//		String newStr = new String(entity);

		Pattern pattern = Pattern.compile("\\$\\w+");
		Matcher matcher = pattern.matcher(entity);
		boolean matchFound = matcher.find();
		Vector<String> allMatches = new Vector<String>();
		while (matchFound) {
			for (int i = 0; i <= matcher.groupCount(); i++) {
				String groupStr = matcher.group(i);
				String temp = groupStr;
				if (temp == null) {
					continue;
				}
				//groupStr = groupStr.replaceAll("\\$", "\\\\\\$");
				allMatches.add(groupStr);
				//				entity = entity.replaceAll(groupStr, (String) parameterTableModel.getValue(temp));
				//				if (matcher.end() + 1 < newStr.length()) {
				matchFound = matcher.find(matcher.end());
				//				} else {
				//					break;
				//				}
			}
		}

		for (String match : allMatches) {
			entity = entity.replaceAll(match.replaceAll("\\$", "\\\\\\$"), (String) parameterTableModel.getValue(match));
		}

		return entity;
	}

	public static void initParameters(String command, ParameterTableModel parameterTableModel) {
		if (token == null) {
			initToken();
		}
		//		parameterTableModel.parameters.clear();
		//		parameterTableModel.values.clear();

		if (!parameterTableModel.parameters.contains("Url")) {
			parameterTableModel.parameters.add("Url");
			parameterTableModel.values.add(command);
		}

		Pattern pattern = Pattern.compile("\\$\\w+");
		Matcher matcher = pattern.matcher(command);
		boolean matchFound = matcher.find();
		while (matchFound) {
			for (int i = 0; i <= matcher.groupCount(); i++) {
				String groupStr = matcher.group(i);
				if (!parameterTableModel.parameters.contains(groupStr)) {
					parameterTableModel.parameters.add(groupStr);
					if (groupStr.equals("$Tenant_name")) {
						parameterTableModel.values.add(PandoraServerSetting.getInstance().novaOsTenantName);
					} else if (groupStr.equals("$Username")) {
						parameterTableModel.values.add(PandoraServerSetting.getInstance().novaOsUsername);
					} else if (groupStr.equals("$Password")) {
						parameterTableModel.values.add(PandoraServerSetting.getInstance().novaOsPassword);
					} else if (groupStr.equals("$Token")) {
						parameterTableModel.values.add(token);
					} else if (groupStr.equals("$Tenant_Id")) {
						parameterTableModel.values.add(tenantId);
					} else {
						parameterTableModel.values.add("");
					}
				}
			}
			if (matcher.end() + 1 <= command.length()) {
				matchFound = matcher.find(matcher.end());
			} else {
				break;
			}
		}
		parameterTableModel.fireTableDataChanged();
	}

	private static void log(EnhancedTextArea enhancedTextArea, String str) {
		if (enhancedTextArea == null) {
			return;
		}
		enhancedTextArea.setText(enhancedTextArea.getText() + "\n" + str);
	}
}
