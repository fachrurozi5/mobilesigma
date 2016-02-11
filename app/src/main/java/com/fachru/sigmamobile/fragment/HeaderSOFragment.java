package com.fachru.sigmamobile.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.CustomerActivity;
import com.fachru.sigmamobile.Login;
import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.adapters.AdapterSoHead;
import com.fachru.sigmamobile.adapters.spinners.AdapterWarehouse;
import com.fachru.sigmamobile.fragment.interfaces.OnSetSoHeadListener;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.model.SoHead;
import com.fachru.sigmamobile.model.Warehouse;
import com.fachru.sigmamobile.utils.BaseFragmentForm;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by fachru on 28/12/15.
 */
public class HeaderSOFragment extends BaseFragmentForm implements
        View.OnClickListener, DatePickerDialog.OnDateSetListener {

    protected boolean isUpdate = false;
    protected boolean isReadyAddItem = false;
    private Activity activity;
    private Bundle bundle;
    /*
    * listener
    * */
    private OnSetSoHeadListener mListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private AdapterView.OnItemSelectedListener onWarehouseSelected;
    private AdapterView.OnItemSelectedListener onTypeOfPriceSelected;
    private AdapterView.OnItemLongClickListener onSoHeadLongClick;
    /*
    * plain old java object
    * */
    private SoHead soHead;
    private Customer customer;
    private Employee employee;
    private Warehouse warehouse;
    /*
    * widget
    * */
    private View view;
    private RelativeLayout layout;
    private EditText et_so;
    private EditText et_po_date;
    private EditText et_del_date;
    private EditText et_customer_po;
    private Spinner sp_warehouse;
    private Spinner sp_type_of_price;
    private EditText et_salesman;
    private EditText et_customer;
    private Button btn_po_date_picker;
    private Button btn_del_date_picker;
    private Button btn_add;
    private Button btn_edit;
    private Button btn_del;
    private ListView lv_so_head_items;
    /*
    * adapter
    * */
    private ArrayAdapter<CharSequence> adapter;
    /*
    * custom adapter
    * */
    private AdapterWarehouse adapterWarehouse;
    private AdapterSoHead adapterSoHead;
    /*
    * label
    * */
    private int type_of_price;
    private List<SoHead> soHeads = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        bundle = getArguments();
        customer = Customer.load(Customer.class, bundle.getLong(CustomerActivity.CUSTID));
        employee = Employee.load(Employee.class, bundle.getLong(Login.EMPLID));

        adapterWarehouse = new AdapterWarehouse(activity, Warehouse.all());
        adapterWarehouse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter = ArrayAdapter.createFromResource(activity, R.array.type_of_price, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        soHeads = SoHead.getAllWhereCustomer(customer.custid);
        adapterSoHead = new AdapterSoHead(activity, soHeads);

    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP &&
                        keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isReadyAddItem) {
                        clearForm(layout);
                        onCancelOrAfterEdit();
                        enableForm(layout);
                        setButtonEnable(btn_add);
                    }
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mListener.unSetSoHead();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_header_so, container, false);

        initComp();
        initListener();

        et_so.setText(SoHead.generateId());
        et_customer.setText(customer.name);
        et_salesman.setText(employee.name);

        btn_po_date_picker.setOnClickListener(this);
        btn_del_date_picker.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        sp_warehouse.setOnItemSelectedListener(onWarehouseSelected);
        sp_type_of_price.setOnItemSelectedListener(onTypeOfPriceSelected);
        lv_so_head_items.setOnItemLongClickListener(onSoHeadLongClick);

        sp_warehouse.setAdapter(adapterWarehouse);
        sp_type_of_price.setAdapter(adapter);
        lv_so_head_items.setAdapter(adapterSoHead);

        setButtonEnable(btn_add);

        return view;
    }

    @Override
    protected void actionAdd() {
        if (!errorChecked()) {
            try {
                soHead = new SoHead.Builder()
                        .setSo(et_so.getText().toString())
                        .setDate_order(CommonUtil.stringToDateLong(et_po_date.getText().toString()))
                        .setDeldate(CommonUtil.stringToDateLong(et_del_date.getText().toString()))
                        .setCustid(customer.custid)
                        .setPurchaseOrder(et_customer_po.getText().toString())
                        .setEmpid(employee.employee_id)
                        .setWhid(warehouse.whid)
                        .setPriceType(type_of_price)
                        .Build();

                soHead.save();
                clearForm(layout);

                adapterSoHead.add(soHead);

                Log.e(Constanta.TAG, soHead.toString());
            } catch (ParseException e) {
                e.printStackTrace();
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
            try {
                soHead.so = et_so.getText().toString();
                soHead.date_order = CommonUtil.stringToDateLong(et_po_date.getText().toString());
                soHead.delivery_date = CommonUtil.stringToDateLong(et_del_date.getText().toString());
                soHead.purchase_order = et_customer_po.getText().toString();
                soHead.whid = warehouse.whid;
                soHead.empid = employee.employee_id;
                soHead.priceType = type_of_price;
                soHead.updated_at = new Date();
                soHead.save();

                adapterSoHead.set(soHead);
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
                .content(soHead.so + " akan dihapus dari dohead ?")
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        adapterSoHead.delete(soHead);
                        soHead.delete();
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
    protected void clearForm(ViewGroup group) {
        super.clearForm(group);
        et_so.setText(SoHead.generateId());
        et_customer.setText(customer.name);
        et_salesman.setText(employee.name);
        et_customer_po.requestFocus();
    }

    @Override
    protected void enableButton(ViewGroup group) {
        super.enableButton(group);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btn_add.setBackground(getResources().getDrawable(R.drawable.button_add_item, activity.getTheme()));
        } else {
            btn_add.setBackground(getResources().getDrawable(R.drawable.button_add_item));
        }
        btn_del_date_picker.setEnabled(false);
        btn_po_date_picker.setEnabled(false);
        isReadyAddItem = true;
    }

    @Override
    protected void initListener() {
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                et_del_date.setText(CommonUtil.stringToDateHelper(dayOfMonth + "-" + (++monthOfYear) + "-" + year, Constanta.MEDIUM_UK));
            }
        };

        onWarehouseSelected = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                warehouse = Warehouse.load(Warehouse.class, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        onTypeOfPriceSelected = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if (isUpdate) {
                    new MaterialDialog.Builder(activity)
                            .title("Peringatan")
                            .content("Mengubah tipe harga akan berdampak pada perubahan nilai harga pada setiap item sales order dan total harga seluruh item")
                            .positiveText(R.string.agree)
                            .negativeText(R.string.disagree)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                    type_of_price = position + 1;
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                    sp_type_of_price.setSelection(type_of_price - 1);
                                }
                            })
                            .show();
                } else {
                    type_of_price = position + 1;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        onSoHeadLongClick = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                soHead = (SoHead) parent.getItemAtPosition(position);
                editDoHead(soHead);
                enableButton(layout);
                return false;
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_po_date_picker:
                actionDPD(HeaderSOFragment.this);
                break;
            case R.id.btn_del_date_picker:
                actionDPD(onDateSetListener);
                break;
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        et_po_date.setText(CommonUtil.stringToDateHelper(dayOfMonth + "-" + (++monthOfYear) + "-" + year, Constanta.MEDIUM_UK));
    }

    private void initComp() {
        layout = (RelativeLayout) view.findViewById(R.id.vg_fragment_header_so);
        et_so = (EditText) view.findViewById(R.id.et_doc_no);
        et_po_date = (EditText) view.findViewById(R.id.et_po_date);
        et_del_date = (EditText) view.findViewById(R.id.et_del_date);
        et_customer_po = (EditText) view.findViewById(R.id.et_customers_po);
        sp_warehouse = (Spinner) view.findViewById(R.id.sp_warehouse);
        sp_type_of_price = (Spinner) view.findViewById(R.id.sp_type_of_price);
        et_salesman = (EditText) view.findViewById(R.id.et_salesman);
        et_customer = (EditText) view.findViewById(R.id.et_customer);
        btn_po_date_picker = (Button) view.findViewById(R.id.btn_po_date_picker);
        btn_del_date_picker = (Button) view.findViewById(R.id.btn_del_date_picker);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_edit = (Button) view.findViewById(R.id.btn_edit);
        btn_del = (Button) view.findViewById(R.id.btn_delete);
        lv_so_head_items = (ListView) view.findViewById(R.id.lv_so_head_items);
    }

    private void actionDPD(DatePickerDialog.OnDateSetListener onDateSetListener) {

        DatePickerDialog dpd = getNewInstance(onDateSetListener);

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

    private DatePickerDialog getNewInstance(DatePickerDialog.OnDateSetListener onDateSetListener) {
        Calendar now = Calendar.getInstance();
        return DatePickerDialog.newInstance(
                onDateSetListener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
    }

    private void editDoHead(SoHead soHead) {
        customer = Customer.getCustomer(soHead.custid);
        employee = Employee.getEmployee(soHead.empid);
        warehouse = Warehouse.getWarehouse(soHead.whid);

        et_so.setText(soHead.so);
        et_po_date.setText(soHead.getPoDate());
        et_del_date.setText(soHead.getDelDate());
        et_customer_po.setText(soHead.purchase_order);
        int posWHouse = adapterWarehouse.getPosition(warehouse);
        sp_type_of_price.setSelection(soHead.priceType - 1);
        sp_warehouse.setSelection(posWHouse);
        et_customer.setText(customer.name);
        et_salesman.setText(employee.name);
        disableForm(layout);
    }

    private void actionAddItem() {
        mListener.onSetSoHead(soHead);
        clearForm(layout);
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

    private void setButtonEnable(Button button) {
        btn_add.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_del.setEnabled(false);
        btn_del_date_picker.setEnabled(true);
        btn_po_date_picker.setEnabled(true);
        button.setEnabled(true);
    }

    private void setButtonDisable(Button button) {
        btn_add.setEnabled(true);
        btn_edit.setEnabled(true);
        btn_del.setEnabled(true);
        btn_del_date_picker.setEnabled(true);
        btn_po_date_picker.setEnabled(true);
        button.setEnabled(false);
    }

    public void setOnSoHeadListener(OnSetSoHeadListener mListener) {
        this.mListener = mListener;
    }

    private boolean errorChecked() {
        if (et_po_date.getText().toString().equals("") ||
                et_po_date.getText() == null) {
            et_po_date.setError("Purchase Order Date tidak boleh kosong!");
            return true;
        } else if (et_del_date.getText().toString().equals("") ||
                et_del_date.getText() == null) {
            et_po_date.setError("Delivery Date tidak boleh kosong!");
            return true;
        } else if (et_customer_po.getText().toString().equals("") ||
                et_customer_po.getText() == null) {
            et_customer_po.setError("Customer's PO tidak boleh kosong!");
            return true;
        } else {
            return false;
        }
    }
}
