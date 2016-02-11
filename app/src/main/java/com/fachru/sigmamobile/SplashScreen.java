package com.fachru.sigmamobile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.model.ProductStatus;
import com.fachru.sigmamobile.model.ProductStatus2;
import com.fachru.sigmamobile.model.Warehouse;
import com.fachru.sigmamobile.model.WarehouseStock;
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

    private static final int INTERVAL = 1000;
    private static final int KEY_CUSTOMER = 0;
    private static final int KEY_WAREHOUSE = 1;
    private static final int KEY_WAREHOUSE_STOCK = 2;
    private static final int KEY_PRODUCT_STATUS = 3;
    private static final int KEY_PRODUCT_STATUS2 = 4;
    private static final int KEY_PRODUCT = 5;

    private RestApiManager apiManager = new RestApiManager();


    /*
    * utils
    * */
    private SessionManager manager;

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
    private Call<String> callback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initComp();

        manager = new SessionManager(this);

        if (manager.getAffterInstall()) {
            downloadOrContinue();
        } else {
            LoadingThread(10, 100, true);
        }
    }

    private void initComp() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        label = (TextView) findViewById(R.id.tv_loading_info);
    }


    /*
    * Show progress bar loading with Thread
    * */
    private void LoadingThread(final int sleepDuration, final int state, final boolean afterinstall) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                String stringLabel = "";
                switch (state) {
                    case KEY_CUSTOMER:
                        stringLabel = "Load Customer";
                        downloadCustomer();
                        break;
                    case KEY_WAREHOUSE:
                        stringLabel = "Load Warehouse";
                        downloadWarehouse();
                        break;
                    case KEY_WAREHOUSE_STOCK:
                        stringLabel = "Load WarehouseStock";
                        downloadWarehouseStock();
                        break;
                    case KEY_PRODUCT_STATUS:
                        stringLabel = "Load Prstatid";
                        downloadPrstat();
                        break;
                    case KEY_PRODUCT_STATUS2:
                        stringLabel = "Load Prstatid2";
                        downloadPrstat2();
                        break;

                    case KEY_PRODUCT:
                        stringLabel = "Load Product";
                        downloadProduct();
                        break;
                    default:
                        stringLabel = "Loading...";
                }

                while (progress < progressBar.getMax()) {
                    progress += 1;
                    final int progressFinal = progress;
                    final String finalLebel = stringLabel;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressFinal);
                            progressBar.setSecondaryProgress(progressFinal + 5);
                            label.setText(finalLebel);
                        }
                    });

                    try {
                        Thread.sleep(sleepDuration);
                    } catch (InterruptedException e) {
                        thread.interrupt();
                    }
                }

                if (afterinstall) {
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                } else {
                    callback.cancel();
                }

            }
        });

        thread.start();
    }


    /******************
     * Method Download
     **************************/

    /*
    * Download Customer
    * */
    private void downloadCustomer() {
        callback = apiManager.getCustomerAPI().Records();
        callback.enqueue(new Callback<String>() {
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
                showError("Customer", t.getMessage());
            }
        });
    }

    /*
    * Download Warehouse
    * */
    private void downloadWarehouse() {
        callback = apiManager.getWarehouseAPI().Records();
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.d(Constanta.TAG, response.body());
                if (response.isSuccess() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getBoolean(Constanta.TAG_STATUS)) {
                            storeWarehouse(jsonObject.getJSONArray(Constanta.TAG_DATA));
                        } else {
                            label.setText(jsonObject.getString(Constanta.TAG_MESSAGE));
                            label.setTextColor(Color.RED);
                        }
                    } catch (JSONException e) {
                        Log.e(Constanta.TAG, "JSONException", e);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(Constanta.TAG, "On Product Failure", t);
                showError("Warehouse", t.getMessage());
            }
        });
    }

    /*
    * Download WarehouseStock
    * */
    private void downloadWarehouseStock() {
        callback = apiManager.getWhStock().Records();
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.d(Constanta.TAG, response.body());
                if (response.isSuccess() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getBoolean(Constanta.TAG_STATUS)) {
                            storeWarehouseStock(jsonObject.getJSONArray(Constanta.TAG_DATA));
                        } else {
                            label.setText(jsonObject.getString(Constanta.TAG_MESSAGE));
                            label.setTextColor(Color.RED);
                        }
                    } catch (JSONException e) {
                        Log.e(Constanta.TAG, "JSONException", e);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(Constanta.TAG, "On Product Failure", t);
                showError("Warehouse", t.getMessage());
            }
        });
    }

    /*
    * Download Prstatid
    * */
    private void downloadPrstat() {
        callback = apiManager.getPrstat().Records();
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.d(Constanta.TAG, response.body());
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
                showError("Prstatid", t.getMessage());
            }
        });
    }

    /*
    * Download Prstatid2
    * */
    private void downloadPrstat2() {
        callback = apiManager.getPrstat2().Records();
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.d(Constanta.TAG, response.body());
                if (response.isSuccess() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getBoolean(Constanta.TAG_STATUS)) {
                            storePrstat2(jsonObject.getJSONArray(Constanta.TAG_DATA));
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
                Log.e(Constanta.TAG, "On Prstat2 Failure", t);
                showError("Prstatid2", t.getMessage());
            }
        });
    }

    /*
    * Download Product
    * */
    private void downloadProduct() {
        callback = apiManager.getProduct().Records();
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.d(Constanta.TAG, response.body());
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
                showError("Prodcut", t.getMessage());
            }
        });
    }


    /*****************
     * METHOD STORE
     *************************/

    /*
    * Store Customer into SQLite3
    * */
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
                            progressBar.setProgress((finali * 100) / max);
                            progressBar.setSecondaryProgress(((finali * 100) / max) + 5);
                            label.setText("Inserting Customer");
                        }
                    });
                }
                manager.setCustomerDone(true);
                LoadingThread(1000, KEY_PRODUCT_STATUS, false);

            }
        }).start();
    }

    /*
    * Store Prstatid into SQLite3
    * */
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
                            progressBar.setProgress((finali * 100) / max);
                            progressBar.setSecondaryProgress(((finali * 100) / max) + 5);
                            label.setText("Inserting Prastatid");
                        }
                    });
                }
                manager.setPrstatDone(true);
                LoadingThread(1000, KEY_PRODUCT_STATUS2, false);

            }
        }).start();
    }

    /*
    * Store Prstatid2 into SLQite3
    * */
    private void storePrstat2(final JSONArray jsonArray) {
        thread.interrupt();
        progressBar.setProgress(0);
        final int max = jsonArray.length();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < max; i++) {
                    try {
                        ProductStatus2.findOrCreateFromJson(jsonArray.getJSONObject(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    final int finali = i;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress((finali * 100) / max);
                            progressBar.setSecondaryProgress(((finali * 100) / max) + 5);
                            label.setText("Inserting Prastatid2");
                        }
                    });
                }
                manager.setPrstat2Done(true);
                LoadingThread(1000, KEY_PRODUCT, false);

            }
        }).start();
    }

    /*
    * Insert Product into SQLite3
    * */
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
                            progressBar.setProgress((finali * 100) / max);
                            progressBar.setSecondaryProgress(((finali * 100) / max) + 5);
                            label.setText("Inserting Product");
                        }
                    });
                }
                manager.setProductDone(true);
                LoadingThread(1000, KEY_WAREHOUSE, false);
            }
        }).start();
    }

    /*
    * Store Warehouse into SQLite3
    * */
    private void storeWarehouse(final JSONArray jsonArray) {
        thread.interrupt();
        progressBar.setProgress(0);
        final int max = jsonArray.length();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < max; i++) {
                    try {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Warehouse.findOrCreateFromJson(Warehouse.class, Warehouse.PRIMARYKEY, object.getString("WHID"), object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    final int finali = i;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress((finali * 100) / max);
                            progressBar.setSecondaryProgress(((finali * 100) / max) + 5);
                            label.setText("Inserting Warehouse");
                        }
                    });
                }
                manager.setWarehouseDone(true);
                LoadingThread(1000, KEY_WAREHOUSE_STOCK, false);
            }
        }).start();
    }

    /*
    * Store WarehouseStock inti SLQite3
    * */
    private void storeWarehouseStock(final JSONArray jsonArray) {
        thread.interrupt();
        progressBar.setProgress(0);
        final int max = jsonArray.length();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < max; i++) {
                    try {
                        JSONObject object = jsonArray.getJSONObject(i);
                        WarehouseStock stock = WarehouseStock.fromJson(object);
                        stock.product = Product.find(stock.product_id);
                        stock.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    final int finali = i;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress((finali * 100) / max);
                            progressBar.setSecondaryProgress(((finali * 100) / max) + 5);
                            label.setText("Inserting WarehouseStock");
                        }
                    });
                }
                manager.setWarehouseStockDone(true);
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        }).start();
    }

    /*************
     * Error Handling
     ******************/

    /*
    * Show dialog Error
    * */
    private void showError(String title, String message) {
        thread.interrupt();
        new MaterialDialog.Builder(this)
                .title(title)
                .iconRes(android.R.drawable.ic_dialog_alert)
                .content(message)
                .positiveText("Try again")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        downloadOrContinue();
                    }
                })
                .show();
    }

    /*
    * method for continue download
    * */
    private void downloadOrContinue() {
        if (!manager.hasCustomer()) {
            LoadingThread(INTERVAL, KEY_CUSTOMER, false);
        } else if (!manager.hasPrstat()) {
            LoadingThread(INTERVAL, KEY_PRODUCT_STATUS, false);
        } else if (!manager.hasPrstat2()) {
            LoadingThread(INTERVAL, KEY_PRODUCT_STATUS2, false);
        } else if (!manager.hasProduct()) {
            LoadingThread(INTERVAL, KEY_PRODUCT, false);
        } else if (!manager.hasWarehouse()) {
            LoadingThread(INTERVAL, KEY_WAREHOUSE, false);
        } else if (!manager.hasWarehouseStock()) {
            LoadingThread(INTERVAL, KEY_WAREHOUSE_STOCK, false);
        }
    }

}
