package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fachru on 28/03/16.
 */
@Table(name = "UnitConverters")
public class UnitConverter extends Model {

	@Expose
	@SerializedName("from_unit")
	@Column(name = "from_unit")
	public String unitFrom;

	@Expose
	@SerializedName("to_Unit")
	@Column(name = "to_Unit")
	public String toUnit;

	@Expose
	@SerializedName("factor")
	@Column(name = "factor")
	public double factor;

	public UnitConverter() {
		super();
	}

	public static UnitConverter find(String unitId, String unitCon) {
		return new Select()
				.from(UnitConverter.class)
				.where("from_unit =?", unitId)
				.and("to_Unit =?", unitCon)
				.executeSingle();
	}

	public static List<UnitConverter> getAll() {
		return new Select()
				.from(UnitConverter.class)
				.orderBy("from_unit ASC")
				.execute();
	}

	public static List<String> getAllByUnitIdArray(String unitId) {
		List<String> stringList = new ArrayList<>();

		List<UnitConverter> converters = new Select()
				.from(UnitConverter.class)
				.where("from_unit =?", unitId)
				.orderBy("from_unit ASC")
				.execute();


		for (UnitConverter unitConverter : converters)
			stringList.add(unitConverter.toUnit);

		return stringList;
	}

	public static List<UnitConverter> getAllByUnitId(String unitId) {
		return new Select()
				.from(UnitConverter.class)
				.orderBy("from_unit ASC")
				.where("from_unit =?", unitId)
				.execute();
	}

	@Override
	public String toString() {
		return "UnitConverter{" +
				"unitFrom='" + unitFrom + '\'' +
				", toUnit='" + toUnit + '\'' +
				", factor=" + factor +
				'}';
	}
}
