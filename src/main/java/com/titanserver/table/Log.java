package com.titanserver.table;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "log")
public class Log implements java.io.Serializable {
	private Integer logID;
	private String username;
	private Date date;
	private String priority;
	private String message;
	private String category;

	public Log() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "logID", unique = true, nullable = false)
	public Integer getLogID() {
		return this.logID;
	}

	public void setLogID(Integer logID) {
		this.logID = logID;
	}

	@Column(name = "date")
	public Date getDate() {
		return date;
	}

	@Column(name = "priority")
	public String getPriority() {
		return priority;
	}

	@Column(name = "message", length = 40960)
	public String getMessage() {
		return message;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "category", length = 2048)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "username", length = 256)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}