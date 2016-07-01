package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.Warehouse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Created by fachru on 07/01/16.
 */
public interface WarehouseAPI {

	@GET("whouse")
	Call<String> Records();

	@GET("whouses")
	Call<List<Warehouse>> _Records();

	@GET("whouses/sync")
	Call<List<Warehouse>> Sync();

}
