package com.fachru.sigmamobile.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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

import com.fachru.sigmamobile.CustomerActivity;
import com.fachru.sigmamobile.Login;
import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.adapters.AdapterSoHead;
import com.fachru.sigmamobile.adapters.spinners.AdapterWarehouse;
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
import java.util.List;

/**
 * Created by fachru on 28/12/15.
 */
public class HeaderSOFragment extends BaseFragmentForm implements
        View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private Activity activity;
    private Bundle bundle;

    /*
    * interface
    * */
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private AdapterView.OnItemSelectedListener onWarehouseSelected;
    private AdapterView.OnItemSelectedListener onTypeOfPriceSelected;

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

        soHeads = SoHead.getAll();
        adapterSoHead = new AdapterSoHead(activity, soHeads);

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
        sp_warehouse.setOnItemSelectedListener(onWarehouseSelected);
        sp_type_of_price.setOnItemSelectedListener(onTypeOfPriceSelected);

        sp_warehouse.setAdapter(adapterWarehouse);
        sp_type_of_price.setAdapter(adapter);
        lv_so_head_items.setAdapter(adapterSoHead);
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

    }

    @Override
    protected void actionUpdate() {

    }

    @Override
    protected void actionDelete() {

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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type_of_price = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                actionAdd();
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
                et_customer_po.getText() == null){
            et_customer_po.setError("Customer's PO tidak boleh kosong!");
            return true;
        } else {
            return false;
        }
    }
}
