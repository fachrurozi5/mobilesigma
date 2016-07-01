package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.IdleTimeAnalysis;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by fachru on 25/05/16.
 */
public interface IdleTimeAnalysisAPI {

	@POST("timeanalysis/store")
	Call<IdleTimeAnalysis> _Store(@Body IdleTimeAnalysis timeAnalysis);

}
