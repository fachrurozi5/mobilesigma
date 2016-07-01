package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fachru on 14/06/16.
 */
@Table(name = "Discounts_lv1")
public class DiscountLv1 extends Model {

	@Expose
	@SerializedName("id")
	@Column(name = "discount_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String discount_id;

	@Expose
	@SerializedName("outlet_type_id")
	@Column(name = "outlet_type_id")
	private int outletTypeId;

	@Expose
	@SerializedName("principal_id")
	@Column(name = "principal_id")
	private String principalId;

	@Expose
	@SerializedName("percent_discount")
	@Column(name = "percent_discount")
	private double percentDiscount;

	@Expose
	@SerializedName("min_qty")
	@Column(name = "min_qty")
	private int minQty;

	public DiscountLv1() {
		super();
	}

	public static DiscountLv1 find(String id) {
		return new Select()
				.from(DiscountLv1.class)
				.where("discount_id = ?", id)
				.executeSingle();
	}

	public static List<DiscountLv1> get() {
		return new Select()
				.from(DiscountLv1.class)
				.execute();
	}

	public static List<DiscountLv1> get(int outletTypeId, String principalId) {
		return new Select()
				.from(DiscountLv1.class)
				.where("outlet_type_id = ?", outletTypeId)
				.and("principal_id = ?", principalId)
				.execute();
	}

	public static List<DiscountLv1> get(int outletTypeId) {
		return new Select()
				.from(DiscountLv1.class)
				.where("outlet_type_id = ?", outletTypeId)
				.execute();
	}

	public String getDiscount_id() {
		return discount_id;
	}

	public int getOutletTypeId() {
		return outletTypeId;
	}

	public String getPrincipalId() {
		return principalId;
	}

	public double getPercentDiscount() {
		return percentDiscount;
	}

	public int getMinQty() {
		return minQty;
	}

	@Override
	public String toString() {
		return "DiscountLv1{" +
				"discount_id='" + discount_id + '\'' +
				", outletTypeId=" + outletTypeId +
				", principalId='" + principalId + '\'' +
				", percentDiscount=" + percentDiscount +
				", minQty=" + minQty +
				'}';
	}
}
