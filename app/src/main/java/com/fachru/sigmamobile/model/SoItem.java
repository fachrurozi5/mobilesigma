package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fachru on 28/01/16.
 */
@Table(name = "SoItems")
public class SoItem extends Model {

    @SerializedName("SO")
    @Column(name = "so")
    public String so;

    @SerializedName("PRODID")
    @Column(name = "product_id")
    public String product_id;

    @SerializedName("QUANTITY")
    @Column(name = "qty")
    public int qty;

    @Column(name = "sub_total")
    public double sub_total;

    public SoItem() {
        super();
    }

    public SoItem(Builder builder) {
        super();
        so = builder.so;
        product_id = builder.prodid;
        qty = builder.qty;
        sub_total = builder.total;
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
                ", product_id='" + product_id + '\'' +
                ", qty=" + qty +
                ", sub_total=" + sub_total +
                '}';
    }

    public static class Builder {
        public String so;

        public String prodid;

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
