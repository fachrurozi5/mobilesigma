package com.fachru.sigmamobile.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.activeandroid.query.Select;
import com.fachru.sigmamobile.Login;
import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.model.IdleTime;
import com.fachru.sigmamobile.model.IdleTimeAnalysis;
import com.fachru.sigmamobile.model.Outlet;
import com.fachru.sigmamobile.model.Tolerance;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;
import com.fachru.sigmamobile.utils.SessionManager;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fachru on 08/06/16.
 */
public class RTCService extends Service implements LocationListener {

	// The minimum distance to change updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 10;
	private final String TAG = this.getClass().getSimpleName();
	// Binder given to clients
	private final IBinder mBinder = new LocalBinder();
	// flag for GPS Status
	public boolean isGPSEnabled = false;
	// flag for network status
	public boolean isNetworkEnabled = false;
	// flag for GPS Tracking is enabled
	public boolean isGPSTrackingEnabled = false;
	private Timer mTimer = null;
	private LocationManager locationManager;
	private Location location;
	private Location oldLocation;
	private Outlet outlet = null;
	private Tolerance tolerance = null;
	// flag for active count
	private int activeCount = 0;
	// flag for non active count
	private int nonActiveCount = 0;
	// flag for Outlet Status
	private boolean activeOutlet = false;
	// Store LocationManager.GPS_PROVIDER or LocationManager.NETWORK_PROVIDER information
	private String providerInfo = LocationManager.GPS_PROVIDER;

	private Boolean isServiceRunning = true;

	private long outlet_id = -1;

