package com.fachru.sigmamobile.api.interfaces;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by fachru on 29/12/15.
 */
public interface EmployeeApi {

    @FormUrlEncoded
    @POST("login/getlogin")
    Call<String> login(@Field("username1") String username, @Field("password1") String password);
}
