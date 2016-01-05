package com.fachru.sigmamobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.fachru.sigmamobile.fragment.DoneOrderFragment;
import com.fachru.sigmamobile.fragment.HeaderPOSFragment;
import com.fachru.sigmamobile.fragment.HeaderSOFragment;
import com.fachru.sigmamobile.fragment.PointOfSaleFragment;
import com.fachru.sigmamobile.fragment.SalesOrderFragment;
import com.fachru.sigmamobile.fragment.interfaces.OnSetDoItemListener;
import com.fachru.sigmamobile.utils.Constanta;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class SalesOrderActivity extends AppCompatActivity implements OnTabSelectedListener, OnSetDoItemListener {


    /*
    * TAG
    * */
    protected static final String TAG_DO_HEAD = "doheadtag";
    protected static final String TAG_DO_ITEM = "doitemtag";

    /*
    * widget
    * */
    protected Toolbar toolbar;
    protected TabLayout tabLayout;
    private long custid;
    private long emplid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order);
        initComp();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        custid = intent.getLongExtra(CustomerActivity.CUSTID, -1);
        emplid = intent.getLongExtra(Login.EMPLID, -1);
        fragmentPosition(0);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        fragmentPosition(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void unSetDoItem() {

        Log.d(Constanta.TAG, "unSetDoItem");
    }

    private void initComp() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_order), true);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_order_product), false);
        tabLayout.setOnTabSelectedListener(this);

    }

    private void fragmentPosition(int position) {
        FragmentTransaction fragmentTransaction;
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new HeaderSOFragment();
                Bundle bundle = new Bundle();
                bundle.putLong(CustomerActivity.CUSTID, custid);
                bundle.putLong(Login.EMPLID, emplid);
                fragment.setArguments(bundle);
                break;
            case 1:
                SalesOrderFragment salesOrderFragment = new SalesOrderFragment();
                salesOrderFragment.setOnDoItemListener(this);
                fragment = salesOrderFragment;
                break;
            default:
                break;
        }

        if (fragment != null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment).commit();

        }
    }
}
