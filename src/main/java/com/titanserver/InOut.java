package com.c2.pandoraserver;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class InOut {
	public ObjectOutputStream out;
	public ObjectInputStream in;
	public int port;

	public InOut(ObjectInputStream in, ObjectOutputStream out, int port) {
		this.in = in;
		this.out = out;
		this.port = port;
	}
}
