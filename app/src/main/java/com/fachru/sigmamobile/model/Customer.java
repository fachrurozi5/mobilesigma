package com.fachru.sigmamobile.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by fachru on 17/12/15.
 */
@Table(name = "customer", id = "_id")
public class Customer {

    @SerializedName("CUSTID")
    @Column(name = "id", unique = true)
    private String id;

    @SerializedName("CUSTNAME")
    @Column(name = "name")
    private String name;

    @SerializedName("csstatid1")    // as group
    @Column(name = "csstadid")
    private String csstatid1;

    @SerializedName("CSSTATID2")
    @Column(name = "csstatid2")
    private String csstatid2;

    @SerializedName("CSSTATID3")
    @Column(name = "csstatid3")
    private String csstatid3;

    @SerializedName("CSSTATID4")
    @Column(name = "csstatid4")
    private String csstatid4;

    @SerializedName("CSSTATID5")
    @Column(name = "csstatid5")
    private String csstatid5;

    @SerializedName("INVADD1")      //
    @Column(name = "invadd1")
    private String invadd1;

    @SerializedName("INVADD2")
    @Column(name = "invadd2")
    private String invadd2;

    @SerializedName("INVADD3")
    @Column(name = "invadd3")
    private String invadd3;

    @SerializedName("INVADD4")
    @Column(name = "invadd4")
    private String invadd4;

    @SerializedName("INVZIP")
    @Column(name = "invzip")
    private String invzip;

    @SerializedName("DELADDID")
    @Column(name = "deladdid")
    private String deladdid;

    @SerializedName("DELADD1")
    @Column(name = "deladd1")
    private String deladd1;

    @SerializedName("DELADD2")
    @Column(name = "deladd2")
    private String deladd2;

    @SerializedName("DELADD3")
    @Column(name = "deladd3")
    private String deladd3;

    @SerializedName("DELADD4")
    @Column(name = "deladd4")
    private String deladd4;

    @SerializedName("DELZIP")
    @Column(name = "delzip")
    private String delzip;

    @SerializedName("PHONE")
    @Column(name = "phone")
    private String phone;

    @SerializedName("HP")
    @Column(name = "hp")
    private String hp;

    @SerializedName("FAX")
    @Column(name = "fax")
    private String fax;

    @SerializedName("EMAIL")
    @Column(name = "email")
    private String email;

    @SerializedName("CONTACT")
    @Column(name = "contact")
    private String contact;

    @SerializedName("OWNER")
    @Column(name = "owner")
    private String owner;

    @SerializedName("EMPID")
    @Column(name = "empid")
    private String empid;

    @SerializedName("TOPID")
    @Column(name = "topid")
    private String topid;

    @SerializedName("VATID")
    @Column(name = "vatid")
    private String vatid;

    @SerializedName("TAXID")
    @Column(name = "taxid")
    private String taxid;

    @SerializedName("CURRID")
    @Column(name = "currid")
    private String currency;

    @SerializedName("PRICEGRP")
    @Column(name = "pricegpr")
    private String pricegrp;

    @SerializedName("WHID")
    @Column(name = "whid")
    private String warehouse;

    @SerializedName("DISCOUNT")
    @Column(name = "discount")
    private double DISCOUNT;

    @SerializedName("DISCOUNT2")
    @Column(name = "discount2")
    private double DISCOUNT2;

    @SerializedName("DISCOUNT3")
    @Column(name = "discount3")
    private double DISCOUNT3;

    @SerializedName("DISCOUNT4")
    @Column(name = "discount4")
    private double DISCOUNT4;

    @SerializedName("DISCOUNT5")
    @Column(name = "discount5")
    private double DISCOUNT5;

    @SerializedName("CRLIMIT")
    @Column(name = "crlimit")
    private double crlimit;

    @SerializedName("TOTALAR")
    @Column(name = "totalar")
    private double totalar;

    @SerializedName("DEPOSIT")
    @Column(name = "desposit")
    private double deposit;

    @SerializedName("PCMAMT")
    @Column(name = "pcmamt")
    private double pcmamt;

    @SerializedName("TOTALCN")
    @Column(name = "totalcn")
    private double totalcn;

    @SerializedName("TOTALDN")
    @Column(name = "totaldn")
    private double totaldn;

    @SerializedName("sublsg")
    @Column(name = "sublsg")
    private double sublsg;

    @SerializedName("paytype")
    @Column(name = "paytype")
    private int paytype;

    @SerializedName("ACCID")
    @Column(name = "accid")
    private String accid;

    @SerializedName("ARMAXDUE")
    @Column(name = "armaxdue")
    private int armaxdue;

    @SerializedName("ONHOLD")
    @Column(name = "onhold")
    private int inhold;

    @SerializedName("INACTIVE")
    @Column(name = "inactive")
    private int inactive;

