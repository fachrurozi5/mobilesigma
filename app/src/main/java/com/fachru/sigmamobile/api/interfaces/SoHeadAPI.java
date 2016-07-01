package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.SoHead;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


/**
 * Created by fachru on 31/12/15.
 */
public interface SoHeadAPI {

	@GET("sohead")
	Call<String> Records();

	@POST("so/store")
	Call<SoHead> _Store(@Body SoHead soHead);
}
