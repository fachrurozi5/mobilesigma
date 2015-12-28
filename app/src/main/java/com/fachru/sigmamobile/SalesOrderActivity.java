package com.fachru.sigmamobile;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fachru.sigmamobile.fragment.DoneOrderFragment;
import com.fachru.sigmamobile.fragment.HeaderPOSFragment;
import com.fachru.sigmamobile.fragment.HeaderSOFragment;
import com.fachru.sigmamobile.fragment.PointOfSaleFragment;
import com.fachru.sigmamobile.utils.Constanta;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class SalesOrderActivity extends AppCompatActivity implements OnTabSelectedListener {


    /*
    * TAG
    * */
    protected static final String TAG_DO_HEAD = "doheadtag";
    protected static final String TAG_DO_ITEM = "doitemtag";
    protected static final String TAG_DONE_ORDER = "doneoredertag";

    /*
    * widget
    * */
    protected Toolbar toolbar;
    protected TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order);
        initComp();
        setSupportActionBar(toolbar);

        fragmentPosition(0);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void initComp() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_order), true);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_order_product), false);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_order_done), false);
        tabLayout.setOnTabSelectedListener(this);

    }

    private void fragmentPosition(int position) {
        FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0:
                HeaderSOFragment fragment = new HeaderSOFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment, TAG_DO_HEAD).commit();
                break;
            default:
                break;
        }
    }


}
