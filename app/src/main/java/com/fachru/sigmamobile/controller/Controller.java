package com.fachru.sigmamobile.controller;

import android.util.Log;

import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.controller.interfaces.OnFetchListener;
import com.fachru.sigmamobile.controller.interfaces.OnStoreListener;
import com.fachru.sigmamobile.model.Discount;
import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.utils.Constanta;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by fachru on 26/02/16.
 */
public class Controller {

    protected RestApiManager apiManager;
    protected OnFetchListener<Discount> fetchListener;
    protected OnStoreListener storeListener;


    public Controller(OnStoreListener storeListener) {
        this.storeListener = storeListener;
        this.apiManager = new RestApiManager();
    }

    public Controller(OnFetchListener<Discount> fetchListener) {
        this.fetchListener = fetchListener;
        this.apiManager = new RestApiManager();
    }

    public void startFetching() {
        fetchListener.onFetchStart();
        Call<List<Discount>> listCall = apiManager.getDiscountAPI()._Records();
        listCall.enqueue(new Callback<List<Discount>>() {
            @Override
            public void onResponse(Call<List<Discount>> call, Response<List<Discount>> response) {
                Log.i(Constanta.TAG, response.toString());
                Log.i(Constanta.TAG, "Status " + response.code() + " " + response.message() + " " + response.headers());
                if (response.isSuccess() && response.body() != null) {
                    fetchListener.onFetchProgress(response.body());
                }
                fetchListener.onFetchComplete();
            }

            @Override
            public void onFailure(Call<List<Discount>> call, Throwable t) {
                fetchListener.onFetchFailed(t);
            }
        });
    }

    public void startStore(DoHead doHead) {
        storeListener.onStoreStart();
        Call<DoHead> call = apiManager.getDoHeadAPI()._Store(doHead);
        call.enqueue(new Callback<DoHead>() {
            @Override
            public void onResponse(Call<DoHead> call, Response<DoHead> response) {
                Log.i(Constanta.TAG, "Status " + response.code() + " " + response.message() + " " + response.headers());
                if (response.isSuccess()) {
                    Log.d(Constanta.TAG, response.body().toString());
                }
                storeListener.onStoreComplete();
            }

            @Override
            public void onFailure(Call<DoHead> call, Throwable t) {
                storeListener.onStoreFailed(t);
            }
        });

    }
}
