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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.fachru.sigmamobile.Login;
import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.model.IdleTime;
import com.fachru.sigmamobile.model.IdleTimeAnalysis;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;
import com.fachru.sigmamobile.utils.SessionManager;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fachru on 26/05/16.
 */
public class RealTimeClockInterrupt extends Service implements LocationListener {

	public static final long INTERVAL = 5 * 1000;
	// The minimum distance to change updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 10;
	// Get Class Name
	private static String TAG = Constanta.TAG;
	// flag for GPS Status
	public boolean isGPSEnabled = false;
	// flag for network status
	public boolean isNetworkEnabled = false;
	// flag for GPS Tracking is enabled
	public boolean isGPSTrackingEnabled = false;
	public Location location;
	public double latitude;
	public double longitude;
	// How many Geocoder should return our GPSTracker
	public int geocoderMaxResults = 1;
	// Declaring a Location Manager
	protected LocationManager locationManager;
	// Store LocationManager.GPS_PROVIDER or LocationManager.NETWORK_PROVIDER information
	private String provider_info;
	private Boolean isServiceRunning = true;
	private Timer mTimer = null;

	@Override
	public void onCreate() {
		super.onCreate();
		threadDateTime();
		createNotification(getApplicationContext());
		getLocation();

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
		stopUsingGPS();
		isServiceRunning = false;
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.i(TAG, "onLocationChanged: " + location.toString());
		this.location = location;
		try {
			publishResultLocation(CommonUtil.getAddress(this, location));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.i(TAG, "onStatusChanged: " + provider + ", status " + status);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.i(TAG, "onProviderEnabled: " + provider);
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.i(TAG, "onProviderDisabled: " + provider);
	}

	/**
	 * Try to get my current location by GPS or Network Provider
	 */
	public void getLocation() {

		try {
			locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

			//getting GPS status
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			//getting network status
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			// Try to get location if you GPS Service is enabled
			if (isGPSEnabled) {
				this.isGPSTrackingEnabled = true;

				Log.d(TAG, "Application use GPS Service");

                /*
                 * This provider determines location using
                 * satellites. Depending on conditions, this provider may take a while to return
                 * a location fix.
                 */

				provider_info = LocationManager.GPS_PROVIDER;

			} else if (isNetworkEnabled) { // Try to get location if you Network Service is enabled
				this.isGPSTrackingEnabled = true;

				Log.d(TAG, "Application use Network State to get GPS coordinates");

                /*
                 * This provider determines location based on
                 * availability of cell tower and WiFi access points. Results are retrieved
                 * by means of a network lookup.
                 */
				provider_info = LocationManager.NETWORK_PROVIDER;

			}

			// Application can use GPS or Network Provider
			if (!provider_info.isEmpty()) {
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
				locationManager.requestLocationUpdates(
						provider_info,
						MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES,
						this
				);

				if (locationManager != null) {
					location = locationManager.getLastKnownLocation(provider_info);
					updateGPSCoordinates();
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			Log.e(TAG, "Impossible to connect to LocationManager", e);
		}
	}

	/**
	 * Update GPSTracker latitude and longitude
	 */
	public void updateGPSCoordinates() {
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			Log.e(TAG, location.toString());
		}
	}

	/**
	 * GPSTracker latitude getter and setter
	 *
	 * @return latitude
	 */
	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}

		return latitude;
	}

