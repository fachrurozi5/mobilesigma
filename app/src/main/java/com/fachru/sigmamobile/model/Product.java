package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by fachru on 31/12/15.
 */
@Table(name = "Products")
public class Product extends Model {

    @SerializedName("PRODID")
    @Column(name = "product_id")
    public String product_id;

    @SerializedName("PRODNAME1")
    @Column(name = "name")
    public String name;

    @SerializedName("ARTICLEID")
    @Column(name = "articleid")
    public String articleid;

    @SerializedName("PRSTATID1")
    @Column(name = "prstatid1")
    public String prstatid1;

    @SerializedName("PRSTATID2")
    @Column(name = "prstatid2")
    public String prstatid2;

    @SerializedName("PRODTYPE")
    @Column(name = "type")
    public int type;

    @SerializedName("UNITID")
    @Column(name = "unitid")
    public String unitid;

    @SerializedName("UNITPO")
    @Column(name = "unitpo")
    public String unitpo;

    @SerializedName("UNITSELL")
    @Column(name = "unitsell")
    public String unitsell;

    @SerializedName("unitkecil")
    @Column(name = "unitkecil")
    public String unitkecil;

    @SerializedName("UNITCONV")
    @Column(name = "unitconv")
    public double unitconv;

    @SerializedName("SELLCURRID")
    @Column(name = "sellcurrid")
    public String sellcurrid;

    @SerializedName("SELLPRC")
    @Column(name = "sellprice")
    public double sellprice;

    @SerializedName("DISCOUNT")
    @Column(name = "discount")
    public double discount;

    @SerializedName("SUPPID")
    @Column(name = "supplier_id")
    public String supplier_id;

    @SerializedName("LOCATION")
    @Column(name = "location")
    public String location;

    @SerializedName("WEIGHT")
    @Column(name = "wight")
    public double weight;

    @SerializedName("VOLUME")
    @Column(name = "volume")
    public double volume;

    @SerializedName("ACCIDCOGS")
    @Column(name = "accidcogs")
    public String accidcogs;

    @SerializedName("ACCIDINV")
    @Column(name = "accidinv")
    public String accidinv;

    @SerializedName("ACCIDSLS")
    @Column(name = "accidsls")
    public String accidsls;

    @SerializedName("accidslsrt")
    @Column(name = "accidslsrt")
    public String accidslsrt;

    @SerializedName("EMPID")
    @Column(name = "empid")
    public String empid;

    @SerializedName("ABC")
    @Column(name = "abc")
    public String abc;

    @SerializedName("FMR")
    @Column(name = "")
    public String fmr;

    @SerializedName("BASEPRICE")
    @Column(name = "baseprice")
    public double baseprice;

    @SerializedName("OLDPRICE")
    @Column(name = "oldprice")
    public double oldprice;

    @SerializedName("TESTPRICE")
    @Column(name = "testprice")
    public double testprice;

    @SerializedName("INACTIVE")
    @Column(name = "inactive")
    public int inactive;

    @SerializedName("EV_EVOUCHER")
    @Column(name = "ev_evoucher")
    public String ev_evoucher;

    @SerializedName("USRCREATE")
    @Column(name = "creator")
    public String creator;

    @SerializedName("DATECREATE")
    @Column(name = "created_at")
    public Date created_at;

    @SerializedName("USRUPDATE")
    @Column(name = "updator")
    public String updator;

    @SerializedName("USRUPDATE")
    @Column(name = "updated_at")
    public Date updated_at;

    public Product() {
        super();
    }

    public static Product findOrCreateFromJson(JSONObject json) throws JSONException {
        String product_id = json.getString("PRODID");
        Product existingProduct = new Select().from(Product.class).where("product_id = ?", product_id).executeSingle();
        if (existingProduct != null) {
            return existingProduct;
        } else {
            Product product = Product.fromJson(json);
            product.save();
            return product;
        }
    }

    public static Product fromJson(JSONObject json) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls();
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json.toString(), Product.class);
    }
}
