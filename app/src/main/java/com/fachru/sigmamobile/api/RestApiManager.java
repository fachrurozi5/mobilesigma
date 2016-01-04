package com.fachru.sigmamobile.api;

import com.fachru.sigmamobile.api.interfaces.CustomerApi;
import com.fachru.sigmamobile.api.interfaces.DoHeadAPI;
import com.fachru.sigmamobile.api.interfaces.EmployeeApi;
import com.fachru.sigmamobile.api.interfaces.ProductApi;
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

    private DoHeadAPI doHeadAPI;
    private CustomerApi customerApi;
    private EmployeeApi employeeApi;
    private WarehouseStockApi warehouseStockApi;
    private ProductApi productApi;
    private GsonBuilder builder;

    public RestApiManager() {
        builder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(String.class, new StringDesirializer());
    }

    /*
    * DoHead API
    * */
    public DoHeadAPI getDoHeadAPI() {
        if (doHeadAPI == null) {
            doHeadAPI = retrofit().create(DoHeadAPI.class);
        }
        return doHeadAPI;
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
            employeeApi = retrofit().create(EmployeeApi.class);
        }

        return employeeApi;
    }

    /*
    * WarehouseStock API
    * */
    public WarehouseStockApi getWhStock() {
        if (warehouseStockApi == null) {
            warehouseStockApi = retrofit().create(WarehouseStockApi.class);
        }

        return warehouseStockApi;
    }

    /*
    * Product API
    * */
    public ProductApi getProduct() {
        if (productApi == null) {
            productApi = retrofit().create(ProductApi.class);
        }

        return productApi;
    }

    private Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constanta.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(builder.create()))
                .build();
    }
}
