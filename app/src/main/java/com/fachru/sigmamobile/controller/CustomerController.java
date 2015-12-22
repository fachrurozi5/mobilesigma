package com.fachru.sigmamobile.controller;

import android.util.Log;

import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.controller.interfaces.OnCustomerCallbackListener;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.utils.Constanta;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by fachru on 17/12/15.
 */
public class CustomerController {

    private OnCustomerCallbackListener listener;
    private RestApiManager apiManager;

    public CustomerController(OnCustomerCallbackListener listener) {
        this.listener = listener;
        apiManager = new RestApiManager();
    }

    public void startFetching() {
        listener.onFetchStart();
        Call<List<Customer>> call = apiManager.getCustomerApi().getCustomers();
        call.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Response<List<Customer>> response, Retrofit retrofit) {
                listener.onFetchProgress(response.body());
                listener.onFetchComplete();
            }

            @Override
            public void onFailure(Throwable t) {
                listener.onFetchFailed(t);
            }
        });
    }

    public void startCreating(Customer customer) {
        Call<String> call = apiManager.getCustomerApi().setCustomer(customer);
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
