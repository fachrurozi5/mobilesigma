package com.fachru.sigmamobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.SoItem;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.utils.CommonUtil;

import java.util.List;

/**
 * Created by fachru on 15/10/15.
 */
public class AdapterSoItem extends BaseAdapter {

    private Context context;
    private List<SoItem> list;
    private int type_price_list;

    public AdapterSoItem(Context context, List<SoItem> list, int type_price_list) {
        this.context = context;
        this.list = list;
        this.type_price_list = type_price_list;
    }

    public void update(List<SoItem> list, int type_price_list) {
        this.list = list;
        this.type_price_list = type_price_list;
        this.notifyDataSetChanged();
    }

    public List<SoItem> getList() {
        return this.list;
    }

    public void add(SoItem soItem) {
        this.list.add(soItem);
        this.notifyDataSetChanged();
    }

    public void set(SoItem soItem) {
        this.list.set(this.list.indexOf(soItem), soItem);
        this.notifyDataSetChanged();
    }

    public void delete(SoItem soItem) {
        this.list.remove(soItem);
        this.notifyDataSetChanged();
    }

    public void clearAll() {
        this.list.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = mInflater.inflate(R.layout.item_sales_order, null);
            holder = new Holder();
            holder.no = (TextView) view.findViewById(R.id.item_so_no);
            holder.jenis_barang = (TextView) view.findViewById(R.id.item_so_jenis_barang);
            holder.unit = (TextView) view.findViewById(R.id.item_so_unit);
            holder.harga_list = (TextView) view.findViewById(R.id.item_so_harga_list);
            holder.jumlah_order = (TextView) view.findViewById(R.id.item_so_jumlah_order);/*
            holder.persen_d_nusantara = (TextView) view.findViewById(R.id.item_so_persen_d_nusantara);
            holder.nilai_d_nusantara = (TextView) view.findViewById(R.id.item_so_nilai_d_nusantara);
            holder.persen_d_principal = (TextView) view.findViewById(R.id.item_so_persen_d_principal);
            holder.nilai_d_principal = (TextView) view.findViewById(R.id.item_so_nilai_d_principal);*/
            holder.sub_total = (TextView) view.findViewById(R.id.item_so_sub_total);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        SoItem soItem = (SoItem) getItem(i);
        Product product = Product.find(soItem.product_id);
        String product_price = null;

        switch (type_price_list){
            case 1 :
                product_price = CommonUtil.priceFormat(product.po_price);
                break;
            case 2 :
                product_price = CommonUtil.priceFormat(product.sellprice);
                break;
            case 3 :
                product_price = CommonUtil.priceFormat(product.base_price);
                break;
            case 4 :
                product_price = CommonUtil.priceFormat(product.old_price);
                break;
            case 5 :
                product_price = CommonUtil.priceFormat(product.test_price);
                break;
        }

        holder.no.setText(String.valueOf(i + 1));
        holder.jenis_barang.setText(product.name);
        holder.unit.setText(product.unitid);
        holder.harga_list.setText(product_price);
        holder.jumlah_order.setText(CommonUtil.priceFormat(soItem.qty));
//        holder.persen_d_nusantara.setText(CommonUtil.percentFormat(soItem.discount_nusantara));
//        double nilai_nusantara = (soItem.product.price * soItem.discount_nusantara) / 100;
//        holder.nilai_d_nusantara.setText(CommonUtil.priceFormat(nilai_nusantara));
//        holder.persen_d_principal.setText(CommonUtil.percentFormat(soItem.discount_principal));
//        double nilai_principal = (soItem.product.price * soItem.discount_principal) / 100;
//        holder.nilai_d_principal.setText(CommonUtil.priceFormat(nilai_principal));
        holder.sub_total.setText(CommonUtil.priceFormat(soItem.sub_total));

        return view;
    }

    private class Holder {
        TextView no;
        TextView jenis_barang;
        TextView unit;
        TextView harga_list;
        TextView jumlah_order;/*
        TextView persen_d_nusantara;
        TextView nilai_d_nusantara;
        TextView persen_d_principal;
        TextView nilai_d_principal;*/
        TextView sub_total;
    }
}
