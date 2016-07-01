package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.Discount;
import com.fachru.sigmamobile.model.DiscountLv1;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by fachru on 09/05/16.
 */
public interface DiscountAPI {

	@GET("discounts/structures")
	Call<List<Discount>> _Records();

	@GET("discountslv1")
	Call<List<DiscountLv1>> Lv1Records();

	@GET("discounts/sync")
	Call<List<Discount>> Sync();

	@GET("discountslv1/sync")
	Call<List<DiscountLv1>> Lv1Sync();

}
