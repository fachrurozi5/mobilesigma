package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.SoItem;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


/**
 * Created by fachru on 31/12/15.
 */
public interface SoItemAPI {

	@GET("soitem")
	Call<String> Records();

	@POST("soitem/store")
	Call<SoItem> _Store(@Body SoItem doItem);
}
