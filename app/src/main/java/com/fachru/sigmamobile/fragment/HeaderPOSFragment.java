package com.fachru.sigmamobile.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.Login;
import com.fachru.sigmamobile.OutletActivity;
import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.adapters.AdapterDoHead;
import com.fachru.sigmamobile.fragment.interfaces.OnSetDoHeadListener;
import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.model.Outlet;
import com.fachru.sigmamobile.model.Warehouse;
import com.fachru.sigmamobile.utils.BaseFragmentForm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HeaderPOSFragment extends BaseFragmentForm implements
		OnClickListener {

	protected Activity activity;
	protected Bundle bundle;
	protected InputMethodManager imm;

	/*
	* widget
	* */
	protected View view;
	protected RelativeLayout layout;
	protected EditText et_doc_no;
	protected EditText et_invoice;
	protected EditText et_salesman;
	protected EditText et_customer;
	protected Spinner sp_warehause;
	protected Button btn_add;
	protected Button btn_edit;
	protected Button btn_del;
	protected ListView lv_do_head_items;


	/*
	* mListener
	* */
	protected OnSetDoHeadListener mListener;
	protected OnItemLongClickListener onDoHeadLongClicked;
	protected OnItemSelectedListener onWarehouseSelected;


	/*
	* plain old data object
	* */
	protected DoHead doHead;
	//    protected Customer customer;
	protected Outlet outlet;
	protected Employee employee;
	protected Warehouse warehouse;

	/*
	* list of object
	* */
	protected List<DoHead> doHeads = new ArrayList<>();
	/*
	* label
	* */
	protected boolean isUpdate = false;
	protected boolean isReadyAddItem = false;
	private List<Warehouse> warehouseList = new ArrayList<>();
	/*
	* custom adapter
	* */
	private AdapterDoHead adapterDoHead;
	private ArrayAdapter<Warehouse> adapterWarehouse;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		bundle = getArguments();
//        customer = Customer.load(Customer.class, bundle.getLong(CustomerActivity.CUSTID));
		outlet = Outlet.load(Outlet.class, bundle.getLong(OutletActivity.OUTLET_ID));
		employee = Employee.load(Employee.class, bundle.getLong(Login.EMPLID));

//        doHeads = DoHead.getAllWhereCustomer(customer.custid);
		doHeads = DoHead.getAllWhereCustomer(outlet.getOutletId());
		warehouseList = Warehouse.all();

		adapterDoHead = new AdapterDoHead(getContext(), doHeads);
//        adapterWarehouse = new AdapterWarehouse(activity, Warehouse.all());
		adapterWarehouse = new ArrayAdapter<Warehouse>(activity,
				android.R.layout.simple_spinner_item,
				android.R.id.text1,
				warehouseList) {

			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent) {

				LayoutInflater mInflater = (LayoutInflater) parent.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

				convertView = mInflater.inflate(R.layout.item_sp_warehouse, parent, false);

				TextView textView1 = (TextView) convertView.findViewById(R.id.textview1);
				TextView textView2 = (TextView) convertView.findViewById(R.id.textview2);

				Warehouse warehouse = getItem(position);
				String label = warehouse.whid + " - " + warehouse.name;
				textView1.setText(label);
				if (warehouse.remarks == null || warehouse.remarks.trim().equals("")) {
					textView2.setText("-");
				} else {
					textView2.setText(warehouse.remarks);
				}

				return convertView;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text1 = (TextView) view.findViewById(android.R.id.text1);
				Warehouse warehouse = getItem(position);
				String label = warehouse.whid + " - " + warehouse.name;
				text1.setText(label);
				return view;
			}
		};

		adapterWarehouse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_header_pos, container, false);
		initComp();
		initListener();

		et_doc_no.setText(DoHead.generateId(employee.getEmployeeId()));

		lv_do_head_items.setOnItemLongClickListener(onDoHeadLongClicked);
		sp_warehause.setOnItemSelectedListener(onWarehouseSelected);
		btn_add.setOnClickListener(this);
		btn_edit.setOnClickListener(this);
		btn_del.setOnClickListener(this);

		lv_do_head_items.setAdapter(adapterDoHead);
		sp_warehause.setAdapter(adapterWarehouse);

