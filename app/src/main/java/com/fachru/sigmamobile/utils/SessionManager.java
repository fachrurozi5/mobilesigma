package com.fachru.sigmamobile.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.fachru.sigmamobile.app.MyApplication;

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
	public static final String KEY_OSC_MANAGEMENT = "osc_management";
	public static final String KEY_DOWNLOAD_CUSTOMER = "dowbload_customer";
	public static final String KEY_DOWNLOAD_PRSTAT = "dowbload_prstat";
	public static final String KEY_DOWNLOAD_PRSTAT2 = "dowbload_prstat2";
	public static final String KEY_DOWNLOAD_PRODUCT = "dowbload_product";
	public static final String KEY_DOWNLOAD_WAREHOUSE = "download_warehouse";
	public static final String KEY_DOWNLOAD_WAREHOUSE_STOCK = "download_warehouse_stock";
	public static final String KEY_DOWNLOAD_UNIT_CONVERSION = "download_unit_converter";
	public static final String KEY_DOWNLOAD_UNIT = "download_unit";
	public static final String KEY_TOKEN = "api_token";
	public static final String KEY_USERNAME = "key_username";
	public static final String KEY_DOWNLOAD_OUTLET = "key_outlet";
	public static final String KEY_DOWNLOAD_OUTLET_TYPE = "key_outlet_type";

	/*
	* service
	* */
	public static final String KEY_LOCATION_TRACKER_SERVICE = "key_location_tracker_service";
	private static final int PRIVATE_MODE = 0;
	private static final String PREF_NAME = "sigma_preference";
	private static final String KEY_DOWNLOAD_DISCOUNT = "download_discount";
	private static final String KEY_DOWNLOAD_TOLERANCE = "download_tolerance";
	private static final String KEY_DOWNLOAD_LV1_TOLERANCE = "download_discount_lv1";
	private SharedPreferences preferences;
	private Editor editor;

	public SessionManager(Context context) {
		preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = preferences.edit();
	}

	public static SharedPreferences pref() {
		return MyApplication.getAppContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
	}

	public static SharedPreferences.Editor prefEdit() {
		return MyApplication.getAppContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE).edit();
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

	public String getUsername() {
		return preferences.getString(KEY_USERNAME, "");
	}

	public void setUsername(String username) {
		editor.putString(KEY_USERNAME, username);
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

	public void setUnitConverterDone(boolean done) {
		editor.putBoolean(KEY_DOWNLOAD_UNIT_CONVERSION, done);
		editor.commit();
	}

	public void setUnitDone(boolean done) {
		editor.putBoolean(KEY_DOWNLOAD_UNIT, done);
		editor.commit();
	}

	public void setDiscountDone(boolean done) {
		editor.putBoolean(KEY_DOWNLOAD_DISCOUNT, done);
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

	public boolean hasUnitConversion() {
		return preferences.getBoolean(KEY_DOWNLOAD_UNIT_CONVERSION, false);
	}

	public boolean hasUnit() {
		return preferences.getBoolean(KEY_DOWNLOAD_UNIT, false);
	}

	public boolean hasDiscount() {
		return preferences.getBoolean(KEY_DOWNLOAD_DISCOUNT, false);
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

	public long getOSCManager() {
		return preferences.getLong(KEY_OSC_MANAGEMENT, -1);
	}

    /*
    * for token
    *
    * */

	public void setEmployee(long employee_id) {
		editor.putLong(KEY_EMPLOYEE, employee_id);
		editor.commit();
	}

	public boolean isPinSaved() {
		return preferences.getBoolean(IS_PIN_SAVED, false);
	}

	public String getToken() {
		return preferences.getString(KEY_TOKEN, "");
	}

	public void setToken(String token) {
		editor.putString(KEY_TOKEN, token);
		editor.commit();
	}


	public void setOutletDone(boolean done) {
		editor.putBoolean(KEY_DOWNLOAD_OUTLET, done);
		editor.commit();
	}

	public boolean hasOutlet() {
		return preferences.getBoolean(KEY_DOWNLOAD_OUTLET, false);
	}

	public boolean hasOutletType() {
		return preferences.getBoolean(KEY_DOWNLOAD_OUTLET_TYPE, false);
	}

	public void setOutletTypeDone(boolean done) {
		editor.putBoolean(KEY_DOWNLOAD_OUTLET_TYPE, done);
		editor.commit();
	}

	public boolean hasTolerance() {
		return preferences.getBoolean(KEY_DOWNLOAD_TOLERANCE, false);
	}

	public void setToleranceDown(boolean done) {
		editor.putBoolean(KEY_DOWNLOAD_TOLERANCE, done);
		editor.commit();
	}

	public void setDiscountLv1Done(boolean done) {
		editor.putBoolean(KEY_DOWNLOAD_LV1_TOLERANCE, done);
		editor.commit();
	}

	public boolean hasDiscountLv1() {
		return preferences.getBoolean(KEY_DOWNLOAD_LV1_TOLERANCE, false);
	}
}
