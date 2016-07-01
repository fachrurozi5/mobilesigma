package com.fachru.sigmamobile.controller.interfaces;

import com.activeandroid.Model;

import java.util.List;

/**
 * Created by fachru on 26/02/16.
 */
public interface OnFetchListener<T extends Model> {

	void onFetchStart();

	void onFetchProgress(T t);

	void onFetchProgress(List<T> list);

	void onFetchComplete();

	void onFetchFailed(Throwable t);

}
