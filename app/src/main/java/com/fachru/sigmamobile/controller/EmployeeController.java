package com.fachru.sigmamobile.controller;

import android.util.Log;

import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.controller.interfaces.OnEmployeeCallbackListener;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.utils.Constanta;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Header;


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
        Call<Employee> call = apiManager.getEmployeeApi().login(username, password);
        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                Log.i(Constanta.TAG, "Status " + response.code() + " " + response.message() + " " + response.headers());
                if (response.isSuccess() && response.body() != null) {
                    Employee employee = response.body();
                    employee.save();
                    listener.onFetchProgress(employee);
                    listener.onFetchComplete();
                } else {
                    listener.onFailureShowMessage(response.message());
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                listener.onFetchFailed(t);
            }
        });
    }

}
