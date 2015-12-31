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

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
        Call<String> call = apiManager.getCustomerApi().Records();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
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
                    listener.onFetchComplete();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                listener.onFetchFailed(t);
            }
        });
    }

    public void startCreating(Customer customer) {
        Call<String> call = apiManager.getCustomerApi().store(customer);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.d(Constanta.TAG, response.body());
                listener.onFetchComplete();
            }

            @Override
            public void onFailure(Throwable t) {
                listener.onFetchFailed(t);
            }
        });
    }
}
