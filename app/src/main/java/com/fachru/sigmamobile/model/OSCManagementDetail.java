package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by fachru on 22/06/16.
 */
@Table(name = "osc_management_details")
public class OSCManagementDetail extends Model {

	@Expose
	@SerializedName("osc_management_id")
	@Column(name = "osc_management_id")
	private String oscManagementId;

	@Expose
	@SerializedName("outlet_id")
	@Column(name = "outlet_id")
	private String outletId;

	@Expose
	@SerializedName("total_order")
	@Column(name = "total_order")
	private double totalOrder;

	@Expose
	@SerializedName("entered_outlet")
	@Column(name = "entered_outlet")
	private Date entered_at;

	@Expose
	@SerializedName("exit_at")
	@Column(name = "exit_at")
	private Date exit_at;

	@Expose
	@SerializedName("active_count")
	@Column(name = "active_count")
	private int activeCount;

	@Expose
	@SerializedName("nonactive_count")
	@Column(name = "nonactive_count")
	private int nonActiveCount;

	@Column(name = "OSCManagement", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
	private OSCManagement oscManagement;

	public OSCManagementDetail() {
		super();
	}

	public String getOscManagementId() {
		return oscManagementId;
	}

	public void setOscManagementId(String oscManagementId) {
		this.oscManagementId = oscManagementId;
	}

	public String getOutletId() {
		return outletId;
	}

	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}

	public double getTotalOrder() {
		return totalOrder;
	}

	public void setTotalOrder(double totalOrder) {
		this.totalOrder = totalOrder;
	}

	public Date getEntered_at() {
		return entered_at;
	}

	public void setEntered_at(Date entered_at) {
		this.entered_at = entered_at;
	}

	public Date getExit_at() {
		return exit_at;
	}

	public void setExit_at(Date exit_at) {
		this.exit_at = exit_at;
	}

	public int getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(int activeCount) {
		this.activeCount = activeCount;
	}

	public int getNonActiveCount() {
		return nonActiveCount;
	}

	public void setNonActiveCount(int nonActiveCount) {
		this.nonActiveCount = nonActiveCount;
	}

	public OSCManagement getOscManagement() {
		return oscManagement;
	}

	public void setOscManagement(OSCManagement oscManagement) {
		this.oscManagement = oscManagement;
	}

	@Override
	public String toString() {
		return "OSCManagementDetail{" +
				"oscManagementId='" + oscManagementId + '\'' +
				", outletId='" + outletId + '\'' +
				", totalOrder=" + totalOrder +
				", entered_at=" + entered_at +
				", exit_at=" + exit_at +
				", activeCount=" + activeCount +
				", nonActiveCount=" + nonActiveCount +
				'}';
	}

	public static OSCManagementDetail get(String oscManagementId, String outletId) {
		return new Select()
				.from(OSCManagementDetail.class)
				.where("osc_management_id = ?", oscManagementId)
				.and("outlet_id = ?", outletId)
				.orderBy("osc_management_id DESC")
				.executeSingle();
	}
}
