package com.fachru.sigmamobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.ReturnsProduct;
import com.fachru.sigmamobile.utils.CommonUtil;

import java.util.List;

/**
 * Created by fachru on 17/11/15.
 */
public class AdapterReturnsProduct extends BaseAdapter {

    private Context context;
    private List<ReturnsProduct> returnsProducts;


    public AdapterReturnsProduct(Context context, List<ReturnsProduct> returnsProducts) {
        this.context = context;
        this.returnsProducts = returnsProducts;
    }

    @Override
    public int getCount() {
        return returnsProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return returnsProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Holder holder;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

//        if (view == null) {
        view = mInflater.inflate(R.layout.item_retur_produk, null);
        holder = new Holder();
        holder.item_r_no = (TextView) view.findViewById(R.id.item_r_no);
        holder.item_r_j_barang = (TextView) view.findViewById(R.id.item_r_j_barang);
        holder.item_r_pcs = (TextView) view.findViewById(R.id.item_r_pcs);
        holder.item_r_cart = (TextView) view.findViewById(R.id.item_r_cart);
        holder.item_r_ambil = (TextView) view.findViewById(R.id.item_r_ambil);
        holder.item_r_proses = (TextView) view.findViewById(R.id.item_r_proses);
        holder.item_r_ket = (TextView) view.findViewById(R.id.item_r_ket);
        view.setTag(holder);
//        } else {
//            holder = (Holder) view.getTag();
//        }

        ReturnsProduct returnsProduct = (ReturnsProduct) getItem(position);

        holder.item_r_no.setText(String.valueOf(position + 1));
        holder.item_r_j_barang.setText(returnsProduct.produk);
        holder.item_r_pcs.setText(returnsProduct.pcs);
        holder.item_r_cart.setText(returnsProduct.cart);
        if (returnsProduct.status)
            CommonUtil.setTextViewColor(holder.item_r_ambil, "#00ff00");
        else
            CommonUtil.setTextViewColor(holder.item_r_proses, "#00ff00");
        /*holder.item_r_ambil.setText(returnsProduct.ambil);
        holder.item_r_proses.setText(returnsProduct.proses);*/
        holder.item_r_ket.setText(returnsProduct.ket);

        return view;
    }

    public void add(ReturnsProduct returnsProduct) {
        returnsProducts.add(returnsProduct);
        this.notifyDataSetChanged();
    }

    public void set(ReturnsProduct returnsProduct) {
        this.returnsProducts.set(returnsProducts.indexOf(returnsProduct), returnsProduct);
        this.notifyDataSetChanged();
    }

    public void delete(ReturnsProduct returnsProduct) {
        this.returnsProducts.remove(returnsProduct);
        this.notifyDataSetChanged();
    }

    private class Holder {
        TextView item_r_no;
        TextView item_r_j_barang;
        TextView item_r_pcs;
        TextView item_r_cart;
        TextView item_r_ambil;
        TextView item_r_proses;
        TextView item_r_ket;
    }
}