//        et_customer.setText(customer.name);
		et_customer.setText(outlet.getName());
		et_salesman.setText(employee.getName());

		setButtonEnable(btn_add);

		return view;
	}

	private void initComp() {
		layout = (RelativeLayout) view.findViewById(R.id.vg_fragment_header_pos);
		et_doc_no = (EditText) view.findViewById(R.id.et_doc_no);
		et_invoice = (EditText) view.findViewById(R.id.et_invoice);
		et_salesman = (EditText) view.findViewById(R.id.et_salesman);
		et_customer = (EditText) view.findViewById(R.id.et_customer);
		sp_warehause = (Spinner) view.findViewById(R.id.sp_warehouse);
		btn_add = (Button) view.findViewById(R.id.btn_add);
		btn_edit = (Button) view.findViewById(R.id.btn_edit);
		btn_del = (Button) view.findViewById(R.id.btn_delete);
		lv_do_head_items = (ListView) view.findViewById(R.id.lv_do_head_items);
	}

	@Override
	public void onPause() {
		super.onPause();
		mListener.unSetDoHead();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_add:
				if (isReadyAddItem) {
					actionAddItem();
				} else {
					actionAdd();
				}
				break;
			case R.id.btn_edit:
				if (isUpdate) {
					actionUpdate();
				} else {
					actionEdit();
				}
				break;
			case R.id.btn_delete:
				if (isUpdate) {
					onCancelOrAfterEdit();
					clearForm(layout);
				} else {
					actionDelete();
				}
				enableForm(layout);
				setButtonEnable(btn_add);
				break;
			default:
				break;
		}

	}

	public void setOnSetDoHeadListener(OnSetDoHeadListener listener) {
		this.mListener = listener;
	}

	private void editDoHead(DoHead doHead) {
//        customer = Customer.getCustomer(doHead.custid);
		outlet = Outlet.find(doHead.custid);
		employee = Employee.getEmployee(doHead.empid);
		warehouse = Warehouse.getWarehouse(doHead.whid);

		et_doc_no.setText(doHead.doc_no);
		et_invoice.setText(doHead.vatno);
		int pos = adapterWarehouse.getPosition(warehouse);
		sp_warehause.setSelection(pos);
//        et_customer.setText(customer.name);
		et_customer.setText(outlet.getName());
		et_salesman.setText(employee.getName());
		disableForm(layout);
	}

	@Override
	protected void actionAdd() {
		if (!errorChecked()) {
			doHead = new DoHead.Builder()
					.setDocNo(et_doc_no.getText().toString())
					.setVatno(et_invoice.getText().toString())
					.setDocDate(new Date())
					.setCustid(et_customer.getText().toString())
//                    .setCustid(customer.custid)
					.setCustid(outlet.getOutletId())
					.setEmpid(employee.getEmployeeId())
					.setWhid(warehouse.whid)
					.build();
			long status = doHead.save();
			if (status != -1) {
				adapterDoHead.add(doHead);
				clearForm(layout);
			} else {
				et_doc_no.setError(getString(R.string.error_input_doc_no_duplicate));
				et_doc_no.requestFocus();
			}
		}
	}

	@Override
	protected void actionEdit() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			btn_edit.setBackground(getResources().getDrawable(R.drawable.button_save, activity.getTheme()));
			btn_del.setBackground(getResources().getDrawable(R.drawable.button_close, activity.getTheme()));
		} else {
			btn_edit.setBackground(getResources().getDrawable(R.drawable.button_save));
			btn_del.setBackground(getResources().getDrawable(R.drawable.button_close));
		}
		isUpdate = true;
		enableForm(layout);
		setButtonDisable(btn_add);
	}

	@Override
	protected void actionUpdate() {
		if (!errorChecked()) {
			doHead.vatno = et_invoice.getText().toString();
//            doHead.custid = customer.custid;
			doHead.custid = outlet.getOutletId();
			doHead.empid = employee.getEmployeeId();
			doHead.whid = warehouse.whid;
			doHead.save();

			adapterDoHead.set(doHead);
			onCancelOrAfterEdit();
			setButtonEnable(btn_add);
			clearForm(layout);
		}
	}

	@Override
	protected void actionDelete() {
		new MaterialDialog.Builder(activity)
				.content(doHead.doc_no + " akan dihapus dari dohead ?")
				.positiveText(R.string.agree)
				.negativeText(R.string.disagree)
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
						adapterDoHead.delete(doHead);
						doHead.delete();
						clearForm(layout);
						onCancelOrAfterEdit();
					}
				})
				.onNegative(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
						clearForm(layout);
						onCancelOrAfterEdit();
					}
				})
				.show();
	}

	@Override
	protected void initListener() {

		onDoHeadLongClicked = new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				doHead = (DoHead) parent.getItemAtPosition(position);
				editDoHead(doHead);
				enableButton(layout);
				return false;
			}
		};

		onWarehouseSelected = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				warehouse = (Warehouse) parent.getItemAtPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	}

	private void onCancelOrAfterEdit() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			btn_add.setBackground(getResources().getDrawable(R.drawable.button_add, activity.getTheme()));
			btn_edit.setBackground(getResources().getDrawable(R.drawable.button_edit, activity.getTheme()));
			btn_del.setBackground(getResources().getDrawable(R.drawable.button_delete, activity.getTheme()));
		} else {
			btn_add.setBackground(getResources().getDrawable(R.drawable.button_add));
			btn_edit.setBackground(getResources().getDrawable(R.drawable.button_edit));
			btn_del.setBackground(getResources().getDrawable(R.drawable.button_delete));
		}
		isUpdate = false;
		isReadyAddItem = false;
	}

	@Override
	protected void enableForm(ViewGroup group) {
		super.enableForm(group);
		et_doc_no.setEnabled(!isUpdate);

	}

	private boolean errorChecked() {
		if (et_doc_no.getText().toString().equals("") ||
				et_doc_no.getText() == null) {
			et_doc_no.setError(getString(R.string.error_input_doc_no));
			return true;
		} else if (et_invoice.getText().toString().equals("") ||
				et_invoice.getText() == null) {
			et_invoice.setError("INVOICE TIDAK BOLEH KOSONG");
			return true;
		} else {
			return false;
		}
	}


	private void actionAddItem() {
		mListener.onSetDoHead(doHead);
		clearForm(layout);
	}

	@Override
	protected void clearForm(ViewGroup group) {
		super.clearForm(group);
		et_doc_no.setEnabled(true);
		et_doc_no.setText(DoHead.generateId(employee.getEmployeeId()));
//        et_customer.setText(customer.name);
		et_customer.setText(outlet.getName());
		et_salesman.setText(employee.getEmployeeId());
		et_doc_no.requestFocus();
		imm.hideSoftInputFromWindow(et_doc_no.getWindowToken(), 0);
	}

	@Override
	protected void enableButton(ViewGroup group) {
		super.enableButton(group);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			btn_add.setBackground(getResources().getDrawable(R.drawable.button_add_item, activity.getTheme()));
		} else {
			btn_add.setBackground(getResources().getDrawable(R.drawable.button_add_item));
		}
		isReadyAddItem = true;
	}

	private void setButtonEnable(Button button) {
		btn_add.setEnabled(false);
		btn_edit.setEnabled(false);
		btn_del.setEnabled(false);

		button.setEnabled(true);
	}

	private void setButtonDisable(Button button) {
		btn_add.setEnabled(true);
		btn_edit.setEnabled(true);
		btn_del.setEnabled(true);

		button.setEnabled(false);
	}

}
