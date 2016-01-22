package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.fachru.sigmamobile.utils.CommonUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by fachru on 22/01/16.
 */
@Table(name = "SoHeads")
public class SoHead extends Model{

    public String so;

    public Date so_date;

    public String custid;

    public String empid;

    public String topid;

    public String vatid;

    public String purchase_order;

    public Date date_order;

    public Date delivery_date;

    public int priceType;

    public String whid;

    public Date updated_at;

    public SoHead() {
        super();
    }

    public SoHead(Builder builder) {
        super();
        so = builder.so;
        so_date = builder.so_date;
        custid = builder.custid;
        empid = builder.empid;
        topid = builder.topid;
        purchase_order = builder.purchaseOrder;
        date_order = builder.date_order;
        delivery_date = builder.deldate;
        priceType = builder.priceType;
        whid = builder.whid;
    }

    public static SoHead find(String so) {
        return new Select()
                .from(SoHead.class)
                .where("so =?", so)
                .executeSingle();
    }

    public static List<SoHead> getAll() {
        return new Select()
                .from(SoHead.class)
                .execute();
    }

    public String getDocDate() {
        return CommonUtil.dateToStringMedium(this.so_date);
    }

    @Override
    public String toString() {
        return "SoHead{" +
                "so='" + so + '\'' +
                ", so_date=" + so_date +
                ", custid='" + custid + '\'' +
                ", empid='" + empid + '\'' +
                ", topid='" + topid + '\'' +
                ", vatid='" + vatid + '\'' +
                ", purchase_order='" + purchase_order + '\'' +
                ", date_order=" + date_order +
                ", delivery_date=" + delivery_date +
                ", priceType=" + priceType +
                ", whid='" + whid + '\'' +
                ", updated_at=" + updated_at +
                '}';
    }

    public static class Builder {
        public String so;

        public Date so_date;

        public String custid;

        public String empid;

        public String topid;

        public String vatid;

        public String purchaseOrder;

        public Date date_order;

        public Date deldate;

        public int priceType;

        public String whid;

        public Date updated_at;

        public Builder setSo(String so) {
            this.so = so;
            return Builder.this;
        }

        public Builder setSo_date(Date so_date) {
            this.so_date = so_date;
            return Builder.this;
        }

        public Builder setCustid(String custid) {
            this.custid = custid;
            return Builder.this;
        }

        public Builder setEmpid(String empid) {
            this.empid = empid;
            return Builder.this;
        }

        public Builder setTopid(String topid) {
            this.topid = topid;
            return Builder.this;
        }

        public Builder setVatid(String vatid) {
            this.vatid = vatid;
            return Builder.this;
        }

        public Builder setPurchaseOrder(String purchaseOrder) {
            this.purchaseOrder = purchaseOrder;
            return Builder.this;
        }

        public Builder setDate_order(Date date_order) {
            this.date_order = date_order;
            return Builder.this;
        }

        public Builder setDeldate(Date deldate) {
            this.deldate = deldate;
            return Builder.this;
        }

        public Builder setPriceType(int priceType) {
            this.priceType = priceType;
            return Builder.this;
        }

        public Builder setWhid(String whid) {
            this.whid = whid;
            return Builder.this;
        }

        public Builder setUpdated_at(Date updated_at) {
            this.updated_at = updated_at;
            return Builder.this;
        }

        public SoHead Build() {
            return new SoHead(Builder.this);
        }
    }
}
