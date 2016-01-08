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

import java.util.List;

/**
 * Created by fachru on 06/01/16.
 */
@Table(name = "ProductStatus")
public class ProductStatus extends Model {

    @SerializedName("PRSTATID")
    @Column(name = "prstat_id", unique = true)
    public String prstat_id;

    @SerializedName("PRSTATNAME")
    @Column(name = "name")
    public String name;

    @SerializedName("PRSTATTYPE")
    @Column(name = "type")
    public String type;

    public ProductStatus() {
        super();
    }

    public ProductStatus(Builder builder) {
        super();
        prstat_id = builder.prstat_id;
        name = builder.name;
        type = builder.type;
    }

    public static ProductStatus find(String prstat_id) {
        return new Select()
                .from(ProductStatus.class)
                .where("prstat_id =?", prstat_id)
                .executeSingle();
    }

    public static ProductStatus findOrCreateFromJson(JSONObject json) throws JSONException {
        String prstat_id = json.getString("PRSTATID");
        ProductStatus existingProductStatus = find(prstat_id);
        if (existingProductStatus != null) {
            return existingProductStatus;
        } else {
            ProductStatus productStatus = ProductStatus.fromJson(json);
            productStatus.save();
            return productStatus;
        }
    }

    public static ProductStatus fromJson(JSONObject json) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls();
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json.toString(), ProductStatus.class);
    }

    @Override
    public String toString() {
        return "ProductStatus{" +
                "prstat_id='" + prstat_id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public static class Builder {

        String prstat_id;
        String name;
        String type;

        public Builder setPrstat_id(String prstat_id) {
            this.prstat_id = prstat_id;
            return Builder.this;
        }

        public Builder setName(String name) {
            this.name = name;
            return Builder.this;
        }

        public Builder setType(String type) {
            this.type = type;
            return Builder.this;
        }

        public ProductStatus Build(){
            return new ProductStatus(Builder.this);
        };
    }
}