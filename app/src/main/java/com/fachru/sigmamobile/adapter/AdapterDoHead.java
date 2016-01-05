package com.fachru.sigmamobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.utils.CommonUtil;

import java.util.List;

/**
 * Created by fachru on 31/12/15.
 */
public class AdapterDoHead extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context context;
    private List<DoHead> list;

    public AdapterDoHead(Context context, List<DoHead> list) {
        this.context = context;
        this.list = list;
    }

    public void update(List<DoHead> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public List<DoHead> getList() {
        return this.list;
    }

    public void add(DoHead doHead) {
        this.list.add(doHead);
        this.notifyDataSetChanged();
    }

    public void set(DoHead doHead) {
        this.list.set(this.list.indexOf(doHead), doHead);
        this.notifyDataSetChanged();
    }

    public void delete(DoHead doHead) {
        this.list.remove(doHead);
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
            view = mInflater.inflate(R.layout.item_pos_header, null);
            holder = new Holder();
            holder.no = (TextView) view.findViewById(R.id.tv_item_no);
            holder.doc_no = (TextView) view.findViewById(R.id.tv_item_doc_no);
            holder.doc_date = (TextView) view.findViewById(R.id.tv_item_doc_date);
            holder.rute = (TextView) view.findViewById(R.id.tv_item_rute);
            holder.tv_customer = (TextView) view.findViewById(R.id.tv_item_customer);
            holder.tv_salesman = (TextView) view.findViewById(R.id.tv_item_salesman);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        DoHead doHead = (DoHead) getItem(i);

        holder.no.setText(String.valueOf(i + 1));
        holder.doc_no.setText(doHead.doc_no);
        holder.doc_date.setText(doHead.getDocDate());
//        holder.rute.setText(doHead.rute);
        holder.tv_customer.setText(Customer.getCustomerName(doHead.custid));
        holder.tv_salesman.setText(Employee.getEmployeeName(doHead.empid));

        return view;
    }

    private class Holder {
        TextView no;
        TextView doc_no;
        TextView doc_date;
        TextView rute;
        TextView tv_customer;
        TextView tv_salesman;
    }

}