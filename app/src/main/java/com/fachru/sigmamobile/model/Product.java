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
 * Created by fachru on 15/10/15.
 */
@Table(name = "Products")
public class Product extends Model {

    @Column(name = "product_id", unique = true)
    public String product_id;

    @Column(name = "product_name")
    public String product_name;

    @Column(name = "price")
    public long price;

    @Column(name = "stock")
    public long stock;

    public Product() {
        super();
    }

    public Product(String product_id, String product_name, long price, long stock) {
        super();
        this.product_id = product_id;
        this.product_name = product_name;
        this.price = price;
        this.stock = stock;
    }

    public static List<Product> all() {
        return new Select().from(Product.class).execute();
    }

    public static List<HashMap<String, String>> toListHashMap() {
        List<HashMap<String, String>> hashMaps = new ArrayList<>();
        HashMap<String, String> map;
        for (Product product : all()) {
            map = new HashMap<>();
            map.put(Constantas.SIMPLE_LIST_ITEM_1, product.product_id);
            map.put(Constantas.SIMPLE_LIST_ITEM_2, product.product_name);
            hashMaps.add(map);
        }

        return hashMaps;
    }

    public static Product find(String product_id) {
        return new Select().from(Product.class)
                .where("product_id = ?", product_id)
                .executeSingle();
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_id='" + product_id + '\'' +
                ", product_name='" + product_name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
