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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.adapters.AdapterDoItem;
import com.fachru.sigmamobile.adapters.AdapterFilter;
import com.fachru.sigmamobile.adapters.AdapterFilterProduct;
import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.model.DoItem;
import com.fachru.sigmamobile.model.Product;
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

    /*
    * list of object
    * */
    protected List<DoItem> doItems;
    protected List<HashMap<String, String>> hashMapList;

    /*
    * custom adapter
    * */
    protected AdapterFilterProduct productFilter;
    protected AdapterDoItem adapterDoItem;

    /*
    * label
    * */
    int QTY;
    double total = 0;
    double sub_total = 0;
//    long sub_total = 0;
    /*long nusantara_value = 0;
    long principal_value = 0;
    double disc_nusantara = 0;
    double disc_principal = 0;*/
    boolean isUpdate = false;

    /*
    * mListener
    * */
    private OnItemClickListener onActProductItemClicked;
    private OnItemLongClickListener onDoItemLongClicked;
    private TextWatcher qtyWatcher;
    /*private TextWatcher discNusantaraWatcher;
    private TextWatcher discPrincipalWatcher;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        bundle = getArguments();
        doItems = new ArrayList<>();
        hashMapList = new ArrayList<>();

        adapterDoItem = new AdapterDoItem(activity, doItems);
        productFilter = new AdapterFilterProduct(activity.getApplicationContext(), hashMapList);
        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_point_of_sale, container, false);
        iniComp();
        initListener();
        et_qty.addTextChangedListener(qtyWatcher);
        /*et_disc_nusantara.addTextChangedListener(discNusantaraWatcher);
        et_disc_principal.addTextChangedListener(discPrincipalWatcher);*/
        act_product.setOnItemClickListener(onActProductItemClicked);
        lv_do_items.setOnItemLongClickListener(onDoItemLongClicked);
        btn_add.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_del.setOnClickListener(this);

        disableButton(layout);
        disableForm(layout);

        if (bundle != null) {
            doHead = DoHead.find(bundle.getString(Constanta.KEY_DOC_NO));
            doItems = doHead.doItems();
            hashMapList = WarehouseStock.toListHashMap(doHead.whid);
            adapterDoItem.update(doItems);
            productFilter.update(hashMapList);
            setButtonEnable(btn_add);
            enableForm(layout);
            calcTotal();
        }



        lv_do_items.setAdapter(adapterDoItem);
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
                if (isUpdate) actionUpdate(); else actionEdit();
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
                    .setProduct_id(product.prodid)
                    .setQty(QTY)
                    .setSubTotal(sub_total)
                    .build();
            doItem.save();

            Log.d(Constanta.TAG, doItem.toString());
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
            /* TODO: doItem.product = product;
            doItem.jumlah_order = QTY;
            doItem.discount_nusantara = disc_nusantara;
            doItem.discount_principal = disc_principal;
            doItem.sub_total = sub_total;
            adapterDoItem.set(doItem);*/

            doItem.product_id = product.prodid;
            doItem.qty = QTY;
            doItem.sub_total = sub_total;
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
                        adapterDoItem.delete(doItem);
                    }
                }).show();
    }

    @Override
    protected void clearForm(ViewGroup group) {
        super.clearForm(group);
        doItem = null;
        product = null;
        act_product.requestFocus();
        QTY = 0;
        /*disc_nusantara = disc_principal = principal_value = nusantara_value =*/ sub_total = 0;
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

        /*discNusantaraWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calcAll();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        discPrincipalWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // TODO: calcAll();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };*/

        onActProductItemClicked = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap<String, String>) adapterView.getItemAtPosition(i);
                product = Product.find(map.get(Constanta.SIMPLE_LIST_ITEM_1));
                act_product.setText(product.name);
                et_product_price.setGravity(Gravity.RIGHT);
                et_product_price.setText(CommonUtil.priceFormat2Decimal(product.sellprice));
                calcAll();
                et_qty.requestFocus();
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

    }

    private void iniComp() {
        layout = (RelativeLayout) view.findViewById(R.id.vg_fragment_pos);
        act_product = (AutoCompleteTextView) view.findViewById(R.id.act_product);
        et_product_price = (EditText) view.findViewById(R.id.et_product_price);
        et_qty = (EditText) view.findViewById(R.id.et_qty);
        /*et_disc_nusantara = (EditText) view.findViewById(R.id.et_disc_nusantara);
        et_disc_nusantara_value = (EditText) view.findViewById(R.id.et_disc_nusantara_value);
        et_disc_principal = (EditText) view.findViewById(R.id.et_disc_principal);
        et_disc_principal_value = (EditText) view.findViewById(R.id.et_disc_principal_value);*/
        et_sub_total = (EditText) view.findViewById(R.id.et_sub_total);
        et_total = (EditText) view.findViewById(R.id.et_total);
        lv_do_items = (ListView) view.findViewById(R.id.lv_do_items);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_edit = (Button) view.findViewById(R.id.btn_edit);
        btn_del = (Button) view.findViewById(R.id.btn_delete);
    }

    public void setOnDoItemListener(OnSetDoItemListener listener) {
        this.listener = listener;
    }

    /* TODO: private long calcDiscNusantara(String s) {
        long value = 0;
        try {
            disc_nusantara = Double.parseDouble(s);
            value = (long) ((product.price * disc_nusantara) / 100);
            et_disc_nusantara_value.setGravity(Gravity.RIGHT);
            et_disc_nusantara_value.setText(CommonUtil.priceFormat2Decimal(value));
        } catch (Exception e) {
            et_disc_nusantara_value.setGravity(Gravity.LEFT);
            et_disc_nusantara_value.getText().clear();
        }

        return value;
    }

    private long calcDiscPrincipal(String s) {
        long value = 0;
        try {
            disc_principal = Double.parseDouble(s);
            value = (long) ((product.price * disc_principal) / 100);
            et_disc_principal_value.setGravity(Gravity.RIGHT);
            et_disc_principal_value.setText(CommonUtil.priceFormat2Decimal(value));
        } catch (Exception e) {
            et_disc_principal_value.setGravity(Gravity.LEFT);
            et_disc_principal_value.getText().clear();
        }

        return value;
    }*/

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

    private int callSubTotal(int QTY) {
        if (QTY > 0 || product != null) {
            sub_total = QTY * product.sellprice;/*(product.price - (nusantara_value + principal_value));*/
            et_sub_total.setGravity(Gravity.RIGHT);
            et_sub_total.setText(CommonUtil.priceFormat2Decimal(sub_total));
        }
        return QTY;
    }

    private void onEdit() {
        enableForm(layout);
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
        }/* TODO: else if (et_disc_nusantara.getText().toString().equals("") ||
                et_disc_nusantara.getText() == null) {
            et_disc_nusantara.setError(getString(R.string.error_input_disc));
            return true;
        } else if (et_disc_principal.getText().toString().equals("") ||
                et_disc_principal.getText() == null) {
            et_disc_principal.setError(getString(R.string.error_input_disc));
            return true;
        }*/  else {
            return false;
        }
    }

    private void editDoItem(DoItem doItem) {
        product = Product.find(doItem.product_id);
        act_product.setText(product.name);
        et_product_price.setText(CommonUtil.priceFormat2Decimal(product.sellprice));
        et_qty.setText(String.valueOf(doItem.qty));
        /*
        et_disc_nusantara.setText(CommonUtil.percentFormat(doItem.discount_nusantara));
        et_disc_principal.setText(CommonUtil.percentFormat(doItem.discount_principal));*/
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
