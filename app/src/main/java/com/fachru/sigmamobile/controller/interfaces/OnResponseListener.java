package com.fachru.sigmamobile.controller.interfaces;

/**
 * Created by fachru on 25/02/16.
 */
public interface OnResponseListener {

    void onFetchStart();

    void onFetchComplete();

    void onStoreStart();

    void onStoreComplete();

    void onFetchFailed(Throwable t);

}
