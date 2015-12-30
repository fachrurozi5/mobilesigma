package com.fachru.sigmamobile.controller;

import android.util.Log;

import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.controller.interfaces.OnEmployeeCallbackListener;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

                try {
                    JSONObject object = new JSONObject(response.body());
                    listener.onFetchProgress(Employee.findOrCreateFromJson(object));
                } catch (JSONException e) {
                    e.printStackTrace();
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
