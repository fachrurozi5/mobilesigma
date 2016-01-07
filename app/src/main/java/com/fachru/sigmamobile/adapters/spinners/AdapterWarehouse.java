package com.fachru.sigmamobile.adapters.spinners;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.Warehouse;

import java.util.List;

/**
 * Created by fachru on 07/01/16.
 */
public class AdapterWarehouse extends BaseAdapter {

    private Context context;
    private List<Warehouse> list;

    public AdapterWarehouse(Context context, List<Warehouse> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_customer, null);
            holder = new Holder();
        } else {

        }

        return null;
    }

    private class Holder {
        TextView textView1;
        TextView textView2;
    }
}
