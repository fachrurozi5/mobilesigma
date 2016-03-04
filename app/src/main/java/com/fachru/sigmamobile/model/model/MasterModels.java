package com.fachru.sigmamobile.model.model;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by fachru on 27/02/16.
 */
public abstract class MasterModels extends Model {

    protected static Class aClass;

    public static List<? extends Model> getAll() {
        return new Select()
                .from(aClass)
                .orderBy("name ASC")
                .execute();
    }

}
