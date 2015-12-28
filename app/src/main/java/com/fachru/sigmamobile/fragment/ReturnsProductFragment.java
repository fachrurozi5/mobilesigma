package com.fachru.sigmamobile.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.adapter.AdapterReturnsProduct;
import com.fachru.sigmamobile.model.ReturnsProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fachru on 17/11/15.
 */
public class ReturnsProductFragment extends Fragment
        implements OnClickListener {

    protected Activity activity;
    protected Bundle bundle;
    protected InputMethodManager imm;

    /*
    * widget
    * */
//    protected AutoCompleteTextView act_product;
    protected EditText act_product;
    protected EditText et_pcs;
    protected EditText et_cart;
    protected EditText et_ket;
    protected Button btn_add;
    protected Button btn_edit;
    protected Button btn_del;
    protected RadioButton rb_ambil;
    protected RadioButton rb_proses;
    protected ListView lv_returns_product;

    /*
    * mListener
    * */
    protected OnItemLongClickListener onReturnsProductItemClick;

    /*
    * plain old java object
    * */
    protected ReturnsProduct returnsProduct;

    /*
    * list of POJO
    * */
    protected List<ReturnsProduct> returnsProducts;

    /*
    * adapter
    * */
    protected AdapterReturnsProduct adapterReturnsProduct;

    /*
    * label
    * */
    boolean isUpdate = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        bundle = getArguments();
        returnsProducts = new ArrayList<>();
        adapterReturnsProduct = new AdapterReturnsProduct(activity, returnsProducts);

        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_returns_product, container, false);

