package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by fachru on 25/05/16.
 */
@Table(name = "IdleTime")
public class IdleTime extends Model {

	@Expose
	@SerializedName("idle_time_analysis")
	@Column(name = "ita_id")
	public String _id;
	@Expose
	@SerializedName("last_idle")
	@Column(name = "last_idle")
	public Date lastIdle;
	@Expose
	@SerializedName("duration_idle")
	@Column(name = "duration_idle")
	public int durationIdle;
	@Expose
	@SerializedName("description")
	@Column(name = "desc")
	public String description;
	@Column(name = "IdleTimeAnalysis", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
	public IdleTimeAnalysis timeAnalysis;
	@Expose
	@Column(name = "lat")
	private double latitude;
	@Expose
	@Column(name = "long")
	private double longitude;

	public IdleTime() {
		super();
	}

	public IdleTime(String _id, double latitude, double longitude, Date lastIdle, int durationIdle, IdleTimeAnalysis timeAnalysis) {
		super();
		this._id = _id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.lastIdle = lastIdle;
		this.durationIdle = durationIdle;
		this.timeAnalysis = timeAnalysis;
	}

	public static long getSumDurationIdle(long l) {
		List<IdleTime> list = new Select()
				.from(IdleTime.class)
				.where("IdleTimeAnalysis = ?", l)
				.execute();

		int total_ds = 0;

		for (IdleTime idleTime :
				list) {
			total_ds += idleTime.durationIdle;
		}

		return total_ds;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public long getSumDurationIdle() {
		List<IdleTime> list = new Select()
				.from(IdleTime.class)
				.where("IdleTimeAnalysis = ?", timeAnalysis.getId())
				.execute();

		int total_ds = 0;

		for (IdleTime idleTime :
				list) {
			total_ds += idleTime.durationIdle;
		}

		return total_ds;
	}

	@Override
	public String toString() {
		return "IdleTime{" +
				"latitude=" + latitude +
				", longitude=" + longitude +
				", lastIdle=" + lastIdle +
				", durationIdle=" + durationIdle +
				", description='" + description + '\'' +
				", timeAnalysis=" + timeAnalysis +
				'}';
	}
}
