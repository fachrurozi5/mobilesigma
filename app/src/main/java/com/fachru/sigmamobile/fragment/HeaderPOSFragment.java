package com.fachru.sigmamobile.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.CustomerActivity;
import com.fachru.sigmamobile.Login;
import com.fachru.sigmamobile.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fachru.sigmamobile.adapters.AdapterDoHead;
import com.fachru.sigmamobile.adapters.spinners.AdapterWarehouse;
import com.fachru.sigmamobile.fragment.interfaces.OnSetDoHeadListener;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.model.Warehouse;
import com.fachru.sigmamobile.utils.BaseFragmentForm;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


public class HeaderPOSFragment extends BaseFragmentForm implements
        OnClickListener, DatePickerDialog.OnDateSetListener{

    protected Activity activity;
    protected Bundle bundle;
    protected InputMethodManager imm;


    /*
    * widget
    * */
    protected View view;
    protected RelativeLayout layout;
    protected EditText et_doc_no;
    protected EditText et_doc_date;
    protected EditText et_salesman;
    protected EditText et_customer;
    protected Spinner sp_warehause;
    /*protected AutoCompleteTextView act_salesman;
    protected AutoCompleteTextView act_outlet;*/
    protected Button btn_date_picker;
    protected Button btn_add;
    protected Button btn_edit;
    protected Button btn_del;
    protected ListView lv_do_head_items;


    /*
    * mListener
    * */
    protected OnSetDoHeadListener mListener;
    /*protected OnItemClickListener onSalesmanClicked;
    protected OnItemClickListener onOutletClicked;*/
    protected OnItemLongClickListener onDoHeadLongClicked;
    protected OnItemSelectedListener onWarehouseSelected;


    /*
    * plain old data object
    * */
    protected DoHead doHead;
    /*protected Salesman salesman;
    protected Outlet outlet;*/
    protected Customer customer;
    protected Employee employee;
    protected Warehouse warehouse;

    /*
    * list of object
    * */
    protected List<DoHead> doHeads;

    /*
    * costum adapter
    * */
    /*protected AdapterFilter salesmanFilter;
    protected AdapterFilter outletFilter;*/
    private AdapterDoHead adapterDoHead;
    private AdapterWarehouse adapterWarehouse;

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
        customer = Customer.load(Customer.class, bundle.getLong(CustomerActivity.CUSTID));
        employee = Employee.load(Employee.class, bundle.getLong(Login.EMPLID));

        doHeads = new ArrayList<>();

        if (DoHead.getAllWhereCustomer(customer.getCustomerId()).size() > 0)
            doHeads = DoHead.getAllWhereCustomer(customer.getCustomerId());

        adapterDoHead = new AdapterDoHead(getContext(), doHeads);
        adapterWarehouse = new AdapterWarehouse(activity, Warehouse.all());
        adapterWarehouse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_header_pos, container, false);
        initComp();
        initListener();
        lv_do_head_items.setOnItemLongClickListener(onDoHeadLongClicked);
        /*act_salesman.setOnItemClickListener(onSalesmanClicked);
        act_outlet.setOnItemClickListener(onOutletClicked);*/
        sp_warehause.setOnItemSelectedListener(onWarehouseSelected);
        btn_date_picker.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_del.setOnClickListener(this);

        /*act_salesman.setAdapter(salesmanFilter);
        act_outlet.setAdapter(outletFilter);
        lv_do_head_items.setAdapter(adapterDoHeadItem);*/

        lv_do_head_items.setAdapter(adapterDoHead);
        sp_warehause.setAdapter(adapterWarehouse);

        et_customer.setText(customer.getName());
        et_salesman.setText(employee.name);

        setButtonEnable(btn_add);

        return view;
    }

    private void initComp() {
        layout = (RelativeLayout) view.findViewById(R.id.vg_fragment_header_pos);
        et_doc_no = (EditText) view.findViewById(R.id.et_doc_no);
        et_doc_date = (EditText) view.findViewById(R.id.et_doc_date);
        et_salesman = (EditText) view.findViewById(R.id.et_salesman);
        et_customer = (EditText) view.findViewById(R.id.et_customer);
        sp_warehause = (Spinner) view.findViewById(R.id.et_warehouse);
        /*act_salesman = (AutoCompleteTextView) view.findViewById(R.id.act_salesman);
        act_outlet = (AutoCompleteTextView) view.findViewById(R.id.act_outlet);*/
        btn_date_picker = (Button) view.findViewById(R.id.btn_date_picker);
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        et_doc_date.setText(CommonUtil.stringToDateHelper(dayOfMonth + "-" + (++monthOfYear) + "-" + year, Constanta.MEDIUM_UK));
    }

    public void setOnSetDoHeadListener(OnSetDoHeadListener listener) {
        this.mListener = listener;
    }

    private void editDoHead(DoHead doHead) {
        /*salesman = doHead.salesman;
        outlet = doHead.outlet;
        customer = doHead.customer;*/

        customer = Customer.getCustomer(doHead.custid);
        employee = Employee.getEmployee(doHead.empid);
        warehouse = Warehouse.getWarehouse(doHead.whid);

        et_doc_no.setText(doHead.doc_no);
        et_doc_date.setText(doHead.getDocDate());
        /*sp_warehause.setText(doHead.rute);*/
        /*act_salesman.setText(doHead.salesman.salesman_name);
        act_outlet.setText(doHead.outlet.outlet_name);*/
        int pos = adapterWarehouse.getPosition(warehouse);
        sp_warehause.setSelection(pos);
        et_customer.setText(customer.getName());
        et_salesman.setText(employee.name);
        disableForm(layout);
        setButtonDisable(btn_date_picker);
    }

    @Override
    protected void actionAdd() {
        if (!errorChecked()) {
                    doHead = new DoHead.Builder()
                    .setDocNo(et_doc_no.getText().toString())
                    .setDocDate(new Date())
                    .setCustid(et_customer.getText().toString())
                    .setCustid(customer.getCustomerId())
                    .setEmpid(employee.employee_id)
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
        btn_edit.setBackground(getResources().getDrawable(R.drawable.button_save));
        btn_del.setBackground(getResources().getDrawable(R.drawable.button_close));
        isUpdate = true;
        enableForm(layout);
        setButtonDisable(btn_add);
        btn_date_picker.setEnabled(true);
    }

    @Override
    protected void actionUpdate() {
        if (!errorChecked()) {
            try {
                doHead.custid = customer.getCustomerId();
                doHead.empid = employee.employee_id;
                doHead.whid = warehouse.whid;
                doHead.setDateFromString(et_doc_date.getText().toString());
                doHead.save();

                adapterDoHead.set(doHead);
                onCancelOrAfterEdit();
                setButtonEnable(btn_add);
                clearForm(layout);
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
        /*onSalesmanClicked = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition(position);
                salesman = Salesman.find(map.get(Constanta.SIMPLE_LIST_ITEM_1));
                act_salesman.setText(salesman.salesman_name);
                act_outlet.requestFocus();
            }
        };

        onOutletClicked = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition(position);
                outlet = Outlet.find(map.get(Constanta.SIMPLE_LIST_ITEM_1));
                act_outlet.setText(outlet.outlet_name);
                et_doc_no.requestFocus();
                imm.hideSoftInputFromWindow(et_doc_no.getWindowToken(), 0);
            }
        };*/

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
                warehouse = Warehouse.load(Warehouse.class, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private void onCancelOrAfterEdit() {
        doHead = null;
        warehouse = null;
        /*salesman = null;
        outlet = null;*/
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
        } /*else if (et_doc_date.getText().toString().equals("") ||
                et_doc_date.getText() == null) {
            et_doc_date.setError(getString(R.string.error_input_doc_date));
            return true;
        } else if (sp_warehause.getText().toString().equals("") ||
                sp_warehause.getText() == null) {
            sp_warehause.setError(getString(R.string.error_input_rute));
            return true;
        } else if (salesman == null) {
            act_salesman.setError(getString(R.string.error_input_salesman));
            return true;
        } else if (outlet == null) {
            act_outlet.setError(getString(R.string.error_input_outlet));
            return true;
        }*/ else {
            return false;
        }
    }

    private void actionDPD() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                HeaderPOSFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setThemeDark(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dpd.setAccentColor(getResources().getColor(R.color.colorAccentDialog, activity.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dpd.setAccentColor(ContextCompat.getColor(activity, R.color.colorAccentDialog));
        } else {
            dpd.setAccentColor(getResources().getColor(R.color.colorAccentDialog));
        }
        dpd.show(activity.getFragmentManager(), getString(R.string.tag_date_picker_dialog));
    }


    private void actionAddItem() {
        mListener.onSetDoHead(doHead);
        clearForm(layout);
    }

    @Override
    protected void clearForm(ViewGroup group) {
        super.clearForm(group);
        doHead = null;
        warehouse = null;
        /*salesman = null;
        outlet = null;*/
        et_customer.setText(customer.getName());
        et_salesman.setText(employee.employee_id);
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

}
