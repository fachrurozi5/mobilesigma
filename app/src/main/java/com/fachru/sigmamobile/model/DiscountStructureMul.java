package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fachru on 17/05/16.
 */
@Table(name = "DiscountStructureMul")
public class DiscountStructureMul extends Model {

	@Expose
	@SerializedName("id_disc")
	@Column(name = "id_disc")
	public String _idDisc;

	@Expose
	@SerializedName("product_id")
	@Column(name = "productId")
	public String productId;

	@Expose
	@SerializedName("min_multiples")
	@Column(name = "min_multiples")
	public int minMultiples;

	@Expose
	@SerializedName("p_qty_carton")
	@Column(name = "p_qty_carton")
	public int princQtyCarton;

	@Expose
	@SerializedName("p_qty_pcs")
	@Column(name = "p_qty_pcs")
	public int princQtyPcs;

	@Expose
	@SerializedName("n_qty_carton")
	@Column(name = "n_qty_carton")
	public int nspQtyCarton;

	@Expose
	@SerializedName("n_qty_pcs")
	@Column(name = "n_qty_pcs")
	public int npsQtyPcs;

	@Column(name = "discount", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
	public Discount discount;

	public DiscountStructureMul() {
		super();
	}

	@Override
	public String toString() {
		return "DiscountStructureMul{" +
				"_idDisc='" + _idDisc + '\'' +
				", productId='" + productId + '\'' +
				", minMultiples=" + minMultiples +
				", princQtyCarton=" + princQtyCarton +
				", princQtyPcs=" + princQtyPcs +
				", nspQtyCarton=" + nspQtyCarton +
				", npsQtyPcs=" + npsQtyPcs +
				", discount=" + discount +
				'}';
	}
}
