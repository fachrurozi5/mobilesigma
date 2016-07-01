package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.DoItem;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


/**
 * Created by fachru on 31/12/15.
 */
public interface DoItemAPI {

	@GET("doitem")
	Call<String> Records();

	@POST("doitem/store")
	Call<DoItem> _Store(@Body DoItem doItem);
}
