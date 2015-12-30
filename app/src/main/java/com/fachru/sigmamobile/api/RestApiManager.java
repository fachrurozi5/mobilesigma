package com.fachru.sigmamobile.api;

import com.fachru.sigmamobile.api.interfaces.CustomerApi;
import com.fachru.sigmamobile.api.interfaces.EmployeeApi;
import com.fachru.sigmamobile.api.interfaces.WarehouseStockApi;
import com.fachru.sigmamobile.utils.Constanta;
import com.fachru.sigmamobile.utils.StringDesirializer;
import com.google.gson.GsonBuilder;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by fachru on 17/12/15.
 */
public class RestApiManager {

    private CustomerApi customerApi;
    private EmployeeApi employeeApi;
    private WarehouseStockApi warehouseStockApi;
    private GsonBuilder builder;

    public RestApiManager() {
        builder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(String.class, new StringDesirializer());
    }

    /*
    * Customer/Outlet API
    * */
    public CustomerApi getCustomerApi() {
        if (customerApi == null) {
            customerApi = retrofit().create(CustomerApi.class);
        }
        return customerApi;
    }

    /*
    * Employee/Salesman API
    * */
    public EmployeeApi getEmployeeApi() {
        if (employeeApi == null) {
            builder.registerTypeAdapter(String.class, new StringDesirializer());

            employeeApi = new Retrofit.Builder()
                    .baseUrl(Constanta.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(builder.create()))
                    .build()
                    .create(EmployeeApi.class);
        }

        return employeeApi;
    }

    /*
    * WarehouseStock API
    * */
    public WarehouseStockApi getWhStock() {
        if (warehouseStockApi == null) {
            builder.registerTypeAdapter(String.class, new StringDesirializer());

            warehouseStockApi = retrofit().create(WarehouseStockApi.class);
        }

        return warehouseStockApi;
    }

    private Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constanta.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(builder.create()))
                .build();
    }
}
