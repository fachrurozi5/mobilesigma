package com.fachru.sigmamobile.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.adapters.AdapterReturns;
import com.fachru.sigmamobile.model.SoHead;
import com.fachru.sigmamobile.model.SoItem;
import com.fachru.sigmamobile.utils.BaseFragmentForm;
import com.fachru.sigmamobile.utils.Constanta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fachru on 18/05/16.
 */
public class ReturnsFragment extends BaseFragmentForm {

	protected Activity activity;
	protected Bundle bundle;
	protected InputMethodManager imm;

	protected View view;
	protected RelativeLayout layout;
	protected EditText et_product;
	protected EditText et_pcs;
	protected EditText et_cart;
	protected EditText et_ket;
	protected Button btn_add;
	protected Button btn_edit;
	protected Button btn_del;
	protected RadioButton rb_ambil;
	protected RadioButton rb_proses;
	protected ListView lv_returns_product;

	private AdapterReturns adapterReturns;

	private List<SoItem> soItems = new ArrayList<>();

	private SoHead soHead;

	private int price_type = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		bundle = getArguments();

		if (bundle != null) {
			soHead = SoHead.find(bundle.getString(Constanta.KEY_SO));
			price_type = soHead.priceType;
			soItems = soHead.soItems();
		}

		adapterReturns = new AdapterReturns(activity, soItems, price_type);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_returns, container, false);

		initComponent();

		lv_returns_product.setAdapter(adapterReturns);

		disableButton(layout);
		disableForm(layout);

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

	private void initComponent() {
		layout = (RelativeLayout) view.findViewById(R.id.layout_fragment_returns);
		et_product = (EditText) view.findViewById(R.id.act_product);
		et_pcs = (EditText) view.findViewById(R.id.et_qty);
		et_cart = (EditText) view.findViewById(R.id.et_unit);
		et_ket = (EditText) view.findViewById(R.id.et_explanation);
		btn_add = (Button) view.findViewById(R.id.btn_add);
		btn_edit = (Button) view.findViewById(R.id.btn_edit);
		btn_del = (Button) view.findViewById(R.id.btn_delete);
		rb_ambil = (RadioButton) view.findViewById(R.id.rb_ambil);
		rb_proses = (RadioButton) view.findViewById(R.id.rb_proses);
		lv_returns_product = (ListView) view.findViewById(R.id.lv_returns_product);
	}
}
