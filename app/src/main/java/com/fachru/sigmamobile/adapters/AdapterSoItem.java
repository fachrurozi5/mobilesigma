package com.fachru.sigmamobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.model.SoItem;
import com.fachru.sigmamobile.utils.CommonUtil;

import java.util.List;

/**
 * Created by fachru on 15/10/15.
 */
public class AdapterSoItem extends BaseAdapter {

	private Context context;
	private List<SoItem> list;
	private int type_price_list;

	public AdapterSoItem(Context context, List<SoItem> list, int type_price_list) {
		this.context = context;
		this.list = list;
		this.type_price_list = type_price_list;
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
	public boolean isEnabled(int position) {
		SoItem item = (SoItem) getItem(position);
		if (item.mulBonus == -1) return false;

		return super.isEnabled(position);
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		Holder holder;

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (view == null) {
			view = mInflater.inflate(R.layout.item_sales_order, null);
			holder = new Holder();
			holder.no = (TextView) view.findViewById(R.id.item_so_no);
			holder.jenis_barang = (TextView) view.findViewById(R.id.item_so_jenis_barang);
			holder.unit = (TextView) view.findViewById(R.id.item_so_unit);
			holder.persen_d_nusantara = (TextView) view.findViewById(R.id.item_so_persen_d_nusantara);
			holder.nilai_d_nusantara = (TextView) view.findViewById(R.id.item_so_nilai_d_nusantara);
			holder.persen_d_principal = (TextView) view.findViewById(R.id.item_so_persen_d_principal);
			holder.nilai_d_principal = (TextView) view.findViewById(R.id.item_so_nilai_d_principal);
			holder.harga_list = (TextView) view.findViewById(R.id.item_so_harga_list);
			holder.jumlah_order = (TextView) view.findViewById(R.id.item_so_jumlah_order);
			holder.sub_total = (TextView) view.findViewById(R.id.item_so_sub_total);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}

		SoItem soItem = (SoItem) getItem(i);
		Product product = Product.find(soItem.productId);
		holder.no.setText(String.valueOf(i + 1));
		holder.jenis_barang.setText(product.name);
		holder.unit.setText(soItem.unitId);
		holder.persen_d_nusantara.setText(CommonUtil.percentFormat(soItem.discountNst));
		double nilai_nusantara = (soItem.priceList * soItem.discountNst) / 100;
		holder.nilai_d_nusantara.setText(CommonUtil.priceFormat(nilai_nusantara));
		holder.persen_d_principal.setText(CommonUtil.percentFormat(soItem.discountPrinc));
		double nilai_principal = (soItem.priceList * soItem.discountPrinc) / 100;
		holder.nilai_d_principal.setText(CommonUtil.priceFormat(nilai_principal));
		holder.harga_list.setText(CommonUtil.priceFormat(soItem.priceList));
//        holder.jumlah_order.setText(CommonUtil.priceFormat(soItem.qty + soItem.mulBonus));
		holder.jumlah_order.setText(CommonUtil.priceFormat(soItem.qty));
		holder.sub_total.setText(CommonUtil.priceFormat(soItem.subTotal));

		if (i % 2 == 1) {
			view.setBackgroundColor(Color.parseColor("#424242"));
		} else {
			view.setBackgroundColor(Color.TRANSPARENT);
		}

		return view;
	}

	private class Holder {
		TextView no;
		TextView jenis_barang;
		TextView unit;
		TextView persen_d_nusantara;
		TextView nilai_d_nusantara;
		TextView persen_d_principal;
		TextView nilai_d_principal;
		TextView harga_list;
		TextView jumlah_order;
		TextView sub_total;
	}
}
