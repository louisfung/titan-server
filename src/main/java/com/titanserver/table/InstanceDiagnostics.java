package com.titanserver.table;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "instanceDiagnostics")
public class InstanceDiagnostics implements java.io.Serializable {
	private Integer instanceDiagnosticsID;
	private String instanceID;
	private Date date;
	private int hdd_errors;
	private int vnet7_tx_drop;
	private int vnet7_tx_packets;
	private int vda_read;
	private int vda_write;
	private int vda_write_req;
	private int vnet7_rx_errors;
	private int vnet7_tx_errors;
	private int vnet7_rx;
	private int vda_errors;
	private int vnet7_rx_drop;
	private int hdd_read;
	private int hdd_read_req;
	private int memory;
	private int hdd_write_req;
	private int vnet7_rx_packets;
	private int cpu0_time;
	private int vnet7_tx;
	private int vda_read_req;
	private int hdd_write;

	public InstanceDiagnostics() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "instanceDiagnosticsID", unique = true, nullable = false)
	public Integer getDiagnosticsID() {
		return this.instanceDiagnosticsID;
	}

	public void setDiagnosticsID(Integer instanceDiagnosticsID) {
		this.instanceDiagnosticsID = instanceDiagnosticsID;
	}

	public String getInstanceID() {
		return instanceID;
	}

	public void setInstanceID(String instanceID) {
		this.instanceID = instanceID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getHdd_errors() {
		return hdd_errors;
	}

	public int getVnet7_tx_drop() {
		return vnet7_tx_drop;
	}

	public int getVnet7_tx_packets() {
		return vnet7_tx_packets;
	}

	public int getVda_read() {
		return vda_read;
	}

	public int getVda_write() {
		return vda_write;
	}

	public int getVda_write_req() {
		return vda_write_req;
	}

	public int getVnet7_rx_errors() {
		return vnet7_rx_errors;
	}

	public int getVnet7_tx_errors() {
		return vnet7_tx_errors;
	}

	public int getVnet7_rx() {
		return vnet7_rx;
	}

	public int getVda_errors() {
		return vda_errors;
	}

	public int getVnet7_rx_drop() {
		return vnet7_rx_drop;
	}

	public int getHdd_read() {
		return hdd_read;
	}

	public int getHdd_read_req() {
		return hdd_read_req;
	}

	public int getMemory() {
		return memory;
	}

	public int getHdd_write_req() {
		return hdd_write_req;
	}

	public int getVnet7_rx_packets() {
		return vnet7_rx_packets;
	}

	public int getCpu0_time() {
		return cpu0_time;
	}

	public int getVnet7_tx() {
		return vnet7_tx;
	}

	public int getVda_read_req() {
		return vda_read_req;
	}

	public int getHdd_write() {
		return hdd_write;
	}

	public void setHdd_errors(int hdd_errors) {
		this.hdd_errors = hdd_errors;
	}

	public void setVnet7_tx_drop(int vnet7_tx_drop) {
		this.vnet7_tx_drop = vnet7_tx_drop;
	}

	public void setVnet7_tx_packets(int vnet7_tx_packets) {
		this.vnet7_tx_packets = vnet7_tx_packets;
	}

	public void setVda_read(int vda_read) {
		this.vda_read = vda_read;
	}

	public void setVda_write(int vda_write) {
		this.vda_write = vda_write;
	}

	public void setVda_write_req(int vda_write_req) {
		this.vda_write_req = vda_write_req;
	}

	public void setVnet7_rx_errors(int vnet7_rx_errors) {
		this.vnet7_rx_errors = vnet7_rx_errors;
	}

	public void setVnet7_tx_errors(int vnet7_tx_errors) {
		this.vnet7_tx_errors = vnet7_tx_errors;
	}

	public void setVnet7_rx(int vnet7_rx) {
		this.vnet7_rx = vnet7_rx;
	}

	public void setVda_errors(int vda_errors) {
		this.vda_errors = vda_errors;
	}

	public void setVnet7_rx_drop(int vnet7_rx_drop) {
		this.vnet7_rx_drop = vnet7_rx_drop;
	}

	public void setHdd_read(int hdd_read) {
		this.hdd_read = hdd_read;
	}

	public void setHdd_read_req(int hdd_read_req) {
		this.hdd_read_req = hdd_read_req;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public void setHdd_write_req(int hdd_write_req) {
		this.hdd_write_req = hdd_write_req;
	}

	public void setVnet7_rx_packets(int vnet7_rx_packets) {
		this.vnet7_rx_packets = vnet7_rx_packets;
	}

	public void setCpu0_time(int cpu0_time) {
		this.cpu0_time = cpu0_time;
	}

	public void setVnet7_tx(int vnet7_tx) {
		this.vnet7_tx = vnet7_tx;
	}

	public void setVda_read_req(int vda_read_req) {
		this.vda_read_req = vda_read_req;
	}

	public void setHdd_write(int hdd_write) {
		this.hdd_write = hdd_write;
	}

}