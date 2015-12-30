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

/**
 * Created by fachru on 30/12/15.
 */
@Table(name = "whstocks")
public class WareHouseStock extends Model {

    @SerializedName("WHID")
    @Column(name = "whid")
    public String whid;

    @SerializedName("PRODID")
    @Column(name = "product_id")
    public String product_id;

    @SerializedName("PRSTATID1")
    @Column(name = "prstatid1")
    public String prstatid1;

    @SerializedName("INN")
    @Column(name = "inn")
    public double inn;

    @SerializedName("OUT")
    @Column(name = "out")
    public double out;

    @SerializedName("BALANCE")
    @Column(name = "balance")
    public double balance;

    @SerializedName("BALANCE2")
    @Column(name = "balance2")
    public double balance2;

    @SerializedName("RESERVED")
    @Column(name = "reserved")
    public double reserved;

    @SerializedName("SOO")
    @Column(name = "sor")
    public double soo;

    @SerializedName("COR")
    @Column(name = "cor")
    public double cor;

    @SerializedName("BOR")
    @Column(name = "bor")
    public double bor;

    @SerializedName("average")
    @Column(name = "average")
    public double averege;

    @SerializedName("TRANSTYPE")
    @Column(name = "transtype")
    public int transtype;

    public WareHouseStock() {
        super();
    }

    public static WareHouseStock findOrCreateFromJson(JSONObject json) throws JSONException {
        String whid = json.getString("WHID");
        WareHouseStock existingWareHouseStock = new Select().from(WareHouseStock.class).where("whid = ?", whid).executeSingle();
        if (existingWareHouseStock != null) {
            return existingWareHouseStock;
        } else {
            WareHouseStock wareHouseStock = WareHouseStock.fromJson(json);
            wareHouseStock.save();
            return wareHouseStock;
        }
    }

    public static WareHouseStock fromJson(JSONObject json) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls();
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json.toString(), WareHouseStock.class);
    }
}
