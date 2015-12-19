package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by fachru on 20/10/15.
 */
@Table(name = "DoItems")
public class DoItem extends Model {

    @Column(name = "DoHead", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public DoHead doHead;

    @Column(name = "Product")
    public Product product;

    @Column(name = "jumlah_order")
    public long jumlah_order;

    @Column(name = "discount_nusantara")
    public double discount_nusantara;

    @Column(name = "discount_principal")
    public double discount_principal;

    @Column(name = "sub_total")
    public long sub_total;

    public DoItem() {
        super();
    }

    public DoItem(DoHead doHead, Product product, long jumlah_order, double discount_nusantara, double discount_principal, long sub_total) {
        super();
        this.doHead = doHead;
        this.product = product;
        this.jumlah_order = jumlah_order;
        this.discount_nusantara = discount_nusantara;
        this.discount_principal = discount_principal;
        this.sub_total = sub_total;
    }

    @Override
    public String toString() {
        return "DoItem{" +
                "doHead=" + doHead +
                ", product=" + product +
                ", jumlah_order=" + jumlah_order +
                ", discount_nusantara=" + discount_nusantara +
                ", discount_principal=" + discount_principal +
                ", sub_total=" + sub_total +
                '}';
    }
}
