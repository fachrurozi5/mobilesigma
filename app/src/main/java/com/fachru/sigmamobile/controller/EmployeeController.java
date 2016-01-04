package com.fachru.sigmamobile.controller;

import android.util.Log;

import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.controller.interfaces.OnEmployeeCallbackListener;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by fachru on 29/12/15.
 */
public class EmployeeController {

    private OnEmployeeCallbackListener listener;
    private RestApiManager apiManager;

    public EmployeeController(OnEmployeeCallbackListener listener) {
        this.listener = listener;
        apiManager = new RestApiManager();
    }

    public void startFetch() {
        listener.onFetchStart();
        Call<String> call = apiManager.getEmployeeApi().login("FachruRozi", "1234");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(Constanta.TAG, response.body());
                if (response.isSuccess() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getBoolean(Constanta.TAG_STATUS)) {
                            Object o = jsonObject.get(Constanta.TAG_DATA);
                            if (o instanceof JSONArray) {
                                /*JSONArray jsonArray = (JSONArray) o;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    customers.add(Customer.findOrCreateFromJson(jsonArray.getJSONObject(i)));
                                }
                                listener.onFetchProgress(customers);*/
                            } else {
                               listener.onFetchProgress(Employee.findOrCreateFromJson((JSONObject) o));
                            }
                        } else {
                            Log.d(Constanta.TAG, jsonObject.getString(Constanta.TAG_MESSAGE));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listener.onFetchComplete();
                }

                listener.onFetchComplete();
            }

            @Override
            public void onFailure(Throwable t) {
                listener.onFetchFailed(t);
            }
        });
    }
}
