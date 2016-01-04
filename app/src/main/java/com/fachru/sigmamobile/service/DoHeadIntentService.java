package com.fachru.sigmamobile.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.utils.Constanta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by fachru on 31/12/15.
 */
public class DoHeadIntentService extends IntentService {

    public DoHeadIntentService() {
        super("Document Head");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RestApiManager apiManager = new RestApiManager();
        Call<String> call = apiManager.getDoHeadAPI().Records();
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
                                    DoHead doHead = DoHead.findOrCreateFromJson(jsonArray.getJSONObject(i));
                                    doHead.save();
                                }
                            } else {
                                DoHead doHead = DoHead.findOrCreateFromJson(jsonObject.getJSONObject(Constanta.TAG_DATA));
                                doHead.save();
                            }
                        } else {
                            Log.d(Constanta.TAG, jsonObject.getString(Constanta.TAG_MESSAGE));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(Constanta.TAG, "CustomerIntentService", t);
            }
        });
    }
}
