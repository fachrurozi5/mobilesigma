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

import java.util.Date;
import java.util.List;

/**
 * Created by fachru on 17/12/15.
 */
@Table(name = "Customers")
public class Customer extends Model {

    @SerializedName("CUSTID")
    @Column(name = "custid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String custid;

    @SerializedName("CUSTNAME")
    @Column(name = "name")
    public String name;

    @SerializedName("csstatid1")    // as group
    @Column(name = "csstadid")
    public String csstatid1;

    @SerializedName("CSSTATID2")
    @Column(name = "csstatid2")
    public String csstatid2;

    @SerializedName("CSSTATID3")
    @Column(name = "csstatid3")
    public String csstatid3;

    @SerializedName("CSSTATID4")
    @Column(name = "csstatid4")
    public String csstatid4;

    @SerializedName("type_group")
    @Column(name = "group_type")
    public String group_type;

    @SerializedName("cs_group")
    @Column(name = "cs_group")
    public String cs_group;

    @SerializedName("channel")
    @Column(name = "channel")
    public String channel;

    @SerializedName("CSSTATID5")
    @Column(name = "csstatid5")
    public String csstatid5;

    @SerializedName("INVADD1")      //
    @Column(name = "invadd1")
    public String invadd1;

    @SerializedName("INVADD2")
    @Column(name = "invadd2")
    public String invadd2;

    @SerializedName("INVADD3")
    @Column(name = "invadd3")
    public String invadd3;

    @SerializedName("INVADD4")
    @Column(name = "invadd4")
    public String invadd4;

    @SerializedName("INVZIP")
    @Column(name = "invzip")
    public String invzip;

    @SerializedName("DELADDID")
    @Column(name = "deladdid")
    public String deladdid;

    @SerializedName("DELADD1")
    @Column(name = "deladd1")
    public String deladd1;

    @SerializedName("DELADD2")
    @Column(name = "deladd2")
    public String deladd2;

    @SerializedName("DELADD3")
    @Column(name = "deladd3")
    public String deladd3;

    @SerializedName("DELADD4")
    @Column(name = "deladd4")
    public String deladd4;

    @SerializedName("DELZIP")
    @Column(name = "delzip")
    public String delzip;

    @SerializedName("PHONE")
    @Column(name = "phone")
    public String phone;

    @SerializedName("HP")
    @Column(name = "hp")
    public String hp;

    @SerializedName("FAX")
    @Column(name = "fax")
    public String fax;

    @SerializedName("EMAIL")
    @Column(name = "email")
    public String email;

    @SerializedName("CONTACT")
    @Column(name = "contact")
    public String contact;

    @SerializedName("OWNER")
    @Column(name = "owner")
    public String owner;

    @SerializedName("EMPID")
    @Column(name = "empid")
    public String empid;

    @SerializedName("TOPID")
    @Column(name = "topid")
    public String topid;

    @SerializedName("VATID")
    @Column(name = "vatid")
    public String vatid;

    @SerializedName("TAXID")
    @Column(name = "taxid")
    public String taxid;

    @SerializedName("CURRID")
    @Column(name = "currid")
    public String currency;

    @SerializedName("PRICEGRP")
    @Column(name = "pricegpr")
    public String pricegrp;

    @SerializedName("WHID")
    @Column(name = "whid")
    public String warehouse;

    @SerializedName("DISCOUNT")
    @Column(name = "discount")
    public double DISCOUNT;

    @SerializedName("DISCOUNT2")
    @Column(name = "discount2")
    public double DISCOUNT2;

    @SerializedName("DISCOUNT3")
    @Column(name = "discount3")
    public double DISCOUNT3;

    @SerializedName("DISCOUNT4")
    @Column(name = "discount4")
    public double DISCOUNT4;

    @SerializedName("DISCOUNT5")
    @Column(name = "discount5")
    public double DISCOUNT5;

    @SerializedName("CRLIMIT")
    @Column(name = "crlimit")
    public double crlimit;

    @SerializedName("TOTALAR")
    @Column(name = "totalar")
    public double totalar;

    @SerializedName("DEPOSIT")
    @Column(name = "desposit")
    public double deposit;

    @SerializedName("PCMAMT")
    @Column(name = "pcmamt")
    public double pcmamt;

    @SerializedName("TOTALCN")
    @Column(name = "totalcn")
    public double totalcn;

    @SerializedName("TOTALDN")
    @Column(name = "totaldn")
    public double totaldn;

    @SerializedName("sublsg")
    @Column(name = "sublsg")
    public double sublsg;

    @SerializedName("paytype")
    @Column(name = "paytype")
    public int paytype;

    @SerializedName("ACCID")
    @Column(name = "accid")
    public String accid;

    @SerializedName("ARMAXDUE")
    @Column(name = "armaxdue")
    public int armaxdue;

    @SerializedName("ONHOLD")
    @Column(name = "onhold")
    public int inhold;

    @SerializedName("INACTIVE")
    @Column(name = "inactive")
    public int inactive;

    @SerializedName("EV_COMPANY")
    @Column(name = "ev_company")
    public String ev_company;

    @SerializedName("EV_OFFADDRESS")
    @Column(name = "ev_off_address")
    public String ev_off_address;

    @SerializedName("EV_OFFTELP")
    @Column(name = "ev_off_telp")
    public String ev_off_telp;

    @SerializedName("EV_PIN")
    @Column(name = "ev_pim")
    public String ev_pin;

    @SerializedName("EV_TOTALPENJUALAN")
    @Column(name = "ev_taotal_penjualan")
    public double ev_total_penjualan;

    @SerializedName("EV_CUSTID2")
    @Column(name = "ev_cust_id_2")
    public String ev_cust_id_2;

    @SerializedName("EV_SALESID")
    @Column(name = "ev_sales_id")
    public String ev_sales_id;

    @SerializedName("EV_AKTIF")
    @Column(name = "ev_active")
    public String ev_aktive;

    @SerializedName("USRCREATE")
    @Column(name = "creator")
    public String creator;

    @SerializedName("DATECREATE")
    @Column(name = "created_at")
    public Date created_at;

    @SerializedName("USRUPDATE")
    @Column(name = "updater")
    public String updater;

    @SerializedName("DATEUPDATE")
    @Column(name = "updated_at")
    public Date updated_at;


    public Customer() {
        super();
    }

    public Customer(Builder builder) {
        super();
        this.custid = builder.id;
        this.name = builder.name;
        this.csstatid1 = builder.csstatid1;
        this.csstatid2 = builder.csstatid2;
        this.csstatid3 = builder.csstatid3;
        this.csstatid4 = builder.csstatid4;
        this.csstatid5 = builder.csstatid5;
        this.invadd1 = builder.invadd1;
        this.invadd2 = builder.invadd2;
        this.invadd3 = builder.invadd3;
        this.invadd4 = builder.invadd4;
        this.invzip = builder.invzip;
        this.created_at = builder.created_at;
        this.updated_at = builder.updated_at;
    }

    public static Customer getCustomer(String custid) {
        return new Select()
                .from(Customer.class)
                .where("custid = ?", custid)
                .executeSingle();
    }

    public static String getCustomerName(String custid) {
        Customer customer = getCustomer(custid);

        if (customer != null)
            return customer.name;
        return "";
    }

    public static List<Customer> getAll() {
        return new Select()
                .from(Customer.class)
                .orderBy("name ASC")
                .execute();
    }

    public static Customer findOrCreateFromJson(JSONObject json) throws JSONException {
        String custid = json.getString("CUSTID");
        Customer existingCustomer = new Select().from(Customer.class).where("custid = ?", custid).executeSingle();
        Customer customer = Customer.fromJson(json);
        if (existingCustomer != null) {
            if (customer.updated_at.after(existingCustomer.updated_at)) {
                existingCustomer = customer;
                existingCustomer.save();
            }
            return existingCustomer;
        } else {
            customer.save();
            return customer;
        }
    }

    public static Customer fromJson(JSONObject json) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls();
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json.toString(), Customer.class);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "custid='" + custid + '\'' +
                ", name='" + name + '\'' +
                ", csstatid1='" + csstatid1 + '\'' +
                ", csstatid2='" + csstatid2 + '\'' +
                ", csstatid3='" + csstatid3 + '\'' +
                ", csstatid4='" + csstatid4 + '\'' +
                ", group_type='" + group_type + '\'' +
                ", cs_group='" + cs_group + '\'' +
                ", channel='" + channel + '\'' +
                ", csstatid5='" + csstatid5 + '\'' +
                ", invadd1='" + invadd1 + '\'' +
                ", invadd2='" + invadd2 + '\'' +
                ", invadd3='" + invadd3 + '\'' +
                ", invadd4='" + invadd4 + '\'' +
                ", invzip='" + invzip + '\'' +
                ", deladdid='" + deladdid + '\'' +
                ", deladd1='" + deladd1 + '\'' +
                ", deladd2='" + deladd2 + '\'' +
                ", deladd3='" + deladd3 + '\'' +
                ", deladd4='" + deladd4 + '\'' +
                ", delzip='" + delzip + '\'' +
                ", phone='" + phone + '\'' +
                ", hp='" + hp + '\'' +
                ", fax='" + fax + '\'' +
                ", email='" + email + '\'' +
                ", contact='" + contact + '\'' +
                ", owner='" + owner + '\'' +
                ", empid='" + empid + '\'' +
                ", topid='" + topid + '\'' +
                ", vatid='" + vatid + '\'' +
                ", taxid='" + taxid + '\'' +
                ", currency='" + currency + '\'' +
                ", pricegrp='" + pricegrp + '\'' +
                ", warehouse='" + warehouse + '\'' +
                ", DISCOUNT=" + DISCOUNT +
                ", DISCOUNT2=" + DISCOUNT2 +
                ", DISCOUNT3=" + DISCOUNT3 +
                ", DISCOUNT4=" + DISCOUNT4 +
                ", DISCOUNT5=" + DISCOUNT5 +
                ", crlimit=" + crlimit +
                ", totalar=" + totalar +
                ", deposit=" + deposit +
                ", pcmamt=" + pcmamt +
                ", totalcn=" + totalcn +
                ", totaldn=" + totaldn +
                ", sublsg=" + sublsg +
                ", paytype=" + paytype +
                ", accid='" + accid + '\'' +
                ", armaxdue=" + armaxdue +
                ", inhold=" + inhold +
                ", inactive=" + inactive +
                ", ev_company='" + ev_company + '\'' +
                ", ev_off_address='" + ev_off_address + '\'' +
                ", ev_off_telp='" + ev_off_telp + '\'' +
                ", ev_pin='" + ev_pin + '\'' +
                ", ev_total_penjualan=" + ev_total_penjualan +
                ", ev_cust_id_2='" + ev_cust_id_2 + '\'' +
                ", ev_sales_id='" + ev_sales_id + '\'' +
                ", ev_aktive='" + ev_aktive + '\'' +
                ", creator='" + creator + '\'' +
                ", created_at=" + created_at +
                ", updater='" + updater + '\'' +
                ", updated_at=" + updated_at +
                '}';
    }

    public static class Builder {
        public String id;

        public String name;

        public String csstatid1;

        public String csstatid2;

        public String csstatid3;

        public String csstatid4;

        public String csstatid5;

        public String invadd1;

        public String invadd2;

        public String invadd3;

        public String invadd4;

        public String invzip;

        public String deladdid;

        public String deladd1;

        public String deladd2;

        public String deladd3;

        public String deladd4;

        public String delzip;

        public String phone;

        public String hp;

        public String fax;

        public String email;

        public String contact;

        public String owner;

        public String empid;

        public String topid;

        public String vatid;

        public String taxid;

        public String currency;

        public String pricegrp;

        public String warehouse;

        public double DISCOUNT;

        public double DISCOUNT1;

        public double DISCOUNT2;

        public double DISCOUNT3;

        public double DISCOUNT4;

        public double crlimit;

        public double totalar;

        public double deposit;

        public double pcmamt;

        public double totalcn;

        public double totaldn;

        public double sublsg;

        public int paytype;

        public String accid;

        public int armaxdue;

        public int inhold;

        public int inactive;

        public String ev_company;

        public String ev_off_address;

        public String ev_off_telp;

        public String ev_pin;

        public double ev_total_penjualan;

        public String ev_cust_id_2;

        public String ev_sales_id;

        public String ev_aktive;

        public String creator;

        public Date created_at;

        public String updater;

        public Date updated_at;

        public Builder setId(String id) {
            this.id = id;
            return Builder.this;
        }

        public Builder setNama(String nama) {
            this.name = nama;
            return Builder.this;
        }

        public Builder setCsstatid1(String csstatid1) {
            this.csstatid1 = csstatid1;
            return Builder.this;
        }

        public Builder setCsstatid2(String csstatid2) {
            this.csstatid2 = csstatid2;
            return Builder.this;
        }

        public Builder setCsstatid3(String csstatid3) {
            this.csstatid3 = csstatid3;
            return Builder.this;
        }

        public Builder setCsstatid4(String csstatid4) {
            this.csstatid4 = csstatid4;
            return Builder.this;
        }

        public Builder setCsstatid5(String csstatid5) {
            this.csstatid5 = csstatid5;
            return Builder.this;
        }

        public Builder setInvaadd1(String invaadd1) {
            this.invadd1 = invaadd1;
            return Builder.this;
        }

        public Builder setInvaadd2(String invaadd2) {
            this.invadd2 = invaadd2;
            return Builder.this;
        }

        public Builder setInvaadd3(String invaadd3) {
            this.invadd3 = invaadd3;
            return Builder.this;
        }

        public Builder setInvaadd4(String invaadd4) {
            this.invadd4 = invaadd4;
            return Builder.this;
        }

        public Builder setInvzip(String invzip) {
            this.invzip = invzip;
            return Builder.this;
        }

        public Builder setSublsg(Double sublsg) {
            this.sublsg = sublsg;
            return Builder.this;
        }

        public Builder setCreate_at(Date create_at) {
            this.created_at = create_at;
            return Builder.this;
        }

        public Builder setUpdate_at(Date update_at) {
            this.updated_at = update_at;
            return Builder.this;
        }

        public Customer builde() {
            return new Customer(Builder.this);
        }
    }
}