package com.fachru.sigmamobile.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.adapters.AdapterDoItem;
import com.fachru.sigmamobile.adapters.AdapterFilterProduct;
import com.fachru.sigmamobile.model.Discount;
import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.model.DoItem;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.model.UnitConverter;
import com.fachru.sigmamobile.model.WarehouseStock;
import com.fachru.sigmamobile.utils.BaseFragmentForm;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by fachru on 28/10/15.
 */
public class PointOfSaleFragment extends BaseFragmentForm implements OnClickListener {

	protected Activity activity;
	protected Bundle bundle;
	protected InputMethodManager imm;
	protected OnSetDoItemListener listener;

	/*
	* widget
	* */
	protected View view;
	protected RelativeLayout layout;
	protected AutoCompleteTextView act_product;
	protected Spinner sp_unit_conv;
	protected EditText et_product_price;
	protected EditText et_qty;
	/*protected EditText et_disc_nusantara;
	protected EditText et_disc_nusantara_value;
	protected EditText et_disc_principal;
	protected EditText et_disc_principal_value;*/
	protected EditText et_sub_total;
	protected EditText et_total;
	protected Button btn_add;
	protected Button btn_edit;
	protected Button btn_del;
	protected ListView lv_do_items;

	/*
	* plain old java object
	* */
	protected DoHead doHead;
	protected DoItem doItem;
	protected Product product;
	protected UnitConverter unitConverter;

	/*
	* list of object
	* */
	protected List<DoItem> doItems;
	protected List<String> unitConverters = new ArrayList<>();
	protected List<HashMap<String, String>> hashMapList;

	/*
	* custom adapter
	* */
	protected AdapterFilterProduct productFilter;
	protected AdapterDoItem adapterDoItem;
	protected ArrayAdapter<String> unitConverterArrayAdapter;

	/*
	* label
	* */
	private int QTY;
	private double total = 0;
	private double sub_total = 0;
	private double price;
	private boolean isUpdate = false;
	private String unit_conversion;
	/*private long nusantara_value;
	private long principal_value;
	private double disc_nusantara;
	private double disc_principal;*/
	private double[] discount;
	/*
	* mListener
	* */
	private OnItemClickListener onActProductItemClicked;
	private OnItemLongClickListener onDoItemLongClicked;
	private OnItemSelectedListener onUnitConvSelected;
	private TextWatcher qtyWatcher;
	private TextWatcher productWatcher;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		bundle = getArguments();
		doItems = new ArrayList<>();
		hashMapList = new ArrayList<>();

