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
    @Column(name = "noitem")
    public String noitem;

    @Expose
    @SerializedName("PRODID")
    @Column(name = "product_id")
    public String product_id;

    @Expose
    @SerializedName("QUANTITY")
    @Column(name = "qty")
    public int qty;

    @Expose
    @SerializedName("PRICELIST")
    @Column(name = "pricelist")
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

    @Column(name = "sub_total")
    public double sub_total;

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

    public DoItem() {
        super();
    }

    public DoItem(Builder builder) {
        super();
        docno = builder.docno;
        product_id = builder.product_id;
        qty = builder.qty;
        sub_total = builder.sub_total;
        noitem = builder.noitem;
        /*pricelist   = builder.pricelist;
        pricelist2  = builder.pricelist2;
        unitprice   = builder.unitprice;
        nilai       = builder.nilai;*/
    }

    public List<DoItem> allWhereDoHead(String docno) {
        return new Select()
                .from(DoItem.class)
                .where("docno =? ", docno)
                .execute();
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

    @Override
    public String toString() {
        return "DoItem{" +
                "docno='" + docno + '\'' +
                ", noitem='" + noitem + '\'' +
                ", product_id='" + product_id + '\'' +
                ", qty=" + qty +
                ", pricelist=" + pricelist +
                ", pricelist2=" + pricelist2 +
                ", unitprice=" + unitprice +
                ", nilai=" + nilai +
                ", sub_total=" + sub_total +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", uploaded=" + uploaded +
                '}';
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
                .and("noitem = ? ", noitem)
                .executeSingle();
    }

    public static int count(String docno) {
        return new Select()
                .from(DoItem.class)
                .where("docno = ?", docno)
                .count();
    }

    public static class Builder {
        public String docno;

        public String noitem;

        public String product_id;

        public int qty;

        public double pricelist;

        public double pricelist2;

        public double unitprice;

        public double nilai;

        public double sub_total;


        public Builder setDocno(String docno) {
            this.docno = docno;
            return Builder.this;
        }

        public Builder setNoItem(String noitem) {
            this.noitem = noitem;
            return Builder.this;
        }

        public Builder setProduct_id(String product_id) {
            this.product_id = product_id;
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

        /*public Builder setPricelist(double pricelist) {
            this.pricelist = pricelist;
            return Builder.this;
        }

        public Builder setPricelist2(double pricelist2) {
            this.pricelist2 = pricelist2;
            return Builder.this;
        }

        public Builder setUnitprice(double unitprice) {
            this.unitprice = unitprice;
            return Builder.this;
        }

        public Builder setNilai(double nilai) {
            this.nilai = nilai;
            return Builder.this;
        }*/

        public DoItem build() {
            return new DoItem(Builder.this);
        }
    }
}
