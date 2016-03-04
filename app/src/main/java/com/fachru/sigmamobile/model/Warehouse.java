package com.fachru.sigmamobile.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fachru on 07/01/16.
 */
@Table(name = "Warehouse")
public class Warehouse extends MasterModel<Warehouse> {

    public static final String PRIMARYKEY = "whid";

    @Expose
    @SerializedName("WHID")
    @Column(name = "whid")
    public String whid;

    @Expose
    @SerializedName("WHNAME")
    @Column(name = "name")
    public String name;

    @Expose
    @SerializedName("whgroupid")
    @Column(name = "whgroup")
    public String whgroup;

    @Expose
    @SerializedName("REMARKS")
    @Column(name = "remarks")
    public String remarks;

    public Warehouse() {
        super(Warehouse.class);
    }

    public Warehouse(Builder builder) {
        super(Warehouse.class);
        whid = builder.whid;
        name = builder.name;
        whgroup = builder.whgroup;
        remarks = builder.remarks;
    }

    public static Warehouse getWarehouse(String whid) {
        return new Select()
                .from(Warehouse.class)
                .where("whid =?", whid)
                .executeSingle();
    }

    public static String getWarehouseName(String whid) {
        Warehouse warehouse = new Select()
                .from(Warehouse.class)
                .where("whid =?", whid)
                .executeSingle();
        return warehouse.name;
    }

    public static List<Warehouse> all() {
        return new Select()
                .from(Warehouse.class)
                .execute();
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "whid='" + whid + '\'' +
                ", name='" + name + '\'' +
                ", whgroup='" + whgroup + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }

    public static class Builder {

        public String whid;

        public String name;

        public String whgroup;

        public String remarks;

        public Builder setWhid(String whid) {
            this.whid = whid;
            return Builder.this;
        }

        public Builder setName(String name) {
            this.name = name;
            return Builder.this;
        }

        public Builder setWhgroup(String whgroup) {
            this.whgroup = whgroup;
            return Builder.this;
        }

        public Builder setRemarks(String remarks) {
            this.remarks = remarks;
            return Builder.this;
        }

        public Warehouse Build() {
            return new Warehouse(Builder.this);
        }
    }

}
