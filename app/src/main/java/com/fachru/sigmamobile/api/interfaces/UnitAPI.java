package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.Unit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by fachru on 16/05/16.
 */
public interface UnitAPI {

	@GET("units")
	Call<List<Unit>> _Records();

	@GET("units/sync")
	Call<List<Unit>> Sync();

}