    @SerializedName("EV_COMPANY")
    @Column(name = "ev_company")
    private String ev_company;

    @SerializedName("EV_OFFADDRESS")
    @Column(name = "ev_off_address")
    private String ev_off_address;

    @SerializedName("EV_OFFTELP")
    @Column(name = "ev_off_telp")
    private String ev_off_telp;

    @SerializedName("EV_PIN")
    @Column(name = "ev_pim")
    private String ev_pin;

    @SerializedName("EV_TOTALPENJUALAN")
    @Column(name = "ev_taotal_penjualan")
    private double ev_total_penjualan;

    @SerializedName("EV_CUSTID2")
    @Column(name = "ev_cust_id_2")
    private String ev_cust_id_2;

    @SerializedName("EV_SALESID")
    @Column(name = "ev_sales_id")
    private String ev_sales_id;

    @SerializedName("EV_AKTIF")
    @Column(name = "ev_active")
    private String ev_aktive;

    @SerializedName("USRCREATE")
    @Column(name = "creator")
    private String creator;

    @SerializedName("DATECREATE")
    @Column(name = "created_at")
    private Date created_at = new Date();

    @SerializedName("USRUPDATE")
    @Column(name = "updater")
    private String updater;

    @SerializedName("DATEUPDATE")
    @Column(name = "updated_at")
    private Date updated_at = new Date();


    public Customer() {
        super();
    }

