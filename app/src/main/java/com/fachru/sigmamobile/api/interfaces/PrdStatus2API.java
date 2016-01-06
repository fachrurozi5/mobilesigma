package com.fachru.sigmamobile.api.interfaces;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by fachru on 06/01/16.
 */
public interface PrdStatus2API {
    @GET("prstatids")
    Call<String> Records();
}
