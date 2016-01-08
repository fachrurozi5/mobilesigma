package com.fachru.sigmamobile.model.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.fachru.sigmamobile.utils.Constanta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fachru on 20/10/15.
 */
@Table(name = "Salesman")
public class Salesman extends Model {

    @Column(name = "salesman_id", unique = true)
    public String salesman_id;

    @Column(name = "salesman_name")
    public String salesman_name;

    public Salesman() {
        super();
    }

    public Salesman(String salesman_id, String salesman_name) {
        super();
        this.salesman_id = salesman_id;
        this.salesman_name = salesman_name;
    }

    public static List<Salesman> all() {
        return new Select().from(Salesman.class).execute();
    }

    public static List<HashMap<String, String>> toListHashMap() {
        List<HashMap<String, String>> hashMaps = new ArrayList<>();
        HashMap<String, String> map;
        for (Salesman salesman : all()) {
            map = new HashMap<>();
            map.put(Constanta.SIMPLE_LIST_ITEM_1, salesman.salesman_id);
            map.put(Constanta.SIMPLE_LIST_ITEM_2, salesman.salesman_name);
            hashMaps.add(map);
        }

        return hashMaps;
    }

    public static String[] toStringArray() {
        String[] strings = new String[all().size()];
        int i = 0;
        for (Salesman salesman : all()) {
            strings[i++] = salesman.salesman_name;
        }

        return strings;
    }

    public static Salesman find(String salesman_id) {
        return new Select().from(Salesman.class)
                .where("salesman_id = ?", salesman_id)
                .executeSingle();
    }


    @Override
    public String toString() {
        return "Salesman{" +
                "salesman_id='" + salesman_id + '\'' +
                ", salesman_name='" + salesman_name + '\'' +
                '}';
    }
}
