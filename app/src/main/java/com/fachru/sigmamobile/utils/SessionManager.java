package com.fachru.sigmamobile.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by fachru on 29/10/15.
 */
public class SessionManager {

    public static final String KEY_AFTER_INSTALL = "after_install";
    public static final String KEY_PIN = "pin";
    public static final String IS_PIN_SAVED = "is_pin_saved";
    public static final String KEY_LAST_APP_PN = "last_app_pin";
    public static final String KEY_CUSTOMER = "key_customer";
    public static final String KEY_EMPLOYEE = "last_outlet";
    public static final String KEY_DOWNLOAD_CUSTOMER = "dowbload_customer";
    public static final String KEY_DOWNLOAD_PRSTAT = "dowbload_prstat";
    public static final String KEY_DOWNLOAD_PRSTAT2 = "dowbload_prstat2";
    public static final String KEY_DOWNLOAD_PRODUCT = "dowbload_product";
    public static final String KEY_DOWNLOAD_WAREHOUSE = "download_warehouse";
    public static final String KEY_DOWNLOAD_WAREHOUSE_STOCK = "download_warehouse_stock";
    public static final String KEY_IP_ADDRESS = "ipaddress";
    /*
    * service
    * */
    public static final String KEY_LOCATION_TRACKER_SERVICE = "key_location_tracker_service";
    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "sigma_preference";
    private SharedPreferences preferences;
    private Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public boolean getLocationTrackerService() {
        return preferences.getBoolean(KEY_LOCATION_TRACKER_SERVICE, false);
    }

    /*
    * for service
    * */
    public void setLocationTrackerService(boolean b) {
        editor.putBoolean(KEY_LOCATION_TRACKER_SERVICE, b);
        editor.commit();
    }

    public void setAfterInstall(boolean b) {
        editor.putBoolean(KEY_AFTER_INSTALL, b);
        editor.commit();
    }

    public void savePin(String pin) {
        editor.putBoolean(IS_PIN_SAVED, true);
        editor.putString(KEY_PIN, pin);
        editor.commit();
    }

    public void saveLastAppPn(String lastApp) {
        editor.putString(KEY_LAST_APP_PN, lastApp);
        editor.commit();
    }

    public void unSavePin() {
        editor.putBoolean(IS_PIN_SAVED, false);
        editor.commit();
    }

    public void setCustomerDone(boolean done) {
        editor.putBoolean(KEY_DOWNLOAD_CUSTOMER, done);
        editor.commit();
    }

    public void setPrstatDone(boolean done) {
        editor.putBoolean(KEY_DOWNLOAD_PRSTAT, done);
        editor.commit();
    }

    public void setPrstat2Done(boolean done) {
        editor.putBoolean(KEY_DOWNLOAD_PRSTAT2, done);
        editor.commit();
    }

    public void setProductDone(boolean done) {
        editor.putBoolean(KEY_DOWNLOAD_PRODUCT, done);
        editor.commit();
    }

    public void setWarehouseDone(boolean done) {
        editor.putBoolean(KEY_DOWNLOAD_WAREHOUSE, done);
        editor.commit();
    }

    public void setWarehouseStockDone(boolean done) {
        editor.putBoolean(KEY_DOWNLOAD_WAREHOUSE_STOCK, done);
        editor.commit();
    }

    public boolean hasCustomer() {
        return preferences.getBoolean(KEY_DOWNLOAD_CUSTOMER, false);
    }

    public boolean hasPrstat() {
        return preferences.getBoolean(KEY_DOWNLOAD_PRSTAT, false);
    }

    public boolean hasPrstat2() {
        return preferences.getBoolean(KEY_DOWNLOAD_PRSTAT2, false);
    }

    public boolean hasProduct() {
        return preferences.getBoolean(KEY_DOWNLOAD_PRODUCT, false);
    }

    public boolean hasWarehouse() {
        return preferences.getBoolean(KEY_DOWNLOAD_WAREHOUSE, false);
    }

    public boolean hasWarehouseStock() {
        return preferences.getBoolean(KEY_DOWNLOAD_WAREHOUSE_STOCK, false);
    }

    public boolean getAffterInstall() {
        return preferences.getBoolean(KEY_AFTER_INSTALL, true);
    }

    public String getPin() {
        return preferences.getString(KEY_PIN, "");
    }

    public String getLastApp() {
        return preferences.getString(KEY_LAST_APP_PN, "");
    }

    public long getCustomer() {
        return preferences.getLong(KEY_CUSTOMER, -1);
    }

    public void setCustomer(long customer_id) {
        editor.putLong(KEY_CUSTOMER, customer_id);
        editor.commit();
    }

    public long getEmployee() {
        return preferences.getLong(KEY_EMPLOYEE, -1);
    }

    public void setEmployee(long employee_id) {
        editor.putLong(KEY_EMPLOYEE, employee_id);
        editor.commit();
    }

    public void setIpAddress(String ipAddress) {
        Constanta.IP_ADDRESS = ipAddress;
        editor.putString(KEY_IP_ADDRESS, ipAddress);
        editor.commit();
    }

    public String getIpAddress() {
        return preferences.getString(KEY_IP_ADDRESS, Constanta.IP_ADDRESS);
    }

    public boolean isPinSaved() {
        return preferences.getBoolean(IS_PIN_SAVED, false);
    }
}
