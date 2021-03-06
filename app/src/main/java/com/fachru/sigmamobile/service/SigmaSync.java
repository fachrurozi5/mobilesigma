package com.fachru.sigmamobile.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.model.DoItem;
import com.fachru.sigmamobile.model.IdleTime;
import com.fachru.sigmamobile.model.IdleTimeAnalysis;
import com.fachru.sigmamobile.model.OSCManagement;
import com.fachru.sigmamobile.model.SoHead;
import com.fachru.sigmamobile.model.SoItem;
import com.fachru.sigmamobile.utils.Constanta;
import com.fachru.sigmamobile.utils.Upload;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fachru on 01/03/16.
 */
public class SigmaSync extends Service {

	public static final String TAG = Constanta.TAG;

	public static final long INTERVAL = 60 * 1000;

	private Handler mHandler = new Handler();

	private Timer mTimer = null;


	public SigmaSync() {
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		if (mTimer != null) {
			mTimer.cancel();
		} else {
			mTimer = new Timer();
		}

		mTimer.scheduleAtFixedRate(new UploadTimerTask(), 0, INTERVAL);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {


		return START_STICKY;
	}

	class UploadTimerTask extends TimerTask {

		@Override
		public void run() {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					if (DoHead.hasDataToUpload())
						for (DoHead doHead : DoHead.getAllNotUpload())
							if (DoHead.hasPrint(doHead.doc_no))
								new Upload<DoHead>().setData(doHead);

					if (DoItem.hasDataToUpload())
						for (DoItem doItem : DoItem.getAllNotUpload())
							if (DoHead.hasPrint(doItem.docno)) new Upload<DoItem>().setData(doItem);

					if (SoHead.hasDataToUpload())
						for (SoHead soHead : SoHead.getAllNotUpload())
							if (SoHead.hasPrint(soHead.so))
								new Upload<SoHead>().setData(soHead);

					if (SoItem.hasDataToUpload())
						for (SoItem soItem : SoItem.getAllNotUpload())
							if (SoHead.hasPrint(soItem.so)) new Upload<SoItem>().setData(soItem);

					if (IdleTimeAnalysis.hasDataToUpload())
						for (IdleTimeAnalysis timeAnalysis : IdleTimeAnalysis.getAllNotUpload()) {
							timeAnalysis.idleTimeList = timeAnalysis.getIdleTimeList();
							timeAnalysis.totalStaticDuration = IdleTime.getSumDurationIdle(timeAnalysis.getId());
							new Upload<IdleTimeAnalysis>().setData(timeAnalysis);
						}

					if (OSCManagement.hasDataToUpload())
						for (OSCManagement oscManagement : OSCManagement.getAllNotUpload()) {
							oscManagement.oscManagementDetails = oscManagement.getManagementDetails();
							Log.e("_OSCM", oscManagement.toString());
							new Upload<OSCManagement>().setData(oscManagement);
						}
				}
			});
		}
	}

}
