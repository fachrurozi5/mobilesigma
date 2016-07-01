package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fachru on 30/05/16.
 */
@Table(name = "Principal")
public class Principal extends Model {

	public static final String ID = "principal_id";
	public static final String NAME = "name";

	@Expose
	@SerializedName("id")
	@Column(name = ID)
	private String principal_id;

	@Expose
	@Column(name = NAME)
	private String name;

	public Principal() {
		super();
	}

	public Principal(String name, String principal_id) {
		super();
		this.name = name;
		this.principal_id = principal_id;
	}

	public String getPrincipal_id() {
		return principal_id;
	}

	public void setPrincipal_id(String principal_id) {
		this.principal_id = principal_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Principal{" +
				"principal_id='" + principal_id + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
