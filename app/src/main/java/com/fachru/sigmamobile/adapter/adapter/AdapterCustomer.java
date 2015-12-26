package com.fachru.sigmamobile.adapter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fachru on 18/12/15.
 */
public class AdapterCustomer extends RecyclerView.Adapter<AdapterCustomer.ViewHolder> implements Filterable{

    private List<Customer> list;
    private List<Customer> listFiltered;
    private ItemFilter filter = new ItemFilter();


    @Override
    public Filter getFilter() {
        return filter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_id;
        public TextView tv_name;

        public ViewHolder(View view) {
            super(view);
            tv_id = (TextView) view.findViewById(R.id.tv_customer_id);
            tv_name = (TextView) view.findViewById(R.id.tv_custom_name);
        }
    }

    public AdapterCustomer(List<Customer> list) {
        this.list = list;
        this.listFiltered = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_id.setText(listFiltered.get(position).getCustomerId());
        holder.tv_name.setText(listFiltered.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Customer> customers = new ArrayList<>();

            for (Customer customer : AdapterCustomer.this.list) {
                if (customer.getCustomerId().toLowerCase().contains(filterString) ||
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

}
