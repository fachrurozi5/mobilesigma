package com.fachru.sigmamobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fachru.sigmamobile.fragment.HeaderReturns;
import com.fachru.sigmamobile.fragment.ReturnsFragment;
import com.fachru.sigmamobile.fragment.interfaces.OnSetSoHeadListener;
import com.fachru.sigmamobile.model.SoHead;
import com.fachru.sigmamobile.utils.Constanta;

public class ReturnsProductActivity extends AppCompatActivity implements
		OnTabSelectedListener, OnSetSoHeadListener {

	protected TabLayout tabLayout;

	private SoHead soHead;

	private long custid;
	private long emplid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_returns_product);
		initComp();
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
	public void onSetSoHead(SoHead soHead) {
		this.soHead = soHead;
		tabLayout.getTabAt(1).select();
	}

	@Override
	public void unSetSoHead() {

	}

	private void initComp() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_list_so), true);
		tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_product_return), false);
		tabLayout.setOnTabSelectedListener(this);
	}

	private void fragmentPosition(int position) {
		FragmentTransaction fragmentTransaction;
		Fragment fragment = null;
		Bundle bundle;
		switch (position) {
			case 0:
				fragment = new HeaderReturns();
				((HeaderReturns) fragment).setOnSoHeadListener(this);
				bundle = new Bundle();
				bundle.putLong(CustomerActivity.CUSTID, custid);
				bundle.putLong(Login.EMPLID, emplid);
				fragment.setArguments(bundle);
				break;
			case 1:
				fragment = new ReturnsFragment();
				if (soHead != null) {
					bundle = new Bundle();
					bundle.putString(Constanta.KEY_SO, soHead.so);
					bundle.putString(Constanta.KEY_WHID, soHead.whid);
					fragment.setArguments(bundle);
				}
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
