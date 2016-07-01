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
@Table(name = "OutletTypes")
public class OutletType extends Model {

	@Expose
	@SerializedName("id")
	@Column(name = "outlet_type_id")
	private int outletTypeId;

	@Expose
	@Column(name = "name", unique = true, onNullConflict = Column.ConflictAction.REPLACE)
	private String name;

	public OutletType() {
		super();
	}

	public int getOutletTypeId() {
		return outletTypeId;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "OutletType{" +
				"outletTypeId=" + outletTypeId +
				", name='" + name + '\'' +
				'}';
	}


	public static List<OutletType> all() {
		return new Select()
				.from(OutletType.class)
				.execute();
	}

	public static OutletType find(int id) {
		return new Select()
				.from(OutletType.class)
				.where("outlet_type_id = ?", id)
				.executeSingle();
	}
}
