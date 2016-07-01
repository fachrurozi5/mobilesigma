package com.fachru.sigmamobile.model;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.fachru.sigmamobile.utils.Constanta;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by fachru on 25/05/16.
 */
@Table(name = "IdleTimeAnalysis")
public class IdleTimeAnalysis extends Model {

	@Expose
	@SerializedName("id")
	@Column(name = "ita_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public String ita_id;

	@Expose
	@SerializedName("analysis_at")
	@Column(name = "analysis_at")
	public Date analysisAt = new Date();

	@Expose
	@SerializedName("empid")
	@Column(name = "empid")
	public String empId;

	@Expose
	@SerializedName("total_ds")
	@Column(name = "total_ds")
	public long totalStaticDuration;

	@Expose
	@SerializedName("uploaded")
	@Column(name = "uploaded")
	public boolean uploaded = false;

	@Expose
	@SerializedName("idle_time_list")
	public List<IdleTime> idleTimeList = new ArrayList<>();

	@Column(name = "employee")
	public Employee employee;

	public IdleTimeAnalysis() {
		super();
	}

	public IdleTimeAnalysis(Employee employee) {
		super();
		this.employee = employee;
		this.empId = this.employee.getEmployeeId();
		this.ita_id = generateId(this.empId);
	}

	public static String generateId(String empid) {

		String id = getFirstId();
		long size = count();
		if (size > 0) {
			long v = Long.parseLong(last().ita_id.split("\\.")[2]);
			if (size <= 9) {
				id += "00" + (v + 1);
			} else if (size > 9) {
				id += "0" + (v + 1);
			} else if (size > 99) {
				id += (v + 1);
			}
		} else {
			id += "001";
		}

		return id + "." + empid.toUpperCase();
	}

	public static int count() {
		return new Select()
				.from(IdleTimeAnalysis.class)
				.where("ita_id like ?", "%" + getFirstId() + "%").execute().size();
	}

	public static IdleTimeAnalysis last() {
		return new Select().from(IdleTimeAnalysis.class)
				.orderBy("ita_id DESC")
				.executeSingle();
	}

	private static String getFirstId() {
		String id = "ITA.";
		SimpleDateFormat format = new SimpleDateFormat("MMddyy.", Locale.getDefault());
		id += format.format(new Date());
		return id;
	}

	public static boolean hasDataToUpload() {
		return IdleTimeAnalysis.getAllNotUpload().size() > 0;
	}

	public static List<IdleTimeAnalysis> getAllNotUpload() {
		return new Select()
				.from(IdleTimeAnalysis.class)
				.where("uploaded =?", false)
				.execute();
	}

	public static IdleTimeAnalysis getToDayAnalysisByEmp(String empId) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);

		Log.e(Constanta.TAG, "Date " + cal.getTimeInMillis());

		return new Select()
				.from(IdleTimeAnalysis.class)
				.where("analysis_at > ?", cal.getTimeInMillis())
				.and("empid = ? ", empId)
				.orderBy("analysis_at DESC")
				.executeSingle();
	}

	public static IdleTimeAnalysis find(String ita_id) {
		return new Select()
				.from(IdleTimeAnalysis.class)
				.where("ita_id = ?", ita_id)
				.executeSingle();
	}

	public List<IdleTime> getIdleTimeList() {
		return getMany(IdleTime.class, "IdleTimeAnalysis");
	}

	@Override
	public String toString() {
		return "IdleTimeAnalysis{" +
				"analysisAt=" + analysisAt +
				", empId='" + empId + '\'' +
				", totalStaticDuration=" + totalStaticDuration +
				", uploaded=" + uploaded +
//                ", idleTimeList=" + idleTimeList +
				", employee=" + employee +
				'}';
	}
}
