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
    @SerializedName("unitid1")
    @Column(name = "unitid")
    public String unitId;

    @Expose
    @SerializedName("unitid2")
    @Column(name = "unitCon")
    public String unitCon;

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
                .where("unitid =?", unitId)
                .and("unitCon =?", unitCon)
                .executeSingle();
    }

    public static List<UnitConverter> getAll() {
        return new Select()
                .from(UnitConverter.class)
                .orderBy("unitid ASC")
                .execute();
    }

    public static List<String> getAllByUnitIdArray(String unitId) {
        List<String> stringList = new ArrayList<>();

        List<UnitConverter> converters = new Select()
                .from(UnitConverter.class)
                .where("unitid =?", unitId)
                .orderBy("unitid ASC")
                .execute();


        for (UnitConverter unitConverter : converters)
            stringList.add(unitConverter.unitCon);

        return stringList;
    }

    public static List<UnitConverter> getAllByUnitId(String unitId) {
        return new Select()
                .from(UnitConverter.class)
                .orderBy("unitid ASC")
                .where("unitid =?", unitId)
                .execute();
    }

    @Override
    public String toString() {
        return "UnitConverter{" +
                "unitId='" + unitId + '\'' +
                ", unitCon='" + unitCon + '\'' +
                ", factor=" + factor +
                '}';
    }
}
