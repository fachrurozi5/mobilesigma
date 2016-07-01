package com.fachru.sigmamobile.adapters.recycle;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.Outlet;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.model.WarehouseStock;
import com.fachru.sigmamobile.utils.CommonUtil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by fachru on 03/06/16.
 */
public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ViewHolder> {

	private OnItemClickListener listener;
	private List<Product> products;
	private String warehouseId;


	public AdapterProduct(List<Product> products, String warehouseId, OnItemClickListener listener) {
		this.products = products;
		this.listener = listener;
		this.warehouseId = warehouseId;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_products, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Product product = products.get(position);

		WarehouseStock warehouseStock = WarehouseStock.find(warehouseId, product.prodid);

		String productId = product.prodid;
		String productName = product.name;
		String productPrice = CommonUtil.priceFormat2Decimal(product.sellPrice);
		String productStock = "null";
		if (warehouseStock != null)
			productStock = new DecimalFormat("#,##0.0000").format((warehouseStock.balance));

		holder.textViewProductId.setText(productId);
		holder.textViewProductName.setText(productName);
		holder.textViewProductPrice.setText(productPrice);
		holder.textViewProductStock.setText(productStock);

	}

	@Override
	public int getItemCount() {
		return products.size();
	}

	public interface OnItemClickListener {
		void itemClick(Product product);
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public TextView textViewProductId;
		public TextView textViewProductName;
		public TextView textViewProductPrice;
		public TextView textViewProductStock;

		public ViewHolder(View itemView) {
			super(itemView);
			textViewProductId = (TextView) itemView.findViewById(R.id.simple_list_item_1);
			textViewProductName = (TextView) itemView.findViewById(R.id.simple_list_item_2);
			textViewProductPrice = (TextView) itemView.findViewById(R.id.tv_sell_price);
			textViewProductStock = (TextView) itemView.findViewById(R.id.tv_stock);


			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.itemClick(products.get(getLayoutPosition()));
				}
			});
		}
	}

}
