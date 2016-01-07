package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fachru on 07/01/16.
 */
@Table(name = "Warehouse")
public class Warehouse extends MasterModel {

    public static final String PRIMARYKEY = "whid";

    @SerializedName("WHID")
    @Column(name = "whid")
    public String whid;

    @SerializedName("WHNAME")
    @Column(name = "name")
    public String name;

    @SerializedName("whgroupid")
    @Column(name = "whgroup")
    public String whgroup;

    @SerializedName("REMARKS")
    @Column(name = "remarks")
    public String remarks;

    public Warehouse() {
        super();
    }

    public Warehouse(Builder builder) {
        super();
        whid = builder.whid;
        name = builder.name;
        whgroup = builder.whgroup;
        remarks = builder.remarks;
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
