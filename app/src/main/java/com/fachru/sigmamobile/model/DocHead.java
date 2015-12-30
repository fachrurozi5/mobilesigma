package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by fachru on 30/12/15.
 */
@Table(name = "DocHeads")
public class DocHead extends Model {

    @SerializedName("DOCNO")
    @Column(name = "docno", unique = true)
    public String doc_no;

    @SerializedName("DATEORDER")
    @Column(name = "date_order")
    public Date date_order;

    @SerializedName("Customer")
    @Column(name = "customer")
    public Customer customer;

    @SerializedName("Employee")
    @Column(name = "employee")
    public Employee employee;


    @SerializedName("Total")
    @Column(name = "total")
    public double Total;

    @SerializedName("Bonus")
    @Column(name = "bonus")
    public double bonus;

    @SerializedName("GrandTotal")
    @Column(name = "grand_total")
    public double grand_total;





}
