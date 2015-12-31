package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;

/**
 * Created by fachru on 31/12/15.
 */
@Table(name = "Products")
public class Product extends Model{

    public String product_id;

    public String name;

    public String articleid;

    public String prstatid1;

    public String prstatid2;

    public int type;

    public String unitid;

    public String unitpo;

    public String unitsell;

    public String unitkecil;

    public double unitconv;

    public String sellcurrid;

    public double sellprice;



}
