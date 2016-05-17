package com.fachru.sigmamobile;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.controller.Controller;
import com.fachru.sigmamobile.controller.interfaces.OnFetchListener;
import com.fachru.sigmamobile.model.Discount;
import com.fachru.sigmamobile.utils.Constanta;

import java.util.List;

public class ActivityTest extends AppCompatActivity implements OnFetchListener<Discount> {


    private MaterialDialog dialog;

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_test);
        controller = new Controller(this);
    }

    public void onShowPdf(View view) {

    }

    public void onStore(View view) {
        controller.startFetching();
    }

    @Override
    public void onFetchStart() {
        dialog = new MaterialDialog.Builder(this)
                .title("Geting Discount")
                .content("Please Wait")
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();
    }

    @Override
    public void onFetchProgress(Discount discount) {

    }

    @Override
    public void onFetchProgress(List<Discount> list) {
        Log.e(Constanta.TAG, list.toString());
    }

    @Override
    public void onFetchComplete() {
        dialog.dismiss();
    }

    @Override
    public void onFetchFailed(Throwable t) {
        dialog.dismiss();
        Log.e(Constanta.TAG, t.getMessage(), t);
    }
}
