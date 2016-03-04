package com.fachru.sigmamobile;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.controller.Controller;
import com.fachru.sigmamobile.controller.interfaces.OnStoreListener;
import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.service.SigmaSync;
import com.fachru.sigmamobile.utils.Constanta;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ActivityTest extends AppCompatActivity implements OnStoreListener{


    private MaterialDialog dialog;

    private Controller controller;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_test);

        controller = new Controller(this);
        GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .excludeFieldsWithoutExposeAnnotation();
        gson = builder.create();

        startService(new Intent(this, SigmaSync.class));

        Log.e(Constanta.TAG, DoHead.getAllNotUpload().toString());

    }

    public void onShowPdf(View view) {

    }

    public void onStore(View view) {

        DoHead doHead = DoHead.last();
        Log.e(Constanta.TAG, gson.toJson(doHead));
        controller.startStore(doHead);
    }

    @Override
    public void onStoreStart() {
        dialog = new MaterialDialog.Builder(this)
                .title("Store DoHead")
                .content("Please Wait")
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();
    }

    @Override
    public void onStoreComplete() {
        dialog.dismiss();
    }

    @Override
    public void onStoreFailed(Throwable t) {
        dialog.dismiss();
        Log.e(Constanta.TAG, t.getMessage(), t);
    }

}
