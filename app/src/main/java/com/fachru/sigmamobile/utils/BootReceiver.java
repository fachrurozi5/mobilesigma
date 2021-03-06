package com.fachru.sigmamobile.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fachru.sigmamobile.service.SigmaSync;

/**
 * Created by fachru on 10/12/15.
 */
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//            Intent serviceIntent = new Intent(context, LocationTrackerService.class);
//            Intent serviceIntent = new Intent(context, RealTimeClockInterrupt.class);
//            context.startService(serviceIntent);
			Intent serviceIntent = new Intent(context, SigmaSync.class);
			context.startService(serviceIntent);
		}
	}
}
