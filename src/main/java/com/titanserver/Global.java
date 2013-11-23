package com.titanserver;

import java.net.Socket;
import java.util.Hashtable;

public class Global {
	public static String version = "20131123";
	public static String stage = "dev";
	public static Hashtable<Integer, Socket> clients = new Hashtable<Integer, Socket>();
}
