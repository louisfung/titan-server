package com.titanserver;

import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {
	static Properties prop = null;

	public static String getProperty(String name) {
		if (prop == null) {
			prop = new Properties();
			try {
				prop.load(PropertyUtil.class.getClassLoader().getResourceAsStream("main.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return prop.getProperty(name);
	}
}
