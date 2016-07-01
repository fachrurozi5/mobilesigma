package com.fachru.sigmamobile;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.fachru.sigmamobile.adapters.ImageAdapter;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.model.OSCManagement;
import com.fachru.sigmamobile.model.OSCManagementDetail;
import com.fachru.sigmamobile.model.Outlet;
import com.fachru.sigmamobile.model.SoHead;
import com.fachru.sigmamobile.service.RTCService;
import com.fachru.sigmamobile.utils.Constanta;
import com.fachru.sigmamobile.utils.SessionManager;

import java.util.Date;

public class ChooseROActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

	private Context context = this;
	private Intent intent;

	/*
	* widget
	* */
	private Toolbar toolbar;
	private GridView gridview;
	private TextView text_time;
	private TextView text_date;
	private TextView text_location;
	private TextView textViewOutletInfo;

	/*
	* utils
	* */
	private SessionManager manager;

	/*
	* label
	* */
	private long outletId;

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				if (intent.hasExtra(Constanta.RESULT_DATE)) {
					text_time.setText(bundle.getString(Constanta.RESULT_TIME));
					text_date.setText(bundle.getString(Constanta.RESULT_DATE));
				} else if (intent.hasExtra(Constanta.RESULT_ADDRESS)) {
					text_location.setText(bundle.getString(Constanta.RESULT_ADDRESS));
				}
			} else {
				Toast.makeText(context, "FAILED",
						Toast.LENGTH_LONG).show();
			}
		}
	};

	private RTCService mService;
	private boolean mBound = false;
	/**
	 * Defines callbacks for service binding, passed to bindService()
	 */
	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get LocalService instance
			RTCService.LocalBinder binder = (RTCService.LocalBinder) service;
			mService = binder.getService();
			mService.setActiveOutlet(true);
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBound = false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_ro);
		manager = new SessionManager(context);
		Intent intent = getIntent();
		initComp();
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		gridview.setAdapter(new ImageAdapter(this, getResources().getStringArray(R.array.sub_menu_c_ro),
				getResources().obtainTypedArray(R.array.icon_sub_menu_c_ro)));
		gridview.setOnItemClickListener(this);

		text_date.setText(intent.getStringExtra(Constanta.RESULT_DATE));
		text_time.setText(intent.getStringExtra(Constanta.RESULT_TIME));
		text_location.setText(intent.getStringExtra(Constanta.RESULT_ADDRESS));

//        outletId = intent.getLongExtra(CustomerActivity.CUSTID, -1);
		outletId = intent.getLongExtra(OutletActivity.OUTLET_ID, -1);

		if (outletId >= 0)
			manager.setCustomer(outletId);
		Outlet outlet = Outlet.load(Outlet.class, manager.getCustomer());
		StringBuilder builder = new StringBuilder();
		builder.append(outlet.getOutletId()).append(" | ").append(outlet.getName()).append(" | Lat:").append(outlet.getLat()).append(" | Lng:").append(outlet.getLng());
		textViewOutletInfo.setText(builder.toString());

		long oscManagerId = manager.getOSCManager();
		OSCManagement oscManagement = OSCManagement.load(OSCManagement.class, oscManagerId);
		OSCManagementDetail detail = OSCManagementDetail.get(oscManagement.getOscManagementId(), outlet.getOutletId());
		if (detail == null) {
			detail = new OSCManagementDetail();
			detail.setOscManagement(oscManagement);
			detail.setOscManagementId(oscManagement.getOscManagementId());
			detail.setOutletId(outlet.getOutletId());
			detail.setEntered_at(new Date());
			detail.save();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		intent = new Intent(this, RTCService.class);
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(
				receiver, new IntentFilter(Constanta.SERVICE_RECEIVER)
		);
	}

	@Override
	protected void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
	}

	@Override
	public void onBackPressed() {
		activityResult();
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		long outletId = manager.getCustomer();
		Outlet outlet = Outlet.load(Outlet.class, outletId);

		long oscManagerId = manager.getOSCManager();
		OSCManagement oscManagement = OSCManagement.load(OSCManagement.class, oscManagerId);

		OSCManagementDetail detail = OSCManagementDetail.get(oscManagement.getOscManagementId(), outlet.getOutletId());

		if (detail != null) {
			detail.setExit_at(new Date());
			detail.setActiveCount(mService.getActiveCount());
			detail.setNonActiveCount(mService.getNonActiveCount());
			detail.setTotalOrder(SoHead.getToDayTotalOrderByOutlet(outlet.getOutletId()));
			detail.save();
		}

		mService.setActiveOutlet(false);
		mService.setOutlet_id(-1);

		Log.e("RTCService", "Active Count " + mService.getActiveCount());
		Log.e("RTCService", "Nonactive Count " + mService.getNonActiveCount());
		if (mBound) {
			unbindService(connection);
			mBound = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				activityResult();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		Intent intent = null;

		switch (position) {
			case 2:
				intent = new Intent(context, PointOfSaleActivity.class);
				break;
			case 3:
				intent = new Intent(context, SalesOrderActivity.class);
				break;
			case 4:
				intent = new Intent(context, ReturnsProductActivity.class);
				break;
			default:
				break;
		}

		if (intent != null) {
			intent.putExtra(OutletActivity.OUTLET_ID, manager.getCustomer());
			intent.putExtra(Login.EMPLID, manager.getEmployee());
			startActivity(intent);
		}
	}

	private void initComp() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		gridview = (GridView) findViewById(R.id.gridmenu);
		text_time = (TextView) findViewById(R.id.text_time);
		text_date = (TextView) findViewById(R.id.text_date);
		text_location = (TextView) findViewById(R.id.text_locality);
		textViewOutletInfo = (TextView) findViewById(R.id.textViewOutletInfo);
	}

	private void activityResult() {
		Intent intent = new Intent();
		intent.putExtra(Constanta.RESULT_DATE, text_date.getText().toString());
		intent.putExtra(Constanta.RESULT_TIME, text_time.getText().toString());
		intent.putExtra(Constanta.RESULT_ADDRESS, text_location.getText().toString());
		setResult(2, intent);
		finish();
	}

}
