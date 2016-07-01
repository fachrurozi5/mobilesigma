package com.fachru.sigmamobile.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fachru.sigmamobile.CustomerActivity;
import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.adapters.AdapterSoHead;
import com.fachru.sigmamobile.fragment.interfaces.OnSetSoHeadListener;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.model.SoHead;
import com.fachru.sigmamobile.utils.BaseFragmentForm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fachru on 18/05/16.
 */
public class HeaderReturns extends BaseFragmentForm implements AdapterView.OnItemLongClickListener {

	private Activity activity;
	private Bundle bundle;

	private View view;
	private ListView lv_so_head_items;

	private AdapterSoHead adapterSoHead;

	private List<SoHead> soHeads = new ArrayList<>();

	private Customer customer;
	private SoHead soHead;

	private OnSetSoHeadListener mListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		bundle = getArguments();
		customer = Customer.load(Customer.class, bundle.getLong(CustomerActivity.CUSTID));

		soHeads = SoHead.getAllWhereCust(customer.custid);
		adapterSoHead = new AdapterSoHead(activity, soHeads);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_header_returns, container, false);

		initComponent();

		lv_so_head_items.setOnItemLongClickListener(this);
		lv_so_head_items.setAdapter(adapterSoHead);

		return view;
	}

	@Override
	protected void actionAdd() {

	}

	@Override
	protected void actionEdit() {

	}

	@Override
	protected void actionUpdate() {

	}

	@Override
	protected void actionDelete() {

	}

	@Override
	protected void initListener() {

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		soHead = (SoHead) parent.getItemAtPosition(position);
		mListener.onSetSoHead(soHead);
		return false;
	}

	private void initComponent() {
		lv_so_head_items = (ListView) view.findViewById(R.id.lv_so_head_items);
	}

	public void setOnSoHeadListener(OnSetSoHeadListener mListener) {
		this.mListener = mListener;
	}
}
