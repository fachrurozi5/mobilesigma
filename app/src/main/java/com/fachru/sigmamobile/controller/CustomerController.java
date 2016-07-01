package com.fachru.sigmamobile.controller;

import android.util.Log;

import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.controller.interfaces.OnCustomerCallbackListener;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.utils.Constanta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;*/

/**
 * Created by fachru on 17/12/15.
 */
public class CustomerController {

	private RestApiManager apiManager;
	private OnCustomerCallbackListener listener;

	private List<Customer> customers = new ArrayList<>();

	public CustomerController(OnCustomerCallbackListener listener) {
		this.listener = listener;
		apiManager = new RestApiManager();
	}

	public void startFetching() {
		listener.onFetchStart();
		Call<String> call = apiManager.getCustomerAPI().Records();
		call.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				if (response.isSuccessful() && response.body() != null) {
					Log.d(Constanta.TAG, response.body());
					try {
						JSONObject jsonObject = new JSONObject(response.body());
						if (jsonObject.getBoolean(Constanta.TAG_STATUS)) {
							Object o = jsonObject.get(Constanta.TAG_DATA);
							if (o instanceof JSONArray) {
								JSONArray jsonArray = (JSONArray) o;
								for (int i = 0; i < jsonArray.length(); i++) {
									customers.add(Customer.findOrCreateFromJson(jsonArray.getJSONObject(i)));
								}
								listener.onFetchProgress(customers);
							} else {
								listener.onFetchProgress(Customer.findOrCreateFromJson((JSONObject) o));
							}
						} else {
							Log.d(Constanta.TAG, jsonObject.getString(Constanta.TAG_MESSAGE));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					Log.i(Constanta.TAG, "Status " + response.code() + " " + response.message() + " " + response.headers());
				}
				listener.onFetchComplete();
			}

			@Override
			public void onFailure(Call<String> call, Throwable t) {
				listener.onFetchFailed(t);
			}
		});
	}

	public void _startFetching() {
		listener.onFetchStart();
		Call<List<Customer>> listCall = apiManager.getCustomerAPI()._Records();
		listCall.enqueue(new Callback<List<Customer>>() {
			@Override
			public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
				Log.i(Constanta.TAG, "Status " + response.code() + " " + response.message() + " " + response.headers());
				if (response.isSuccessful() && response != null) {
					Log.d(Constanta.TAG, response.body().toString());
				}
				listener.onFetchComplete();
			}

			@Override
			public void onFailure(Call<List<Customer>> call, Throwable t) {
				listener.onFetchFailed(t);
			}
		});
	}


}
