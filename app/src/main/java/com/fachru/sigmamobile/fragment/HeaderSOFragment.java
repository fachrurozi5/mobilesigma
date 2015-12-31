package com.fachru.sigmamobile.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fachru.sigmamobile.CustomerActivity;
import com.fachru.sigmamobile.MainActivity;
import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.model.Employee;
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
    * plain old java object
    * */
    protected Customer customer;
    protected Employee employee;

    /*
    * widget
    * */
    protected View view;
    protected RelativeLayout layout;
    protected EditText et_doc_no;
    protected EditText et_doc_date;
    protected EditText et_rute;
    protected EditText et_salesman;
    protected EditText et_customer;
    protected AutoCompleteTextView act_salesman;
    protected AutoCompleteTextView act_outlet;
    protected Button btn_date_picker;
    protected Button btn_add;
    protected Button btn_edit;
    protected Button btn_del;
    protected ListView lv_do_head_items;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        bundle = getArguments();
        customer = Customer.load(Customer.class, bundle.getLong(CustomerActivity.CUSTID));
        employee = Employee.load(Employee.class, bundle.getLong(MainActivity.EMPLID));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_header_so, container, false);
        initComp();
        et_customer.setText(customer.getName());
        et_salesman.setText(employee.name);
        btn_date_picker.setOnClickListener(this);
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_date_picker:
                actionDPD();
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        et_doc_date.setText(CommonUtil.stringToDateHelper(dayOfMonth + "-" + (++monthOfYear) + "-" + year, Constanta.MEDIUM_UK));
    }

    private void initComp() {
        layout = (RelativeLayout) view.findViewById(R.id.vg_fragment_header_so);
        et_doc_no = (EditText) view.findViewById(R.id.et_doc_no);
        et_doc_date = (EditText) view.findViewById(R.id.et_doc_date);
        et_rute = (EditText) view.findViewById(R.id.et_rute);
        et_salesman = (EditText) view.findViewById(R.id.et_salesman);
        et_customer = (EditText) view.findViewById(R.id.et_customer);
        act_salesman = (AutoCompleteTextView) view.findViewById(R.id.act_salesman);
        act_outlet = (AutoCompleteTextView) view.findViewById(R.id.act_outlet);
        btn_date_picker = (Button) view.findViewById(R.id.btn_date_picker);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_edit = (Button) view.findViewById(R.id.btn_edit);
        btn_del = (Button) view.findViewById(R.id.btn_delete);
        lv_do_head_items = (ListView) view.findViewById(R.id.lv_do_head_items);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dpd.setAccentColor(getResources().getColor(R.color.colorAccentDialog, activity.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dpd.setAccentColor(ContextCompat.getColor(activity, R.color.colorAccentDialog));
        } else {
            dpd.setAccentColor(getResources().getColor(R.color.colorAccentDialog));
        }
        dpd.show(activity.getFragmentManager(), getString(R.string.tag_date_picker_dialog));
    }
}
