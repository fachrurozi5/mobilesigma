package com.fachru.sigmamobile.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.fachru.sigmamobile.CustomerActivity;
import com.fachru.sigmamobile.Login;
import com.fachru.sigmamobile.MainActivity;
import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.adapters.spinners.AdapterWarehouse;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.model.Warehouse;
import com.fachru.sigmamobile.utils.BaseFragmentForm;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * Created by fachru on 28/12/15.
 */
public class HeaderSOFragment extends BaseFragmentForm implements
        View.OnClickListener, DatePickerDialog.OnDateSetListener{

    protected Activity activity;
    protected Bundle bundle;

    /*
    * interface
    * */
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    /*
    * plain old java object
    * */
    protected Customer customer;
    protected Employee employee;

    /*
    * widget
    * */
    protected View view;
    protected RelativeLayout layout;
    protected EditText et_so;
    protected EditText et_po_date;
    protected EditText et_del_date;
    protected Spinner sp_warehouse;
    protected Spinner sp_type_of_price;
    protected EditText et_salesman;
    protected EditText et_customer;
    protected Button btn_po_date_picker;
    protected Button btn_del_date_picker;
    protected Button btn_add;
    protected Button btn_edit;
    protected Button btn_del;
    protected ListView lv_do_head_items;

    /*
    * adapter
    * */
    private ArrayAdapter<CharSequence> adapter;

    /*
    * custom adapter
    * */
    private AdapterWarehouse adapterWarehouse;

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

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_header_so, container, false);

        initComp();
        initListener();

        et_customer.setText(customer.name);
        et_salesman.setText(employee.name);

        btn_po_date_picker.setOnClickListener(this);
        btn_del_date_picker.setOnClickListener(this);

        sp_warehouse.setAdapter(adapterWarehouse);
        sp_type_of_price.setAdapter(adapter);

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
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                et_del_date.setText(CommonUtil.stringToDateHelper(dayOfMonth + "-" + (++monthOfYear) + "-" + year, Constanta.MEDIUM_UK));
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
        sp_warehouse = (Spinner) view.findViewById(R.id.sp_warehouse);
        sp_type_of_price = (Spinner) view.findViewById(R.id.sp_type_of_price);
        et_salesman = (EditText) view.findViewById(R.id.et_salesman);
        et_customer = (EditText) view.findViewById(R.id.et_customer);
        btn_po_date_picker = (Button) view.findViewById(R.id.btn_po_date_picker);
        btn_del_date_picker = (Button) view.findViewById(R.id.btn_del_date_picker);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_edit = (Button) view.findViewById(R.id.btn_edit);
        btn_del = (Button) view.findViewById(R.id.btn_delete);
        lv_do_head_items = (ListView) view.findViewById(R.id.lv_do_head_items);
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
}
