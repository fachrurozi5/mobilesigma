package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by fachru on 05/01/16.
 */
@Table(name = "DoItems")
public class DoItem extends Model {

    @Expose
    @SerializedName("DOCNO")
    @Column(name = "docno")
    public String docno;

    @Expose
    @SerializedName("NOITEM")
    @Column(name = "noItem")
    public String noitem;

    @Expose
    @SerializedName("PRODID")
    @Column(name = "productId")
    public String product_id;

    @Expose
    @SerializedName("UNITID")
    @Column(name = "unitid")
    public String unit_id;

    @Expose
    @SerializedName("QUANTITY")
    @Column(name = "qty")
    public int qty;

    @Expose
    @SerializedName("PRICELIST")
    @Column(name = "priceList")
    public double pricelist;

    @Expose
    @SerializedName("pricelst2")
    @Column(name = "pricelst2")
    public double pricelist2;

    @Expose
    @SerializedName("UNITPRICE")
    @Column(name = "unitprice")
    public double unitprice;

    @Expose
    @SerializedName("nilai")
    @Column(name = "nilai")
    public double nilai;

    @Column(name = "subTotal")
    public double sub_total;

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

    public DoItem() {
        super();
    }

    public DoItem(Builder builder) {
        super();
        docno = builder.docno;
        product_id = builder.product_id;
        pricelist = builder.price_list;
        qty = builder.qty;
        sub_total = builder.sub_total;
        unit_id = builder.unitid;
    }

    public static List<DoItem> getAllNotUpload() {
        return new Select()
                .from(DoItem.class)
                .where("uploaded =?", false)
                .execute();
    }

    public static boolean hasDataToUpload() {
        return DoItem.getAllNotUpload().size() > 0;
    }

    public static DoItem find(String docno) {
        return new Select()
                .from(DoItem.class)
                .where("docno =? ", docno)
                .executeSingle();
    }

    public static DoItem find(String docno, String noitem) {
        return new Select()
                .from(DoItem.class)
                .where("docno = ? ", docno)
                .and("noItem = ? ", noitem)
                .executeSingle();
    }

    public static int count(String docno) {
        return new Select()
                .from(DoItem.class)
                .where("docno = ?", docno)
                .count();
    }

    public List<DoItem> allWhereDoHead(String docno) {
        return new Select()
                .from(DoItem.class)
                .where("docno =? ", docno)
                .execute();
    }

    @Override
    public String toString() {
        return "DoItem{" +
                "docno='" + docno + '\'' +
                ", noItem='" + noitem + '\'' +
                ", productId='" + product_id + '\'' +
                ", qty=" + qty +
                ", priceList=" + pricelist +
                ", pricelist2=" + pricelist2 +
                ", unitprice=" + unitprice +
                ", nilai=" + nilai +
                ", subTotal=" + sub_total +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", uploaded=" + uploaded +
                '}';
    }

    public static class Builder {
        public String docno;

        public String product_id;

        public String unitid;

        public double price_list;

        public int qty;

        public double sub_total;


        public Builder setDocno(String docno) {
            this.docno = docno;
            return Builder.this;
        }

        public Builder setUnitId(String unitId) {
            this.unitid = unitId;
            return Builder.this;
        }

        public Builder setProductId(String product_id) {
            this.product_id = product_id;
            return Builder.this;
        }

        public Builder setPriceList(double price_list) {
            this.price_list = price_list;
            return Builder.this;
        }

        public Builder setQty(int qty) {
            this.qty = qty;
            return Builder.this;
        }

        public Builder setSubTotal(double sub_total) {
            this.sub_total = sub_total;
            return Builder.this;
        }

        public DoItem build() {
            return new DoItem(Builder.this);
        }
    }
}
