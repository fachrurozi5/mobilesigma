package com.fachru.sigmamobile.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
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
import com.fachru.sigmamobile.adapters.AdapterFilter;
import com.fachru.sigmamobile.adapters.AdapterSoItem;
import com.fachru.sigmamobile.fragment.interfaces.OnSetSoItemListener;
import com.fachru.sigmamobile.model.Discount;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.model.SoHead;
import com.fachru.sigmamobile.model.SoItem;
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
public class SalesOrderFragment extends BaseFragmentForm implements OnClickListener {

    private static final String TAG = "SalesOrderActivity";

    protected Activity activity;
    protected Bundle bundle;
    protected InputMethodManager imm;
    protected OnSetSoItemListener listener;

    /*
    * widget
    * */
    protected View view;
    protected RelativeLayout layout;
    protected AutoCompleteTextView act_product;
    protected EditText et_product_price;
    protected EditText et_qty;
    protected EditText et_disc_nusantara;
    protected EditText et_disc_nusantara_value;
    protected EditText et_disc_principal;
    protected EditText et_disc_principal_value;
    protected EditText et_sub_total;
    protected EditText et_total;
    protected Button btn_add;
    protected Button btn_edit;
    protected Button btn_del;
    protected Spinner sp_unit_conv;
    protected ListView lv_so_items;

    /*
    * plain old java object
    * */
    protected SoHead soHead;
    protected SoItem soItem;
    protected Product product;
    protected UnitConverter unitConverter;

    /*
    * list of object
    * */
    protected List<SoItem> soItems;
    protected List<String> unitConverters = new ArrayList<>();
    protected List<HashMap<String, String>> hashMapList;

    /*
    * custom adapter
    * */
    protected AdapterFilter productFilter;
    protected AdapterSoItem adapterSoItem;
    protected ArrayAdapter<String> unitConverterArrayAdapter;


    /*
    * label
    * */
    private int qty = 0;
    private int price_type = 0;
    private long total = 0;
    private double price;
    private double sub_total = 0;
    private boolean isUpdate = false;
    private String unit_conversion;
    private long nusantara_value;
    private long principal_value;
    private double disc_nusantara;
    private double disc_principal;
    private double[] discount;

    /*
    * mListener
    * */
    private OnItemClickListener onActProductItemClicked;
    private OnItemLongClickListener onSoItemLongClicked;
    private AdapterView.OnItemSelectedListener onUnitConvSelected;
    private TextWatcher qtyWatcher;
    private TextWatcher productWatcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        bundle = getArguments();
        soHead = null;
        soItem = null;
        product = null;
        soItems = new ArrayList<>();
        hashMapList = new ArrayList<>();

