package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Created by fachru on 31/12/15.
 */
public interface ProductAPI {

    @GET("product")
    Call<String> Records();

    @GET("products")
    Call<List<Product>> _Records();

}
