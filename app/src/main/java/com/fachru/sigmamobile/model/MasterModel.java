package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by fachru on 07/01/16.
 */
public class MasterModel<T extends Model> extends Model {


    protected Class aClass;
    protected String id;

    public MasterModel(Class aClass) {
        super();
        this.aClass = aClass;
    }

    public T find(String value) {
        return new Select()
                .from(aClass)
                .where(id + " =?", value)
                .executeSingle();
    }

    public static <T extends Model> T find(String column, String value, Class<? extends Model> aClass) {
        return new Select()
                .from(aClass)
                .where(column + "=?", value)
                .executeSingle();
    }


    public static <T extends Model> T findOrCreateFromJson(Class<? extends Model> aClass, String whereclause, String value, JSONObject json) throws JSONException {
        Object existingObject = new Select().from(aClass).where(whereclause + " =?", value).executeSingle();
        if (existingObject != null) {
            return (T) existingObject;
        } else {
            return fromJson(json, aClass);
        }
    }

    public static <T extends Model> T fromJson(JSONObject json, Class<? extends Model> aClass) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls();
        Gson gson = gsonBuilder.create();

        return (T) gson.fromJson(json.toString(), aClass);
    }

    public static List<? extends Model> all(Class<? extends Model> aClass) {
        return new Select()
                .from(aClass)
                .execute();
    }

    /*private static <T extends Model> T getInstance(Object object) {
        if (object instanceof Customer) {
            Customer customer = (Customer) object;
            customer.save();
            return (T) customer;
        } else if (object instanceof Warehouse) {
            Warehouse warehouse = (Warehouse) object;
            warehouse.save();
            return (T) warehouse;
        } else {
            return null;
        }

    }*/

}
