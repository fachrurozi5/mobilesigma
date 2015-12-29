package com.fachru.sigmamobile.controller.interfaces;

import com.fachru.sigmamobile.model.Employee;

/**
 * Created by fachru on 29/12/15.
 */
public interface OnEmployeeCallbackListener extends OnCallbackListener {
    void onFetchProgress(Employee employee);
}