	/**
	 * GPSTracker longitude getter and setter
	 *
	 * @return
	 */
	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}

		return longitude;
	}

	/**
	 * GPSTracker isGPSTrackingEnabled getter.
	 * Check GPS/wifi is enabled
	 */
	public boolean getIsGPSTrackingEnabled() {

		return this.isGPSTrackingEnabled;
	}

	/**
	 * Stop using GPS listener
	 * Calling this method will stop using GPS in your app
	 */
	public void stopUsingGPS() {
		if (locationManager != null) {
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
			locationManager.removeUpdates(this);
		}
	}

	/**
	 * Get list of address by latitude and longitude
	 *
	 * @return null or List<Address>
	 */
	public List<Address> getGeocoderAddress(Context context) {
		if (location != null) {

			Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

			try {
				/**
				 * Geocoder.getFromLocation - Returns an array of Addresses
				 * that are known to describe the area immediately surrounding the given latitude and longitude.
				 */
				List<Address> addresses = geocoder.getFromLocation(latitude, longitude, this.geocoderMaxResults);

				return addresses;
			} catch (IOException e) {
				//e.printStackTrace();
				Log.e(TAG, "Impossible to connect to Geocoder", e);
			}
		}

		return null;
	}

	/**
	 * Try to get AddressLine
	 *
	 * @return null or addressLine
	 */
	public String getAddressLine(Context context) {
		List<Address> addresses = getGeocoderAddress(context);

		if (addresses != null && addresses.size() > 0) {
			Address address = addresses.get(0);

			return address.getAddressLine(0);
		} else {
			return null;
		}
	}

	/**
	 * Try to get Locality
	 *
	 * @return null or locality
	 */
	public String getLocality(Context context) {
		List<Address> addresses = getGeocoderAddress(context);

		if (addresses != null && addresses.size() > 0) {
			Address address = addresses.get(0);

			return address.getLocality();
		} else {
			return null;
		}
	}

	/**
	 * Try to get Postal Code
	 *
	 * @return null or postalCode
	 */
	public String getPostalCode(Context context) {
		List<Address> addresses = getGeocoderAddress(context);

		if (addresses != null && addresses.size() > 0) {
			Address address = addresses.get(0);

			return address.getPostalCode();
		} else {
			return null;
		}
	}

	/**
	 * Try to get CountryName
	 *
	 * @return null or postalCode
	 */
	public String getCountryName(Context context) {
		List<Address> addresses = getGeocoderAddress(context);
		if (addresses != null && addresses.size() > 0) {
			Address address = addresses.get(0);

			return address.getCountryName();
		} else {
			return null;
		}
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

		private Handler mHandler = new Handler();

		private int rtcCounter = 1, activeCount = 0, nonactiveCount = 0;

		private long oldLat = 0, oldLong = 0, nonactiveLat = 0, nonactiveLong = 0;

		// TODO: 24/05/16 status aktif dioutlet
		private boolean active = false;

		// TODO: 24/05/16 set TOLC and TOLCr
		private long TOLC = 0, TOLCr = 0;

		@Override
		public void run() {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					if (rtcCounter >= 250) { // di execute setiap 5 detik
						rtcCounter = 1; // RTC counter di reset
						Log.i(TAG, "run: RTC " + rtcCounter);
						Log.i(TAG, "run: CheckLocation");
						if (location != null) { // baca latitude dan longitude
							Log.i(TAG, "run: Lat " + location.getLatitude() + " Long " + location.getLongitude());
							if (checkActive()) { // kalau lagi active di outlet
								Log.e(TAG, "run: Active dioutlet");
								if (checkMovement(location, TOLC)) {
									activeCount++;
									Log.i(TAG, "run: movement true " + activeCount);
								} else {
									nonactiveCount++;
									Log.i(TAG, "run: movement false " + nonactiveCount);
								}
							} else { // kalau lagi tidak active di outlet
								Log.e(TAG, "run: Tidak Active dioutlet");
								if (checkMovement(location, TOLCr)) { // kalau lagi tidak bergerak
									Log.i(TAG, "run: tidak bergerak");
									nonactiveCount++;
									if (nonactiveCount > 120) { // tidak bergerak selama 10 menit
										nonactiveLat = Double.doubleToLongBits(location.getLatitude());
										nonactiveLong = Double.doubleToLongBits(location.getLongitude());
										// TODO: 24/05/16 baca waktu dan simpan nonactiveCount, nonactiveLat, nonActiveLong
										insertIdleAnalysis(nonactiveLat, nonactiveLong, nonactiveCount);
										nonactiveCount = 0;
										Log.i(TAG, "run: nonactive location" + nonactiveLat + " " + nonactiveLong);
									}
								} else {
									nonactiveCount = 0;
									oldLat = Double.doubleToLongBits(location.getLatitude());
									oldLong = Double.doubleToLongBits(location.getLongitude());
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
			long empid = SessionManager.pref().getLong(SessionManager.KEY_EMPLOYEE, -1);
			Employee employee = Employee.load(Employee.class, empid);
			Log.e(Constanta.TAG, employee.toString());
			if (employee != null) {
				IdleTimeAnalysis timeAnalysis = IdleTimeAnalysis.getToDayAnalysisByEmp(employee.getEmployeeId());
				Log.e(Constanta.TAG, timeAnalysis.toString());
				if (timeAnalysis == null) {
					timeAnalysis = new IdleTimeAnalysis(employee);
					timeAnalysis.save();
				}

				IdleTime idleTime = new IdleTime(timeAnalysis.ita_id, latitude, longitude, new Date(), durationIdle, timeAnalysis);
				idleTime.save();
				timeAnalysis.uploaded = false;
				timeAnalysis.save();
			}
		}
	}
}
