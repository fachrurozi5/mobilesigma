package com.fachru.sigmamobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.fachru.sigmamobile.utils.Constanta;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class OSCActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

	protected Context context = this;

	protected Toolbar toolbar;
	protected GridView gridview;
	protected TextView text_time;
	protected TextView text_date;
	protected TextView text_location;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_osc);
		Intent intent = getIntent();
		initComp();
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		gridview.setAdapter(new ImageAdapter(this,
				getResources().getStringArray(R.array.sub_menus_osc),
				getResources().obtainTypedArray(R.array.icon_sub_menu_osc)));
		gridview.setOnItemClickListener(this);
		text_date.setText(intent.getStringExtra(Constanta.RESULT_DATE));
		text_time.setText(intent.getStringExtra(Constanta.RESULT_TIME));
		text_location.setText(intent.getStringExtra(Constanta.RESULT_ADDRESS));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (scanningResult != null) {
			String scanContent = scanningResult.getContents();
			String scanFormat = scanningResult.getFormatName();
			Toast.makeText(getApplicationContext(),
					"Format : " + scanFormat + ", Content : " + scanContent,
					Toast.LENGTH_SHORT).show();
		} else if (resultCode == Constanta.RESULT_OK
				&& requestCode == Constanta.REQUEST_CODE) {
			if (data.hasExtra(Constanta.RESULT_DATE)) {
				text_date.setText(data.getStringExtra(Constanta.RESULT_DATE));
				text_time.setText(data.getStringExtra(Constanta.RESULT_TIME));
				text_location.setText(data.getStringExtra(Constanta.RESULT_ADDRESS));
			}
		}
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
	}

	@Override
	public void onBackPressed() {
		activityResult();
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
				Log.d(Constanta.TAG, "HOME");
				activityResult();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		Intent intent;

		switch (position) {
			case 0:
				scanBarcode();
				break;
			case 1:
				intent = new Intent(context, ChooseROActivity.class);
				intent.putExtra(Constanta.RESULT_DATE, text_date.getText().toString());
				intent.putExtra(Constanta.RESULT_TIME, text_time.getText().toString());
				intent.putExtra(Constanta.RESULT_ADDRESS, text_location.getText().toString());
				startActivityForResult(intent, Constanta.REQUEST_CODE);
				break;
			case 2:

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

	private void scanBarcode() {
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}

	private void activityResult() {
		Intent intent = new Intent();
		intent.putExtra(Constanta.RESULT_DATE, text_date.getText().toString());
		intent.putExtra(Constanta.RESULT_TIME, text_time.getText().toString());
		intent.putExtra(Constanta.RESULT_ADDRESS, text_location.getText().toString());
		setResult(Constanta.RESULT_OK, intent);
		finish();
	}
}
