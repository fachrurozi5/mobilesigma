package com.fachru.sigmamobile;

import android.os.Bundle;
import android.app.Activity;

public class CustomerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
