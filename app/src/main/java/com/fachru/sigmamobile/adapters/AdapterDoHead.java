package com.fachru.sigmamobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.model.Warehouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fachru on 31/12/15.
 */
public class AdapterDoHead extends BaseAdapter implements Filterable {

	private static LayoutInflater inflater = null;
	private Context context;
	private List<DoHead> list;
	private List<DoHead> listFiltered;
	private ItemFilter filter = new ItemFilter();

	public AdapterDoHead(Context context, List<DoHead> list) {
		this.context = context;
		this.list = list;
		this.listFiltered = this.list;
	}

	public void update(List<DoHead> list) {
		this.list = list;
		this.listFiltered = this.list;
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
		return listFiltered.size();
	}

	@Override
	public Object getItem(int i) {
		return listFiltered.get(i);
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
			holder.invoice = (TextView) view.findViewById(R.id.tv_item_invoice);
			holder.tv_warehouse = (TextView) view.findViewById(R.id.tv_item_warehosue);
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
		holder.invoice.setText(doHead.vatno);
		holder.tv_warehouse.setText(Warehouse.getWarehouseName(doHead.whid));
		holder.tv_customer.setText(Customer.getCustomerName(doHead.custid));
		holder.tv_salesman.setText(Employee.getEmployeeName(doHead.empid));

		if (i % 2 == 1) {
			view.setBackgroundColor(Color.parseColor("#424242"));
		} else {
			view.setBackgroundColor(Color.TRANSPARENT);
		}

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

			final List<DoHead> doHeads = new ArrayList<>();

			for (DoHead doHead : getList()) {
				if (doHead.doc_no.toLowerCase().contains(filterString) ||
						doHead.vatno.toLowerCase().contains(filterString))
					doHeads.add(doHead);
			}

			results.values = doHeads;
			results.count = doHeads.size();

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			listFiltered = (List<DoHead>) results.values;
			notifyDataSetChanged();
		}
	}

	private class Holder {
		TextView no;
		TextView doc_no;
		TextView doc_date;
		TextView invoice;
		TextView tv_warehouse;
		TextView tv_customer;
		TextView tv_salesman;
	}

}
