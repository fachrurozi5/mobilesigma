package com.fachru.sigmamobile.controller.interfaces;

/**
 * Created by fachru on 29/12/15.
 */
public interface OnCallbackListener {
    void onFetchStart();
    void onFetchComplete();
    void onFetchFailed(Throwable t);
}
