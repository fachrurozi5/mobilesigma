package com.fachru.sigmamobile.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

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
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener{

    protected GoogleApiClient mGoogleApiClient;

    protected LocationRequest mLocationRequest;

    protected Location mCurrentLocation;

    protected String mLastUpdateTime = "";

    @Override
    public void onCreate() {
        super.onCreate();
//        createNotification();
        buildGoogleApiClient();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
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
    public void onConnectionFailed(ConnectionResult connectionResult) {
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
                    publishResult(CommonUtil.getAddress(this, location));
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

    private void publishResult(String s) {
        Intent intent = new Intent(Constanta.SERVICE_RECEIVER);
        intent.putExtra(Constanta.RESULT_ADDRESS, s);
        intent.putExtra(Constanta.LATITUDE, mCurrentLocation.getLatitude());
        intent.putExtra(Constanta.LONGITUDE, mCurrentLocation.getLatitude());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /*private void createNotification() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(),
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Mobile Sigma")
                .setContentText("Android Tracking Service is Running.").setSmallIcon(R.drawable.ic_action_bar)
                .setColor(Color.parseColor("#2196F3"))
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0, notification);
    }*/
}
