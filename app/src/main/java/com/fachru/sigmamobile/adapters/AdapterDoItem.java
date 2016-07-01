package com.fachru.sigmamobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.DoItem;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.utils.CommonUtil;

import java.util.List;

/**
 * Created by fachru on 15/10/15.
 */
public class AdapterDoItem extends BaseAdapter {

	private Context context;
	private List<DoItem> list;

	public AdapterDoItem(Context context, List<DoItem> list) {
		this.context = context;
		this.list = list;
	}

	public void update(List<DoItem> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	public List<DoItem> getList() {
		return this.list;
	}

	public void add(DoItem doItem) {
		this.list.add(doItem);
		this.notifyDataSetChanged();
	}

	public void set(DoItem doItem) {
		this.list.set(this.list.indexOf(doItem), doItem);
		this.notifyDataSetChanged();
	}

	public void delete(DoItem doItem) {
		this.list.remove(doItem);
		this.notifyDataSetChanged();
	}

	public void clearAll() {
		this.list.clear();
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
			view = mInflater.inflate(R.layout.item_point_of_sale, null);
			holder = new Holder();
			holder.linearLayout = (LinearLayout) view.findViewById(R.id.layout_item_pos);
			holder.no = (TextView) view.findViewById(R.id.item_so_no);
			holder.jenis_barang = (TextView) view.findViewById(R.id.item_so_jenis_barang);
			holder.unit = (TextView) view.findViewById(R.id.item_so_unit);
			holder.harga_list = (TextView) view.findViewById(R.id.item_so_harga_list);
			holder.jumlah_order = (TextView) view.findViewById(R.id.item_so_jumlah_order);
			holder.persen_d_nusantara = (TextView) view.findViewById(R.id.item_so_persen_d_nusantara);
			holder.nilai_d_nusantara = (TextView) view.findViewById(R.id.item_so_nilai_d_nusantara);
			holder.persen_d_principal = (TextView) view.findViewById(R.id.item_so_persen_d_principal);
			holder.nilai_d_principal = (TextView) view.findViewById(R.id.item_so_nilai_d_principal);
			holder.sub_total = (TextView) view.findViewById(R.id.item_so_sub_total);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}

		DoItem doItem = (DoItem) getItem(i);
		Product product = Product.find(doItem.product_id);

		holder.no.setText(String.valueOf(i + 1));
		holder.jenis_barang.setText(product.name);
		holder.unit.setText(doItem.unit_id);
		holder.harga_list.setText(CommonUtil.priceFormat(doItem.pricelist));
		holder.jumlah_order.setText(CommonUtil.priceFormat(doItem.qty));
		holder.persen_d_nusantara.setText(CommonUtil.percentFormat(doItem.discountNst));
		double nilai_nusantara = (doItem.pricelist * doItem.discountNst) / 100;
		holder.nilai_d_nusantara.setText(CommonUtil.priceFormat(nilai_nusantara));
		holder.persen_d_principal.setText(CommonUtil.percentFormat(doItem.discountPrinc));
		double nilai_principal = (doItem.pricelist * doItem.discountPrinc) / 100;
		holder.nilai_d_principal.setText(CommonUtil.priceFormat(nilai_principal));
		holder.sub_total.setText(CommonUtil.priceFormat(doItem.sub_total));

		if (i % 2 == 1) {
			view.setBackgroundColor(Color.parseColor("#424242"));
		} else {
			view.setBackgroundColor(Color.TRANSPARENT);
		}

		return view;
	}

	private class Holder {
		LinearLayout linearLayout;
		TextView no;
		TextView jenis_barang;
		TextView unit;
		TextView harga_list;
		TextView jumlah_order;
		TextView persen_d_nusantara;
		TextView nilai_d_nusantara;
		TextView persen_d_principal;
		TextView nilai_d_principal;
		TextView sub_total;
	}
}
