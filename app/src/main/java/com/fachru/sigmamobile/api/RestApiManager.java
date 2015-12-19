package com.fachru.sigmamobile.api;

import com.fachru.sigmamobile.utils.Constanta;
import com.google.gson.GsonBuilder;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by fachru on 17/12/15.
 */
public class RestApiManager {
    private CustomerApi customerApi;

    public CustomerApi getCustomerApi() {
        if (customerApi == null) {
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .registerTypeAdapter(String.class, new StringDesirializer());

            customerApi = new Retrofit.Builder()
                    .baseUrl(Constanta.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .build()
                    .create(CustomerApi.class);
        }

        return customerApi;
    }
}
