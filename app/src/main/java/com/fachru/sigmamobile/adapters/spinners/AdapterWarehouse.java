package com.fachru.sigmamobile.adapters.spinners;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.Warehouse;
import com.fachru.sigmamobile.utils.CommonUtil;

import java.util.List;

/**
 * Created by fachru on 07/01/16.
 */
public class AdapterWarehouse extends ArrayAdapter<Warehouse> {

    private Context context;
    private List<Warehouse> list;

    public AdapterWarehouse(Context context, List<Warehouse> list) {
        super(context, android.R.layout.simple_spinner_item, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Warehouse getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = getCustomView(position, convertView, parent);

        Holder holder = (Holder) convertView.getTag();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(
                CommonUtil.getPx(context, 8),
                CommonUtil.getPx(context, 4),
                CommonUtil.getPx(context, 8),
                0);
        holder.view.setLayoutParams(params);
        holder.view.setMinimumHeight(CommonUtil.getPx(context, 1));
        holder.textView1.setLayoutParams(params);
        holder.textView2.setLayoutParams(params);

        return convertView;
    }

    

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return  getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_sp_warehouse, parent, false);
            holder = new Holder();
            holder.textView1 = (TextView) convertView.findViewById(R.id.textview1);
            holder.textView2 = (TextView) convertView.findViewById(R.id.textview2);
            holder.view = convertView.findViewById(R.id.view);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Warehouse warehouse = getItem(position);
        String label = warehouse.whid + " - " + warehouse.name;
        holder.textView1.setText(label);
        if (warehouse.remarks == null || warehouse.remarks.trim().equals("")) {
            holder.textView2.setText("-");
        } else {
            holder.textView2.setText(warehouse.remarks);
        }

        return convertView;
    }

    private class Holder {
        TextView textView1;
        TextView textView2;
        View view;
    }
}
