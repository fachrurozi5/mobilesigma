package com.fachru.sigmamobile.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.fachru.sigmamobile.adapter.AdapterDoHeadItem;
import com.fachru.sigmamobile.adapter.AdapterFilter;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.model.Outlet;
import com.fachru.sigmamobile.model.Salesman;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


public class HeaderSOFragment extends Fragment implements
        OnClickListener, DatePickerDialog.OnDateSetListener{

    protected Activity activity;
    protected Bundle bundle;
    protected InputMethodManager imm;
    protected OnSetDoHeadListener listener;

    /*
    * widget
    * */
    protected EditText et_doc_no;
    protected EditText et_doc_date;
    protected EditText et_rute;
    protected EditText et_customer;
    protected AutoCompleteTextView act_salesman;
    protected AutoCompleteTextView act_outlet;
    protected Button btn_date_picker;
    protected Button btn_add;
    protected Button btn_edit;
    protected Button btn_del;
    protected ListView lv_do_head_items;

    /*
    * plain old data object
    * */
    protected DoHead doHead;
    protected Salesman salesman;
    protected Outlet outlet;
    protected Customer customer;

    /*
    * list of object
    * */
    protected List<DoHead> doHeads;

    /*
    * costum adapter
    * */
    protected AdapterFilter salesmanFilter;
    protected AdapterFilter outletFilter;
    protected AdapterDoHeadItem adapterDoHeadItem;

    /*
    * label
    * */
    protected boolean isUpdate = false;
    protected boolean isReadyAddItem = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        bundle = getArguments();
        customer = Customer.load(Customer.class, bundle.getLong("id"));
        salesman = null;
        outlet = null;
        doHeads = new ArrayList<>();
        if (DoHead.allPending().size() > 0)
            doHeads = DoHead.allPending();
        salesmanFilter = new AdapterFilter(activity, Salesman.toListHashMap());
        outletFilter = new AdapterFilter(activity, Outlet.toListHashMap());
        adapterDoHeadItem = new AdapterDoHeadItem(activity, doHeads);
        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header_pos, container, false);

        et_doc_no = (EditText) view.findViewById(R.id.et_doc_no);
        et_doc_date = (EditText) view.findViewById(R.id.et_doc_date);
        et_rute = (EditText) view.findViewById(R.id.et_rute);
        et_customer = (EditText) view.findViewById(R.id.et_customer);
        act_salesman = (AutoCompleteTextView) view.findViewById(R.id.act_salesman);
        act_outlet = (AutoCompleteTextView) view.findViewById(R.id.act_outlet);
        btn_date_picker = (Button) view.findViewById(R.id.btn_date_picker);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_edit = (Button) view.findViewById(R.id.btn_edit);
        btn_del = (Button) view.findViewById(R.id.btn_delete);
        lv_do_head_items = (ListView) view.findViewById(R.id.lv_do_head_items);

        act_salesman.setOnItemClickListener(onSalesmanClicked);
        act_outlet.setOnItemClickListener(onOutletClicked);
        btn_date_picker.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        lv_do_head_items.setOnItemLongClickListener(onDoHeadLongClicked);

        act_salesman.setAdapter(salesmanFilter);
        act_outlet.setAdapter(outletFilter);
        lv_do_head_items.setAdapter(adapterDoHeadItem);

        et_customer.setText(customer.getName());

        setButtonEnable(btn_add);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        listener.unSetDoHead();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_date_picker :
                actionDPD();
                break;
            case R.id.btn_add :
                if (isReadyAddItem) {
                    actionAddItem();
                } else {
                    actionAdd();
                }
                break;
            case R.id.btn_edit :
                if (isUpdate) {
                    actionUpdate();
                } else {
                    actionEdit();
                }
                break;
            case R.id.btn_delete :
                if (isUpdate) {
                    onCancelOrAfterEdit();
                    clearText();
                } else {
                    actionDelete();
                }
                setFormEnable();
                setButtonEnable(btn_add);
                break;
            default:
                break;
        }

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        et_doc_date.setText(CommonUtil.stringToDateHelper(dayOfMonth + "-" + (++monthOfYear) + "-" + year, Constanta.MEDIUM_UK));

    }

    public void setOnSetDoHeadListener(OnSetDoHeadListener listener) {
        this.listener = listener;
    }

    private OnItemClickListener onSalesmanClicked = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition(position);
            salesman = Salesman.find(map.get(Constanta.SIMPLE_LIST_ITEM_1));
            act_salesman.setText(salesman.salesman_name);
            act_outlet.requestFocus();
        }
    };

    private OnItemClickListener onOutletClicked = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition(position);
            outlet = Outlet.find(map.get(Constanta.SIMPLE_LIST_ITEM_1));
            act_outlet.setText(outlet.outlet_name);
            et_doc_no.requestFocus();
            imm.hideSoftInputFromWindow(et_doc_no.getWindowToken(), 0);
        }
    };

    private OnItemLongClickListener onDoHeadLongClicked = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            doHead = (DoHead) parent.getItemAtPosition(position);
            editDoHead(doHead);
            setButtonEnableAll();
            return false;
        }
    };

    private void editDoHead(DoHead doHead) {
        salesman = doHead.salesman;
        outlet = doHead.outlet;
        et_doc_no.setText(doHead.docno);
        et_doc_date.setText(
                CommonUtil.dateToStringMedium(doHead.docdate)
        );
        et_rute.setText(doHead.rute);
        act_salesman.setText(doHead.salesman.salesman_name);
        act_outlet.setText(doHead.outlet.outlet_name);
        setFormDisable();
        setButtonDisable(btn_date_picker);
    }

    private void setFormDisable() {
        et_doc_no.setEnabled(false);
        et_doc_date.setEnabled(false);
        et_rute.setEnabled(false);
        act_salesman.setEnabled(false);
        act_outlet.setEnabled(false);
    }

    private void actionEdit() {
        btn_edit.setBackground(getResources().getDrawable(R.drawable.button_save));
        btn_del.setBackground(getResources().getDrawable(R.drawable.button_close));
        isUpdate = true;
        setFormEnable();
        setButtonDisable(btn_add);
        btn_date_picker.setEnabled(true);
    }

    private void actionUpdate() {
        if (!errorChecked()) {
            try {
                doHead.setDateFromString(et_doc_date.getText().toString());
                doHead.rute = et_rute.getText().toString();
                doHead.salesman = salesman;
                doHead.outlet = outlet;
                doHead.save();
                adapterDoHeadItem.set(doHead);
                onCancelOrAfterEdit();
                setButtonEnable(btn_add);
                clearText();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void actionDelete() {
        new MaterialDialog.Builder(activity)
                .content(doHead.docno + " akan dihapus dari dohead ?")
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        adapterDoHeadItem.delete(doHead);
                        doHead.delete();
                        clearText();
                        onCancelOrAfterEdit();
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

    private void onCancelOrAfterEdit() {
        doHead = null;
        salesman = null;
        outlet = null;
        btn_add.setBackground(getResources().getDrawable(R.drawable.button_add));
        btn_edit.setBackground(getResources().getDrawable(R.drawable.button_edit));
        btn_del.setBackground(getResources().getDrawable(R.drawable.button_delete));
        isUpdate = false;
        isReadyAddItem = false;
    }

    private void setFormEnable() {
        et_doc_no.setEnabled(!isUpdate);
        et_doc_date.setEnabled(true);
        et_rute.setEnabled(true);
        act_salesman.setEnabled(true);
        act_outlet.setEnabled(true);
    }

    private boolean errorChecked() {
        if (et_doc_no.getText().toString().equals("") ||
                et_doc_no.getText() == null) {
            et_doc_no.setError(getString(R.string.error_input_doc_no));
            return true;
        } else if (et_doc_date.getText().toString().equals("") ||
                et_doc_date.getText() == null) {
            et_doc_date.setError(getString(R.string.error_input_doc_date));
            return true;
        } else if (et_rute.getText().toString().equals("") ||
                et_rute.getText() == null) {
            et_rute.setError(getString(R.string.error_input_rute));
            return true;
        } else if (salesman == null) {
            act_salesman.setError(getString(R.string.error_input_salesman));
            return true;
        } else if (outlet == null) {
            act_outlet.setError(getString(R.string.error_input_outlet));
            return true;
        } else {
            return false;
        }
    }

    private void actionDPD() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                HeaderSOFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setThemeDark(true);
        dpd.setAccentColor(getResources().getColor(R.color.colorAccentDialog));
        dpd.show(activity.getFragmentManager(), getString(R.string.tag_date_picker_dialog));
    }

    private void actionAdd() {
        if (!errorChecked()) {
            DoHead doHead = new DoHead(
                    et_doc_no.getText().toString(),
                    et_rute.getText().toString(),
                    salesman,
                    outlet,
                    false
            );
            try {
                doHead.setDateFromString(et_doc_date.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long status = doHead.save();
            if (status != -1) {
                adapterDoHeadItem.add(doHead);
                clearText();
            } else {
                et_doc_no.setError(getString(R.string.error_input_doc_no_duplicate));
                et_doc_no.requestFocus();
            }
        }
    }

    private void actionAddItem() {
        listener.onSetDoHead(doHead);
        clearText();
    }

    private void clearText() {
        doHead = null;
        salesman = null;
        outlet = null;
        et_doc_no.getText().clear();
        et_doc_date.getText().clear();
        et_rute.getText().clear();
        act_salesman.getText().clear();
        act_outlet.getText().clear();
        et_doc_no.requestFocus();
        imm.hideSoftInputFromWindow(et_doc_no.getWindowToken(), 0);
    }

    private void setButtonEnable(Button button) {
        btn_add.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_del.setEnabled(false);
        btn_date_picker.setEnabled(true);

        button.setEnabled(true);
    }

    private void setButtonDisable(Button button) {
        btn_add.setEnabled(true);
        btn_edit.setEnabled(true);
        btn_del.setEnabled(true);
        btn_date_picker.setEnabled(false);

        button.setEnabled(false);
    }

    private void setButtonEnableAll() {
        btn_add.setEnabled(true);
        btn_edit.setEnabled(true);
        btn_del.setEnabled(true);
        btn_add.setBackground(getResources().getDrawable(R.drawable.button_add_item));
        isReadyAddItem = true;
    }

    public interface OnSetDoHeadListener {
        void onSetDoHead(DoHead doHead);
        void unSetDoHead();
    }

}
