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
 * Created by fachru on 22/06/16.
 */
@Table(name = "osc_managements")
public class OSCManagement extends Model {

	@Column(name = "employee")
	public Employee employee;

	@Expose
	@SerializedName("uploaded")
	@Column(name = "uploaded")
	public boolean uploaded = false;

	@Expose
	@SerializedName("id")
	@Column(name = "osc_management_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String oscManagementId;

	@Expose
	@SerializedName("empid")
	@Column(name = "employee_id")
	private String employeeId;

	@Expose
	@SerializedName("analysis_at")
	@Column(name = "analysis_at")
	private Date analysisAt = new Date();

	@Expose
	@SerializedName("management_detail")
	public List<OSCManagementDetail> oscManagementDetails = new ArrayList<>();

	public OSCManagement() {
		super();
	}

	public OSCManagement(Employee employee) {
		this.employee = employee;
		this.employeeId = employee.getEmployeeId();
		this.oscManagementId = generateId(this.employeeId);
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public boolean isUploaded() {
		return uploaded;
	}

	public void setUploaded(boolean uploaded) {
		this.uploaded = uploaded;
	}

	public String getOscManagementId() {
		return oscManagementId;
	}

	public void setOscManagementId(String oscManagementId) {
		this.oscManagementId = oscManagementId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Date getAnalysisAt() {
		return analysisAt;
	}

	public void setAnalysisAt(Date analysisAt) {
		this.analysisAt = analysisAt;
	}

	public static String generateId(String empid) {

		String id = getFirstId();
		long size = count();
		if (size > 0) {
			long v = Long.parseLong(last().oscManagementId.split("\\.")[2]);
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
				.from(OSCManagement.class)
				.where("osc_management_id like ?", "%" + getFirstId() + "%").execute().size();
	}

	public static OSCManagement last() {
		return new Select().from(OSCManagement.class)
				.orderBy("osc_management_id DESC")
				.executeSingle();
	}

	private static String getFirstId() {
		String id = "OSCM.";
		SimpleDateFormat format = new SimpleDateFormat("MMddyy.", Locale.getDefault());
		id += format.format(new Date());
		return id;
	}

	public static List<OSCManagement> getAllNotUpload() {
		return new Select()
				.from(OSCManagement.class)
				.where("uploaded =?", false)
				.execute();
	}

	public static OSCManagement getToDayManagementByEmployee(String employeeId) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);

		Log.e(Constanta.TAG, "Date " + cal.getTimeInMillis());

		return new Select()
				.from(OSCManagement.class)
				.where("analysis_at > ?", cal.getTimeInMillis())
				.and("employee_id = ? ", employeeId)
				.orderBy("analysis_at DESC")
				.executeSingle();
	}

	public static OSCManagement find(String oscManagementId) {
		return new Select()
				.from(OSCManagement.class)
				.where("osc_management_id = ?", oscManagementId)
				.executeSingle();
	}

	public List<OSCManagementDetail> getManagementDetails() {
		return getMany(OSCManagementDetail.class, "OSCManagement");
	}

	public static boolean hasDataToUpload() {
		return OSCManagement.getAllNotUpload().size() > 0;
	}

	@Override
	public String toString() {
		return "OSCManagement{" +
				"employee=" + employee +
				", uploaded=" + uploaded +
				", oscManagementId='" + oscManagementId + '\'' +
				", employeeId='" + employeeId + '\'' +
				", analysisAt=" + analysisAt +
				", oscManagementDetails=" + oscManagementDetails +
				'}';
	}
}
