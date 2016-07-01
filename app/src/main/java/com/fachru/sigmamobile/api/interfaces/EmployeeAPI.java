package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.Employee;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by fachru on 29/12/15.
 */
public interface EmployeeAPI {

	@FormUrlEncoded
	@POST("login")
	Call<Employee> login(@Field("username") String username, @Field("password") String password);
}
