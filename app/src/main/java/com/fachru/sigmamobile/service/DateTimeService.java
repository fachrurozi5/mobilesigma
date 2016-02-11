package com.fachru.sigmamobile.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;

public class DateTimeService extends Service {

    private Boolean isServiceRunning = true;

    public DateTimeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e(Constanta.TAG, "ThreadDateTime has been intrrupted", e);
                    }
                    publishResult(
                            CommonUtil.dateHelper(Constanta.ID),
                            CommonUtil.dateHelper(Constanta.TIME)
                    );
                } while (isServiceRunning);
            }
        });

        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceRunning = false;
    }

    private void publishResult(String date, String time) {
        Intent intent = new Intent(Constanta.SERVICE_RECEIVER);
        intent.putExtra(Constanta.RESULT_DATE, date);
        intent.putExtra(Constanta.RESULT_TIME, time);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
