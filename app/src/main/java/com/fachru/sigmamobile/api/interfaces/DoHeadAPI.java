package com.fachru.sigmamobile.api.interfaces;

import com.activeandroid.Model;
import com.fachru.sigmamobile.model.DoHead;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


/**
 * Created by fachru on 31/12/15.
 */
public interface DoHeadAPI {

    @GET("dohead")
    Call<String> Records();

    @POST("dohead/store")
    Call<String> Store(@Body DoHead doHead);

    @POST("do/store")
    Call<DoHead> _Store(@Body DoHead doHead);

    /*@POST("do/store")
    <T extends Model> Call<T> _Store(T data);*/
}
