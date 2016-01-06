package com.fachru.sigmamobile.api.interfaces;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by fachru on 30/12/15.
 */
public interface WhStockAPI {

    @GET("whstock")
    Call<String> whstock();

    @FormUrlEncoded
    @POST("whstock/getwhstock")
    Call<String> getwhstock(@Field("whid") String whid);
}
