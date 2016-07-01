package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by fachru on 17/11/15.
 */
@Table(name = "retur_produk")
public class ReturnsProduct extends Model {

	@Column(name = "produk")
	public String produk;

	@Column(name = "pcs")
	public String pcs;

	@Column(name = "cart")
	public String cart;

	@Column(name = "status")
	public boolean status;

	@Column(name = "ket")
	public String ket;

	public ReturnsProduct() {
		super();
	}

	@Override
	public String toString() {
		return "ReturnsProduct{" +
				"produk='" + produk + '\'' +
				", pcs='" + pcs + '\'' +
				", cart='" + cart + '\'' +
				", status=" + status +
				", ket='" + ket + '\'' +
				'}';
	}
}
