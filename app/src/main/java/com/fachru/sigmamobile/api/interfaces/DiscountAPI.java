package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.Discount;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by fachru on 09/05/16.
 */
public interface DiscountAPI {

    @GET("discounts/structures")
    Call<List<Discount>> _Records();

    @GET("discounts/sync")
    Call<List<Discount>> Sync();

}
