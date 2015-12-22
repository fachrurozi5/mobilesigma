package com.fachru.sigmamobile;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.fachru.sigmamobile.adapter.AdapterCustomer;
import com.fachru.sigmamobile.controller.CustomerController;
import com.fachru.sigmamobile.controller.interfaces.OnCustomerCallbackListener;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.utils.Constanta;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, OnCustomerCallbackListener{

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

        adapter = new AdapterCustomer(context,list);

        searchView.setOnQueryTextListener(this);

        listview.setAdapter(adapter);

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
            case R.id.action_sync :
                controller.startFetching();
                menuItem = item;
                menuItem.setActionView(R.layout.progressbar);
                menuItem.expandActionView();
                return true;
            case R.id.action_settings :
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void initComp() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (SearchView) findViewById(R.id.search_view);
        listview = (ListView) findViewById(R.id.lv_customer);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        adapter.update(list);
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
}
