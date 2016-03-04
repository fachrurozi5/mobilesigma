package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.Customer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


/**
 * Created by fachru on 17/12/15.
 */
public interface CustomerAPI {

    @GET("customers")
    Call<String> Records();

    @GET("customers")
    Call<List<Customer>> _Records();

    @FormUrlEncoded
    @POST("customer/view")
    Call<String> getRecord(@Field("custid") String s);

    @POST("customer/store")
    Call<List<Customer>> store(@Body Customer customer);

}
