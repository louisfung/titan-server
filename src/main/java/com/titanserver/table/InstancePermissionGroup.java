package com.titanserver.table;

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
@Table(name = "instancePermissionGroup")
public class InstancePermissionGroup implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "instancePermissionGroupId", unique = true, nullable = false)
	private Integer instancePermissionGroupId;

	@Column(name = "name")
	private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "permissionGroup_Instance", joinColumns = { @JoinColumn(name = "instancePermissionGroupId", referencedColumnName = "instancePermissionGroupId") }, inverseJoinColumns = { @JoinColumn(name = "instancePermissionId", referencedColumnName = "instancePermissionId") })
	private Set<InstancePermission> instancePermissions = new HashSet<InstancePermission>();

	public InstancePermissionGroup() {
	}

	public Integer getInstancePermissionGroupId() {
		return instancePermissionGroupId;
	}

	public void setInstancePermissionGroupId(Integer instancePermissionGroupId) {
		this.instancePermissionGroupId = instancePermissionGroupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<InstancePermission> getInstancePermissions() {
		return instancePermissions;
	}

	public void setInstancePermissions(Set<InstancePermission> instancePermissions) {
		this.instancePermissions = instancePermissions;
	}

	public String toString() {
		return "Id=" + getInstancePermissionGroupId() + ", name=" + getName();
	}
}