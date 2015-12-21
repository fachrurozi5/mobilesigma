package com.fachru.sigmamobile.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.NotificationCompat;

import com.fachru.sigmamobile.MainActivity;
import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.service.DateTimeService;
import com.fachru.sigmamobile.service.LocationTrackerService;
import com.fachru.sigmamobile.service.SaveMyAppsService;

/**
 * Created by fachru on 10/12/15.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, SaveMyAppsService.class);
            context.startService(serviceIntent);
            serviceIntent = new Intent(context, LocationTrackerService.class);
            context.startService(serviceIntent);
            createNotification(context);
        }
    }

    private void createNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
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
