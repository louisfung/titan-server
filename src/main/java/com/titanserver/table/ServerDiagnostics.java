package com.titanserver.table;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "serverDiagnostics")
public class ServerDiagnostics implements java.io.Serializable {
	private Integer serverDiagnosticsID;
	private Date date;
	private double cpu;
	private double memory;
	private double disk;
	private double network;

	public ServerDiagnostics() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "serverDiagnosticsID", unique = true, nullable = false)
	public Integer getServerDiagnosticsID() {
		return this.serverDiagnosticsID;
	}

	public void setServerDiagnosticsID(Integer serverDiagnosticsID) {
		this.serverDiagnosticsID = serverDiagnosticsID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getCpu() {
		return cpu;
	}

	public double getMemory() {
		return memory;
	}

	public double getDisk() {
		return disk;
	}

	public double getNetwork() {
		return network;
	}

	public void setCpu(double cpu) {
		this.cpu = cpu;
	}

	public void setMemory(double memory) {
		this.memory = memory;
	}

	public void setDisk(double disk) {
		this.disk = disk;
	}

	public void setNetwork(double network) {
		this.network = network;
	}

}