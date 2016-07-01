package com.fachru.sigmamobile.adapters;

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
import com.fachru.sigmamobile.utils.Constanta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fachru on 23/10/15.
 */
public class AdapterFilter extends BaseAdapter implements Filterable {

	private Context context;
	private List<HashMap<String, String>> datas = new ArrayList<>();
	private List<HashMap<String, String>> filtered = new ArrayList<>();
	private ItemFilter mFilter = new ItemFilter();
	private String label1, label2;

	public AdapterFilter(Context context, List<HashMap<String, String>> datas) {
		this.context = context;
		this.datas = datas;
		filtered = datas;
	}

	@Override
	public int getCount() {
		try {
			return filtered.size();
		} catch (NullPointerException e) {
			return 0;
		}
	}

	@Override
	public Object getItem(int i) {
		return filtered.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder holder;

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (view == null) {
			view = mInflater.inflate(R.layout.item_filter, null);
			holder = new ViewHolder();
			holder.simple_list_item_1 = (TextView) view.findViewById(R.id.simple_list_item_1);
			holder.simple_list_item_2 = (TextView) view.findViewById(R.id.simple_list_item_2);
	        /*holder.tv_stock = (TextView) view.findViewById(R.id.tv_stock);
            holder.tv_sell_price = (TextView) view.findViewById(R.id.tv_sell_price);*/
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		HashMap<String, String> map = (HashMap<String, String>) getItem(i);

		holder.simple_list_item_1.setText(map.get(Constanta.SIMPLE_LIST_ITEM_1));
		holder.simple_list_item_2.setText(map.get(Constanta.SIMPLE_LIST_ITEM_2));
        /*holder.tv_stock.setText(map.get(Constanta.SIMPLE_LIST_ITEM_STOCK));
        holder.tv_sell_price.setText(map.get(Constanta.SIMPLE_LIST_ITEM_PRICE));*/

		return view;
	}

	@Override
	public Filter getFilter() {
		return mFilter;
	}

	public void update(List<HashMap<String, String>> hashMapList) {
		datas = hashMapList;
		filtered = datas;
		this.notifyDataSetChanged();
	}

	private class ItemFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence charSequence) {

			String filterString = "";
			if (charSequence != null)
				filterString = charSequence.toString().toLowerCase();
			final List<HashMap<String, String>> mList = new ArrayList<>();
			for (HashMap<String, String> map : datas) {
				label1 = map.get(Constanta.SIMPLE_LIST_ITEM_2);
				label2 = map.get(Constanta.SIMPLE_LIST_ITEM_1);
				if (label1.toLowerCase().contains(filterString) ||
						label2.toLowerCase().toLowerCase().contains(filterString))
					mList.add(map);
			}

			FilterResults results = new FilterResults();
			results.values = mList;
			results.count = mList.size();

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
			filtered = (List<HashMap<String, String>>) filterResults.values;
			notifyDataSetChanged();
		}
	}

	private class ViewHolder {
		TextView simple_list_item_1;
		TextView simple_list_item_2;
        /*TextView tv_stock;
        TextView tv_sell_price;*/
	}
}
