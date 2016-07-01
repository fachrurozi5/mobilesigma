package com.fachru.sigmamobile.adapters.recycle;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.Outlet;

import java.util.List;

/**
 * Created by fachru on 03/06/16.
 */
public class AdapterOutlet extends RecyclerView.Adapter<AdapterOutlet.ViewHolder> {

	private final OnItemClickListener listener;
	private List<Outlet> outlets;

	public AdapterOutlet(List<Outlet> outletList, OnItemClickListener listener) {
		this.outlets = outletList;
		this.listener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_outlet, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Outlet outlet = outlets.get(position);

		String nameAndId = outlet.getNameAndId();
		String address = outlet.getAddress();

		holder.tv_outlet_name.setText(nameAndId);
		holder.tv_outlet_address.setText(address);

	}

	@Override
	public int getItemCount() {
		return outlets.size();
	}

	public interface OnItemClickListener {
		void onChooseClick(Outlet outlet);

		void onDetailClick(Outlet outlet);
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public TextView tv_outlet_name;
		public TextView tv_outlet_address;
		public Button btn_pilih;
		public Button btn_detail;

		public ViewHolder(View itemView) {
			super(itemView);
			tv_outlet_name = (TextView) itemView.findViewById(R.id.tv_outlet_id);
			tv_outlet_address = (TextView) itemView.findViewById(R.id.tv_outlet_address);
			btn_detail = (Button) itemView.findViewById(R.id.btn_detail);
			btn_pilih = (Button) itemView.findViewById(R.id.btn_pilih);

			btn_pilih.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onChooseClick(outlets.get(getLayoutPosition()));
				}
			});

			btn_detail.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onDetailClick(outlets.get(getLayoutPosition()));
				}
			});
		}
	}

}
