package com.fachru.sigmamobile.api.interfaces;


import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by fachru on 06/01/16.
 */
public interface PrdStatusAPI {

	@GET("prstatid")
	Call<String> Records();

}
