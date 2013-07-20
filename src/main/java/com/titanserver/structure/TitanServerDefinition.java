package com.titanserver.structure;

import java.io.Serializable;

public class TitanServerDefinition implements Serializable {
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
