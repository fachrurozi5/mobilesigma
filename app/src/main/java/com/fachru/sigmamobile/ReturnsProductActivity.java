package com.fachru.sigmamobile;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fachru.sigmamobile.fragment.ReturnsProductFragment;

public class ReturnsProductActivity extends AppCompatActivity implements
        OnTabSelectedListener {

    protected TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returns_product);
        initComp();
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

    private void initComp() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_order_product), true);
        tabLayout.setOnTabSelectedListener(this);*/
    }

    private void fragmentPosition(int position) {
        FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0:
                ReturnsProductFragment fragment = new ReturnsProductFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment).commit();
                break;
            default:
                break;
        }
    }
}
