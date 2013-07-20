package com.c2.pandoraserver.structure;

import java.io.Serializable;

public class PandoraServerDefinition implements Serializable {
	public String id;
	public String ip;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String toString() {
		return id + " - " + ip;
	}
}
