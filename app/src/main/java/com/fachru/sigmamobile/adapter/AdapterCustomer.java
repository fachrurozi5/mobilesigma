package com.fachru.sigmamobile.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fachru.sigmamobile.model.Customer;

import java.util.List;

/**
 * Created by fachru on 21/12/15.
 */
public class AdapterCustomer extends BaseAdapter{

    private List<Customer> list;

    public AdapterCustomer(List<Customer> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
