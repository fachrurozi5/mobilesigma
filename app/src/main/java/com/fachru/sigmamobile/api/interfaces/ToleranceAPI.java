package com.fachru.sigmamobile.api.interfaces;


import com.fachru.sigmamobile.model.Tolerance;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by fachru on 09/06/16.
 */
public interface ToleranceAPI {

	@GET("tolerances")
	Call<List<Tolerance>> _Records();

	@GET("tolerances/sync")
	Call<List<Tolerance>> Sync();
}