	@Override
	public void onCreate() {
		super.onCreate();
		this.getLocation();
		Intent intent = new Intent(getApplicationContext(), Login.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
				0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification = new NotificationCompat.Builder(getApplicationContext())
				.setContentTitle("Mobile Sigma")
				.setContentText("Android Tracking Service is Running.").setSmallIcon(R.drawable.ic_action_bar)
				.setColor(Color.parseColor("#2196F3"))
				.setContentIntent(pendingIntent)
				.build();

		notification.flags = Notification.FLAG_FOREGROUND_SERVICE;

		NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

		manager.notify(0, notification);

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				do {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						Log.e(Constanta.TAG, "ThreadDateTime has been interrupted", e);
					}
					publishResultDateTime(
							CommonUtil.dateHelper(Constanta.ID),
							CommonUtil.dateHelper(Constanta.TIME)
					);
				} while (isServiceRunning);
			}
		});

		thread.start();

		if (mTimer != null) {
			mTimer.cancel();
		} else {
			mTimer = new Timer();
		}

		long INTERVAL = 5 * 1000;
		mTimer.scheduleAtFixedRate(new RealTimeClock(), 0, INTERVAL);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		locationManager.removeUpdates(this);
		isServiceRunning = false;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return START_STICKY;
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.i(TAG, "onLocationChanged: " + location.toString());
		if (oldLocation == null) oldLocation = location;
		this.location = location;
		try {
			publishResultLocation(CommonUtil.getAddress(this, location));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		providerInfo = provider;
	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	private void publishResultLocation(String s) {
		Log.e("publishResultLocation", "Address " + s);
		Intent intent = new Intent(Constanta.SERVICE_RECEIVER);
		intent.putExtra(Constanta.RESULT_ADDRESS, s);
		intent.putExtra(Constanta.LATITUDE, location.getLatitude());
		intent.putExtra(Constanta.LONGITUDE, location.getLatitude());
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	private void publishResultDateTime(String date, String time) {
		Intent intent = new Intent(Constanta.SERVICE_RECEIVER);
		intent.putExtra(Constanta.RESULT_DATE, date);
		intent.putExtra(Constanta.RESULT_TIME, time);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	public void getLocation() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		/*//getting GPS status
		isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		//getting network status
		isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		// Try to get location if you GPS Service is enabled
		if (isGPSEnabled) {
			this.isGPSTrackingEnabled = true;

			Log.d(TAG, "Application use GPS Service");

                *//*
                 * This provider determines location using
                 * satellites. Depending on conditions, this provider may take a while to return
                 * a location fix.
                 *//*

			providerInfo = LocationManager.GPS_PROVIDER;

		} else if (isNetworkEnabled) { // Try to get location if you Network Service is enabled
			this.isGPSTrackingEnabled = true;

			Log.d(TAG, "Application use Network State to get GPS coordinates");

                *//*
                 * This provider determines location based on
                 * availability of cell tower and WiFi access points. Results are retrieved
                 * by means of a network lookup.
                 *//*
			providerInfo = LocationManager.NETWORK_PROVIDER;

		}*/

		if (!providerInfo.isEmpty()) {
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				return;
			}
			locationManager.requestLocationUpdates(
					providerInfo,
					MIN_TIME_BW_UPDATES,
					MIN_DISTANCE_CHANGE_FOR_UPDATES,
					this
			);
		}
	}

	public double getLatitude() {
		return location != null ? location.getLatitude() : 0.0;
	}

	public double getLongitude() {
		return location != null ? location.getLongitude() : 0.0;
	}

	public int getActiveCount() {
		return activeCount;
	}

	public int getNonActiveCount() {
		return nonActiveCount;
	}

	public boolean isActiveOutlet() {
		return activeOutlet;
	}

	public void setActiveOutlet(boolean activeOutlet) {
		this.activeOutlet = activeOutlet;
	}

	public long getOutlet_id() {
		return outlet_id;
	}

	public void setOutlet_id(long outlet_id) {
		this.outlet_id = outlet_id;
		outlet = Outlet.load(Outlet.class, outlet_id);
		if (outlet != null)
			tolerance = new Select()
					.from(Tolerance.class)
					.where(Tolerance.ID + " = ?", outlet.getToleranceId())
					.executeSingle();
	}

	/**
	 * Class used for the client Binder.  Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		public RTCService getService() {
			// Return this instance of RTCService so clients can call public methods
			return RTCService.this;
		}
	}

	private class RealTimeClock extends TimerTask {

		private Handler mHandler = new Handler();

		private double tolerance_large = 50;

//        private int rtcCounter = 1;

		@Override
		public void run() {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					if (location != null) {
						Outlet outlet = Outlet.load(Outlet.class, outlet_id);
						if (outlet != null) {
							checkOutletLocation(outlet);
							double toleranceDist = tolerance != null ? tolerance.getTolInMeters() : 50;

							if (isStillThere(outlet.getLocation(providerInfo), toleranceDist)) {
								activeCount++;
								Log.d(TAG, "Employee Active " + String.valueOf(activeOutlet));
							} else {
								nonActiveCount++;
								Log.d(TAG, "Employee Non Active " + String.valueOf(nonActiveCount));
							}
						} else {
							Log.i(TAG, "run: Outlet Null");
							if (nonActiveCount > 120) // Log.e(TAG, "Save Idle Analysis");
								insertIdleAnalysis(location.getLatitude(), location.getLongitude(), nonActiveCount);

							if (!isStillThere(oldLocation, 20)) {
								oldLocation = location;
								nonActiveCount = 0;
							} else {
								nonActiveCount++;
								if (nonActiveCount > 120)
									insertIdleAnalysis(location.getLatitude(), location.getLongitude(), nonActiveCount);
							}
							Log.d(TAG, "Employee Non Active " + String.valueOf(nonActiveCount));
						}
					} else {
						Log.d(TAG, "run: Location Null");
						Outlet outlet = Outlet.load(Outlet.class, outlet_id);
						if (outlet != null) {
							activeCount++;
						} else {
							nonActiveCount = 0;
						}
					}
				}
			});
		}

		private void checkOutletLocation(Outlet outlet) {
			Log.e(TAG, "checkOutletLocation: " + outlet.toString());
			if (outlet.getLat() == 0 && outlet.getLng() == 0) {
				outlet.setLat(getLatitude());
				outlet.setLng(getLongitude());
				outlet.save();
			}
		}

		private boolean isStillThere(Location outlet_location) {
			Log.i(TAG, "isStillThere: Current Location " + location.toString());
			Log.i(TAG, "isStillThere: Outlet Location " + outlet_location.toString());
			double distanced = location.distanceTo(outlet_location);
			Log.e(TAG, "isStillThere: " + String.valueOf(distanced));
			return distanced < tolerance_large;

		}

		private boolean isStillThere(Location outletLocation, double tolerance) {
			Log.e(TAG, "isStillThere: Lat " + outletLocation.getLatitude() + ", Lng " + outletLocation.getLongitude());
			double distanced = location.distanceTo(outletLocation);
			Log.e(TAG, "isStillThere: " + String.valueOf(distanced));
			return distanced < tolerance;

		}

		private boolean isStillThere(Location outlet_location, Tolerance tolerance) {
			double distanced = location.distanceTo(outlet_location);
			Log.e(TAG, "isStillThere: " + String.valueOf(distanced));
			return distanced < tolerance.getTolInMeters();

		}

		private void insertIdleAnalysis(double latitude, double longitude, int durationIdle) {
			long empid = SessionManager.pref().getLong(SessionManager.KEY_EMPLOYEE, -1);
			Employee employee = Employee.load(Employee.class, empid);
			Log.e(Constanta.TAG, employee.toString());
			if (employee != null) {
				IdleTimeAnalysis timeAnalysis = IdleTimeAnalysis.getToDayAnalysisByEmp(employee.getEmployeeId());
				if (timeAnalysis == null) {
					timeAnalysis = new IdleTimeAnalysis(employee);
					timeAnalysis.save();
				}
				Log.e(Constanta.TAG, timeAnalysis.toString());
				IdleTime idleTime = new IdleTime(timeAnalysis.ita_id, latitude, longitude, new Date(), durationIdle, timeAnalysis);
				idleTime.save();
				timeAnalysis.uploaded = false;
				timeAnalysis.save();
				nonActiveCount = 0;
			}
		}
	}
}
