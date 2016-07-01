package com.fachru.sigmamobile.controller.interfaces;

/**
 * Created by fachru on 29/02/16.
 */
public interface OnStoreListener {

	void onStoreStart();

	void onStoreComplete();

	void onStoreFailed(Throwable t);

}
