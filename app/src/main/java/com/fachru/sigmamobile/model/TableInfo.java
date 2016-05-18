package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.google.gson.annotations.Expose;

/**
 * Created by fachru on 18/05/16.
 */
@Table(name = "TableInfos")
public class TableInfo extends Model {

    private static final String DISCOUNT = "header_discount";

    @Expose
    @Column(name = "target_name")
    public String target_name;

    @Expose
    @Column(name = "target_id")
    public String target_id;

    public TableInfo() {
        super();
    }

    public static void save(TableInfo info) {
        if (info.target_name.equals(DISCOUNT)) {
            new Delete()
                    .from(Discount.class)
                    .where("_id = ?", info.target_id)
                    .execute();
        }
    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "target_name='" + target_name + '\'' +
                ", target_id='" + target_id + '\'' +
                '}';
    }
}
