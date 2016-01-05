package com.fachru.sigmamobile.controller;

import android.util.Log;

import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.controller.interfaces.OnEmployeeCallbackListener;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.utils.Constanta;

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

    public void startFetch(String username, String password) {
        listener.onFetchStart();
        Call<String> call = apiManager.getEmployeeApi().login(username, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(Constanta.TAG, response.body());
                if (response.isSuccess() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getBoolean(Constanta.TAG_STATUS)) {
                            listener.onFetchProgress(Employee.findOrCreateFromJson(jsonObject.getJSONObject(Constanta.TAG_DATA)));
                        } else {
                            listener.onFailureShowMessage(jsonObject.getString(Constanta.TAG_MESSAGE));
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
