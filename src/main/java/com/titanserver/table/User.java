package com.c2.pandoraserver.table;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "userId", unique = true, nullable = false)
	private Integer userId;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "enable")
	private boolean enable;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_screenPermissionGroup", joinColumns = { @JoinColumn(name = "userId", referencedColumnName = "userId") }, inverseJoinColumns = { @JoinColumn(name = "screenPermissionGroupId", referencedColumnName = "screenPermissionGroupId") })
	private Set<ScreenPermissionGroup> screenPermissionGroups = new HashSet<ScreenPermissionGroup>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_instancePermissionGroup", joinColumns = { @JoinColumn(name = "userId", referencedColumnName = "userId") }, inverseJoinColumns = { @JoinColumn(name = "instancePermissionGroupId", referencedColumnName = "instancePermissionGroupId") })
	private Set<InstancePermissionGroup> instancePermissionGroups = new HashSet<InstancePermissionGroup>();

	public User() {
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public Set<ScreenPermissionGroup> getScreenPermissionGroups() {
		return screenPermissionGroups;
	}

	public void setScreenPermissionGroups(Set<ScreenPermissionGroup> screenPermissionGroups) {
		this.screenPermissionGroups = screenPermissionGroups;
	}

	public Set<InstancePermissionGroup> getInstancePermissionGroups() {
		return instancePermissionGroups;
	}

	public void setInstancePermissionGroups(Set<InstancePermissionGroup> instancePermissionGroups) {
		this.instancePermissionGroups = instancePermissionGroups;
	}

	public String toString() {
		return "Id=" + getUserId() + ", username=" + getUsername() + ", email=" + getEmail() + ", enable=" + isEnable();
	}

}