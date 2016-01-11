package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by fachru on 30/12/15.
 */
@Table(name = "DoHeads")
public class DoHead extends Model {

    @SerializedName("DOCNO")
    @Column(name = "docno", unique = true)
    public String doc_no;

    @SerializedName("DOCDATE")
    @Column(name = "doc_date")
    public Date doc_date;

    @SerializedName("CUSTID")
    @Column(name = "custid")
    public String custid;

    @SerializedName("DATEORDER")
    @Column(name = "date_order")
    public Date date_order;

    @SerializedName("CSSTATID1")
    @Column(name = "csstatid1")
    public String csstatid1;

    @SerializedName("INVADD1")
    @Column(name = "invadd1")
    public String invadd1;

    @SerializedName("INVADD2")
    @Column(name = "invadd2")
    public String invadd2;

    @SerializedName("EMPID")
    @Column(name = "empid")
    public String empid;

    @SerializedName("TOPID")
    @Column(name = "topid")
    public String topid;

    @SerializedName("VATID")
    @Column(name = "vatid")
    public String vatid;

    @SerializedName("CURRID")
    @Column(name = "currid")
    public String currid;

    @SerializedName("")
    @Column(name = "typeofso" )
    public String typeofso;

    @SerializedName("VATNO")
    @Column(name = "vatno" )
    public String vatno;

    @SerializedName("INVAMOUNT")
    @Column(name = "invamount" )
    public double invamount;

    @SerializedName("NETAMT")
    @Column(name = "netamt" )
    public double netamt; //

    @SerializedName("VATAMT")
    @Column(name = "vatamt" )
    public double vatamt; //

    @SerializedName("WHID")
    @Column(name = "whid" )
    public String whid;

    @SerializedName("AMTPAID")
    @Column(name = "amtpaid" )
    public double amtpaid;

    @SerializedName("PAYTYPE")
    @Column(name = "paytype" )
    public int paytype;

    @SerializedName("PERIOD")
    @Column(name = "period" )
    public String period;

    @SerializedName("INACTIVE")
    @Column(name = "inactive" )
    public int inactive;


    public DoHead() {
        super();
    }

    public DoHead(Builder builder) {
        super();
        this.doc_no = builder.doc_no;
        this.doc_date = builder.doc_date;
        this.custid = builder.custid;
        this.date_order = builder.date_order;
        this.empid = builder.empid;
        this.whid = builder.whid;
    }

    public static DoHead find(String docno) {
        return new Select().from(DoHead.class)
                .where("docno = ?", docno)
                .executeSingle();
    }

    public static List<DoHead> getAll() {
        return new Select()
                .from(DoHead.class)
                .execute();
    }

    public static List<DoHead> getAllWhereCustomer(String custid) {
        return new Select()
                .from(DoHead.class)
                .where("custid =? ", custid)
                .execute();
    }

    public List<DoItem> doItems() {
        return new Select()
                .from(DoItem.class)
                .where("docno =? ", doc_no)
                .execute();
    }

    public String getDocDate() {
        return CommonUtil.dateToStringMedium(this.doc_date);
    }

    public void setDateFromString(String date) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        sf.setLenient(true);
        this.doc_date = sf.parse(date);
    }

    public static DoHead findOrCreateFromJson(JSONObject json) throws JSONException {
        String docno = json.getString("DOCNO");
        DoHead existingDoHead = new Select().from(DoHead.class).where("docno = ?", docno).executeSingle();
        if (existingDoHead != null) {
            return existingDoHead;
        } else {
            DoHead doHead = DoHead.fromJson(json);
            doHead.save();
            return doHead;
        }
    }

    public static DoHead fromJson(JSONObject json) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls();
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json.toString(), DoHead.class);
    }

    public static int count(String whid) {
        return new Select()
                .from(DoHead.class)
                .where("empid =?", whid).execute().size();
    }

    public static String generateId(String whid, Date date) {
        String id = whid.substring(0, 2).toUpperCase();
        int size = count(whid);
        SimpleDateFormat format = new SimpleDateFormat("DDD", Locale.getDefault());
        id += "-" + format.format(date);
        if (size > 0) {
            if (size <= 9) {
                id += "00" + (size + 1);
            } else if (size > 9) {
                id += "0" + (size + 1);
            } else if (size > 99){
                id += (size + 1);
            }
        } else {
            id += "001";
        }

        return id;
    }

    @Override
    public String toString() {
        return "DoHead{" +
                "doc_no='" + doc_no + '\'' +
                ", doc_date=" + doc_date +
                ", custid='" + custid + '\'' +
                ", date_order=" + date_order +
                ", csstatid1='" + csstatid1 + '\'' +
                ", invadd1='" + invadd1 + '\'' +
                ", invadd2='" + invadd2 + '\'' +
                ", empid='" + empid + '\'' +
                ", topid='" + topid + '\'' +
                ", vatid='" + vatid + '\'' +
                ", currid='" + currid + '\'' +
                ", typeofso='" + typeofso + '\'' +
                ", vatno='" + vatno + '\'' +
                ", invamount=" + invamount +
                ", netamt=" + netamt +
                ", vatamt=" + vatamt +
                ", whid='" + whid + '\'' +
                ", amtpaid=" + amtpaid +
                ", paytype=" + paytype +
                ", period='" + period + '\'' +
                ", inactive=" + inactive +
                '}';
    }

    public static class Builder {
        public String doc_no;
        public Date doc_date;
        public String custid;
        public Date date_order;
        public String empid;
        public String whid;

        public Builder setDocNo(String docno) {
            this.doc_no = docno;
            return Builder.this;
        }

        public Builder setDocDate(Date docdate) {
            this.doc_date = docdate;
            return Builder.this;
        }

        public Builder setCustid(String custid) {
            this.custid = custid;
            return Builder.this;
        }

        public Builder setDate_order(Date date_order) {
            this.date_order = date_order;
            return Builder.this;
        }

        public Builder setEmpid(String empid) {
            this.empid = empid;
            return Builder.this;
        }

        public Builder setWhid(String whid) {
            this.whid = whid;
            return Builder.this;
        }

        public DoHead build(){
            return new DoHead(Builder.this);
        }
    }
}