		adapterDoItem = new AdapterDoItem(activity, doItems);
		unitConverterArrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, unitConverters);
		unitConverterArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		productFilter = new AdapterFilterProduct(activity.getApplicationContext(), hashMapList);
		imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_point_of_sale, container, false);
		iniComp();
		initListener();

		act_product.addTextChangedListener(productWatcher);
		et_qty.addTextChangedListener(qtyWatcher);

		act_product.setOnItemClickListener(onActProductItemClicked);
		sp_unit_conv.setOnItemSelectedListener(onUnitConvSelected);
		lv_do_items.setOnItemLongClickListener(onDoItemLongClicked);

		btn_add.setOnClickListener(this);
		btn_edit.setOnClickListener(this);
		btn_del.setOnClickListener(this);

		disableButton(layout);
		disableForm(layout);

		if (bundle != null) {
			doHead = DoHead.find(bundle.getString(Constanta.KEY_DOC_NO));
			doItems = doHead.doItems();
//            hashMapList = WarehouseStock.toListHashMap(doHead.whid);
			hashMapList = Product.toListHashMap();
			adapterDoItem.update(doItems);
			productFilter.update(hashMapList);
			setButtonEnable(btn_add);
			enableForm(layout);
			calcTotal();
		}

		lv_do_items.setAdapter(adapterDoItem);
		sp_unit_conv.setAdapter(unitConverterArrayAdapter);
		act_product.setAdapter(productFilter);

		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
		clearForm(layout);
		doItems = new ArrayList<>();
		lv_do_items.setAdapter(new AdapterDoItem(activity, doItems));
		listener.unSetDoItem();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_add:
				actionAdd();
				break;
			case R.id.btn_edit:
				if (isUpdate) actionUpdate();
				else actionEdit();
				break;
			case R.id.btn_delete:
				if (!isUpdate) actionDelete();
				clearForm(layout);
				enableForm(layout);
				onCancelOrAfterEdit();
				break;
			default:
				Toast.makeText(activity, "ID IS NOT VALID", Toast.LENGTH_LONG).show();
				break;
		}
	}

	@Override
	protected void actionAdd() {
		if (!errorChecked()) {
			doItem = new DoItem.Builder()
					.setDocno(doHead.doc_no)
					.setProductId(product.prodid)
					.setUnitId(unit_conversion)
					.setPriceList(price)
					.setQty(QTY)
					.setBonus(Discount.getMulDiscount(product.prodid, QTY, unit_conversion))
	                /*.setDiscountNst(disc_nusantara)
                    .setDiscountPrinc(disc_principal)*/
					.setSubTotal(sub_total)
					.build();
			doItem.save();

			adapterDoItem.add(doItem);
			clearForm(layout);
		}
	}

	@Override
	protected void actionEdit() {
		onEdit();
	}

	@Override
	protected void actionUpdate() {
		if (!errorChecked()) {
			doItem.product_id = product.prodid;
			doItem.pricelist = price;
			doItem.unit_id = unit_conversion;
			doItem.qty = QTY;
            /*doItem.discountNst = disc_nusantara;
            doItem.discountPrinc = disc_principal;*/
			doItem.sub_total = sub_total;
			doItem.mulBonus = Discount.getMulDiscount(product.prodid, QTY, unit_conversion);
			doItem.save();
			adapterDoItem.set(doItem);

			onCancelOrAfterEdit();
			clearForm(layout);
		}
	}

	@Override
	protected void actionDelete() {
		new MaterialDialog.Builder(activity)
				.content(product.name + " akan dihapus dari doitem ?")
				.positiveText(R.string.agree)
				.negativeText(R.string.disagree)
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
						doItem.delete();
						adapterDoItem.delete(doItem);
					}
				}).show();
	}

	@Override
	protected void clearForm(ViewGroup group) {
		super.clearForm(group);
		product = null;
		act_product.requestFocus();
		QTY = 0;
        /*disc_nusantara = 0;
        disc_nusantara = 0;*/
		sub_total = 0;
		imm.hideSoftInputFromWindow(act_product.getWindowToken(), 0);
		setButtonEnable(btn_add);
		calcTotal();
	}

	@Override
	protected void initListener() {

		qtyWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				try {
					if (charSequence.length() == 0) {
						et_qty.setGravity(Gravity.LEFT);
						QTY = 0;
					} else et_qty.setGravity(Gravity.RIGHT);
				} catch (Exception e) {
					et_qty.setGravity(Gravity.LEFT);
				}
				calcAll();
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		};

		productWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					if (s.length() == 0) sp_unit_conv.setVisibility(View.GONE);
				} catch (Exception e) {
					sp_unit_conv.setVisibility(View.GONE);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		};

		onActProductItemClicked = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				HashMap<String, String> map = (HashMap<String, String>) adapterView.getItemAtPosition(i);
				product = Product.find(map.get(Constanta.SIMPLE_LIST_ITEM_1));
				act_product.setText(product.name);
				setUpAdapterUnitConv();

			}
		};

		onDoItemLongClicked = new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
				doItem = (DoItem) adapterView.getItemAtPosition(i);
				editDoItem(doItem);
				return false;
			}
		};

		onUnitConvSelected = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				unit_conversion = (String) parent.getItemAtPosition(position);
				if (product.unitId.equals(unit_conversion)) {
					price = product.sellPrice;
				} else {
					unitConverter = UnitConverter.find(product.unitId, unit_conversion);
					price = product.sellPrice / unitConverter.factor;
				}
				et_product_price.setText(CommonUtil.priceFormat2Decimal(price));
				et_product_price.setGravity(Gravity.RIGHT);
				et_qty.requestFocus();
                /*discount = Discount.getDiscounts(product.prodid, (long) product.sellPrice);
                et_disc_nusantara.setText(CommonUtil.percentFormat(discount[0]));
                et_disc_principal.setText(CommonUtil.percentFormat(discount[1]));*/
				calcAll();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		};

	}

	private void setUpAdapterUnitConv() {
		unitConverters.clear();
		unitConverters.add(product.unitId);
		unitConverters.addAll(UnitConverter.getAllByUnitIdArray(product.unitId));
		unitConverterArrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, unitConverters);
		unitConverterArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_unit_conv.setVisibility(View.VISIBLE);
		sp_unit_conv.setAdapter(unitConverterArrayAdapter);
	}

	private void iniComp() {
		layout = (RelativeLayout) view.findViewById(R.id.vg_fragment_pos);
		sp_unit_conv = (Spinner) view.findViewById(R.id.sp_unit_conv);
		act_product = (AutoCompleteTextView) view.findViewById(R.id.act_product);
		et_product_price = (EditText) view.findViewById(R.id.et_product_price);
		et_qty = (EditText) view.findViewById(R.id.et_qty);
        /*et_disc_nusantara = (EditText) view.findViewById(R.id.et_disc_nusantara);
        et_disc_nusantara_value = (EditText) view.findViewById(R.id.et_disc_nusantara_value);
        et_disc_principal = (EditText) view.findViewById(R.id.et_disc_principal);
        et_disc_principal_value = (EditText) view.findViewById(R.id.et_disc_principal_value);
        et_sub_total = (EditText) view.findViewById(R.id.et_sub_total);*/
		et_total = (EditText) view.findViewById(R.id.et_total);
		lv_do_items = (ListView) view.findViewById(R.id.lv_do_items);
		btn_add = (Button) view.findViewById(R.id.btn_add);
		btn_edit = (Button) view.findViewById(R.id.btn_edit);
		btn_del = (Button) view.findViewById(R.id.btn_delete);
	}

	public void setOnDoItemListener(OnSetDoItemListener listener) {
		this.listener = listener;
	}


	private void calcAll() {
		if (product != null) {

            /*if (!et_disc_nusantara.getText().toString().equals("") ||
                    et_disc_nusantara.getText() != null)
                nusantara_value = calcDiscNusantara(et_disc_nusantara.getText().toString());

            if (!et_disc_principal.getText().toString().equals("") ||
                    et_disc_principal.getText() != null)
                principal_value = calcDiscPrincipal(et_disc_principal.getText().toString());*/

			if (!et_qty.getText().toString().equals("") &&
					et_qty.getText() != null)
				QTY = callSubTotal(Integer.parseInt(et_qty.getText().toString()));
		}
	}

    /*private long calcDiscNusantara() {
        long value = 0;
        try {
            disc_nusantara = discount[0];
            value = (long) ((product.sellPrice * disc_nusantara) / 100);
            et_disc_nusantara_value.setGravity(Gravity.RIGHT);
            et_disc_nusantara_value.setText(CommonUtil.priceFormat2Decimal(value));
        } catch (Exception e) {
            et_disc_nusantara_value.setGravity(Gravity.LEFT);
            et_disc_nusantara_value.getText().clear();
        }

        return value;
    }

    private long calcDiscPrincipal() {
        long value = 0;
        try {
            disc_principal = discount[1];
            value = (long) ((product.sellPrice * disc_principal) / 100);
            et_disc_principal_value.setGravity(Gravity.RIGHT);
            et_disc_principal_value.setText(CommonUtil.priceFormat2Decimal(value));
        } catch (Exception e) {
            et_disc_principal_value.setGravity(Gravity.LEFT);
            et_disc_principal_value.getText().clear();
        }

        return value;
    }*/

	private int callSubTotal(int QTY) {
		if (QTY > 0 || product != null) {
			sub_total = QTY * price;
            /*discount = Discount.getDiscounts(product.prodid, (long) sub_total, unit_conversion);

            et_disc_nusantara.setText(CommonUtil.percentFormat(discount[0]));
            et_disc_principal.setText(CommonUtil.percentFormat(discount[1]));

            nusantara_value = calcDiscNusantara();
            principal_value = calcDiscPrincipal();

            double value_discount = (nusantara_value + principal_value) * QTY;
            sub_total -= value_discount;*/

			et_sub_total.setGravity(Gravity.RIGHT);
			et_sub_total.setText(CommonUtil.priceFormat2Decimal(sub_total));
		}
		return QTY;
	}

	private void onEdit() {
		enableForm(layout);
		lv_do_items.setEnabled(false);
		isUpdate = true;
		et_qty.requestFocus();
		et_qty.selectAll();
		imm.showSoftInput(et_qty, InputMethodManager.SHOW_IMPLICIT);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			btn_edit.setBackground(getResources().getDrawable(R.drawable.button_save, activity.getTheme()));
			btn_del.setBackground(getResources().getDrawable(R.drawable.button_close, activity.getTheme()));
		} else {
			btn_edit.setBackground(getResources().getDrawable(R.drawable.button_save));
			btn_del.setBackground(getResources().getDrawable(R.drawable.button_close));
		}

	}

	private void onCancelOrAfterEdit() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			btn_edit.setBackground(getResources().getDrawable(R.drawable.button_edit, activity.getTheme()));
			btn_del.setBackground(getResources().getDrawable(R.drawable.button_delete, activity.getTheme()));
		} else {
			btn_edit.setBackground(getResources().getDrawable(R.drawable.button_edit));
			btn_del.setBackground(getResources().getDrawable(R.drawable.button_delete));
		}
		isUpdate = false;
		lv_do_items.setEnabled(true);
	}

	private boolean errorChecked() {
		if (product == null) {
			act_product.setError(getString(R.string.error_input_product));
			return true;
		} else if (et_qty.getText().toString().equals("") ||
				et_qty.getText() == null) {
			et_qty.setError(getString(R.string.error_input_qty));
			return true;
		} else if (Long.parseLong(et_qty.getText().toString()) > WarehouseStock.findById(doHead.whid, product.prodid).balance) {
			et_qty.setError(getString(R.string.error_input_qty_and_stock));
			return true;
		} else {
			return false;
		}
	}

	private void editDoItem(DoItem doItem) {
		product = Product.find(doItem.product_id);
		act_product.setText(product.name);
		setUpAdapterUnitConv();
		unit_conversion = doItem.unit_id;
		int posUnit = unitConverterArrayAdapter.getPosition(unit_conversion);
		sp_unit_conv.setSelection(posUnit);
		et_product_price.setText(CommonUtil.priceFormat2Decimal(doItem.pricelist));
		et_qty.setText(String.valueOf(doItem.qty));
		discount = Discount.getDiscounts(product.prodid, (long) product.sellPrice);
        /*et_disc_nusantara.setText(CommonUtil.percentFormat(discount[0]));
        et_disc_principal.setText(CommonUtil.percentFormat(discount[1]));*/
		calcAll();
		disableForm(layout);
		setButtonDisable(btn_add);
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

	private void calcTotal() {
		if (adapterDoItem.getList().size() != 0) {
			total = 0;
			for (DoItem item : adapterDoItem.getList()) {
				total += item.sub_total;
			}
		}
		et_total.setText(CommonUtil.priceFormat2Decimal(total));
	}

	public interface OnSetDoItemListener {
		void unSetDoItem();
	}

}
