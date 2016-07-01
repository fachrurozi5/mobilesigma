package com.fachru.sigmamobile.model;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by fachru on 22/01/16.
 */
@Table(name = "SoHeads")
public class SoHead extends Model {

	@Expose
	@SerializedName("SO")
	@Column(name = "so", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public String so;

	@Expose
	@SerializedName("SODATE")
	@Column(name = "so_date")
	public Date so_date;

	@Expose
	@SerializedName("CUSTID")
	@Column(name = "custid")
	public String custid;

	@Expose
	@SerializedName("EMPID")
	@Column(name = "empid")
	public String empid;

	@Expose
	@SerializedName("TOPID")
	@Column(name = "topid")
	public String topid = "30";

	@Expose
	@SerializedName("VATID")
	@Column(name = "vatid")
	public String vatid = "10";

	@Expose
	@SerializedName("PO")
	@Column(name = "po")
	public String purchase_order;

	@Expose
	@SerializedName("DATEORDER")
	@Column(name = "date_oder")
	public Date date_order = new Date();
	;

	@Expose
	@SerializedName("DELDATE")
	@Column(name = "delivery_date")
	public Date delivery_date;

	@Expose
	@SerializedName("PRICETYPE")
	@Column(name = "price_type")
	public int priceType;

	@Expose
	@SerializedName("WHID")
	@Column(name = "whid")
	public String whid;

	@Expose
	@SerializedName("EXRATE")
	@Column(name = "exrate")
	public double exrate = 1;

	@Expose
	@SerializedName("TYPEOFSO")
	@Column(name = "typeofso")
	public int typeofso = 1;

	@Expose
	@SerializedName("DATECREATE")
	@Column(name = "createdAt")
	public Date created_at = new Date();

	@Expose
	@SerializedName("DATEUPDATE")
	@Column(name = "updatedAt")
	public Date updated_at = new Date();

	@Expose
	@SerializedName("uploaded")
	@Column(name = "uploaded")
	public boolean uploaded = false;

	@Column(name = "vatamt")
	public double vatamt;

	@Column(name = "netamt")
	public double netamt;

	@Column(name = "printed")
	public boolean printed = false;


	public SoHead() {
		super();
	}

	public static SoHead find(String so) {
		return new Select()
				.from(SoHead.class)
				.where("so =?", so)
				.executeSingle();
	}

	public static SoHead get(String employeeId, Calendar cal) {
		cal.add(Calendar.DATE, -1);
		return new Select()
				.from(SoHead.class)
				.where("custid = ?", employeeId)
				.and("date_oder > ?", cal.getTimeInMillis())
				.executeSingle();
	}

	public static List<SoHead> getToDayOrderByOutlet(String outletId) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);

		return new Select()
				.from(SoHead.class)
				.where("custid = ?", outletId)
				.and("date_oder > ?", cal.getTimeInMillis())
				.execute();
	}

	public static double getToDayTotalOrderByOutlet(String outletId) {
		double totalOrder = 0;
		for (SoHead soHead :
				getToDayOrderByOutlet(outletId)) {
			totalOrder += soHead.netamt;
		}

		return totalOrder;
	}

	public static List<SoHead> getAll() {
		return new Select()
				.from(SoHead.class)
				.execute();
	}

	public static List<SoHead> getAllWhereCust(String custid) {
		return new Select()
				.from(SoHead.class)
				.where("custid =? ", custid)
				.where("printed =? ", false)
				.orderBy("so DESC")
				.execute();
	}

	public static SoHead last() {
		return new Select().from(SoHead.class)
				.orderBy("so DESC")
				.executeSingle();
	}

	public static int count() {
		return new Select()
				.from(SoHead.class)
				.where("so like ?", "%" + getFirstId() + "%").execute().size();
	}

	public static List<SoHead> getAllHasPrint() {
		return new Select()
				.from(SoHead.class)
				.where("printed =?", true)
				.execute();
	}

	public static boolean hasPrint(String so) {
		return new Select()
				.from(SoHead.class)
				.where("so = ?", so)
				.and("printed =?", true)
				.count() > 0;
	}

	public static List<SoHead> getAllNotUpload() {
		return new Select()
				.from(SoHead.class)
				.where("uploaded =?", false)
				.execute();
	}

	public static boolean hasDataToUpload() {
		return SoHead.getAllNotUpload().size() > 0;
	}

	public static String generateId(String empid) {

		String id = getFirstId();
		long size = count();
		if (size > 0) {
			long v = Long.parseLong(last().so.split("\\.")[2]);
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

	private static String getFirstId() {
		String id = "SO.";
		SimpleDateFormat format = new SimpleDateFormat("MMdd.", Locale.getDefault());
		id += format.format(new Date());
		return id;
	}

	public List<SoItem> soItems() {
		return new Select()
				.from(SoItem.class)
				.where("so =? ", so)
//				.orderBy("id desc")
				.execute();
	}

	public Outlet getOutlet() {
		return new Select()
				.from(Outlet.class)
				.where(Outlet.ID + " = ?", this.custid)
				.executeSingle();
	}

	public String getSoDate() {
		return CommonUtil.dateToStringMedium(this.so_date);
	}

	public String getPoDate() {
		return CommonUtil.dateToStringMedium(this.date_order);
	}

	public String getDelDate() {
		return CommonUtil.dateToStringMedium(this.delivery_date);
	}

	@Override
	public String toString() {
		return "SoHead{" +
				"so='" + so + '\'' +
				", so_date=" + so_date +
				", custid='" + custid + '\'' +
				", empid='" + empid + '\'' +
				", topid='" + topid + '\'' +
				", vatid='" + vatid + '\'' +
				", purchase_order='" + purchase_order + '\'' +
				", date_order=" + date_order +
				", delivery_date=" + delivery_date +
				", priceType=" + priceType +
				", whid='" + whid + '\'' +
				", exrate=" + exrate +
				", typeofso=" + typeofso +
				", createdAt=" + created_at +
				", updatedAt=" + updated_at +
				", uploaded=" + uploaded +
				", vatamt=" + vatamt +
				", netamt=" + netamt +
				", printed=" + printed +
				'}';
	}
}
