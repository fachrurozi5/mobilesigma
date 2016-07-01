package com.fachru.sigmamobile.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.fachru.sigmamobile.utils.Constanta;
import com.fachru.sigmamobile.utils.SessionManager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SaveMyAppsService extends Service {

	private static String CURRENT_PACKAGE_NAME;
	private final IBinder mBinder = new LocalBinder();
	private List<String> strings = new ArrayList<>();
	private long startTime;
	private long maxDurationInMilliseconds = 60 * 1000;
	private boolean isThreadRunning = false;
	private SessionManager manager;
	private ScheduledExecutorService scheduler;

	public SaveMyAppsService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		manager = new SessionManager(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		CURRENT_PACKAGE_NAME = getApplicationContext().getPackageName();

		scheduleMethod();

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stop();
	}

	private void scheduleMethod() {
		startTime = System.currentTimeMillis();
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (System.currentTimeMillis() < startTime + maxDurationInMilliseconds) {
					checkRunningApps();
				} else {
					stop();
				}
			}
		}, 0, 500, TimeUnit.MILLISECONDS);
	}

	public void checkRunningApps() {
		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		if (Build.VERSION.SDK_INT > 20) {
			List<ActivityManager.RunningAppProcessInfo> runningTaskInfos = mActivityManager.getRunningAppProcesses();
			ActivityManager.RunningAppProcessInfo processInfo = runningTaskInfos.get(0);
			String activityOnTop = processInfo.processName;
			if (checkPackage(activityOnTop)) {
				Log.e(Constanta.TAG, activityOnTop);
				interruptedApp(activityOnTop);
			}
		} else {
			List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
			ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
			String activityOnTop = ar.topActivity.getPackageName();
			if (checkPackage(activityOnTop)) {
				Log.e(Constanta.TAG, activityOnTop);
				interruptedApp(activityOnTop);
			}
		}
	}

	public void stop() {
		scheduler.shutdown();
		manager.saveLastAppPn(CURRENT_PACKAGE_NAME);
		scheduleMethod();
	}

	public void setListPackage(List<String> strings) {
		this.strings = strings;
	}

	private boolean checkPackage(String s) {
		for (String s1 : strings) {
			if (s.contains(s1))
				return true;
		}
		return false;
	}

	private void interruptedApp(String s) {
		try {
			Process suProcess = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());

			os.writeBytes("adb shell" + "\n");
			os.flush();


			os.writeBytes("am force-stop " + s + "\n");


			os.flush();
			os.close();
			suProcess.waitFor();

		} catch (IOException ex) {
			ex.getMessage();
			Log.e(Constanta.TAG, "IOException : " + ex.getMessage());
		} catch (SecurityException ex) {
			Log.e(Constanta.TAG, "Can't get root access2");
		} catch (Exception ex) {
			Log.e(Constanta.TAG, "Can't get root access3");
		}
	}

	public class LocalBinder extends Binder {
		public SaveMyAppsService getServiceInstance() {
			return SaveMyAppsService.this;
		}
	}
}
