package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fachru on 16/05/16.
 */
@Table(name = "DiscountStructureLPD")
public class DiscountStructureLPD extends Model {

    @Expose
    @SerializedName("id_disc")
    @Column(name = "id_disc")
    public String _id_disc;

    @Expose
    @SerializedName("product_id")
    @Column(name = "productId")
    public String product_id;

    @Expose
    @SerializedName("p_value")
    @Column(name = "principal_value")
    public int principal_value;

    @Expose
    @SerializedName("p_percent")
    @Column(name = "pricipal_percent")
    public int principal_percent;

    @Expose
    @SerializedName("n_value")
    @Column(name = "nusantara_value")
    public int nusantara_value;

    @Expose
    @SerializedName("n_percent")
    @Column(name = "nusantara_percent")
    public int nusantara_percent;

    @Column(name = "discount", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public Discount discount;

    public DiscountStructureLPD() {
        super();
    }

    @Override
    public String toString() {
        return "DiscountStructureLPD{" +
                "_id_disc='" + _id_disc + '\'' +
                ", productId='" + product_id + '\'' +
                ", principal_value=" + principal_value +
                ", principal_percent=" + principal_percent +
                ", nusantara_value=" + nusantara_value +
                ", nusantara_percent=" + nusantara_percent +
                ", discount=" + discount +
                '}';
    }
}
