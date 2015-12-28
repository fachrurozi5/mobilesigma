package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by fachru on 20/10/15.
 */
@Table(name = "DoHeads")
public class DoHead extends Model {

    @Column(name = "docno", unique = true)
    public String docno;

    @Column(name = "docdate")
    public Date docdate;

    @Column(name = "rute")
    public String rute;

    @Column(name = "total")
    public long total;

    @Column(name = "bonus")
    public long bonus;

    @Column(name = "grand_total")
    public long grand_total;

    @Column(name = "Customer")
    public Customer customer;

    @Column(name = "Salesman")
    public Salesman salesman;

    @Column(name = "Outlet")
    public Outlet outlet;

    @Column(name = "status")
    public boolean status;

    public DoHead() {
        super();
    }

    public DoHead(String docno, String rute, Customer customer, Salesman salesman, Outlet outlet) {
        super();
        this.docno = docno;
        this.rute = rute;
        this.customer = customer;
        this.salesman = salesman;
        this.outlet = outlet;
    }

    public DoHead(String docno, String rute, Customer customer, Salesman salesman, Outlet outlet, boolean status) {
        super();
        this.docno = docno;
        this.rute = rute;
        this.customer = customer;
        this.salesman = salesman;
        this.outlet = outlet;
        this.status = status;
    }

    public DoHead(String docno, Date docdate, String rute, long total, long bonus, long grand_total, Customer customer, Salesman salesman, Outlet outlet, boolean status) {
        super();
        this.docno = docno;
        this.docdate = docdate;
        this.rute = rute;
        this.total = total;
        this.bonus = bonus;
        this.grand_total = grand_total;
        this.customer = customer;
        this.salesman = salesman;
        this.outlet = outlet;
        this.status = status;
    }

    public void setDateFromString(String date) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        sf.setLenient(true);
        this.docdate = sf.parse(date);
    }

    public static List<DoHead> all() {
        return new Select().from(DoHead.class).execute();
    }

    public static List<DoHead> allDone() {
        return new Select().from(DoHead.class)
                .where("status = ?", 1)
                .execute();
    }

    public static List<DoHead> allPending(Customer customer) {
        return new Select().from(DoHead.class)
                .where("status = ? and Customer = ?", 0, customer.getId())
                .execute();
    }

    public List<DoItem> doItems() {
        return getMany(DoItem.class, "DoHead");
    }

    public static DoHead find(String docno) {
        return new Select().from(DoHead.class)
                .where("docno = ?", docno)
                .executeSingle();
    }

    @Override
    public String toString() {
        return "DoHead{" +
                "docno='" + docno + '\'' +
                ", docdate=" + docdate +
                ", rute='" + rute + '\'' +
                ", total=" + total +
                ", bonus=" + bonus +
                ", grand_total=" + grand_total +
                ", salesman=" + salesman +
                ", outlet=" + outlet +
                ", status=" + status +
                '}';
    }
}
