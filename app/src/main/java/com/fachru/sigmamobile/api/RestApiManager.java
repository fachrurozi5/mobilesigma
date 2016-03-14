package com.fachru.sigmamobile.api;

import android.content.Context;

import com.fachru.sigmamobile.BuildConfig;
import com.fachru.sigmamobile.api.interfaces.CustomerAPI;
import com.fachru.sigmamobile.api.interfaces.DoHeadAPI;
import com.fachru.sigmamobile.api.interfaces.DoItemAPI;
import com.fachru.sigmamobile.api.interfaces.EmployeeAPI;
import com.fachru.sigmamobile.api.interfaces.PrdStatus2API;
import com.fachru.sigmamobile.api.interfaces.PrdStatusAPI;
import com.fachru.sigmamobile.api.interfaces.ProductAPI;
import com.fachru.sigmamobile.api.interfaces.WarehouseAPI;
import com.fachru.sigmamobile.api.interfaces.WhStockAPI;
import com.fachru.sigmamobile.utils.Constanta;
import com.fachru.sigmamobile.utils.ServiceGenerator;
import com.fachru.sigmamobile.utils.StringDesirializer;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*import retrofit.GsonConverterFactory;
import retrofit.Retrofit;*/



/**
 * Created by fachru on 17/12/15.
 */
public class RestApiManager {

    private Context context;

    private GsonBuilder builder;

    private DoHeadAPI doHeadAPI;
    private DoItemAPI doItemAPI;
    private CustomerAPI customerAPI;
    private EmployeeAPI employeeApi;
    private WhStockAPI whStockAPI;
    private PrdStatusAPI prdStatusAPI;
    private PrdStatus2API prdStatus2API;
    private ProductAPI productAPI;
    private WarehouseAPI warehouseAPI;

    public RestApiManager() {

        builder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .excludeFieldsWithoutExposeAnnotation();
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
    *  DoItem Api
    * */
    public DoItemAPI getDoItemAPI() {
        if (doItemAPI == null) {
            doItemAPI = retrofit().create(DoItemAPI.class);
        }
        return doItemAPI;
    }

    /*
    * Customer/Outlet API
    * */
    public CustomerAPI getCustomerAPI() {
        if (customerAPI == null) {
            customerAPI = retrofit().create(CustomerAPI.class);
        }
        return customerAPI;
    }

    /*
    * Employee/Salesman API
    * */
    public EmployeeAPI getEmployeeApi() {
        if (employeeApi == null) {
//            employeeApi = retrofit().create(EmployeeAPI.class);
            employeeApi = ServiceGenerator.createService(EmployeeAPI.class, "1234");
        }

        return employeeApi;
    }

    /*
    * WarehouseStock API
    * */
    public WhStockAPI getWhStock() {
        if (whStockAPI == null) {
            whStockAPI = retrofit().create(WhStockAPI.class);
        }

        return whStockAPI;
    }

    /*
    * Product Status API
    * */
    public PrdStatusAPI getPrstat() {
        if (prdStatusAPI == null) {
            prdStatusAPI = retrofit().create(PrdStatusAPI.class);
        }
        return prdStatusAPI;
    }

    /*
    * Product Status 2 API
    * */
    public PrdStatus2API getPrstat2() {
        if (prdStatus2API == null) {
            prdStatus2API = retrofit().create(PrdStatus2API.class);
        }
        return prdStatus2API;
    }

    /*
    * Product API
    * */
    public ProductAPI getProduct() {
        if (productAPI == null) {
            productAPI = retrofit().create(ProductAPI.class);
        }

        return productAPI;
    }

    /*
    * Warehouse
    * */
    public WarehouseAPI getWarehouseAPI() {
        if (warehouseAPI == null) {
            warehouseAPI = retrofit().create(WarehouseAPI.class);
        }

        return warehouseAPI;
    }

    private Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constanta.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(builder.create()))
                .build();
    }

}