//        act_product = (AutoCompleteTextView) view.findViewById(R.id.act_product);
        act_product = (EditText) view.findViewById(R.id.act_product);
        et_pcs = (EditText) view.findViewById(R.id.et_pcs);
        et_cart = (EditText) view.findViewById(R.id.et_cart);
        et_ket = (EditText) view.findViewById(R.id.et_explanation);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_edit = (Button) view.findViewById(R.id.btn_edit);
        btn_del = (Button) view.findViewById(R.id.btn_delete);
        rb_ambil = (RadioButton) view.findViewById(R.id.rb_ambil);
        rb_proses = (RadioButton) view.findViewById(R.id.rb_proses);
        lv_returns_product = (ListView) view.findViewById(R.id.lv_returns_product);
        initListener();
        btn_add.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        rb_ambil.setOnClickListener(this);
        rb_proses.setOnClickListener(this);

        lv_returns_product.setAdapter(adapterReturnsProduct);

        return view;
    }

    @Override
    public void onClick(View view) {
        boolean checked = false;

        if (view instanceof RadioButton)
            checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rb_ambil:
                if (checked)
                break;
            case R.id.rb_proses:
                if (checked)
                    break;
            case R.id.btn_add:
                addRerunsProduct();
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
                    clearText();
                } else {
                    actionDelete();
                }
                setFormEnable();
                break;
            default:
                break;
        }
    }

    public void initListener() {
        lv_returns_product.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                returnsProduct = (ReturnsProduct) parent.getItemAtPosition(position);
                editReturnsProduct(returnsProduct);
                return false;
            }
        });
    }

    public void addRerunsProduct() {
        if (!errorChecked()) {
            returnsProduct = new ReturnsProduct();
            returnsProduct.produk = act_product.getText().toString();
            returnsProduct.pcs = et_pcs.getText().toString();
            returnsProduct.cart = et_cart.getText().toString();
            returnsProduct.status = rb_ambil.isChecked();
            returnsProduct.ket = et_ket.getText().toString();
            adapterReturnsProduct.add(returnsProduct);
        }
    }

    private void actionUpdate() {
        if (!errorChecked()) {
            returnsProduct.produk = act_product.getText().toString();
            returnsProduct.pcs = et_pcs.getText().toString();
            returnsProduct.cart = et_cart.getText().toString();
            returnsProduct.ket = et_ket.getText().toString();
            returnsProduct.status = rb_ambil.isChecked();
            adapterReturnsProduct.set(returnsProduct);
            onCancelOrAfterEdit();
            clearText();
        }
    }

    public void editReturnsProduct(ReturnsProduct returnsProduct) {
        act_product.setText(returnsProduct.produk);
        et_pcs.setText(returnsProduct.pcs);
        et_cart.setText(returnsProduct.cart);
        rb_ambil.setChecked(returnsProduct.status);
        rb_proses.setChecked(!returnsProduct.status);
        et_ket.setText(returnsProduct.ket);
        setButtonDisable(btn_add);
        setFormDisable();
    }


    private void setButtonDisable(Button button) {
        btn_add.setEnabled(true);
        btn_edit.setEnabled(true);
        btn_del.setEnabled(true);

        button.setEnabled(false);
    }

    private void setButtonEnable(Button button) {
        btn_add.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_del.setEnabled(false);

        button.setEnabled(true);
    }

    private void setButtonEnableAll() {
        btn_add.setEnabled(true);
        btn_edit.setEnabled(true);
        btn_del.setEnabled(true);
    }

    private void setButtonDisableAll() {
        btn_add.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_del.setEnabled(false);
    }

    private void setFormDisable() {
        act_product.setEnabled(false);
        et_pcs.setEnabled(false);
        et_cart.setEnabled(false);
        et_ket.setEnabled(false);
        rb_ambil.setEnabled(false);
        rb_proses.setEnabled(false);
    }

    private void setFormEnable() {
        act_product.setEnabled(true);
        et_pcs.setEnabled(true);
        et_cart.setEnabled(true);
        et_ket.setEnabled(true);
        rb_ambil.setEnabled(true);
        rb_proses.setEnabled(true);
    }

    private void actionEdit() {
        onEdit();
    }

    private void onEdit() {
        btn_edit.setBackground(getResources().getDrawable(R.drawable.button_save));
        btn_del.setBackground(getResources().getDrawable(R.drawable.button_close));
        isUpdate = true;
        setFormEnable();
        et_pcs.requestFocus();
        imm.showSoftInput(et_pcs, InputMethodManager.SHOW_IMPLICIT);
        et_pcs.selectAll();
    }

    private void onCancelOrAfterEdit() {
        btn_edit.setBackground(getResources().getDrawable(R.drawable.button_edit));
        btn_del.setBackground(getResources().getDrawable(R.drawable.button_delete));
        isUpdate = false;
    }

    private boolean errorChecked() {
        if (act_product.getText().toString().equals("") ||
                act_product.getText() == null) {
            act_product.setError("Tolong isi Produk");
            return true;
        } else if (et_pcs.getText().toString().equals("") ||
                et_pcs.getText() == null) {
            et_pcs.setError("isi");
            return true;
        } else if (et_cart.getText().toString().equals("") ||
                et_cart.getText() == null) {
            et_cart.setError("isi");
            return true;
        } else if (et_ket.getText().toString().equals("") ||
                et_ket.getText() == null) {
            et_ket.setError("Tolong isi keterangan");
            return true;
        } else if (rb_ambil.isChecked() == rb_proses.isChecked()) {
            Toast.makeText(activity, "Pilih status retur", Toast.LENGTH_LONG).show();
            return true;
        } else {
            return false;
        }
    }

    private void actionDelete() {
        new MaterialDialog.Builder(activity)
                .content(act_product.getText().toString() + " akan di hapus dari Retur Produk?")
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        adapterReturnsProduct.delete(returnsProduct);
                        clearText();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        clearText();
                    }
                })
                .show();
    }

    private void clearText() {
        returnsProduct = null;
        act_product.getText().clear();
        et_pcs.getText().clear();
        et_cart.getText().clear();
        et_ket.getText().clear();
        rb_proses.setChecked(false);
        rb_ambil.setChecked(false);
        act_product.requestFocus();
        imm.hideSoftInputFromWindow(act_product.getWindowToken(), 0);
        setButtonEnable(btn_add);
    }
}
