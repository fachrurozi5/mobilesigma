package com.fachru.sigmamobile.api.interfaces;

import com.fachru.sigmamobile.model.TableInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by fachru on 18/05/16.
 */
public interface TableInfoAPI {

    @GET("tableinfo")
    Call<List<TableInfo>> Sync();
}
