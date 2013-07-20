package com.c2.pandoraserver.table;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "screenPermission")
public class ScreenPermission implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "screenPermissionId", unique = true, nullable = false)
	private Integer screenPermissionId;

	@Column(name = "name", unique = true)
	private String name;

	@Column(name = "description")
	private String description;

	@ManyToMany(mappedBy = "screenPermissions", fetch = FetchType.EAGER)
	private Set<ScreenPermissionGroup> screenPermissionGroups = new HashSet<ScreenPermissionGroup>();

	public Integer getScreenPermissionId() {
		return screenPermissionId;
	}

	public void setScreenPermissionId(Integer screenPermissionId) {
		this.screenPermissionId = screenPermissionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {
		return "Id=" + getScreenPermissionId() + ", name=" + getName() + ", description=" + getDescription();
	}

}