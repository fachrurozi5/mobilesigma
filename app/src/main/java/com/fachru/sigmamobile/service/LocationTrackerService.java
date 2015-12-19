package com.fachru.sigmamobile.service;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constantas;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        Log.i(Constantas.TAG, "Connected to GoogleApiClient");
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        }

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(Constantas.TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(Constantas.TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorMessage());

    }

    @Override
    public void onLocationChanged(Location location) {
        upDateLocation(location);
        Log.d(Constantas.TAG, "LATITUDE : " + mCurrentLocation.getLatitude() + " LONGITUDE : " + mCurrentLocation.getLongitude() +
                " LAST UPDATE : " + mLastUpdateTime);
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(Constantas.TAG, "Building GoogleApiClient");
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
                    Log.d(Constantas.TAG, "network or other I/O problems", e);
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
        Intent intent = new Intent(Constantas.SERVICE_RECEIVER);
        intent.putExtra(Constantas.RESULT_ADDRESS, s);
        intent.putExtra(Constantas.LATITUDE, mCurrentLocation.getLatitude());
        intent.putExtra(Constantas.LONGITUDE, mCurrentLocation.getLatitude());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
