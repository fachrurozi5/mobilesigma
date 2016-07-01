package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fachru on 09/05/16.
 */
@Table(name = "DiscountStructure")
public class DiscountStructure extends Model {

	@Expose
	@SerializedName("id_disc")
	@Column(name = "id_disc")
	public String _id_disc;

	@Expose
	@SerializedName("no_item")
	@Column(name = "noItem")
	public String no_item;

	@Expose
	@Column(name = "min")
	public long min;

	@Expose
	@Column(name = "max")
	public long max;

	@Expose
	@SerializedName("p_value")
	@Column(name = "principal_value")
	public int principal_value;

	@Expose
	@SerializedName("p_percent")
	@Column(name = "pricipal_percent")
	public int principal_percent;

	@Expose
	@SerializedName("n_value")
	@Column(name = "nusantara_value")
	public int nusantara_value;

	@Expose
	@SerializedName("n_percent")
	@Column(name = "nusantara_percent")
	public int nusantara_percent;

	@Column(name = "discount", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
	public Discount discount;

	public DiscountStructure() {
		super();
	}

	public static DiscountStructure find(String id_disc, String noitem) {
		return new Select()
				.from(DiscountStructure.class)
				.where("id_disc = ? ", id_disc)
				.and("noItem = ? ", noitem)
				.executeSingle();
	}

	@Override
	public String toString() {
		return "DiscountStructure{" +
				"_id_disc='" + _id_disc + '\'' +
				", no_item='" + no_item + '\'' +
				", min=" + min +
				", max=" + max +
				", principal_value=" + principal_value +
				", principal_percent=" + principal_percent +
				", nusantara_value=" + nusantara_value +
				", nusantara_percent=" + nusantara_percent +
				'}';
	}
}
