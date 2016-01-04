package com.fachru.sigmamobile.api.interfaces;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by fachru on 31/12/15.
 */
public interface DoHeadAPI {

    @GET("dohead")
    Call<String> Records();

}
