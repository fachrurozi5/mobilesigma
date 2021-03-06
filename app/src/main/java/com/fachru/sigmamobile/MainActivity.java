package com.fachru.sigmamobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.adapters.ImageAdapter;
import com.fachru.sigmamobile.model.Discount;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.model.OSCManagement;
import com.fachru.sigmamobile.service.RTCService;
import com.fachru.sigmamobile.service.SigmaSync;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;
import com.fachru.sigmamobile.utils.SessionManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity implements OnItemClickListener {

	protected Context context = this;
	protected SessionManager sessionManager;
	/*
	* widget
	* */
	protected Toolbar toolbar;
	protected GridView gridview;
	protected TextView text_time;
	protected TextView text_date;
	protected TextView text_location;
	/*private Intent serviceIntent;*/
	/*private SaveMyAppsService mService;*/
	/*
    * label
    * */
	private double latitude = 0;
	private double longitude = 0;

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				if (intent.hasExtra(Constanta.RESULT_DATE)) {
					text_time.setText(bundle.getString(Constanta.RESULT_TIME));
					text_date.setText(bundle.getString(Constanta.RESULT_DATE));
				}

				if (intent.hasExtra(Constanta.RESULT_ADDRESS)) {
					text_location.setText(bundle.getString(Constanta.RESULT_ADDRESS));
				}

				if (intent.hasExtra(Constanta.LATITUDE)) {
					latitude = bundle.getDouble(Constanta.LATITUDE, 0);
					longitude = bundle.getDouble(Constanta.LONGITUDE, 0);
				}
			} else {
				Toast.makeText(MainActivity.this, "FAILED",
						Toast.LENGTH_LONG).show();
			}
		}
	};
    /*private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
           *//* SaveMyAppsService.LocalBinder binder = (SaveMyAppsService.LocalBinder) service;
            mService = binder.getServiceInstance();*//*
            List<String> strings = new ArrayList<>();
            strings.add("memo");
            strings.add("contacts");
            strings.add("phone");
            strings.add("telegram");
            strings.add("samsungapps");
            strings.add("vending");
            strings.add("mms");
            strings.add("call");
            strings.add("apps.plus");
            *//*mService.setListPackage(strings);*//*
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(context, "onServiceDisconnected called", Toast.LENGTH_SHORT).show();
        }
    };*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (scanningResult != null) {
			String scanContent = scanningResult.getContents();
			String scanFormat = scanningResult.getFormatName();
			Toast.makeText(getApplicationContext(),
					"Format : " + scanFormat + ", Content : " + scanContent,
					Toast.LENGTH_SHORT).show();
		} else if (resultCode == Constanta.RESULT_OK
				&& requestCode == Constanta.REQUEST_CODE)
			if (data.hasExtra(Constanta.RESULT_DATE)) {
				text_date.setText(data.getStringExtra(Constanta.RESULT_DATE));
				text_time.setText(data.getStringExtra(Constanta.RESULT_TIME));
				text_location.setText(data.getStringExtra(Constanta.RESULT_ADDRESS));
			}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sessionManager = new SessionManager(context);
		Log.e(Constanta.TAG, SessionManager.pref().getString(SessionManager.KEY_TOKEN, ""));
        /*serviceIntent = new Intent(context, SaveMyAppsService.class);*/
		initComp();

		setSupportActionBar(toolbar);

		getSupportActionBar().setLogo(R.drawable.ic_action_bar);

		if (savedInstanceState != null) {
			text_date.setText(savedInstanceState.getString(Constanta.RESULT_DATE));
			text_time.setText(savedInstanceState.getString(Constanta.RESULT_TIME));
			text_location.setText(savedInstanceState.getString(Constanta.RESULT_ADDRESS));
		}

		gridview.setAdapter(new ImageAdapter(this,
				getResources().getStringArray(R.array.menus),
				getResources().obtainTypedArray(R.array.icon_menus)));

		gridview.setOnItemClickListener(this);

		if (sessionManager.getAffterInstall()) {
			actionAfterInstall();

		}

		if (!CommonUtil.isLocationOn(context)) {
			turnGPSOn();
		}

		long employeeId = SessionManager.pref().getLong(SessionManager.KEY_EMPLOYEE, -1);
		Employee employee = Employee.load(Employee.class, employeeId);
		if (employee != null) {
			OSCManagement oscManagement = OSCManagement.getToDayManagementByEmployee(employee.getEmployeeId());
			if (oscManagement == null) {
				oscManagement = new OSCManagement(employee);
				oscManagement.save();
				SessionManager.prefEdit().putLong(SessionManager.KEY_OSC_MANAGEMENT, oscManagement.getId()).commit();
				Log.e(Constanta.TAG, "After null " + oscManagement.toString());
			} else {
				SessionManager.prefEdit().putLong(SessionManager.KEY_OSC_MANAGEMENT, oscManagement.getId()).commit();
				Log.e(Constanta.TAG, oscManagement.toString());
			}
		}

