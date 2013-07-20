package com.c2.pandoraserver;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

public class Command implements Serializable {
	public static int id = -1;
	public Date date;
	public String type;
	public String command;
	public Vector<Object> parameters = new Vector<Object>();
	public String filename;

	public String toString() {
		String s = "Date=" + date + ", type=" + type + ", command=" + command;
		for (int x = 0; x < parameters.size(); x++) {
			s += ", para[" + x + "]=" + parameters.get(x);
		}
		return s;
	}
}