        productFilter = new AdapterFilter(activity.getApplicationContext(), hashMapList);
        adapterSoItem = new AdapterSoItem(activity, soItems, 0);
        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sales_order, container, false);
        iniComp();
        initListener();

        act_product.addTextChangedListener(productWatcher);
        et_qty.addTextChangedListener(qtyWatcher);

        act_product.setOnItemClickListener(onActProductItemClicked);
        lv_so_items.setOnItemLongClickListener(onSoItemLongClicked);
        sp_unit_conv.setOnItemSelectedListener(onUnitConvSelected);

        btn_add.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_del.setOnClickListener(this);

        disableButton(layout);
        disableForm(layout);

        if (bundle != null) {
            Log.d(TAG, "onCreateView: bundle != null");
            soHead = SoHead.find(bundle.getString(Constanta.KEY_SO));
            price_type = soHead.priceType;
            soItems = soHead.soItems();
            hashMapList = WarehouseStock.toListHashMap(soHead.whid, price_type);
            adapterSoItem.update(soItems, price_type);
            productFilter.update(hashMapList);
            setButtonEnable(btn_add);
            enableForm(layout);
            calcTotal();
        }

        lv_so_items.setAdapter(adapterSoItem);
        act_product.setAdapter(productFilter);
        sp_unit_conv.setAdapter(unitConverterArrayAdapter);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        clearForm(layout);
        soItems = new ArrayList<>();
        lv_so_items.setAdapter(new AdapterSoItem(activity, soItems, price_type));
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
            soItem = new SoItem.Builder()
                    .setSo(soHead.so)
                    .setProductId(product.prodid)
                    .setPriceList(price)
                    .setUnitId(unit_conversion)
                    .setQty(qty)
                    .setBonus(Discount.getMulDiscount(product.prodid, qty, unit_conversion))
                    .setDiscountNst(disc_nusantara)
                    .setDiscountPrinc(disc_principal)
                    .setSubTotal(sub_total)
                    .build();
            soItem.save();

            adapterSoItem.add(soItem);
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
            soItem.productId = product.prodid;
            soItem.priceList = price;
            soItem.unitId = unit_conversion;
            soItem.qty = qty;
            soItem.discountNst = disc_nusantara;
            soItem.discountPrinc = disc_principal;
            soItem.subTotal = sub_total;
            soItem.mulBonus = Discount.getMulDiscount(product.prodid, qty, unit_conversion);
            soItem.save();
            adapterSoItem.set(soItem);
            onCancelOrAfterEdit();
            clearForm(layout);
        }
    }

    @Override
    protected void actionDelete() {
        new MaterialDialog.Builder(activity)
                .content(product.name + " akan dihapus dari soitem ?")
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        adapterSoItem.delete(soItem);
                        soItem.delete();
                    }
                }).show();
    }

    @Override
    protected void clearForm(ViewGroup group) {
        super.clearForm(group);
        product = null;
        act_product.requestFocus();
        qty = 0;
        sub_total = 0;
        disc_nusantara = 0;
        disc_nusantara = 0;
        imm.hideSoftInputFromWindow(act_product.getWindowToken(), 0);
        et_total.setGravity(Gravity.RIGHT);
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
                        qty = 0;
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

        onSoItemLongClicked = new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                soItem = (SoItem) adapterView.getItemAtPosition(i);
                editDoItem(soItem);
                return false;
            }
        };

        onUnitConvSelected = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unit_conversion = (String) parent.getItemAtPosition(position);
                if (product.unitid.equals(unit_conversion)) {
                    price = getProductPrice(price_type);
                    et_product_price.setText(CommonUtil.priceFormat2Decimal(price));
                } else {
                    unitConverter = UnitConverter.find(product.unitid, unit_conversion);
                    price = getProductPrice(price_type) / unitConverter.factor;
                    et_product_price.setText(CommonUtil.priceFormat2Decimal(price));
                }
                et_product_price.setGravity(Gravity.RIGHT);
                et_qty.requestFocus();
                calcAll();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    }

    private void setUpAdapterUnitConv() {
        unitConverters.clear();
        unitConverters.add(product.unitid);
        unitConverters.addAll(UnitConverter.getAllByUnitIdArray(product.unitid));
        unitConverterArrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, unitConverters);
        unitConverterArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_unit_conv.setVisibility(View.VISIBLE);
        sp_unit_conv.setAdapter(unitConverterArrayAdapter);
    }

    private void iniComp() {
        layout = (RelativeLayout) view.findViewById(R.id.vg_fragment_so);
        act_product = (AutoCompleteTextView) view.findViewById(R.id.act_product);
        sp_unit_conv = (Spinner) view.findViewById(R.id.sp_unit_conv);
        et_product_price = (EditText) view.findViewById(R.id.et_product_price);
        et_qty = (EditText) view.findViewById(R.id.et_qty);
        et_disc_nusantara = (EditText) view.findViewById(R.id.et_disc_nusantara);
        et_disc_nusantara_value = (EditText) view.findViewById(R.id.et_disc_nusantara_value);
        et_disc_principal = (EditText) view.findViewById(R.id.et_disc_principal);
        et_disc_principal_value = (EditText) view.findViewById(R.id.et_disc_principal_value);
        et_sub_total = (EditText) view.findViewById(R.id.et_sub_total);
        et_total = (EditText) view.findViewById(R.id.et_total);
        lv_so_items = (ListView) view.findViewById(R.id.lv_so_items);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_edit = (Button) view.findViewById(R.id.btn_edit);
        btn_del = (Button) view.findViewById(R.id.btn_delete);
    }

    public void setOnSoItemListener(OnSetSoItemListener listener) {
        this.listener = listener;
    }

    private void calcAll() {
        if (product != null) {
            if (!et_qty.getText().toString().equals("") &&
                    et_qty.getText() != null)
                qty = callSubTotal(Integer.parseInt(et_qty.getText().toString()));
        }
    }

    private int callSubTotal(int QTY) {
        if (QTY > 0 || product != null) {
            /*if (!unit_conversion.equalsIgnoreCase("pcs")) {
                subTotal = QTY * price;
                discount = Discount.getDiscounts(product.prodId, (long) subTotal, unit_conversion.equalsIgnoreCase("pcs"));
            } else {
                subTotal = QTY * price;
                discount = Discount.getDiscounts(product.prodId, (long) subTotal, false);
            }*/

            sub_total = QTY * price;
            discount = Discount.getDiscounts(product.prodid, (long) sub_total, unit_conversion);

            et_disc_nusantara.setText(CommonUtil.percentFormat(discount[0]));
            et_disc_principal.setText(CommonUtil.percentFormat(discount[1]));

            nusantara_value = calcDiscNusantara();
            principal_value = calcDiscPrincipal();

            sub_total -= (nusantara_value + principal_value);

            et_sub_total.setGravity(Gravity.RIGHT);
            et_sub_total.setText(CommonUtil.priceFormat2Decimal(sub_total));
        }
        return QTY;
    }

    private long calcDiscNusantara() {
        long value = 0;
        try {
            disc_nusantara = discount[0];
            value = (long) ((price * disc_nusantara) / 100);
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
            value = (long) ((price * disc_principal) / 100);
            et_disc_principal_value.setGravity(Gravity.RIGHT);
            et_disc_principal_value.setText(CommonUtil.priceFormat2Decimal(value));
        } catch (Exception e) {
            et_disc_principal_value.setGravity(Gravity.LEFT);
            et_disc_principal_value.getText().clear();
        }

        return value;
    }

    private void onEdit() {
        enableForm(layout);
        lv_so_items.setEnabled(false);
        isUpdate = true;
        et_qty.requestFocus();
        et_qty.selectAll();
        imm.showSoftInput(et_qty, InputMethodManager.SHOW_IMPLICIT);
        btn_edit.setBackground(getResources().getDrawable(R.drawable.button_save));
        btn_del.setBackground(getResources().getDrawable(R.drawable.button_close));
    }

    private void onCancelOrAfterEdit() {
        btn_edit.setBackground(getResources().getDrawable(R.drawable.button_edit));
        btn_del.setBackground(getResources().getDrawable(R.drawable.button_delete));
        isUpdate = false;
        lv_so_items.setEnabled(true);
    }

    private boolean errorChecked() {
        if (product == null) {
            act_product.setError(getString(R.string.error_input_product));
            return true;
        } else if (et_qty.getText().toString().equals("") ||
                et_qty.getText() == null) {
            et_qty.setError(getString(R.string.error_input_qty));
            return true;
        } else {
            return false;
        }
    }

    private void editDoItem(SoItem soItem) {
        product = Product.find(soItem.productId);
        act_product.setText(product.name);
        sp_unit_conv.setVisibility(View.VISIBLE);
        setUpAdapterUnitConv();
        unit_conversion = soItem.unitId;
        int posUnit = unitConverterArrayAdapter.getPosition(unit_conversion);
        sp_unit_conv.setSelection(posUnit);
        et_product_price.setText(CommonUtil.priceFormat2Decimal(soItem.priceList));
        et_qty.setText(String.valueOf(soItem.qty));
//        discount = Discount.getDiscounts(product.prodId, (long) product.sellprice, unit_conversion.equalsIgnoreCase("Pcs"));
        et_disc_nusantara.setText(CommonUtil.percentFormat(discount[0]));
        et_disc_principal.setText(CommonUtil.percentFormat(discount[1]));
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

    private String getProductPriceAsString(int type_price_list) {
        String product_price = "";

        switch (type_price_list) {
            case 1:
                product_price = CommonUtil.priceFormat(product.po_price);
                break;
            case 2:
                product_price = CommonUtil.priceFormat(product.sellprice);
                break;
            case 3:
                product_price = CommonUtil.priceFormat(product.base_price);
                break;
            case 4:
                product_price = CommonUtil.priceFormat(product.old_price);
                break;
            case 5:
                product_price = CommonUtil.priceFormat(product.test_price);
                break;
        }

        return product_price;
    }

    private double getProductPrice(int type_price_list) {
        double product_price = 0;

        switch (type_price_list) {
            case 1:
                product_price = product.po_price;
                break;
            case 2:
                product_price = product.sellprice;
                break;
            case 3:
                product_price = product.base_price;
                break;
            case 4:
                product_price = product.old_price;
                break;
            case 5:
                product_price = product.test_price;
                break;
        }

        return product_price;
    }

    private void calcTotal() {
        if (adapterSoItem.getList().size() != 0) {
            total = 0;
            for (SoItem item : adapterSoItem.getList()) {
                total += item.subTotal;
            }
        }
        et_total.setText(CommonUtil.priceFormat2Decimal(total));
    }
}
