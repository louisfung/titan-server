package com.titanserver.table;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "instancePermission")
public class InstancePermission implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "instancePermissionId", unique = true, nullable = false)
	private Integer instancePermissionId;

	@Column(name = "name", unique = true)
	private String name;

	@Column(name = "description")
	private String description;

	public Integer getInstancePermissionId() {
		return instancePermissionId;
	}

	public void setInstancePermissionId(Integer instancePermissionId) {
		this.instancePermissionId = instancePermissionId;
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
		return "Id=" + getInstancePermissionId() + ", name=" + getName() + ", description=" + getDescription();
	}
}