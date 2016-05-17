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
    @Column(name = "noitem")
    public String noitem;

    @Expose
    @SerializedName("PRODID")
    @Column(name = "product_id")
    public String product_id;

    @Expose
    @SerializedName("PRICELIST")
    @Column(name = "pricelist")
    public double pricelist;

    @Expose
    @SerializedName("UNITID")
    @Column(name = "unitid")
    public String unit_id;

    @Expose
    @SerializedName("QUANTITY")
    @Column(name = "qty")
    public int qty;

    @Expose
    @SerializedName("DATECREATE")
    @Column(name = "created_at")
    public Date created_at = new Date();

    @Expose
    @SerializedName("DATEUPDATE")
    @Column(name = "updated_at")
    public Date updated_at = new Date();

    @Expose
    @SerializedName("uploaded")
    @Column(name = "uploaded")
    public boolean uploaded = false;

    @Column(name = "sub_total")
    public double sub_total;

    @Column(name = "p_disc")
    public double discount_principal;
    @Column(name = "n_disc")
    public double discount_nusantara;

    @Column(name = "bonus")
    public int mul_bonus = 0;

    public SoItem() {
        super();
    }

    public SoItem(Builder builder) {
        super();
        so = builder.so;
        product_id = builder.prodid;
        pricelist = builder.price_list;
        qty = builder.qty;
        sub_total = builder.total;
        unit_id = builder.unitid;
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
                    .and("noitem = ? ", noitem)
                    .executeSingle();
        } catch (Exception e) {
            Log.e(TAG, "So FIND", e);
            return null;
        }

    }

    public static boolean hasDataToUpload() {
        return SoItem.getAllNotUpload().size() > 0;
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
                ", noitem='" + noitem + '\'' +
                ", product_id='" + product_id + '\'' +
                ", pricelist=" + pricelist +
                ", unit_id='" + unit_id + '\'' +
                ", qty=" + qty +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", uploaded=" + uploaded +
                ", sub_total=" + sub_total +
                ", discount_principal=" + discount_principal +
                ", discount_nusantara=" + discount_nusantara +
                ", mul_bonus=" + mul_bonus +
                '}';
    }

    public static class Builder {
        public String so;

        public String prodid;

        public String unitid;

        public double price_list;

        public int qty;

        public double total;

        public Builder setSo(String so) {
            this.so = so;
            return Builder.this;
        }

        public Builder setProductId(String prodid) {
            this.prodid = prodid;
            return Builder.this;
        }

        public Builder setPriceList(double price_list) {
            this.price_list = price_list;
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

        public Builder setSubTotal(double total) {
            this.total = total;
            return Builder.this;
        }

        public SoItem build() {
            return new SoItem(Builder.this);
        }
    }
}
