package com.fachru.sigmamobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.model.SoItem;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;

import java.util.List;

/**
 * Created by fachru on 17/11/15.
 */
public class AdapterReturns extends BaseAdapter {

	private Context context;
	private List<SoItem> list;
	private int type_price_list;


	public AdapterReturns(Context context, List<SoItem> list, int type_price_list) {
		this.context = context;
		this.list = list;
		this.type_price_list = type_price_list;
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
	public View getView(int position, View view, ViewGroup parent) {

		Holder holder;

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (view == null) {
			view = mInflater.inflate(R.layout.item_retur, null);
			holder = new Holder();
			holder.item_no = (TextView) view.findViewById(R.id.item_no);
			holder.item_product = (TextView) view.findViewById(R.id.item_product);
			holder.item_qty = (TextView) view.findViewById(R.id.item_qty);
			holder.item_unit = (TextView) view.findViewById(R.id.item_unit);
			holder.item_r_ket = (TextView) view.findViewById(R.id.item_ket);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}

		SoItem soItem = (SoItem) getItem(position);
		Log.e(Constanta.TAG, soItem.toString());
		Product product = Product.find(soItem.productId);

		holder.item_no.setText(String.valueOf(position + 1));
		holder.item_product.setText(product.name);
		holder.item_qty.setText(CommonUtil.priceFormat(soItem.qty));
		holder.item_unit.setText(soItem.unitId);

		return view;
	}

	public void update(List<SoItem> list, int type_price_list) {
		this.list = list;
		this.type_price_list = type_price_list;
		this.notifyDataSetChanged();
	}

	public List<SoItem> getList() {
		return this.list;
	}

	public void add(SoItem soItem) {
		this.list.add(soItem);
		this.notifyDataSetChanged();
	}

	public void set(SoItem soItem) {
		this.list.set(this.list.indexOf(soItem), soItem);
		this.notifyDataSetChanged();
	}

	public void delete(SoItem soItem) {
		this.list.remove(soItem);
		this.notifyDataSetChanged();
	}

	public void clearAll() {
		this.list.clear();
		this.notifyDataSetChanged();
	}

	private class Holder {
		TextView item_no;
		TextView item_product;
		TextView item_qty;
		TextView item_unit;
		TextView item_r_ket;
	}
}
