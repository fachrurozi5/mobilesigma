package com.fachru.sigmamobile.model;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fachru on 30/12/15.
 */
@Table(name = "WarehouseStocks")
public class WarehouseStock extends Model {

    public static final String UNIQCOLUM = "whid";

    @SerializedName("whid")
    @Column(name = "whid")
    public String whid;

    @SerializedName("prodid")
    @Column(name = "prodid")
    public String product_id;

    @SerializedName("balance")
    @Column(name = "balance")
    public double balance;

    @Column(name = "prodcut")
    public Product product;

    public static List<WarehouseStock> findAllById(String whid) {
        return new Select()
                .from(WarehouseStock.class)
                .where("whid =?", whid)
                .and("balance >?", 0)
                .execute();
    }

    public static List<HashMap<String, String>> toListHashMap(String whid) {
        List<HashMap<String, String>> hashMaps = new ArrayList<>();
        HashMap<String, String> map;
        List<WarehouseStock> warehouseStocks = findAllById(whid);
        Log.d(Constanta.TAG, warehouseStocks.toString());
        for (WarehouseStock stock : warehouseStocks) {
            map = new HashMap<>();
            Product product = stock.product;

            if (product != null) {
                Log.d(Constanta.TAG, product.toString());
                map.put(Constanta.SIMPLE_LIST_ITEM_1, product.prodid);
                map.put(Constanta.SIMPLE_LIST_ITEM_2, product.name);
                map.put(Constanta.SIMPLE_LIST_ITEM_STOCK, new DecimalFormat("#,##0.0000").format(stock.balance));
                map.put(Constanta.SIMPLE_LIST_ITEM_PRICE, CommonUtil.priceFormat2Decimal(product.sellprice));
                hashMaps.add(map);
            }
        }

        return hashMaps;
    }

    public WarehouseStock() {
        super();
    }

    public static List<WarehouseStock> getAll() {
        return new Select().from(WarehouseStock.class).execute();
    }

    public static WarehouseStock findOrCreateFromJson(JSONObject json) throws JSONException {
        String whid = json.getString("WHID");
        WarehouseStock existingWarehouseStock = new Select().from(WarehouseStock.class).where("whid = ?", whid).executeSingle();
        if (existingWarehouseStock != null) {
            return existingWarehouseStock;
        } else {
            WarehouseStock warehouseStock = WarehouseStock.fromJson(json);
            warehouseStock.save();
            return warehouseStock;
        }
    }

    public static WarehouseStock fromJson(JSONObject json) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls();
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json.toString(), WarehouseStock.class);
    }

    @Override
    public String toString() {
        return "WarehouseStock{" +
                "whid='" + whid + '\'' +
                ", product_id='" + product_id + '\'' +
                ", balance=" + balance +
                ", product=" + product +
                '}';
    }
}
