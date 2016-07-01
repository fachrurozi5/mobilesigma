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
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.model.Outlet;
import com.fachru.sigmamobile.model.SoHead;
import com.fachru.sigmamobile.model.Warehouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fachru on 31/12/15.
 */
public class AdapterSoHead extends BaseAdapter implements Filterable {

	private static LayoutInflater inflater = null;
	private Context context;
	private List<SoHead> list;
	private String[] strings;
	private List<SoHead> listFiltered;
	private ItemFilter filter = new ItemFilter();

	public AdapterSoHead(Context context, List<SoHead> list) {
		this.context = context;
		this.list = list;
		this.listFiltered = this.list;
		strings = context.getResources().getStringArray(R.array.type_of_price);
	}

	public void update(List<SoHead> list) {
		this.list = list;
		this.listFiltered = this.list;
		this.notifyDataSetChanged();
	}

	public List<SoHead> getList() {
		return this.list;
	}

	public void add(SoHead soHead) {
		this.list.add(soHead);
		this.listFiltered = this.list;
		this.notifyDataSetChanged();
	}

	public void set(SoHead soHead) {
		this.list.set(this.list.indexOf(soHead), soHead);
		this.listFiltered = this.list;
		this.notifyDataSetChanged();
	}

	public void delete(SoHead soHead) {
		this.list.remove(soHead);
		this.listFiltered = this.list;
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
			view = mInflater.inflate(R.layout.item_so_header, null);
			holder = new Holder();
			holder.no = (TextView) view.findViewById(R.id.tv_item_no);
			holder.so = (TextView) view.findViewById(R.id.tv_item_so);
//            holder.so_date = (TextView) view.findViewById(R.id.tv_item_doc_date);
//            holder.po_date = (TextView) view.findViewById(R.id.tv_item_po_date);
			holder.del_date = (TextView) view.findViewById(R.id.tv_item_del_date);
			holder.tv_customer = (TextView) view.findViewById(R.id.tv_item_customer);
			holder.tv_customers_po = (TextView) view.findViewById(R.id.tv_item_customers_po);
			holder.tv_salesman = (TextView) view.findViewById(R.id.tv_item_salesman);
			holder.tv_warehouse = (TextView) view.findViewById(R.id.tv_item_warehosue);
			holder.tv_type_of_price = (TextView) view.findViewById(R.id.tv_item_type_of_price);

			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}

		SoHead soHead = (SoHead) getItem(i);

		holder.no.setText(String.valueOf(i + 1));
		holder.so.setText(soHead.so);
//        holder.so_date.setText(soHead.getSoDate());
//        holder.po_date.setText(soHead.getPoDate());
		holder.del_date.setText(soHead.getDelDate());
		holder.tv_customer.setText(Outlet.find(soHead.custid).getName());
		holder.tv_customers_po.setText(soHead.purchase_order);
		holder.tv_salesman.setText(Employee.getEmployeeName(soHead.empid));
		holder.tv_warehouse.setText(Warehouse.getWarehouseName(soHead.whid));
		holder.tv_type_of_price.setText(strings[soHead.priceType - 1]);

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

			final List<SoHead> doHeads = new ArrayList<>();

			for (SoHead soHead : getList()) {
				if (soHead.so.toLowerCase().contains(filterString) ||
						soHead.purchase_order.toLowerCase().contains(filterString))
					doHeads.add(soHead);
			}

			results.values = doHeads;
			results.count = doHeads.size();

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			listFiltered = (List<SoHead>) results.values;
			notifyDataSetChanged();
		}
	}

	private class Holder {
		TextView no;
		TextView so;
		//        TextView so_date;
//        TextView po_date;
		TextView del_date;
		TextView tv_customer;
		TextView tv_customers_po;
		TextView tv_salesman;
		TextView tv_warehouse;
		TextView tv_type_of_price;
	}

}
