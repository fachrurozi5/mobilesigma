package com.fachru.sigmamobile.model;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by fachru on 28/01/16.
 */
@Table(name = "SoItems")
public class SoItem extends Model {

	private static final String TAG = "SalesOrderActivity";

	@Expose
	@SerializedName("SO")
	@Column(name = "so")
	public String so;

	@Expose
	@SerializedName("NOITEM")
	@Column(name = "noItem")
	public String noItem;

	@Expose
	@SerializedName("PRODID")
	@Column(name = "productId")
	public String productId;

	@Expose
	@SerializedName("PRICELIST")
	@Column(name = "priceList")
	public double priceList;

	@Expose
	@SerializedName("UNITID")
	@Column(name = "unitId")
	public String unitId;

	@Expose
	@SerializedName("QUANTITY")
	@Column(name = "qty")
	public int qty;

	@Expose
	@SerializedName("DATECREATE")
	@Column(name = "createdAt")
	public Date createdAt = new Date();

	@Expose
	@SerializedName("DATEUPDATE")
	@Column(name = "updatedAt")
	public Date updatedAt = new Date();

	@Expose
	@SerializedName("uploaded")
	@Column(name = "uploaded")
	public boolean uploaded = false;

	@Column(name = "subTotal")
	public double subTotal;

	@Column(name = "p_disc")
	public double discountPrinc;

	@Column(name = "n_disc")
	public double discountNst;

	@Column(name = "bonus")
	public int mulBonus = 0;

	public SoItem() {
		super();
	}

	public SoItem(Builder builder) {
		super();
		so = builder.so;
		productId = builder.prodId;
		priceList = builder.priceList;
		qty = builder.qty;
		mulBonus = builder.bonus;
		discountPrinc = builder.discountPrinc;
		discountNst = builder.discountNst;
		subTotal = builder.total;
		unitId = builder.unitid;
	}

	public static List<SoItem> getAllNotUpload() {
		return new Select()
				.from(SoItem.class)
				.where("uploaded =?", false)
				.execute();
	}

	public static SoItem find(String so, String noitem) {
		// TODO: 24/03/16 onjecr getclass() on a null object reference
		try {
			return new Select()
					.from(SoItem.class)
					.where("so = ? ", so)
					.and("noItem = ? ", noitem)
					.executeSingle();
		} catch (Exception e) {
			Log.e(TAG, "So FIND", e);
			return null;
		}

	}

	public static SoItem findBonus(SoItem soItem) {
		return new Select()
				.from(SoItem.class)
				.where("so = ?", soItem.so)
				.and("productId = ?", soItem.productId)
				.and("unitId = ?", soItem.unitId)
				.and("bonus < ?", 0)
				.executeSingle();
	}

	public static boolean hasDataToUpload() {
		return SoItem.getAllNotUpload().size() > 0;
	}

	public Product getProduct() {
		return new Select()
				.from(Product.class)
				.where(Product.ID + " = ?", this.productId)
				.executeSingle();
	}

	public List<SoItem> allWhereSoHead(String so) {
		return new Select()
				.from(SoItem.class)
				.where("so =? ", so)
				.execute();
	}

	@Override
	public String toString() {
		return "SoItem{" +
				"so='" + so + '\'' +
				", noItem='" + noItem + '\'' +
				", productId='" + productId + '\'' +
				", priceList=" + priceList +
				", unitId='" + unitId + '\'' +
				", qty=" + qty +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt +
				", uploaded=" + uploaded +
				", subTotal=" + subTotal +
				", discountPrinc=" + discountPrinc +
				", discountNst=" + discountNst +
				", mulBonus=" + mulBonus +
				'}';
	}

	public static class Builder {
		public String so;

		public String prodId;

		public String unitid;

		public double priceList;

		public int qty;

		public int bonus;

		public double discountNst;

		public double discountPrinc;

		public double total;

		public Builder setSo(String so) {
			this.so = so;
			return Builder.this;
		}

		public Builder setProductId(String prodId) {
			this.prodId = prodId;
			return Builder.this;
		}

		public Builder setPriceList(double priceList) {
			this.priceList = priceList;
			return Builder.this;
		}

		public Builder setUnitId(String unitId) {
			this.unitid = unitId;
			return Builder.this;
		}

		public Builder setQty(int qty) {
			this.qty = qty;
			return Builder.this;
		}

		public Builder setBonus(int bonus) {
			this.bonus = bonus;
			return Builder.this;
		}

		public Builder setDiscountNst(double discountNst) {
			this.discountNst = discountNst;
			return Builder.this;
		}

		public Builder setDiscountPrinc(double discountPrinc) {
			this.discountPrinc = discountPrinc;
			return Builder.this;
		}

		public Builder setSubTotal(double total) {
			this.total = total;
			return Builder.this;
		}

		public SoItem build() {
			return new SoItem(Builder.this);
		}
	}
}
