package com.fachru.sigmamobile.controller;

import android.util.Log;

import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.controller.interfaces.OnResponseListener;
import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.utils.Constanta;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by fachru on 25/02/16.
 */
public class DoHeadController {

	private RestApiManager apiManager;
	private OnResponseListener responseListener;

	public DoHeadController(OnResponseListener responseListener) {
		this.apiManager = new RestApiManager();
		this.responseListener = responseListener;
	}

	public void startStore(DoHead doHead) {
		responseListener.onStoreStart();
		Call<String> call = apiManager.getDoHeadAPI().Store(doHead);
		call.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				if (response.isSuccessful() && response.body() != null) {
					responseListener.onStoreComplete();
					Log.d(Constanta.TAG, response.body());
				}
			}

			@Override
			public void onFailure(Call<String> call, Throwable t) {
				responseListener.onFetchFailed(t);
			}
		});
	}

}
