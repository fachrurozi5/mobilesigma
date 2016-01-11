package com.fachru.sigmamobile.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.model.WarehouseStock;
import com.fachru.sigmamobile.utils.Constanta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by fachru on 30/12/15.
 */
public class WarehouseStockIntentService extends IntentService{

    public WarehouseStockIntentService() {
        super("Warehouse Stock");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RestApiManager apiManager = new RestApiManager();
        Call<String> call = apiManager.getWhStock().Records();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getBoolean(Constanta.TAG_STATUS)) {
                            Object o = jsonObject.get(Constanta.TAG_DATA);
                            if (o instanceof JSONArray) {
                                JSONArray jsonArray = jsonObject.getJSONArray(Constanta.TAG_DATA);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    WarehouseStock.findOrCreateFromJson(jsonArray.getJSONObject(i));
                                }
                            } else {
                                WarehouseStock.findOrCreateFromJson(jsonObject.getJSONObject(Constanta.TAG_DATA));
                            }
                        } else {
                            Log.d(Constanta.TAG, jsonObject.getString(Constanta.TAG_MESSAGE));
                        }
                    } catch (JSONException e) {
                        Log.e(Constanta.TAG, "WarehouseStockIntentService", e);
                    }

                    Log.d(Constanta.TAG, "WarehouseStockIntentService Success");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(Constanta.TAG, "WarehouseStockIntentService", t);
            }
        });
    }
}
