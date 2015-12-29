package com.fachru.sigmamobile.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by fachru on 29/10/15.
 */
public class SessionManager {

    private SharedPreferences preferences;
    private Editor editor;
    private Context context;
    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "sigma_preference";
    public static final String KEY_AFTER_INSTALL = "after_install";
    public static final String KEY_PIN = "pin";
    public static final String IS_PIN_SAVED = "is_pin_saved";
    public static final String KEY_LAST_APP_PN = "last_app_pin";
    public static final String KEY_CUSTOMER = "key_customer";
    public static final String KEY_EMPLOYEE = "last_outlet";

    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
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

    public void setCustomer(long customer_id) {
        editor.putLong(KEY_CUSTOMER, customer_id);
        editor.commit();
    }

    public void setEmployee(long employee_id) {
        editor.putLong(KEY_EMPLOYEE, employee_id);
        editor.commit();
    }

    public void unSavePin() {
        editor.putBoolean(IS_PIN_SAVED, false);
        editor.commit();
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

    public long getEmployee() {
        return preferences.getLong(KEY_EMPLOYEE, -1);
    }

    public boolean isPinSaved() {
        return preferences.getBoolean(IS_PIN_SAVED, false);
    }
}
