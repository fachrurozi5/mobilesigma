package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fachru on 09/06/16.
 */
@Table(name = "Tolerances")
public class Tolerance extends Model {

	public static final String ID = "tolc_id";

	@Expose
	@SerializedName("id")
	@Column(name = "tolc_id")
	private int tolId;

	@Expose
	@Column(name = "name", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String name;

	@Expose
	@SerializedName("tolc_in_meters")
	@Column(name = "tolc_in_meters")
	private double tolInMeters;

	@Expose
	@Column(name = "description")
	private String description;

	public Tolerance() {
		super();
	}

	public int getTolId() {
		return tolId;
	}

	public String getName() {
		return name;
	}

	public double getTolInMeters() {
		return tolInMeters;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "Tolerance{" +
				"tolId=" + tolId +
				", name='" + name + '\'' +
				", tolInMeters=" + tolInMeters +
				", description='" + description + '\'' +
				'}';
	}
}
