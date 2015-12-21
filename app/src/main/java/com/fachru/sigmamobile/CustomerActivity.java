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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.fachru.sigmamobile.adapter.AdapterCustomer;
import com.fachru.sigmamobile.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private Context context = this;

    /*
    * widget
    * */
    private Toolbar toolbar;
    private SearchView searchView;
//    private RecyclerView recyclerView;
    private ListView listview;
    private MenuItem menuItem;

    /*
    * adapter
    * */
    private AdapterCustomer adapter;

    /*
    * manager
    * */
//    private RecyclerView.LayoutManager layoutManager;

    /*
    * arraylist
    * */
    private List<Customer> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        initComp();

//        layoutManager = new LinearLayoutManager(this);


        Customer customer = new Customer.Builder()
                .setId("9871")
                .setNama("Jhony Deep")
                .setInvaadd1("invaadd1")
                .setCsstatid1("csstatid1")
                .setSublsg(1.0)
                .builde();

        list.add(customer);

        customer = new Customer.Builder()
                .setId("46518")
                .setNama("Micahel Baptista")
                .setInvaadd1("invaadd1")
                .setCsstatid1("csstatid1")
                .setSublsg(1.0)
                .builde();

        list.add(customer);

        customer = new Customer.Builder()
                .setId("19901")
                .setNama("Rial Charless")
                .setInvaadd1("invaadd1")
                .setCsstatid1("csstatid1")
                .setSublsg(1.0)
                .builde();

        list.add(customer);

//        adapter = new AdapterCustomer(list);
        adapter = new AdapterCustomer(context,list);

        searchView.setOnQueryTextListener(this);

//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);

        listview.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer, menu);
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
                menuItem = item;
                menuItem.setActionView(R.layout.progressbar);
                menuItem.expandActionView();
                return true;
            case R.id.action_settings :
                menuItem.collapseActionView();
                menuItem.setActionView(null);
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void initComp() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (SearchView) findViewById(R.id.search_view);
//        recyclerView = (RecyclerView) findViewById(R.id.rv_customer);
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
}
