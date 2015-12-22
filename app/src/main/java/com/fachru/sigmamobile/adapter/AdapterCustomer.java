package com.fachru.sigmamobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fachru on 21/12/15.
 */
public class AdapterCustomer extends BaseAdapter implements Filterable{

    private Context context;
    private List<Customer> listFiltered;
    private List<Customer> original;
    private ItemFilter filter = new ItemFilter();

    public AdapterCustomer(Context context, List<Customer> list) {
        this.context = context;
        original = list;
        listFiltered = original;
    }

    public void update(List<Customer> list) {
        original = list;
        listFiltered = original;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        return listFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Holder holder;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = mInflater.inflate(R.layout.item_customer, null);
            holder = new Holder();
//            holder.tv_no = (TextView) view.findViewById(R.id.tv_customer_no);
            holder.tv_id = (TextView) view.findViewById(R.id.tv_customer_id);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_custom_name);
            holder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

//        holder.tv_no.setText(String.valueOf(position+1));
        holder.tv_id.setText(listFiltered.get(position).getId());
        holder.tv_name.setText(listFiltered.get(position).getName());
        holder.tv_phone.setText(listFiltered.get(position).getPhone());

        return view;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Customer> customers = new ArrayList<>();

            for (Customer customer : AdapterCustomer.this.original) {
                if (customer.getId().toLowerCase().contains(filterString) ||
                        customer.getName().toLowerCase().contains(filterString))
                    customers.add(customer);
            }

            results.values = customers;
            results.count = customers.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listFiltered = (List<Customer>) results.values;
            notifyDataSetChanged();
        }
    }

    private class Holder {
//        public TextView tv_no;
        public TextView tv_id;
        public TextView tv_name;
        public TextView tv_phone;
    }
}
