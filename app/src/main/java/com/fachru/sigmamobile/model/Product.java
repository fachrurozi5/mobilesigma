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
import java.util.HashMap;
import java.util.List;

/**
 * Created by fachru on 31/12/15.
 */
@Table(name = "Products")
public class Product extends Model {

    public static final String UNIQCOLUMN = "prodid";

    @SerializedName("prodid")
    @Column(name = "prodid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String prodid;

    @SerializedName("prodname1")
    @Column(name = "name")
    public String name;

    @SerializedName("prstatid1")
    @Column(name = "prstatid1")
    public String prstatid1;

    @SerializedName("prstatid2")
    @Column(name = "prstatid2")
    public String prstatid2;

    @SerializedName("unitid")
    @Column(name = "unitid")
    public String unitid;

    @SerializedName("sellprc")
    @Column(name = "sellprice")
    public double sellprice;

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
            map.put(Constanta.SIMPLE_LIST_ITEM_1, product.prodid);
            map.put(Constanta.SIMPLE_LIST_ITEM_2, product.name);
            hashMaps.add(map);
        }

        return hashMaps;
    }

    public static Product find(String product_id)  {
        return new Select().from(Product.class)
                .where("prodid = ?", product_id)
                .executeSingle();
    }

    public static Product findOrCreateFromJson(JSONObject json) throws JSONException {
        String product_id = json.getString("prodid");
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
                "prodid='" + prodid + '\'' +
                ", name='" + name + '\'' +
                ", prstatid1='" + prstatid1 + '\'' +
                ", prstatid2='" + prstatid2 + '\'' +
                ", unitid='" + unitid + '\'' +
                ", sellprice=" + sellprice +
                '}';
    }
}