//        startService(new Intent(context, LocationTrackerService.class));
//        startService(new Intent(context, RealTimeClockInterrupt.class));
//        startService(new Intent(context, SaveMyAppsService.class));
		startService(new Intent(this, SigmaSync.class));
		startService(new Intent(this, RTCService.class));


	}

	@Override
	protected void onStart() {
		super.onStart();
        /*bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);*/
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
	protected void onStop() {
		super.onStop();
        /*unbindService(mConnection);*/

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(Constanta.RESULT_DATE, text_date.getText().toString());
		outState.putString(Constanta.RESULT_TIME, text_time.getText().toString());
		outState.putString(Constanta.RESULT_ADDRESS, text_location.getText().toString());

		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Intent intent = new Intent(context, CameraActivity.class);
			intent.putExtra(Constanta.LATITUDE, latitude);
			intent.putExtra(Constanta.LONGITUDE, longitude);
			intent.putExtra(Constanta.RESULT_ADDRESS, text_location.getText().toString());
			startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		switch (position) {
			case 4:
				showDialog();
				break;
			default:
				break;
		}
	}

	private void initComp() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		gridview = (GridView) findViewById(R.id.gridmenu);
		text_time = (TextView) findViewById(R.id.text_time);
		text_date = (TextView) findViewById(R.id.text_date);
		text_location = (TextView) findViewById(R.id.text_locality);
	}

	private void actionAfterInstall() {

		sessionManager.setAfterInstall(false);
	}

	private void turnGPSOn() {
		new MaterialDialog.Builder(context)
				.title(R.string.title_location_dialog)
				.content(R.string.content_location_dialog)
				.positiveText(R.string.agree)
				.iconRes(R.drawable.ic_location_off_white_48dp)
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
						Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
					}
				})
				.show();
	}

	public void showDialog() {
		new MaterialDialog.Builder(this)
				.title("OSC")
				.iconRes(android.R.drawable.ic_dialog_info)
				.items(R.array.sub_menus_osc)
				.itemsCallback(new MaterialDialog.ListCallback() {
					@Override
					public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
						Intent intent = null;
						switch (i) {
							case 0:
								scanBarcode();
								break;
							case 1:
								intent = new Intent(context, OutletActivity.class);
								intent.putExtra(Constanta.RESULT_DATE, text_date.getText().toString());
								intent.putExtra(Constanta.RESULT_TIME, text_time.getText().toString());
								intent.putExtra(Constanta.RESULT_ADDRESS, text_location.getText().toString());
								startActivityForResult(intent, Constanta.REQUEST_CODE);
								break;
							case 2:
								intent = new Intent(context, NewOutletActivity.class);
								startActivity(intent);
								break;
							default:
								break;
						}
					}
				}).show();
	}

	private void scanBarcode() {
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}

}
