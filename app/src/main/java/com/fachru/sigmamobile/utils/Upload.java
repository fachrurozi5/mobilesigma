package com.fachru.sigmamobile.utils;

import android.util.Log;

import com.activeandroid.Model;
import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.model.DoItem;
import com.fachru.sigmamobile.model.SoHead;
import com.fachru.sigmamobile.model.SoItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by fachru on 02/03/16.
 */
public class Upload<T extends Model> {

    private static final String TAG = "SalesOrderActivity";

    private RestApiManager apiManager;

    public Upload() {
        apiManager = new RestApiManager();
    }

    public void setData(T data) {
        call(data).enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccess()) saveData(response.body());
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        });
    }

    private Call<T> call(T data) {
        if (data instanceof DoHead)
            return (Call<T>) apiManager.getDoHeadAPI()._Store((DoHead) data);

        if (data instanceof DoItem)
            return (Call<T>) apiManager.getDoItemAPI()._Store((DoItem) data);

        if (data instanceof SoHead)
            return (Call<T>) apiManager.getSoHeadAPI()._Store((SoHead) data);

        if (data instanceof SoItem)
            return (Call<T>) apiManager.getSoItemAPI()._Store((SoItem) data);

        return null;
    }

    private boolean saveData(T t) {
        if (t instanceof DoItem) {
            DoItem doItem = DoItem.find(((DoItem) t).docno, ((DoItem) t).noitem);
            doItem.uploaded = true;
            doItem.save();
            return true;
        }

        if (t instanceof SoItem) {
            SoItem soItem = SoItem.find(((SoItem) t).so, ((SoItem) t).noItem);
            if (soItem != null) {
                soItem.uploaded = true;
                soItem.save();
            }

            return true;
        }

        if (t instanceof SoHead) {
            SoHead soHead = SoHead.find(((SoHead) t).so);
            Log.i(TAG, "Uploaded Save Data SoHead");
            Log.e(TAG, soHead.toString());
            soHead.uploaded = true;
            soHead.save();
            Log.d(TAG, soHead.toString());
        }

        if (t instanceof DoHead) {
            DoHead doHead = DoHead.find(((DoHead) t).doc_no);
            Log.i(TAG, "Uploaded Save Data DoHead");
            Log.e(TAG, doHead.toString());
            doHead.uploaded = true;
            doHead.save();
            Log.d(TAG, doHead.toString());
        }

        return false;

    }


}
