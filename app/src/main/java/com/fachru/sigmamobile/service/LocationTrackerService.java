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
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.fachru.sigmamobile.Login;
import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fachru on 19/11/15.
 */
public class LocationTrackerService extends Service implements
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

	public static final String TAG = Constanta.TAG;

	public static final long INTERVAL = 5 * 1000;

	private Handler mHandler = new Handler();

	private Timer mTimer = null;

	private int rtcCounter = 1, activeCount = 0, nonactiveCount = 0;

	private long oldLat = 0, oldLong = 0, nonactiveLat = 0, nonactiveLong = 0;

	// TODO: 24/05/16 status aktif dioutlet
	private boolean active = false;

	// TODO: 24/05/16 set TOLC and TOLCr
	private long TOLC = 0, TOLCr = 0;

	private GoogleApiClient mGoogleApiClient;

	private LocationRequest mLocationRequest;

	private Location mCurrentLocation;

	private String mLastUpdateTime = "";

	private Boolean isServiceRunning = true;

	@Override
	public void onCreate() {
		super.onCreate();
		createNotification(getApplicationContext());
		threadDateTime();
		buildGoogleApiClient();

		if (mTimer != null) {
			mTimer.cancel();
		} else {
			mTimer = new Timer();
		}

		mTimer.scheduleAtFixedRate(new RealTimeClock(), 0, INTERVAL);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopLocationUpdates();
		mGoogleApiClient.disconnect();
		isServiceRunning = false;
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mGoogleApiClient.connect();
		return START_STICKY;
	}

	@Override
	public void onConnected(Bundle bundle) {
		Log.i(Constanta.TAG, "Connected to GoogleApiClient");
		if (mCurrentLocation == null) {
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				Log.d(Constanta.TAG, "Location Update isn't have permission");
				return;
			}
			mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
		}

		startLocationUpdates();
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.i(Constanta.TAG, "Connection suspended");
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Log.i(Constanta.TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorMessage());

	}

	@Override
	public void onLocationChanged(Location location) {
		upDateLocation(location);
		Log.d(Constanta.TAG, "LATITUDE : " + mCurrentLocation.getLatitude() + " LONGITUDE : " + mCurrentLocation.getLongitude() +
				" LAST UPDATE : " + mLastUpdateTime);
	}

	protected synchronized void buildGoogleApiClient() {
		Log.i(Constanta.TAG, "Building GoogleApiClient");
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		createLocationRequest();
	}

	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(10000);
		mLocationRequest.setFastestInterval(5000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

	}

	protected void startLocationUpdates() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			Log.d(Constanta.TAG, "Location Update isn't have permission");
			return;
		}
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
	}

	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
	}

	protected void upDateLocation(Location location) {
		try {
			if (canPublishResult(location)) {
				mCurrentLocation = location;
				mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

				try {
					publishResultLocation(CommonUtil.getAddress(this, location));
				} catch (IOException e) {
					Log.d(Constanta.TAG, "network or other I/O problems", e);
				}

			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			mCurrentLocation = location;
		}

	}

	private boolean canPublishResult(Location location) {
		return (mCurrentLocation.getLatitude() != location.getLatitude()) ||
				(mCurrentLocation.getLongitude() != location.getLongitude());
	}

	private void threadDateTime() {
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
	}

	private void publishResultLocation(String s) {
		Intent intent = new Intent(Constanta.SERVICE_RECEIVER);
		intent.putExtra(Constanta.RESULT_ADDRESS, s);
		intent.putExtra(Constanta.LATITUDE, mCurrentLocation.getLatitude());
		intent.putExtra(Constanta.LONGITUDE, mCurrentLocation.getLatitude());
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	private void publishResultDateTime(String date, String time) {
		Intent intent = new Intent(Constanta.SERVICE_RECEIVER);
		intent.putExtra(Constanta.RESULT_DATE, date);
		intent.putExtra(Constanta.RESULT_TIME, time);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	private void createNotification(Context context) {
		Intent intent = new Intent(context, Login.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context,
				0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification = new NotificationCompat.Builder(context)
				.setContentTitle("Mobile Sigma")
				.setContentText("Android Tracking Service is Running.").setSmallIcon(R.drawable.ic_action_bar)
				.setColor(Color.parseColor("#2196F3"))
				.setContentIntent(pendingIntent)
				.build();

		notification.flags = Notification.FLAG_FOREGROUND_SERVICE;

		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		manager.notify(0, notification);
	}

	class RealTimeClock extends TimerTask {
		@Override
		public void run() {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					Log.i(TAG, "run: RTC " + rtcCounter);
					if (rtcCounter >= 250) { // di execute setiap 5 detik
						rtcCounter = 1; // RTC counter di reset
						Log.i(TAG, "run: RTC " + rtcCounter);
						Log.i(TAG, "run: CheckLocation");
						if (mCurrentLocation != null) { // baca latitude dan longitude
							Log.i(TAG, "run: Lat " + mCurrentLocation.getLatitude() + " Long " + mCurrentLocation.getLongitude());
							if (checkActive()) { // kalau lagi active di outlet
								Log.e(TAG, "run: Active dioutlet");
								if (checkMovement(mCurrentLocation, TOLC)) {
									activeCount++;
									Log.i(TAG, "run: movement true " + activeCount);
								} else {
									nonactiveCount++;
									Log.i(TAG, "run: movement false " + nonactiveCount);
								}
							} else { // kalau lagi tidak active di outlet
								Log.e(TAG, "run: Tidak Active dioutlet");
								if (checkMovement(mCurrentLocation, TOLCr)) { // kalau lagi tidak bergerak
									Log.i(TAG, "run: tidak bergerak");
									nonactiveCount++;
									if (nonactiveCount > 120) { // tidak bergerak selama 10 menit
										nonactiveLat = Double.doubleToLongBits(mCurrentLocation.getLatitude());
										nonactiveLong = Double.doubleToLongBits(mCurrentLocation.getLongitude());
										// TODO: 24/05/16 baca waktu dan simpan nonactiveCount, nonactiveLat, nonActiveLong
										insertIdleAnalysis(nonactiveLat, nonactiveLong, nonactiveCount);
										nonactiveCount = 0;
										Log.i(TAG, "run: nonactive location" + nonactiveLat + " " + nonactiveLong);
									}
								} else {
									nonactiveCount = 0;
									oldLat = Double.doubleToLongBits(mCurrentLocation.getLatitude());
									oldLong = Double.doubleToLongBits(mCurrentLocation.getLongitude());
									Log.e(TAG, "run: old location " + oldLat + " " + oldLong);
								}
							}
						} else { // tidak bisa baca gps
							Log.i(TAG, "run: lat&long null");
							if (checkActive()) activeCount++;
							else nonactiveCount = 0;
						}
					} else {
						rtcCounter++;
					}
				}
			});
		}

		private boolean checkMovement(Location location, Long aLong) {
			return (Math.abs(location.getLatitude()) + Math.abs(location.getLongitude())) < aLong;
		}

		private boolean checkActive() {
			return active;
		}

		private void insertIdleAnalysis(long latitude, long longitude, int durationIdle) {
	        /*String empid = SessionManager.pref().getString(SessionManager.KEY_EMPLOYEE, "");
            Employee employee = Employee.getEmployee(empid);
            if (employee != null) {
                IdleTimeAnalysis timeAnalysis = IdleTimeAnalysis.getToDayAnalysisByEmp(empid);
                if (timeAnalysis == null) {
                    timeAnalysis = new IdleTimeAnalysis();
                    timeAnalysis.analysisAt = new Date();
                    timeAnalysis.empId = empid;
                    timeAnalysis.employee = employee;
                    timeAnalysis.save();
                }
                IdleTime idleTime = new IdleTime(latitude, longitude, new Date(), durationIdle, timeAnalysis);
                idleTime.save();
                timeAnalysis.totalStaticDuration = idleTime.getSumDurationIdle();
                timeAnalysis.save();
                return true;
            } else {
                return false;
            }*/
		}
	}
}
