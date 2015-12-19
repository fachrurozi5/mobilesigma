package com.fachru.sigmamobile.api;

import com.fachru.sigmamobile.model.Customer;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by fachru on 17/12/15.
 */
public interface CustomerApi {

    @GET("customer")
    Call<List<Customer>> getCustomers();

    @POST("customer/create")
    Call<String> setCustomer(@Body Customer customer);

}