    public  Customer(Builder builder) {
        super();
        this.id = builder.id;
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
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCsstatid1() {
        return csstatid1;
    }

    public void setCsstatid1(String csstatid1) {
        this.csstatid1 = csstatid1;
    }

    public String getCsstatid2() {
        return csstatid2;
    }

    public void setCsstatid2(String csstatid2) {
        this.csstatid2 = csstatid2;
    }

    public String getCsstatid3() {
        return csstatid3;
    }

    public void setCsstatid3(String csstatid3) {
        this.csstatid3 = csstatid3;
    }

    public String getCsstatid4() {
        return csstatid4;
    }

    public void setCsstatid4(String csstatid4) {
        this.csstatid4 = csstatid4;
    }

    public String getCsstatid5() {
        return csstatid5;
    }

    public void setCsstatid5(String csstatid5) {
        this.csstatid5 = csstatid5;
    }

    public String getInvadd1() {
        return invadd1;
    }

    public void setInvadd1(String invadd1) {
        this.invadd1 = invadd1;
    }

    public String getInvadd2() {
        return invadd2;
    }

    public void setInvadd2(String invadd2) {
        this.invadd2 = invadd2;
    }

    public String getInvadd3() {
        return invadd3;
    }

    public void setInvadd3(String invadd3) {
        this.invadd3 = invadd3;
    }

    public String getInvadd4() {
        return invadd4;
    }

    public void setInvadd4(String invadd4) {
        this.invadd4 = invadd4;
    }

    public String getInvzip() {
        return invzip;
    }

    public void setInvzip(String invzip) {
        this.invzip = invzip;
    }

    public String getDeladdid() {
        return deladdid;
    }

    public void setDeladdid(String deladdid) {
        this.deladdid = deladdid;
    }

    public String getDeladd1() {
        return deladd1;
    }

    public void setDeladd1(String deladd1) {
        this.deladd1 = deladd1;
    }

    public String getDeladd2() {
        return deladd2;
    }

    public void setDeladd2(String deladd2) {
        this.deladd2 = deladd2;
    }

    public String getDeladd3() {
        return deladd3;
    }

    public void setDeladd3(String deladd3) {
        this.deladd3 = deladd3;
    }

    public String getDeladd4() {
        return deladd4;
    }

    public void setDeladd4(String deladd4) {
        this.deladd4 = deladd4;
    }

    public String getDelzip() {
        return delzip;
    }

    public void setDelzip(String delzip) {
        this.delzip = delzip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getTopid() {
        return topid;
    }

    public void setTopid(String topid) {
        this.topid = topid;
    }

    public String getVatid() {
        return vatid;
    }

    public void setVatid(String vatid) {
        this.vatid = vatid;
    }

    public String getTaxid() {
        return taxid;
    }

    public void setTaxid(String taxid) {
        this.taxid = taxid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPricegrp() {
        return pricegrp;
    }

    public void setPricegrp(String pricegrp) {
        this.pricegrp = pricegrp;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public double getDISCOUNT() {
        return DISCOUNT;
    }

    public void setDISCOUNT(double DISCOUNT) {
        this.DISCOUNT = DISCOUNT;
    }

    public double getDISCOUNT2() {
        return DISCOUNT2;
    }

    public void setDISCOUNT2(double DISCOUNT2) {
        this.DISCOUNT2 = DISCOUNT2;
    }

    public double getDISCOUNT3() {
        return DISCOUNT3;
    }

    public void setDISCOUNT3(double DISCOUNT3) {
        this.DISCOUNT3 = DISCOUNT3;
    }

    public double getDISCOUNT4() {
        return DISCOUNT4;
    }

    public void setDISCOUNT4(double DISCOUNT4) {
        this.DISCOUNT4 = DISCOUNT4;
    }

    public double getDISCOUNT5() {
        return DISCOUNT5;
    }

    public void setDISCOUNT5(double DISCOUNT5) {
        this.DISCOUNT5 = DISCOUNT5;
    }

    public double getCrlimit() {
        return crlimit;
    }

    public void setCrlimit(double crlimit) {
        this.crlimit = crlimit;
    }

    public double getTotalar() {
        return totalar;
    }

    public void setTotalar(double totalar) {
        this.totalar = totalar;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getPcmamt() {
        return pcmamt;
    }

    public void setPcmamt(double pcmamt) {
        this.pcmamt = pcmamt;
    }

    public double getTotalcn() {
        return totalcn;
    }

    public void setTotalcn(double totalcn) {
        this.totalcn = totalcn;
    }

    public double getTotaldn() {
        return totaldn;
    }

    public void setTotaldn(double totaldn) {
        this.totaldn = totaldn;
    }

    public double getSublsg() {
        return sublsg;
    }

    public void setSublsg(double sublsg) {
        this.sublsg = sublsg;
    }

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public int getArmaxdue() {
        return armaxdue;
    }

    public void setArmaxdue(int armaxdue) {
        this.armaxdue = armaxdue;
    }

    public int getInhold() {
        return inhold;
    }

    public void setInhold(int inhold) {
        this.inhold = inhold;
    }

    public int getInactive() {
        return inactive;
    }

    public void setInactive(int inactive) {
        this.inactive = inactive;
    }

    public String getEv_company() {
        return ev_company;
    }

    public void setEv_company(String ev_company) {
        this.ev_company = ev_company;
    }

    public String getEv_off_address() {
        return ev_off_address;
    }

    public void setEv_off_address(String ev_off_address) {
        this.ev_off_address = ev_off_address;
    }

    public String getEv_off_telp() {
        return ev_off_telp;
    }

    public void setEv_off_telp(String ev_off_telp) {
        this.ev_off_telp = ev_off_telp;
    }

    public String getEv_pin() {
        return ev_pin;
    }

    public void setEv_pin(String ev_pin) {
        this.ev_pin = ev_pin;
    }

    public double getEv_total_penjualan() {
        return ev_total_penjualan;
    }

    public void setEv_total_penjualan(double ev_total_penjualan) {
        this.ev_total_penjualan = ev_total_penjualan;
    }

    public String getEv_cust_id_2() {
        return ev_cust_id_2;
    }

    public void setEv_cust_id_2(String ev_cust_id_2) {
        this.ev_cust_id_2 = ev_cust_id_2;
    }

    public String getEv_sales_id() {
        return ev_sales_id;
    }

    public void setEv_sales_id(String ev_sales_id) {
        this.ev_sales_id = ev_sales_id;
    }

    public String getEv_aktive() {
        return ev_aktive;
    }

    public void setEv_aktive(String ev_aktive) {
        this.ev_aktive = ev_aktive;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public static class Builder {
        private String id;

        private String name;

        private String csstatid1;

        private String csstatid2;

        private String csstatid3;

        private String csstatid4;

        private String csstatid5;

        private String invadd1;

        private String invadd2;

        private String invadd3;

        private String invadd4;

        private String invzip;

        private String deladdid;

        private String deladd1;

        private String deladd2;

        private String deladd3;

        private String deladd4;

        private String delzip;

        private String phone;

        private String hp;

        private String fax;

        private String email;

        private String contact;

        private String owner;

        private String empid;

        private String topid;

        private String vatid;

        private String taxid;

        private String currency;

        private String pricegrp;

        private String warehouse;

        private double DISCOUNT;

        private double DISCOUNT1;

        private double DISCOUNT2;

        private double DISCOUNT3;

        private double DISCOUNT4;

        private double crlimit;

        private double totalar;

        private double deposit;

        private double pcmamt;

        private double totalcn;

        private double totaldn;

        private double sublsg;

        private int paytype;

        private String accid;

        private int armaxdue;

        private int inhold;

        private int inactive;

        private String ev_company;

        private String ev_off_address;

        private String ev_off_telp;

        private String ev_pin;

        private double ev_total_penjualan;

        private String ev_cust_id_2;

        private String ev_sales_id;

        private String ev_aktive;

        private String creator;

        private Date created_at;

        private String updater;

        private Date updated_at;

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

        public Customer builde() {
            return new Customer(Builder.this);
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", csstatid1='" + csstatid1 + '\'' +
                ", csstatid2='" + csstatid2 + '\'' +
                ", csstatid3='" + csstatid3 + '\'' +
                ", csstatid4='" + csstatid4 + '\'' +
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
}