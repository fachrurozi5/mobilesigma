package com.fachru.sigmamobile.api;

import com.fachru.sigmamobile.api.interfaces.CustomerAPI;
import com.fachru.sigmamobile.api.interfaces.DiscountAPI;
import com.fachru.sigmamobile.api.interfaces.DoHeadAPI;
import com.fachru.sigmamobile.api.interfaces.DoItemAPI;
import com.fachru.sigmamobile.api.interfaces.EmployeeAPI;
import com.fachru.sigmamobile.api.interfaces.PrdStatus2API;
import com.fachru.sigmamobile.api.interfaces.PrdStatusAPI;
import com.fachru.sigmamobile.api.interfaces.ProductAPI;
import com.fachru.sigmamobile.api.interfaces.SoHeadAPI;
import com.fachru.sigmamobile.api.interfaces.SoItemAPI;
import com.fachru.sigmamobile.api.interfaces.TableInfoAPI;
import com.fachru.sigmamobile.api.interfaces.UnitAPI;
import com.fachru.sigmamobile.api.interfaces.UnitConverterAPI;
import com.fachru.sigmamobile.api.interfaces.WarehouseAPI;
import com.fachru.sigmamobile.api.interfaces.WhStockAPI;
import com.fachru.sigmamobile.utils.BooleanSerializer;
import com.fachru.sigmamobile.utils.Constanta;
import com.fachru.sigmamobile.utils.ServiceGenerator;
import com.fachru.sigmamobile.utils.SessionManager;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*import retrofit.GsonConverterFactory;
import retrofit.Retrofit;*/



/**
 * Created by fachru on 17/12/15.
 */
public class RestApiManager {

    private GsonBuilder builder;

    private DoHeadAPI doHeadAPI;
    private DoItemAPI doItemAPI;
    private DiscountAPI discountAPI;
    private SoHeadAPI soHeadAPI;
    private SoItemAPI soItemAPI;
    private CustomerAPI customerAPI;
    private EmployeeAPI employeeApi;
    private WhStockAPI whStockAPI;
    private PrdStatusAPI prdStatusAPI;
    private PrdStatus2API prdStatus2API;
    private ProductAPI productAPI;
    private WarehouseAPI warehouseAPI;
    private UnitAPI unitAPI;
    private UnitConverterAPI unitConverterAPI;
    private TableInfoAPI tableInfoAPI;

    public RestApiManager() {

        BooleanSerializer serializer = new BooleanSerializer();

        builder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(boolean.class, serializer)
                .registerTypeAdapter(Boolean.class, serializer)
                .excludeFieldsWithoutExposeAnnotation();
    }

    /**
     * @return DoHeadAPI
     */
    public DoHeadAPI getDoHeadAPI() {
        if (doHeadAPI == null) {
            doHeadAPI = ServiceGenerator.createService(DoHeadAPI.class, SessionManager.pref().getString(SessionManager.KEY_TOKEN, ""));
        }
        return doHeadAPI;
    }

    public TableInfoAPI getTableInfoAPI() {
        if (tableInfoAPI == null)
            tableInfoAPI = ServiceGenerator.createService(TableInfoAPI.class, SessionManager.pref().getString(SessionManager.KEY_TOKEN, ""));

        return tableInfoAPI;
    }

    /*
    *  DoItem Api
    * */
    public DoItemAPI getDoItemAPI() {
        if (doItemAPI == null) {
            doItemAPI = ServiceGenerator.createService(DoItemAPI.class, SessionManager.pref().getString(SessionManager.KEY_TOKEN, ""));
        }
        return doItemAPI;
    }

    public DiscountAPI getDiscountAPI() {
        if (discountAPI == null) {
            discountAPI = ServiceGenerator.createService(DiscountAPI.class, SessionManager.pref().getString(SessionManager.KEY_TOKEN, ""));
//                    retrofit().create(DiscountAPI.class);
        }

        return discountAPI;
    }

    /*
    * DoHead API
    * */
    public SoHeadAPI getSoHeadAPI() {
        if (soHeadAPI == null) {
            soHeadAPI = ServiceGenerator.createService(SoHeadAPI.class, SessionManager.pref().getString(SessionManager.KEY_TOKEN, ""));
        }
        return soHeadAPI;
    }

    /*
    *  SoItem Api
    * */
    public SoItemAPI getSoItemAPI() {
        if (soItemAPI == null) {
            soItemAPI = ServiceGenerator.createService(SoItemAPI.class, SessionManager.pref().getString(SessionManager.KEY_TOKEN, ""));
        }
        return soItemAPI;
    }

    /*
    * Customer/Outlet API
    * */
    public CustomerAPI getCustomerAPI() {
        if (customerAPI == null) {
            customerAPI = ServiceGenerator.createService(CustomerAPI.class, SessionManager.pref().getString(SessionManager.KEY_TOKEN, ""));
        }
        return customerAPI;
    }

    /*
    * Employee/Salesman API
    * */
    public EmployeeAPI getEmployeeApi() {
        if (employeeApi == null) {
            employeeApi = ServiceGenerator.createService(EmployeeAPI.class, "000000000000000");
        }

        return employeeApi;
    }

    /*
    * WarehouseStock API
    * */
    public WhStockAPI getWhStock() {
        if (whStockAPI == null) {
            whStockAPI = ServiceGenerator.createService(WhStockAPI.class, SessionManager.pref().getString(SessionManager.KEY_TOKEN, ""));
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

    /**
     * @return UnitAPI
     */
    public UnitAPI getUnitAPI() {
        if (unitAPI == null)
            unitAPI = ServiceGenerator.createService(UnitAPI.class, SessionManager.pref().getString(SessionManager.KEY_TOKEN, ""));

        return unitAPI;
    }

    /*
    * Unit Converter API
    * */
    public UnitConverterAPI getUnitConverter() {
        if (unitConverterAPI == null) {
            unitConverterAPI = ServiceGenerator.createService(UnitConverterAPI.class, SessionManager.pref().getString(SessionManager.KEY_TOKEN, ""));
        }
        return unitConverterAPI;
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
            productAPI = ServiceGenerator.createService(ProductAPI.class, SessionManager.pref().getString(SessionManager.KEY_TOKEN, ""));
        }

        return productAPI;
    }

    /*
    * Warehouse
    * */
    public WarehouseAPI getWarehouseAPI() {
        if (warehouseAPI == null) {
            warehouseAPI = ServiceGenerator.createService(WarehouseAPI.class, SessionManager.pref().getString(SessionManager.KEY_TOKEN, ""));
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
