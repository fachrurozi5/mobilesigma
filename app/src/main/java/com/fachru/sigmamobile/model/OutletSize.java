package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fachru on 25/05/16.
 */
@Table(name = "OutletSize")
public class OutletSize extends Model {

	@Expose
	@SerializedName("tolc_large")
	@Column(name = "tolc_large")
	public long TOLCl;

	@Expose
	@SerializedName("tolc_medium")
	@Column(name = "tolc_medium")
	public long TOLCm;

	@Expose
	@SerializedName("tolc_reguler")
	@Column(name = "tolc_reguler")
	public long TOLCr;

	@Expose
	@SerializedName("tolc_small")
	@Column(name = "tolc_small")
	public long TOLCs;

	@Expose
	@SerializedName("custid")
	@Column(name = "cust_id")
	public String custId;

	@Column(name = "Customer")
	public Customer customer;

	public OutletSize() {
		super();
	}

	@Override
	public String toString() {
		return "OutletSize{" +
				"TOLCl=" + TOLCl +
				", TOLCm=" + TOLCm +
				", TOLCr=" + TOLCr +
				", TOLCs=" + TOLCs +
				", customer=" + customer +
				'}';
	}
}
