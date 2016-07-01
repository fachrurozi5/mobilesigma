package com.fachru.sigmamobile.controller.interfaces;

import com.fachru.sigmamobile.model.Customer;

import java.util.List;

/**
 * Created by fachru on 17/12/15.
 */
public interface OnCustomerCallbackListener extends OnCallbackListener {
	void onFetchProgress(Customer customer);

	void onFetchProgress(List<Customer> list);
}
