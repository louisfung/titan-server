package com.c2.pandoraserver.table;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "status")
public class Status implements java.io.Serializable {
	private Integer statusID;
	private String ip;
	private Date date;
	private String message;

	public Status() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "statusID", unique = true, nullable = false)
	public Integer getStatusID() {
		return this.statusID;
	}

	public void setStatusID(Integer statusID) {
		this.statusID = statusID;
	}

	@Column(name = "date")
	public Date getDate() {
		return date;
	}

	@Column(name = "message", length = 40960)
	public String getMessage() {
		return message;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "ip", length = 256)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}