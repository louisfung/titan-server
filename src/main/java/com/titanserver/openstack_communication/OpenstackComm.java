package com.titanserver.openstack_communication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.peterswing.advancedswing.enhancedtextarea.EnhancedTextArea;
import com.titanserver.HttpResult;
import com.titanserver.ParameterTableModel;
import com.titanserver.TitanServerCommonLib;
import com.titanserver.TitanServerSetting;

public class OpenstackComm {

	private static Logger logger = Logger.getLogger(TitanServerCommonLib.class);

	static String url = "http://127.0.0.1:35357/v2.0/tokens";
	static String tenantId;

	public static String tenantID;
	public static String token;
	public static int maxLogLength = 2000;

	public static void main(String args[]) {
		initToken();
	}

	public static HttpResult post(String url, HashMap<String, String> headers, String entity, boolean checkToken) {
		if (token == null && checkToken) {
			initToken();
		}
		logger.info("post(), url=" + url + ", headers=" + headers + ", entity=" + entity.replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\""));

		CloseableHttpClient httpClient = HttpClients.createDefault();

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

			HttpResult httpResult = new HttpResult();
			httpResult.content = myString;
			httpResult.headers = response.getAllHeaders();
			return httpResult;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static HttpResult put(String url, HashMap<String, String> headers, String entity, boolean checkToken) {
		if (token == null && checkToken) {
			initToken();
		}
		logger.info("put(), url=" + url + ", headers=" + headers + ", entity=" + entity.replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\""));

		CloseableHttpClient httpClient = HttpClients.createDefault();

		try {
			HttpPut request = new HttpPut(url);
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

			HttpResult httpResult = new HttpResult();
			httpResult.content = myString;
			httpResult.headers = response.getAllHeaders();
			return httpResult;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static HttpResult postBytes(String url, HashMap<String, String> headers, byte bytes[], boolean checkToken) {
		if (token == null && checkToken) {
			initToken();
		}
		logger.info("postBytes(), url=" + url + ", headers=" + headers + ", bytes length=" + bytes.length);
		CloseableHttpClient httpClient = HttpClients.createDefault();

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

			HttpResult httpResult = new HttpResult();
			httpResult.content = myString;
			httpResult.headers = response.getAllHeaders();
			return httpResult;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static HttpResult get(String url, HashMap<String, String> headers, boolean checkToken) {
		if (token == null && checkToken) {
			initToken();
		}
		//		logger.info("get(), url=" + url + ", headers=" + headers + "checkToken=" + checkToken);
		CloseableHttpClient httpClient = HttpClients.createDefault();

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

			HttpResult httpResult = new HttpResult();
			httpResult.content = myString;
			httpResult.headers = response.getAllHeaders();
			return httpResult;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static HttpResult delete(String url, HashMap<String, String> headers, boolean checkToken) {
		if (token == null && checkToken) {
			initToken();
		}
		logger.info("delete(), url=" + url + ", headers=" + headers + "checkToken=" + checkToken);
		CloseableHttpClient httpClient = HttpClients.createDefault();

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

			HttpResult httpResult = new HttpResult();
			httpResult.content = myString;
			httpResult.headers = response.getAllHeaders();
			return httpResult;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static void initToken() {
		String url = TitanServerSetting.getInstance().novaOsService_endpoint + "/tokens";
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept", "application/json");
		HttpResult result = post(url, headers, "{\"auth\": {\"tenantName\": \"" + TitanServerSetting.getInstance().novaOsTenantName
				+ "\", \"passwordCredentials\": {\"username\": \"" + TitanServerSetting.getInstance().novaOsUsername + "\", \"password\": \""
				+ TitanServerSetting.getInstance().novaOsPassword + "\"}}}", false);

		JSONObject json = JSONObject.fromObject(result);
		token = json.getJSONObject("access").getJSONObject("token").getString("id");
		tenantId = json.getJSONObject("access").getJSONObject("token").getJSONObject("tenant").getString("id");
	}

	public static HttpResult execute(String command, ParameterTableModel parameterTableModel) {
		return execute(command, null, parameterTableModel);
	}

	public static HttpResult execute(String command, EnhancedTextArea enhancedTextArea, ParameterTableModel parameterTableModel) {
		return execute(command, parameterTableModel, enhancedTextArea);
	}

	public static HttpResult execute(String command, ParameterTableModel parameterTableModel, EnhancedTextArea enhancedTextArea) {
		command = initCommand(command);
		if (command == null) {
			HttpResult httpResult = new HttpResult();
			httpResult.content = "command not found";
			httpResult.headers = null;
			return httpResult;
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
		//		logger.info("url=" + url);
		// end reconstruct -d

		// parse -d
		String entity = null;
		boolean postBytes = false;
		byte bytes[] = null;

		if (command.contains("-X POST") || command.contains("-X PUT")) {
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
			HttpResult result = null;
			if (command.contains("-X POST")) {
				log(enhancedTextArea, "POST()");
				if (postBytes) {
					result = OpenstackComm.postBytes(url, headers, bytes, true);
				} else {
					log(enhancedTextArea, "entity=" + entity);
					result = OpenstackComm.post(url, headers, entity, true);
				}
			} else if (command.contains("-X PUT")) {
				log(enhancedTextArea, "PUT()");
				if (postBytes) {
					result = OpenstackComm.postBytes(url, headers, bytes, true);
				} else {
					log(enhancedTextArea, "entity=" + entity);
					result = OpenstackComm.put(url, headers, entity, true);
				}
			} else if (command.contains("-X DELETE")) {
				log(enhancedTextArea, "delete()");
				result = OpenstackComm.delete(url, headers, true);
			} else {
				log(enhancedTextArea, "GET()");
				result = OpenstackComm.get(url, headers, true);
			}
			logJSon(enhancedTextArea, result.content);
			return result;
		} else {
			log(enhancedTextArea, "url error");
			return null;
		}
	}

	private static String initCommand(String command) {
		if (command.startsWith("from titan:")) {
			command = command.substring("from titan:".length()).trim();
			for (String key : TitanServerSetting.getInstance().novaCommands.keySet()) {
				if (command.equals(key)) {
					command = TitanServerSetting.getInstance().novaCommands.get(key);
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
						parameterTableModel.values.add(TitanServerSetting.getInstance().novaOsTenantName);
					} else if (groupStr.equals("$Username")) {
						parameterTableModel.values.add(TitanServerSetting.getInstance().novaOsUsername);
					} else if (groupStr.equals("$Password")) {
						parameterTableModel.values.add(TitanServerSetting.getInstance().novaOsPassword);
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

	private static void logJSon(EnhancedTextArea enhancedTextArea, String str) {
		if (enhancedTextArea == null) {
			return;
		}
		try {
			enhancedTextArea.setText(enhancedTextArea.getText() + "\n" + formatJSon(str));
		} catch (Exception ex) {
			if (str.length() < maxLogLength) {
				enhancedTextArea.setText(enhancedTextArea.getText() + "\n" + str);
			} else {
				enhancedTextArea.setText(enhancedTextArea.getText() + "\n" + str.substring(0, maxLogLength));
			}
		}
	}

	private static String formatJSon(String str) {
		JsonParser parser = new JsonParser();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonElement el = parser.parse(str);
		return gson.toJson(el);
	}
}
