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
@Table(name = "screenPermissionGroup")
public class ScreenPermissionGroup implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "screenPermissionGroupId", unique = true, nullable = false)
	private Integer screenPermissionGroupId;

	@Column(name = "Name")
	private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "permissionGroup_ScreenPermission", joinColumns = { @JoinColumn(name = "screenPermissionGroupId", referencedColumnName = "screenPermissionGroupId") }, inverseJoinColumns = { @JoinColumn(name = "screenPermissionId", referencedColumnName = "screenPermissionId") })
	private Set<ScreenPermission> screenPermissions = new HashSet<ScreenPermission>();

	public ScreenPermissionGroup() {
	}

	public Integer getScreenPermissionGroupId() {
		return screenPermissionGroupId;
	}

	public void setScreenPermissionGroupId(Integer screenPermissionGroupId) {
		this.screenPermissionGroupId = screenPermissionGroupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ScreenPermission> getScreenPermissions() {
		return screenPermissions;
	}

	public void setScreenPermissions(Set<ScreenPermission> screenPermissions) {
		this.screenPermissions = screenPermissions;
	}

	public String toString() {
		return "Id=" + getScreenPermissionGroupId() + ", name=" + getName();
	}

}