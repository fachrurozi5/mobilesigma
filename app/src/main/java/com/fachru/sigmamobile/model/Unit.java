package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fachru on 16/05/16.
 */
@Table(name = "Units")
public class Unit extends Model {

    @Expose
    @SerializedName("unitid")
    @Column(name = "unit_id", unique = true, onUniqueConflicts = Column.ConflictAction.REPLACE)
    public String unitId;

    @Expose
    @SerializedName("unitname")
    @Column(name = "unit_name")
    public String unitName;

    public Unit() {
        super();
    }

    @Override
    public String toString() {
        return "Unit{" +
                "unitId='" + unitId + '\'' +
                ", unitName='" + unitName + '\'' +
                '}';
    }
}
