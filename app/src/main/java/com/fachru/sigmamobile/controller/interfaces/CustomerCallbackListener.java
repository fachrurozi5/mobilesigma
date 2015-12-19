package com.fachru.sigmamobile.controller.interfaces;

import com.fachru.sigmamobile.model.Customer;

import java.util.List;

/**
 * Created by fachru on 17/12/15.
 */
public interface CustomerCallbackListener {
    void onFetchStart();
    void onFetchProgress(Customer customer);
    void onFetchProgress(List<Customer> list);
    void onFetchComplite();
    void onFetchFailed(Throwable t);
}
