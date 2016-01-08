package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fachru on 05/01/16.
 */
@Table(name = "DoItems")
public class DoItem extends Model {

    @SerializedName("DOCNO")
    @Column(name = "docno")
    public String docno;

    @SerializedName("PRODID")
    @Column(name = "product_id")
    public String product_id;

    @SerializedName("QUANTITY")
    @Column(name = "qty")
    public int qty;

    @SerializedName("PRICELIST")
    @Column(name = "pricelist")
    public double pricelist;

    @SerializedName("pricelst2")
    @Column(name = "pricelst2")
    public double pricelist2;

    @SerializedName("UNITPRICE")
    @Column(name = "unitprice")
    public double unitprice;

    @SerializedName("nilai")
    @Column(name = "nilai")
    public double nilai;

    public double sub_total;

    public DoItem() {
        super();
    }

    public DoItem(Builder builder) {
        docno       = builder.docno;
        product_id  = builder.product_id;
        qty         = builder.qty;
        pricelist   = builder.pricelist;
        pricelist2  = builder.pricelist2;
        unitprice   = builder.unitprice;
        nilai       = builder.nilai;
    }

    public List<DoItem> allWhereDoHead(String docno) {
        return new Select()
                .from(DoItem.class)
                .where("docno =? " , docno)
                .execute();
    }

    @Override
    public String toString() {
        return "DoItem{" +
                "docno='" + docno + '\'' +
                ", product_id='" + product_id + '\'' +
                ", qty=" + qty +
                ", pricelist=" + pricelist +
                ", pricelist2=" + pricelist2 +
                ", unitprice=" + unitprice +
                ", nilai=" + nilai +
                '}';
    }

    public static class Builder {
        public String docno;

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
