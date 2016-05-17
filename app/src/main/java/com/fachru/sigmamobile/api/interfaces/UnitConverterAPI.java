package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.UnitConverter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by fachru on 28/03/16.
 */
public interface UnitConverterAPI {

    @GET("unit-converter")
    Call<List<UnitConverter>> _Records();

    @GET("unit-converter/sync")
    Call<List<UnitConverter>> Sync();
}
