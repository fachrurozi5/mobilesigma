package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.Outlet;
import com.fachru.sigmamobile.model.OutletType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by fachru on 30/05/16.
 */
public interface OutletAPI {

	@GET("outlets")
	Call<List<Outlet>> _Records();

	@GET("outlets/visit")
	Call<List<Outlet>> Records();

	@GET("outlets/sync")
	Call<List<Outlet>> Sync();

	@GET("outlets/type/sync")
	Call<List<OutletType>> TypeSync();

	@GET("outlets/type")
	Call<List<OutletType>> TypeRecords();

}
