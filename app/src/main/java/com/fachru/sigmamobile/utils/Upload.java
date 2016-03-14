package com.fachru.sigmamobile.utils;

import android.util.Log;

import com.activeandroid.Model;
import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.model.DoItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by fachru on 02/03/16.
 */
public class Upload<T extends Model> {

    private RestApiManager apiManager;

    public Upload() {
        apiManager = new RestApiManager();
    }

    public void setData(T data) {
        call(data).enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                Log.i(Constanta.TAG, "Status " + response.code() + " " + response.message() + " " + response.headers());
                if (response.isSuccess())
                    if (!saveData(response.body())) response.body().save();
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e(Constanta.TAG, t.getMessage(), t);
            }
        });
    }

    private Call<T> call(T data) {
        if (data instanceof DoHead)
            return (Call<T>) apiManager.getDoHeadAPI()._Store((DoHead) data);
        if (data instanceof DoItem)
            return (Call<T>) apiManager.getDoItemAPI()._Store((DoItem) data);
        else
            return null;
    }

    private boolean getInstance(T t) {
        if (t instanceof DoItem)
            return true;

        return false;
    }

    private boolean saveData(T t) {
        if (t instanceof DoItem) {
            DoItem doItem = DoItem.find(((DoItem) t).docno, ((DoItem) t).noitem);
            doItem.uploaded = true;
            doItem.save();
            return true;
        } else {
            return false;
        }
    }


}
