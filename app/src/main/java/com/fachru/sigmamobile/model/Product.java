package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.fachru.sigmamobile.utils.Constanta;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fachru on 31/12/15.
 */
@Table(name = "Products")
public class Product extends Model {

    @SerializedName("PRODID")
    @Column(name = "product_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
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

    @SerializedName("DATEUPDATE")
    @Column(name = "updated_at")
    public Date updated_at;

    public Product() {
        super();
    }

    public static List<Product> all() {
        return new Select().from(Product.class).execute();
    }

    public static List<HashMap<String, String>> toListHashMap() {
        List<HashMap<String, String>> hashMaps = new ArrayList<>();
        HashMap<String, String> map;
        for (Product product : all()) {
            map = new HashMap<>();
            map.put(Constanta.SIMPLE_LIST_ITEM_1, product.product_id);
            map.put(Constanta.SIMPLE_LIST_ITEM_2, product.name);
            hashMaps.add(map);
        }

        return hashMaps;
    }

    public static Product find(String product_id)  {
        return new Select().from(Product.class)
                .where("product_id = ?", product_id)
                .executeSingle();
    }

    public static Product findOrCreateFromJson(JSONObject json) throws JSONException {
        String product_id = json.getString("PRODID");
        Product existingProduct = find(product_id);
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

    @Override
    public String toString() {
        return "Product{" +
                "product_id='" + product_id + '\'' +
                ", name='" + name + '\'' +
                ", articleid='" + articleid + '\'' +
                ", prstatid1='" + prstatid1 + '\'' +
                ", prstatid2='" + prstatid2 + '\'' +
                ", type=" + type +
                ", unitid='" + unitid + '\'' +
                ", unitpo='" + unitpo + '\'' +
                ", unitsell='" + unitsell + '\'' +
                ", unitkecil='" + unitkecil + '\'' +
                ", unitconv=" + unitconv +
                ", sellcurrid='" + sellcurrid + '\'' +
                ", sellprice=" + sellprice +
                ", discount=" + discount +
                ", supplier_id='" + supplier_id + '\'' +
                ", location='" + location + '\'' +
                ", weight=" + weight +
                ", volume=" + volume +
                ", accidcogs='" + accidcogs + '\'' +
                ", accidinv='" + accidinv + '\'' +
                ", accidsls='" + accidsls + '\'' +
                ", accidslsrt='" + accidslsrt + '\'' +
                ", empid='" + empid + '\'' +
                ", abc='" + abc + '\'' +
                ", fmr='" + fmr + '\'' +
                ", baseprice=" + baseprice +
                ", oldprice=" + oldprice +
                ", testprice=" + testprice +
                ", inactive=" + inactive +
                ", ev_evoucher='" + ev_evoucher + '\'' +
                ", creator='" + creator + '\'' +
                ", created_at=" + created_at +
                ", updator='" + updator + '\'' +
                ", updated_at=" + updated_at +
                '}';
    }
}
