package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.WarehouseStock;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


/**
 * Created by fachru on 30/12/15.
 */
public interface WhStockAPI {

    @GET("whstock")
    Call<String> Records();

    @GET("whstocks")
    Call<List<WarehouseStock>> _Records();

    @FormUrlEncoded
    @POST("whstock/getwhstock")
    Call<String> getwhstock(@Field("whid") String whid);
}
