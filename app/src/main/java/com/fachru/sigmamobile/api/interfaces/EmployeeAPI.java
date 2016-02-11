package com.fachru.sigmamobile.api.interfaces;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by fachru on 29/12/15.
 */
public interface EmployeeAPI {

    @FormUrlEncoded
    @POST("login/getlogin")
    Call<String> login(@Field("username") String username, @Field("password") String password);
}
