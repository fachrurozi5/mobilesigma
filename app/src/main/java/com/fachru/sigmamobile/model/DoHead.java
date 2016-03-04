package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
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

    @Expose
    @SerializedName("DOCNO")
    @Column(name = "docno", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String doc_no;

    @Expose
    @SerializedName("DOCDATE")
    @Column(name = "doc_date")
    public Date doc_date;

    @Expose
    @SerializedName("CUSTID")
    @Column(name = "custid")
    public String custid;

    @Expose
    @SerializedName("EMPID")
    @Column(name = "empid")
    public String empid;

    @Expose
    @SerializedName("TOPID")
    @Column(name = "topid")
    public String topid = "7";

    @Expose
    @SerializedName("VATID")
    @Column(name = "vatid")
    public String vatid = "10";

    @Expose
    @SerializedName("CURRID")
    @Column(name = "currid")
    public String currid = "RP";

    @Expose
    @SerializedName("TYPEOFSO")
    @Column(name = "typeofso")
    public String typeofso = "7";

    @Expose
    @SerializedName("VATNO")
    @Column(name = "vatno")
    public String vatno;

    @Expose
    @SerializedName("NETAMT")
    @Column(name = "netamt")
    public double netamt; //

    @Expose
    @SerializedName("VATAMT")
    @Column(name = "vatamt")
    public double vatamt; //

    @Expose
    @SerializedName("WHID")
    @Column(name = "whid")
    public String whid;

    @Expose
    @SerializedName("PAYTYPE")
    @Column(name = "paytype")
    public int paytype = 5;

    @Expose
    @SerializedName("PERIOD")
    @Column(name = "period")
    public String period;

    @Expose
    @SerializedName("DOCPRINT")
    @Column(name = "docprint")
    public int docprint = 0;

    @Expose
    @SerializedName("DIBAYAR")
    @Column(name = "dibayar")
    public double dibayar;

    @Expose
    @SerializedName("DATECREATE")
    @Column(name = "created_at")
    public Date created_at = new Date();

    @Expose
    @SerializedName("DATEUPDATE")
    @Column(name = "updated_at")
    public Date updated_at = new Date();

    @Expose
    @SerializedName("uploaded")
    @Column(name = "uploaded")
    public boolean uploaded = false;

    public DoHead() {
        super();
    }

    public DoHead(Builder builder) {
        super();
        this.doc_no = builder.doc_no;
        this.vatno = builder.vatno;
        this.doc_date = builder.doc_date;
        this.custid = builder.custid;
        this.empid = builder.empid;
        this.whid = builder.whid;
        this.period = new SimpleDateFormat("yyyydd", Locale.US).format(new Date());
    }

    public static DoHead last() {
        return new Select().from(DoHead.class)
                .orderBy("docno DESC")
                .executeSingle();
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
                .and("docprint =?", 0)
                .execute();
    }

    public static List<DoHead> getAllHasPrint() {
        return new Select()
                .from(DoHead.class)
                .where("docprint =?", 1)
                .execute();
    }

    public static boolean hasPrint(String doc_no) {
        return  new Select()
                .from(DoHead.class)
                .where("docno = ?", doc_no)
                .and("docprint =?", 1)
                .count() > 0;
    }

    public static List<DoHead> getAllNotUpload() {
        return new Select()
                .from(DoHead.class)
                .where("uploaded =?", false)
                .execute();
    }

    public static boolean hasDataToUpload() {
        return DoHead.getAllNotUpload().size() > 0;
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

    public static int count() {
        return new Select()
                .from(DoHead.class)
                .where("docno like ?", "%" + getFirstId() + "%").execute().size();
    }

    public static String generateId() {
        String id = getFirstId();
        int size = count();

        if (size > 0) {
            long v = Long.parseLong(last().doc_no.substring(8));
            if (size <= 9) {
                id += "00" + (v + 1);
            } else if (size > 9) {
                id += "0" + (v + 1);
            } else if (size > 99) {
                id += (v + 1);
            }
        } else {
            id += "001";
        }

        return id;
    }

    private static String getFirstId() {
        String id = "PS.";
        SimpleDateFormat format = new SimpleDateFormat("MMdd.", Locale.getDefault());
        id += format.format(new Date());
        return id;
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

    @Override
    public String toString() {
        return "DoHead{" +
                "doc_no='" + doc_no + '\'' +
                ", doc_date=" + doc_date +
                ", custid='" + custid + '\'' +
                ", empid='" + empid + '\'' +
                ", topid='" + topid + '\'' +
                ", vatid='" + vatid + '\'' +
                ", currid='" + currid + '\'' +
                ", typeofso='" + typeofso + '\'' +
                ", vatno='" + vatno + '\'' +
                ", netamt=" + netamt +
                ", vatamt=" + vatamt +
                ", whid='" + whid + '\'' +
                ", paytype=" + paytype +
                ", period='" + period + '\'' +
                ", docprint=" + docprint +
                ", dibayar=" + dibayar +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", uploaded=" + uploaded +
                '}';
    }

    public static class Builder {
        public String doc_no;
        public String vatno;
        public Date doc_date;
        public String custid;
        public String empid;
        public String whid;

        public Builder setDocNo(String docno) {
            this.doc_no = docno;
            return Builder.this;
        }

        public Builder setVatno(String vatno) {
            this.vatno = vatno;
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

        public Builder setEmpid(String empid) {
            this.empid = empid;
            return Builder.this;
        }

        public Builder setWhid(String whid) {
            this.whid = whid;
            return Builder.this;
        }

        public DoHead build() {
            return new DoHead(Builder.this);
        }
    }
}
