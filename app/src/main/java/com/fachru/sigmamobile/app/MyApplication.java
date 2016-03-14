package com.fachru.sigmamobile.app;

import android.content.Context;

/**
 * Created by fachru on 07/03/16.
 */
public class MyApplication extends com.activeandroid.app.Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
