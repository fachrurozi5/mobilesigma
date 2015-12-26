package com.fachru.sigmamobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.adapter.AdapterCustomer;
import com.fachru.sigmamobile.controller.CustomerController;
import com.fachru.sigmamobile.controller.interfaces.OnCustomerCallbackListener;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity implements
        OnQueryTextListener, OnCustomerCallbackListener, OnItemLongClickListener, AdapterView.OnItemClickListener {

    private Context context = this;

    /*
    * widget
    * */
    private Toolbar toolbar;
    private SearchView searchView;
    private ListView listview;
    private MenuItem menuItem;

    /*
    * adapter
    * */
    private AdapterCustomer adapter;


    /*
    * controller
    * */
    private CustomerController controller;

    /*
    * arraylist
    * */
    private List<Customer> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        initComp();

        controller = new CustomerController(this);

        adapter = new AdapterCustomer(context, Customer.getAll());

        searchView.setOnQueryTextListener(this);

        listview.setAdapter(adapter);
        listview.setOnItemLongClickListener(this);
        listview.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer, menu);
        menuItem = menu.findItem(R.id.action_sync);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_sync:
                controller.startFetching();
                menuItem = item;
                menuItem.setActionView(R.layout.progressbar);
                menuItem.expandActionView();
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        /*showDetailCustomer((Customer) parent.getItemAtPosition(position));*/
        Customer customer = (Customer) parent.getItemAtPosition(position);
        Log.d(Constanta.TAG, customer.toString());
        showDetailCustomer(customer);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Customer customer = (Customer) parent.getItemAtPosition(position);
        showDoalog(customer.getId());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void onFetchStart() {
        menuItem.setActionView(R.layout.progressbar);
        menuItem.expandActionView();
    }

    @Override
    public void onFetchProgress(Customer customer) {

    }

    @Override
    public void onFetchProgress(List<Customer> list) {
        ActiveAndroid.beginTransaction();
        try {
            for (Customer customer : list)
                customer.save();

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
        adapter.update(Customer.getAll());
    }

    @Override
    public void onFetchComplete() {
        menuItem.collapseActionView();
        menuItem.setActionView(null);
    }

    @Override
    public void onFetchFailed(Throwable t) {
        menuItem.collapseActionView();
        menuItem.setActionView(null);
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void initComp() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (SearchView) findViewById(R.id.search_view);
        listview = (ListView) findViewById(R.id.lv_customer);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private EditText et_id;
    private EditText et_name;
    private EditText et_csstatid1;
    private EditText et_csstatid2;
    private EditText et_csstatid3;
    private EditText et_csstatid4;
    private EditText et_csstatid5;
    private EditText et_type_group;
    private EditText et_cs_group;
    private EditText et_channel;
    private EditText et_invadd1;
    private EditText et_invadd2;
    private EditText et_invadd3;
    private EditText et_invadd4;
    private EditText et_invzip;
    private EditText et_created;
    private EditText et_updated;

    private TextView tv_name;
    private TextView tv_csstatid1;
    private TextView tv_csstatid2;
    private TextView tv_csstatid3;
    private TextView tv_csstatid4;
    private TextView tv_csstatid5;
    private TextView tv_type_group;
    private TextView tv_cs_group;
    private TextView tv_channel;
    private TextView tv_invadd1;
    private TextView tv_invadd2;
    private TextView tv_invadd3;
    private TextView tv_invadd4;
    private TextView tv_invzip;
    private TextView tv_created;
    private TextView tv_updated;


    private void showDetailCustomer(Customer customer) {
        MaterialDialog dialog = new MaterialDialog.Builder(CustomerActivity.this)
                .title("Customer")
                .customView(R.layout.detail_customer, true)
                .negativeText("Tutup")
                .cancelable(false)
                .build();

        View view = dialog.getCustomView();

        et_id           = (EditText) view.findViewById(R.id.et_id);
        et_name         = (EditText) view.findViewById(R.id.et_name);
        et_csstatid1    = (EditText) view.findViewById(R.id.et_csstatid1);
        et_csstatid2    = (EditText) view.findViewById(R.id.et_csstatid2);
        et_csstatid3    = (EditText) view.findViewById(R.id.et_csstatid3);
        et_csstatid4    = (EditText) view.findViewById(R.id.et_csstatid4);
        et_csstatid5    = (EditText) view.findViewById(R.id.et_csstatid5);
        et_type_group   = (EditText) view.findViewById(R.id.et_type_group);
        et_cs_group     = (EditText) view.findViewById(R.id.et_cs_group);
        et_channel      = (EditText) view.findViewById(R.id.et_channel);
        et_invadd1      = (EditText) view.findViewById(R.id.et_invadd1);
        et_invadd2      = (EditText) view.findViewById(R.id.et_invadd2);
        et_invadd3      = (EditText) view.findViewById(R.id.et_invadd3);
        et_invadd4      = (EditText) view.findViewById(R.id.et_invadd4);
        et_invzip       = (EditText) view.findViewById(R.id.et_invzip);
        et_created       = (EditText) view.findViewById(R.id.et_created);
        et_updated       = (EditText) view.findViewById(R.id.et_updated);

        tv_name         = (TextView) view.findViewById(R.id.tv_name);
        tv_csstatid1    = (TextView) view.findViewById(R.id.tv_csstatid1);
        tv_csstatid2    = (TextView) view.findViewById(R.id.tv_csstatid2);
        tv_csstatid3    = (TextView) view.findViewById(R.id.tv_csstatid3);
        tv_csstatid4    = (TextView) view.findViewById(R.id.tv_csstatid4);
        tv_csstatid5    = (TextView) view.findViewById(R.id.tv_csstatid5);
        tv_type_group   = (TextView) view.findViewById(R.id.tv_type_group);
        tv_cs_group     = (TextView) view.findViewById(R.id.tv_cs_group);
        tv_channel      = (TextView) view.findViewById(R.id.tv_channel);
        tv_invadd1      = (TextView) view.findViewById(R.id.tv_invadd1);
        tv_invadd2      = (TextView) view.findViewById(R.id.tv_invadd2);
        tv_invadd3      = (TextView) view.findViewById(R.id.tv_invadd3);
        tv_invadd4      = (TextView) view.findViewById(R.id.tv_invadd4);
        tv_invzip       = (TextView) view.findViewById(R.id.tv_invzip);
        tv_created      = (TextView) view.findViewById(R.id.tv_created);
        tv_updated      = (TextView) view.findViewById(R.id.tv_updated);

        setDataCustomer(customer);

        dialog.show();
    }


    private void setDataCustomer(Customer customer) {

        et_id.setText(customer.getCustomerId());

        if (!(customer.getName().equals("") || customer.getName().equals("-"))) {
            et_name.setText(customer.getName());
        } else {
            et_name.setVisibility(View.GONE);
            tv_name.setVisibility(View.GONE);
        }

        if (!(customer.getCsstatid1().equals("") || customer.getCsstatid1().equals("-"))) {
            et_csstatid1.setText(customer.getCsstatid1());
        } else {
            et_csstatid1.setVisibility(View.GONE);
            tv_csstatid1.setVisibility(View.GONE);
        }

        if (!(customer.getCsstatid2().equals("") || customer.getCsstatid2().equals("-"))) {
            et_csstatid2.setText(customer.getCsstatid2());
        } else {
            et_csstatid2.setVisibility(View.GONE);
            tv_csstatid2.setVisibility(View.GONE);
        }

        if (!(customer.getCsstatid3().equals("") || customer.getCsstatid3().equals("-"))) {
            et_csstatid3.setText(customer.getCsstatid3());
        } else {
            et_csstatid3.setVisibility(View.GONE);
            tv_csstatid3.setVisibility(View.GONE);
        }

        if (!(customer.getCsstatid4().equals("") || customer.getCsstatid4().equals("-"))) {
            et_csstatid4.setText(customer.getCsstatid4());
        } else {
            et_csstatid4.setVisibility(View.GONE);
            tv_csstatid4.setVisibility(View.GONE);
        }

        if (!(customer.getCsstatid5().equals("") || customer.getCsstatid5().equals("-"))) {
            et_csstatid5.setText(customer.getCsstatid5());
        } else {
            et_csstatid5.setVisibility(View.GONE);
            tv_csstatid5.setVisibility(View.GONE);
        }

        if (!(customer.getGroup_type().equals("") || customer.getGroup_type().equals("-"))) {
            et_type_group.setText(customer.getGroup_type());
        } else {
            et_type_group.setVisibility(View.GONE);
            tv_type_group.setVisibility(View.GONE);
        }

        if (!(customer.getCs_group().equals("") || customer.getCs_group().equals("-"))) {
            et_cs_group.setText(customer.getCs_group());
        } else {
            et_cs_group.setVisibility(View.GONE);
            tv_cs_group.setVisibility(View.GONE);
        }

        if (!(customer.getChannel().equals("") || customer.getChannel().equals("-"))) {
            et_channel.setText(customer.getChannel());
        } else {
            et_channel.setVisibility(View.GONE);
            tv_channel.setVisibility(View.GONE);
        }

        if (!(customer.getInvadd1().equals("") || customer.getInvadd1().equals("-"))) {
            et_invadd1.setText(customer.getInvadd1());
        } else {
            et_invadd1.setVisibility(View.GONE);
            tv_invadd1.setVisibility(View.GONE);
        }

        if (!(customer.getInvadd2().equals("") || customer.getInvadd2().equals("-"))) {
            et_invadd2.setText(customer.getInvadd2());
        } else {
            et_invadd2.setVisibility(View.GONE);
            tv_invadd2.setVisibility(View.GONE);
        }

        if (!(customer.getInvadd3().equals("") || customer.getInvadd3().equals("-"))) {
            et_invadd3.setText(customer.getInvadd3());
        } else {
            et_invadd3.setVisibility(View.GONE);
            tv_invadd3.setVisibility(View.GONE);
        }

        if (!(customer.getInvadd4().equals("") || customer.getInvadd4().equals("-"))) {
            et_invadd4.setText(customer.getInvadd4());
        } else {
            et_invadd4.setVisibility(View.GONE);
            tv_invadd4.setVisibility(View.GONE);
        }

        if (!(customer.getInvzip().equals("") || customer.getInvzip().equals("-"))) {
            et_invzip.setText(customer.getInvzip());
        } else {
            et_invzip.setVisibility(View.GONE);
            tv_invzip.setVisibility(View.GONE);
        }

        if (customer.getCreated_at() != null) {
            et_created.setText(CommonUtil.dateHelper(customer.getCreated_at(), Constanta.ID_LONG));
        } else {
            et_created.setVisibility(View.GONE);
            tv_created.setVisibility(View.GONE);
        }

        if (customer.getUpdated_at() != null) {
            et_updated.setText(CommonUtil.dateHelper(customer.getUpdated_at(), Constanta.ID_LONG));
        } else {
            et_updated.setVisibility(View.GONE);
            tv_updated.setVisibility(View.GONE);
        }

    }

    public void showDoalog(final Long id) {
        new MaterialDialog.Builder(this)
                .title("OSC")
                .iconRes(android.R.drawable.ic_dialog_info)
                .items(R.array.sub_menus_osc)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        switch (i) {
                            case 0:

                                break;
                            case 1:
                                Intent intent = new Intent(context, ChooseROActivity.class);
                                intent.putExtra("id", id);
                                startActivity(intent);
                                break;
                            case 2:
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }
}
