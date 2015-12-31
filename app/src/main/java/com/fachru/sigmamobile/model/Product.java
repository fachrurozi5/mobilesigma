package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by fachru on 31/12/15.
 */
@Table(name = "Products")
public class Product extends Model{

    @SerializedName("PRODID")
    public String product_id;
    @SerializedName("PRODID")
    @Column(name = "")
    public String name;
    @SerializedName("PRODID")
    @Column(name = "")
    public String articleid;
    @SerializedName("PRODID")
    @Column(name = "")
    public String prstatid1;
    @SerializedName("PRODID")
    @Column(name = "")
    public String prstatid2;
    @SerializedName("PRODID")
    @Column(name = "")
    public int type;
    @SerializedName("PRODID")
    @Column(name = "")
    public String unitid;
    @SerializedName("PRODID")
    @Column(name = "")
    public String unitpo;
    @SerializedName("PRODID")
    @Column(name = "")
    public String unitsell;
    @SerializedName("PRODID")
    @Column(name = "")
    public String unitkecil;
    @SerializedName("PRODID")
    @Column(name = "")
    public double unitconv;
    @SerializedName("PRODID")
    @Column(name = "")
    public String sellcurrid;
    @SerializedName("PRODID")
    @Column(name = "")
    public double sellprice;
    @SerializedName("PRODID")
    @Column(name = "")
    public double discount;
    @SerializedName("PRODID")
    @Column(name = "")
    public String suppid;
    @SerializedName("PRODID")
    @Column(name = "")
    public String location;
    @SerializedName("PRODID")
    @Column(name = "")
    public double weight;
    @SerializedName("PRODID")
    @Column(name = "")
    public double volume;
    @SerializedName("PRODID")
    @Column(name = "")
    public String accidcogs;
    @SerializedName("PRODID")
    @Column(name = "")
    public String accidinv;
    @SerializedName("PRODID")
    @Column(name = "")
    public String accidsls;
    @SerializedName("PRODID")
    @Column(name = "")
    public String accidslsrt;
    @SerializedName("PRODID")
    @Column(name = "")
    public String empid;
    @SerializedName("PRODID")
    @Column(name = "")
    public String abc;
    @SerializedName("PRODID")
    @Column(name = "")
    public String fmr;
    @SerializedName("PRODID")
    @Column(name = "")
    public double baseprice;
    @SerializedName("PRODID")
    @Column(name = "")
    public double oldprice;
    @SerializedName("PRODID")
    @Column(name = "")
    public double testprice;
    @SerializedName("PRODID")
    @Column(name = "")
    public int inactive;
    @SerializedName("PRODID")
    @Column(name = "")
    public String ev_evoucher;
    @SerializedName("PRODID")
    @Column(name = "")
    public String creator;
    @SerializedName("PRODID")
    @Column(name = "")
    public Date created_at;
    @SerializedName("PRODID")
    @Column(name = "")
    public String updator;
    @SerializedName("PRODID")
    @Column(name = "")
    public Date updated_at;

    public Product() {
        super();
    }
}
