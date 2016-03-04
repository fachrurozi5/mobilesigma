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
 * Created by fachru on 06/01/16.
 */
@Table(name = "ProductStatus2")
public class ProductStatus2 extends Model {

    @SerializedName("prstatid")
    @Column(name = "prstat_id")
    public String prstat_id;

    @SerializedName("prstatname")
    @Column(name = "name")
    public String name;

    public ProductStatus2() {
        super();
    }

    public ProductStatus2(Builder builder) {
        super();
        prstat_id = builder.prstat_id;
        name = builder.name;
    }

    /*public static ProductStatus2 find(String prstat_id) {
        return new Select()
                .from(ProductStatus2.class)
                .where("prstat_id =?", prstat_id)
                .executeSingle();
    }

    public static ProductStatus2 findOrCreateFromJson(JSONObject json) throws JSONException {
        String prstat_id = json.getString("prstatid");
        ProductStatus2 existingProductStatus = find(prstat_id);
        if (existingProductStatus != null) {
            return existingProductStatus;
        } else {
            ProductStatus2 productStatus = ProductStatus2.fromJson(json);
            productStatus.save();
            return productStatus;
        }
    }

    public static ProductStatus2 fromJson(JSONObject json) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls();
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json.toString(), ProductStatus2.class);
    }*/

    @Override
    public String toString() {
        return "ProductStatus2{" +
                "prstat_id='" + prstat_id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public static class Builder {

        String prstat_id;
        String name;

        public Builder setPrstat_id(String prstat_id) {
            this.prstat_id = prstat_id;
            return Builder.this;
        }

        public Builder setName(String name) {
            this.name = name;
            return Builder.this;
        }

        public ProductStatus2 Build() {
            return new ProductStatus2(Builder.this);
        }

        ;
    }

}
