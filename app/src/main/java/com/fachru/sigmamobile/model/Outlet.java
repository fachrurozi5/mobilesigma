package com.fachru.sigmamobile.model;

import android.location.Location;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fachru on 30/05/16.
 */
@Table(name = "Outlets")
public class Outlet extends Model {

	public static final String ID = "outlet_id";
	public static final String NAME = "name";
	public static final String ADDRESS = "address";
	public static final String OWNER = "owner";
	public static final String SECOND_OWNER = "second_owner";
	public static final String THIRD_OWNER = "third_owner";
	public static final String TELEPHONE_NUMBER = "tel_number";
	public static final String MOBILE = "mobile";
	public static final String LATITUDE = "lat";
	public static final String LONGITUDE = "lng ";
	public static final String PAYMENT = "payment";
	public static final String TOLERANCE_ID = "tolc_id";
	public static final String OUTLET_TYPE_ID = "outlet_type_id";
	public static final String DESCRIPTION = "description";

	@Expose
	@SerializedName("id")
	@Column(name = "outlet_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String outletId;

	@Expose
	@Column(name = NAME)
	private String name;

	@Expose
	@Column(name = ADDRESS)
	private String address;

	@Expose
	@Column(name = OWNER)
	private String owner;

	@Expose
	@SerializedName("second_owner")
	@Column(name = SECOND_OWNER)
	private String secondOwner;

	@Expose
	@SerializedName("third_owner")
	@Column(name = THIRD_OWNER)
	private String thirdOwner;

	@Expose
	@SerializedName("tel_number")
	@Column(name = TELEPHONE_NUMBER)
	private String telephoneNumber;

	@Expose
	@Column(name = MOBILE)
	private String mobile;

	@Expose
	@Column(name = "lat")
	private double lat;

	@Expose
	@SerializedName("lng")
	@Column(name = "lng")
	private double lng;

	@Expose
	@Column(name = PAYMENT)
	private String payment = "Cash";

	@Expose
	@Column(name = TOLERANCE_ID)
	@SerializedName("tolc_id")
	private int toleranceId;

	@Expose
	@SerializedName("outlet_type_id")
	@Column(name = OUTLET_TYPE_ID)
	private int outletTypeId;

	@Expose
	@Column(name = DESCRIPTION)
	private String description;

	public Outlet() {
		super();
	}

	public static Outlet find(String outlet_id) {
		return new Select()
				.from(Outlet.class)
				.where(ID + " = ?", outlet_id)
				.executeSingle();
	}

	public static List<Outlet> get() {
		return new Select()
				.from(Outlet.class)
				.execute();
	}

	public String getOutletId() {
		return outletId;
	}

	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getSecondOwner() {
		return secondOwner;
	}

	public void setSecondOwner(String secondOwner) {
		this.secondOwner = secondOwner;
	}

	public String getThirdOwner() {
		return thirdOwner;
	}

	public void setThirdOwner(String thirdOwner) {
		this.thirdOwner = thirdOwner;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public int getToleranceId() {
		return toleranceId;
	}

	public void setToleranceId(int toleranceId) {
		this.toleranceId = toleranceId;
	}

	public int getOutletTypeId() {
		return outletTypeId;
	}

	public void setOutletTypeId(int outletTypeId) {
		this.outletTypeId = outletTypeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Outlet{" +
				"outletId='" + outletId + '\'' +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				", owner='" + owner + '\'' +
				", secondOwner='" + secondOwner + '\'' +
				", thirdOwner='" + thirdOwner + '\'' +
				", telephoneNumber='" + telephoneNumber + '\'' +
				", mobile='" + mobile + '\'' +
				", lat=" + lat +
				", lng=" + lng +
				", payment='" + payment + '\'' +
				", toleranceId=" + toleranceId +
				", outletTypeId=" + outletTypeId +
				", description='" + description + '\'' +
				'}';
	}

	public String getNameAndId() {
		return name + " (" + outletId + ")";
	}

	public String getOwners() {
		StringBuilder stringBuilder = new StringBuilder();

		if (isStringNull(owner)) stringBuilder.append(owner);
		if (isStringNull(secondOwner)) stringBuilder.append(" | ").append(secondOwner);
		if (isStringNull(thirdOwner)) stringBuilder.append(" | ").append(thirdOwner);

		return stringBuilder.toString();
	}

	public Location getLocation(String provider) {
		Location outlet_location = new Location(provider);
		outlet_location.setLatitude(getLat());
		outlet_location.setLongitude(getLng());
		return outlet_location;
	}

	private boolean isStringNull(String s) {
		return s.equals("") || s.length() == 0 || s == null;
	}
}
