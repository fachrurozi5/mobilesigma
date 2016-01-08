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
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.fachru.sigmamobile.Login;
import com.fachru.sigmamobile.MainActivity;
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

/**
 * Created by fachru on 19/11/15.
 */
public class LocationTrackerService extends Service implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

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
                        Log.e(Constanta.TAG, "ThreadDateTime has been intrrupted", e);
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
}
