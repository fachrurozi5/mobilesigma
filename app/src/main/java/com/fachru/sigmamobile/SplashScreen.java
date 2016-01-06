package com.fachru.sigmamobile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.model.ProductStatus;
import com.fachru.sigmamobile.utils.Constanta;
import com.fachru.sigmamobile.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SplashScreen extends AppCompatActivity {

    private static final int KEY_CUSTOMER = 0;
    private static final int KEY_PRODUCT_STATUS = 1;
    private static final int KEY_PRODUCT = 2;

    private RestApiManager apiManager = new RestApiManager();

    /*
    * widget
    * */
    private ProgressBar progressBar;
    private TextView label;

    /*
    * android.os
    * */
    private Handler handler = new Handler();
    private Thread thread;

    /*
    * Retrofit Call for Callback
    * */
    private Call<String> callbackCustomer;
    private Call<String> callbackPrstat;
    private Call<String> callbaclProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initComp();

        SessionManager manager = new SessionManager(this);

        if (manager.getAffterInstall()) {
            /*ShowLoading(1000, false);
            downloadCustomer();*/
            LoadingThread(1000, KEY_CUSTOMER, false);
        } else {
            LoadingThread(50, KEY_CUSTOMER, false);
//            ShowLoading(50, true);
        }
    }

    /*private void ShowLoading(int duration, boolean afterisntall) {
        LoadingThread(duration, afterisntall);
    }*/

    private void LoadingThread(final int sleepDuration,final int state, final boolean afterinstall) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;

                switch (state) {
                    case KEY_CUSTOMER:
                        downloadCustomer();
                        break;
                    case KEY_PRODUCT_STATUS:
                        downloadPrstat();
                        break;
                    case KEY_PRODUCT:
                        downloadProduct();
                        break;
                }

                while (progress < progressBar.getMax()) {
                    progress += 1;
                    final int progressFinal = progress;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressFinal);
                            progressBar.setSecondaryProgress(progressFinal+5);
                            label.setText("Loading...");
                        }
                    });

                    try {
                        Thread.sleep(sleepDuration);
                    } catch (InterruptedException e){
                        thread.interrupt();
                    }
                }

                if (afterinstall) {
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                } else {
                    switch (state) {
                        case KEY_CUSTOMER:
                            callbackCustomer.cancel();
                            break;
                        case KEY_PRODUCT_STATUS:
                            callbackPrstat.cancel();
                            break;
                        case KEY_PRODUCT:
                            callbaclProduct.cancel();
                            break;
                    }
                }

            }
        });

        thread.start();
    }

    private void storeCustomer(final JSONArray jsonArray) {
        thread.interrupt();
        progressBar.setProgress(0);
        final int max = jsonArray.length();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < max; i++) {
                    try {
                        Customer.findOrCreateFromJson(jsonArray.getJSONObject(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    final int finali = i;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress((finali*100) / max);
                            progressBar.setSecondaryProgress(((finali*100) / max) + 5);
                            label.setText("Inserting Customer");
                        }
                    });
                }
                /*ShowLoading(1000, false);
                downloadPrstat();*/
                LoadingThread(1000, KEY_PRODUCT_STATUS, false);

            }
        }).start();
    }

    private void storePrstat(final JSONArray jsonArray) {
        thread.interrupt();
        progressBar.setProgress(0);
        final int max = jsonArray.length();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < max; i++) {
                    try {
                        ProductStatus.findOrCreateFromJson(jsonArray.getJSONObject(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    final int finali = i;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress((finali*100) / max);
                            progressBar.setSecondaryProgress(((finali*100) / max) + 5);
                            label.setText("Inserting Prastatid");
                        }
                    });
                }

                /*ShowLoading(1000, false);
                downloadProduct();*/
                LoadingThread(1000, KEY_PRODUCT, false);

            }
        }).start();
    }

    private void storeProduct(final JSONArray jsonArray) {
        thread.interrupt();
        progressBar.setProgress(0);
        final int max = jsonArray.length();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < max; i++) {
                    try {
                        Product.findOrCreateFromJson(jsonArray.getJSONObject(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    final int finali = i;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress((finali*100) / max);
                            progressBar.setSecondaryProgress(((finali*100) / max) + 5);
                            label.setText("Inserting Product");
                        }
                    });
                }

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }).start();
    }

    private void initComp() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        label       = (TextView) findViewById(R.id.tv_loading_info);
    }

    private void downloadCustomer() {
        callbackCustomer = apiManager.getCustomerAPI().Records();
        callbackCustomer.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.d(Constanta.TAG, response.body());
                if (response.isSuccess() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getBoolean(Constanta.TAG_STATUS)) {
                            storeCustomer(jsonObject.getJSONArray(Constanta.TAG_DATA));
                        } else {
                            label.setText(jsonObject.getString(Constanta.TAG_MESSAGE));
                            label.setTextColor(Color.RED);
                        }

                    } catch (JSONException e) {
                        Log.e(Constanta.TAG, "JSONExceptopn", e);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(Constanta.TAG, "On Customer Failure", t);
            }
        });
    }

    private void downloadPrstat() {
        callbackPrstat = apiManager.getPrstat().Records();
        callbackPrstat.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getBoolean(Constanta.TAG_STATUS)) {
                            storePrstat(jsonObject.getJSONArray(Constanta.TAG_DATA));
                        } else {
                            label.setText(jsonObject.getString(Constanta.TAG_MESSAGE));
                            label.setTextColor(Color.RED);
                        }
                    } catch (JSONException e) {
                        Log.e(Constanta.TAG, "JSONExceptopn", e);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(Constanta.TAG, "On Prstat Failure", t);
            }
        });
    }

    private void downloadProduct() {
        callbaclProduct = apiManager.getProduct().Records();
        callbaclProduct.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getBoolean(Constanta.TAG_STATUS)) {
                            storeProduct(jsonObject.getJSONArray(Constanta.TAG_DATA));
                        } else {
                            label.setText(jsonObject.getString(Constanta.TAG_MESSAGE));
                            label.setTextColor(Color.RED);
                        }
                    } catch (JSONException e) {
                        Log.e(Constanta.TAG, "JSONExceptopn", e);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(Constanta.TAG, "On Product Failure", t);
            }
        });
    }

}
