package com.fachru.sigmamobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.model.DoItem;
import com.fachru.sigmamobile.utils.CommonUtil;

import java.util.List;

/**
 * Created by fachru on 15/10/15.
 */
public class AdapterDoItem extends BaseAdapter {

    private Context context;
    private List<DoItem> list;

    public AdapterDoItem(Context context, List<DoItem> list) {
        this.context = context;
        this.list = list;
    }

    public void update(List<DoItem> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public List<DoItem> getList() {
        return this.list;
    }

    public void add(DoItem doItem) {
        this.list.add(doItem);
        this.notifyDataSetChanged();
    }

    public void set(DoItem doItem) {
        this.list.set(this.list.indexOf(doItem), doItem);
        this.notifyDataSetChanged();
    }

    public void delete(DoItem doItem) {
        this.list.remove(doItem);
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
            holder.harga_list = (TextView) view.findViewById(R.id.item_so_harga_list);
            holder.jumlah_order = (TextView) view.findViewById(R.id.item_so_jumlah_order);
            holder.persen_d_nusantara = (TextView) view.findViewById(R.id.item_so_persen_d_nusantara);
            holder.nilai_d_nusantara = (TextView) view.findViewById(R.id.item_so_nilai_d_nusantara);
            holder.persen_d_principal = (TextView) view.findViewById(R.id.item_so_persen_d_principal);
            holder.nilai_d_principal = (TextView) view.findViewById(R.id.item_so_nilai_d_principal);
            holder.sub_total = (TextView) view.findViewById(R.id.item_so_sub_total);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        DoItem doItem = (DoItem) getItem(i);

        holder.no.setText(String.valueOf(i + 1));
        holder.jenis_barang.setText(doItem.product.product_name);
        holder.harga_list.setText(CommonUtil.priceFormat(doItem.product.price));
        holder.jumlah_order.setText(CommonUtil.priceFormat(doItem.jumlah_order));
        holder.persen_d_nusantara.setText(CommonUtil.percentFormat(doItem.discount_nusantara));
        double nilai_nusantara = (doItem.product.price * doItem.discount_nusantara) / 100;
        holder.nilai_d_nusantara.setText(CommonUtil.priceFormat(nilai_nusantara));
        holder.persen_d_principal.setText(CommonUtil.percentFormat(doItem.discount_principal));
        double nilai_principal = (doItem.product.price * doItem.discount_principal) / 100;
        holder.nilai_d_principal.setText(CommonUtil.priceFormat(nilai_principal));
        holder.sub_total.setText(CommonUtil.priceFormat(doItem.sub_total));

        return view;
    }

    private class Holder {
        TextView no;
        TextView jenis_barang;
        TextView harga_list;
        TextView jumlah_order;
        TextView persen_d_nusantara;
        TextView nilai_d_nusantara;
        TextView persen_d_principal;
        TextView nilai_d_principal;
        TextView sub_total;
    }
}
