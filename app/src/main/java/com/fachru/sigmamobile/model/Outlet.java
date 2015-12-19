package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.fachru.sigmamobile.utils.Constantas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fachru on 20/10/15.
 */
@Table(name = "Outlets")
public class Outlet extends Model {

    @Column(name = "outlet_id", unique = true)
    public String outlet_id;

    @Column(name = "outlet_name")
    public String outlet_name;

    public Outlet() {
        super();
    }

    public Outlet(String outlet_id, String outlet_name) {
        this.outlet_id = outlet_id;
        this.outlet_name = outlet_name;
    }

    public static List<Outlet> all() {
        return new Select().from(Outlet.class).execute();
    }

    public static List<HashMap<String, String>> toListHashMap() {
        List<HashMap<String, String>> hashMaps = new ArrayList<>();
        HashMap<String, String> map;
        for (Outlet outlet : all()) {
            map = new HashMap<>();
            map.put(Constantas.SIMPLE_LIST_ITEM_1, outlet.outlet_id);
            map.put(Constantas.SIMPLE_LIST_ITEM_2, outlet.outlet_name);
            hashMaps.add(map);
        }

        return hashMaps;
    }

    public static Outlet find(String outlet_id) {
        return new Select().from(Outlet.class)
                .where("outlet_id = ?", outlet_id)
                .executeSingle();
    }

    public List<DoHead> doHeads() {
        return getMany(DoHead.class, "Outlet");
    }

    @Override
    public String toString() {
        return "Outlet{" +
                "outlet_id='" + outlet_id + '\'' +
                ", outlet_name='" + outlet_name + '\'' +
                '}';
    }
}
