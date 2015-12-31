package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.Customer;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by fachru on 17/12/15.
 */
public interface CustomerApi {

    @GET("customer")
    Call<String> Records();

    @FormUrlEncoded
    @POST("customer/view")
    Call<String> getRecord(@Field("custid") String s);

    @POST("customer/create")
    Call<String> store(@Body Customer customer);

}
