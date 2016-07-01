package com.fachru.sigmamobile;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.adapters.recycle.AdapterOutlet;
import com.fachru.sigmamobile.model.Outlet;
import com.fachru.sigmamobile.model.OutletType;
import com.fachru.sigmamobile.service.RTCService;
import com.fachru.sigmamobile.utils.Constanta;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class OutletActivity extends AppCompatActivity implements AdapterOutlet.OnItemClickListener {


	public static final String OUTLET_ID = "key_custid";

	private Context context = this;
	private Intent intent;

	/**
	 * widget
	 */
	private CoordinatorLayout coordinatorLayout;
	private Toolbar toolbar;
	private RecyclerView rv_outlet;
	private RecyclerView.Adapter adapter;
	private RecyclerView.LayoutManager layoutManager;

	/*
	* label
	* */
	private String text_time = "";
	private String text_date = "";
	private String text_location = "";

	/*
	* reciever datetime
	* */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				if (intent.hasExtra(Constanta.RESULT_DATE)) {
					text_time = bundle.getString(Constanta.RESULT_TIME);
					text_date = bundle.getString(Constanta.RESULT_DATE);
				}

				if (intent.hasExtra(Constanta.RESULT_ADDRESS)) {
					text_location = bundle.getString(Constanta.RESULT_ADDRESS);
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
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outlet);
		initComponent();

		layoutManager = new LinearLayoutManager(this);
		rv_outlet.setLayoutManager(layoutManager);

		adapter = new AdapterOutlet(new Select().from(Outlet.class).<Outlet>execute(), this);
		rv_outlet.setAdapter(adapter);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		intent = new Intent(this, RTCService.class);
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Outlet Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.fachru.sigmamobile/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
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
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Outlet Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.fachru.sigmamobile/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.disconnect();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		/*mService.setActiveOutlet(false);
		mService.setOutlet_id(-1);*/
		if (mBound) {
			unbindService(connection);
			mBound = false;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		activityResult();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Constanta.RESULT_OK
				&& requestCode == Constanta.REQUEST_CODE) {
			if (data.hasExtra(Constanta.RESULT_DATE)) {
				text_date = data.getStringExtra(Constanta.RESULT_DATE);
				text_time = data.getStringExtra(Constanta.RESULT_TIME);
			}

			if (data.hasExtra(Constanta.RESULT_ADDRESS))
				text_location = data.getStringExtra(Constanta.RESULT_ADDRESS);
		}
	}

	@Override
	public void onChooseClick(Outlet outlet) {
		mService.setActiveOutlet(true);
		mService.setOutlet_id(outlet.getId());
		Intent intent = new Intent(context, ChooseROActivity.class);
		intent.putExtra(OUTLET_ID, outlet.getId());
		intent.putExtra(Constanta.RESULT_DATE, text_date);
		intent.putExtra(Constanta.RESULT_TIME, text_time);
		intent.putExtra(Constanta.RESULT_ADDRESS, text_location);
		startActivityForResult(intent, Constanta.REQUEST_CODE);
	}

	@Override
	public void onDetailClick(Outlet outlet) {
		MaterialDialog dialog = initDialog(true);

		View view = dialog.getCustomView();
		TextView textViewId = (TextView) view.findViewById(R.id.textViewId);
		TextView textViewName = (TextView) view.findViewById(R.id.textViewName);
		TextView textViewAddress = (TextView) view.findViewById(R.id.textViewAddress);
		TextView textViewOwner = (TextView) view.findViewById(R.id.textViewOwner);
		TextView textViewSecondOwner = (TextView) view.findViewById(R.id.textViewSecondOwner);
		TextView textViewThirdOwner = (TextView) view.findViewById(R.id.textViewThirdOwner);
		TextView textViewTelp = (TextView) view.findViewById(R.id.textViewTelp);
		TextView textViewMobile = (TextView) view.findViewById(R.id.textViewMobile);
		TextView textViewLat = (TextView) view.findViewById(R.id.textViewLat);
		TextView textViewLng = (TextView) view.findViewById(R.id.textViewLng);
		TextView textViewPayment = (TextView) view.findViewById(R.id.textViewPayment);
		TextView textViewTypeOutlet = (TextView) view.findViewById(R.id.textViewTypeOutlet);

		textViewId.setText(outlet.getOutletId());
		textViewName.setText(outlet.getName());
		textViewAddress.setText(getValueOrNull(outlet.getAddress()));
		textViewOwner.setText(getValueOrNull(outlet.getOwner()));
		textViewSecondOwner.setText(getValueOrNull(outlet.getSecondOwner()));
		textViewThirdOwner.setText(getValueOrNull(outlet.getThirdOwner()));
		textViewTelp.setText(getValueOrDash(outlet.getTelephoneNumber()));
		textViewMobile.setText(getValueOrDash(outlet.getMobile()));
		textViewLat.setText(String.valueOf(outlet.getLat()));
		textViewLng.setText(String.valueOf(outlet.getLng()));
		textViewPayment.setText(outlet.getPayment());
		OutletType type = OutletType.find(outlet.getOutletTypeId());
		textViewTypeOutlet.setText(getValueOrNull(type != null ? type.getName() : "null"));
	}

	/**
	 * initial component
	 */
	private void initComponent() {
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		rv_outlet = (RecyclerView) findViewById(R.id.rv_outlets);
	}

	private MaterialDialog initDialog(boolean wrapInScrollView) {
		return new MaterialDialog.Builder(this)
				.title("Detail Outlet")
				.customView(R.layout.dialog_detail_outlet, wrapInScrollView)
				.positiveText("OK")
				.show();
	}

	private String getValueOrNull(String s) {
		if (s != null)
			if (!s.trim().equals("")) return s;

		return "null";
	}

	private String getValueOrDash(String s) {
		if (s != null)
			if (!s.trim().equals("")) return s;

		return "-";
	}

	private void activityResult() {
		Intent intent = new Intent();
		intent.putExtra(Constanta.RESULT_DATE, text_date);
		intent.putExtra(Constanta.RESULT_TIME, text_time);
		intent.putExtra(Constanta.RESULT_ADDRESS, text_location);
		setResult(Constanta.RESULT_OK, intent);
		finish();
	}
}
