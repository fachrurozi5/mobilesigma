package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.OSCManagement;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by fachru on 24/06/16.
 */
public interface OSCManagementAPI {

	@POST("osc/store")
	Call<OSCManagement> _Store(@Body OSCManagement oscManagement);

}
