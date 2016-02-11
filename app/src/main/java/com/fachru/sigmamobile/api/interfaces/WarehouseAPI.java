package com.fachru.sigmamobile.api.interfaces;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by fachru on 07/01/16.
 */
public interface WarehouseAPI {

    @GET("whouse")
    Call<String> Records();

}
