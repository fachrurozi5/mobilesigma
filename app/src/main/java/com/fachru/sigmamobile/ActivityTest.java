package com.fachru.sigmamobile;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fachru.sigmamobile.model.Outlet;
import com.fachru.sigmamobile.service.RTCService;
import com.fachru.sigmamobile.service.SigmaSync;

public class ActivityTest extends AppCompatActivity {

	Intent intent;
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
		setContentView(R.layout.activity_activity_test);
		intent = new Intent(this, RTCService.class);
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
		startService(new Intent(this, SigmaSync.class));
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
//        stopService(new Intent(this, SigmaSync.class));
		if (mBound) {
			unbindService(connection);
			mBound = false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(intent);
	}

	public void onShowPdf(View view) {
		mService.setActiveOutlet(true);
		Outlet outlet = Outlet.load(Outlet.class, 9);
		mService.setOutlet_id(outlet.getId());
	}

	public void onStore(View view) {
		mService.setActiveOutlet(false);
		mService.setOutlet_id(-1);
	}
}
