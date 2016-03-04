package com.fachru.sigmamobile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.model.Warehouse;
import com.fachru.sigmamobile.model.WarehouseStock;
import com.fachru.sigmamobile.utils.Constanta;
import com.fachru.sigmamobile.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


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
    private Call<List<Customer>> customerCall;
    private Call<List<Product>> productCall;
    private Call<List<WarehouseStock>> whStockCall;
    private Call<List<Warehouse>> whouseCall;

    /*
    * label
    * */
    String stringLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initComp();

        manager = new SessionManager(this);

        downloadOrContinue();

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
                    /*case KEY_PRODUCT_STATUS:
                        stringLabel = "Load Prstatid";
                        downloadPrstat();
                        break;
                    case KEY_PRODUCT_STATUS2:
                        stringLabel = "Load Prstatid2";
                        downloadPrstat2();
                        break;*/
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
                    if (callback != null)
                        callback.cancel();
                    if (customerCall != null)
                        customerCall.cancel();
                    if (productCall != null)
                        productCall.cancel();
                    if (whStockCall != null)
                        whStockCall.cancel();
                    if (whouseCall != null)
                        whouseCall.cancel();
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
        customerCall = apiManager.getCustomerAPI()._Records();
        customerCall.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccess() && response.body() != null) {
                    storeCustomer(response.body());
                }else {
                    label.setText(response.message());
                    label.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                Log.e(Constanta.TAG, "On Customer Failure", t);
                showError("Customer", t.getMessage());
            }
        });
    }

    /*
    * Download Warehouse
    * */
    private void downloadWarehouse() {
        whouseCall = apiManager.getWarehouseAPI()._Records();
        whouseCall.enqueue(new Callback<List<Warehouse>>() {
            @Override
            public void onResponse(Call<List<Warehouse>> call, Response<List<Warehouse>> response) {
                if (response.isSuccess() && response.body() != null) {
                    storeWarehouse(response.body());
                }else {
                    label.setText(response.message());
                    label.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFailure(Call<List<Warehouse>> call, Throwable t) {
                Log.e(Constanta.TAG, "On Product Failure", t);
                showError("Warehouse", t.getMessage());
            }
        });
    }

    /*
    * Download WarehouseStock
    * */
    private void downloadWarehouseStock() {
        whStockCall = apiManager.getWhStock()._Records();
        whStockCall.enqueue(new Callback<List<WarehouseStock>>() {
            @Override
            public void onResponse(Call<List<WarehouseStock>> call, Response<List<WarehouseStock>> response) {
                if (response.isSuccess() && response.body() != null) {
                    storeWarehouseStock(response.body());
                } else {
                    thread.interrupt();
                    label.setText(response.message());
                    label.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFailure(Call<List<WarehouseStock>> call, Throwable t) {
                Log.e(Constanta.TAG, "On Product Failure", t);
                showError("Warehouse", t.getMessage());
            }
        });
    }

    /*
    * Download Product
    * */
    private void downloadProduct() {
        productCall = apiManager.getProduct()._Records();
        productCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccess() && response.body() != null) {
                    storeProduct(response.body());
                } else {
                    label.setText(response.message());
                    label.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
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
    private void storeCustomer(final List<Customer> list) {
        thread.interrupt();
        progressBar.setProgress(0);
        final int max = list.size();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ActiveAndroid.beginTransaction();
                try {
                    int progress = 0;
                    for (Customer customer : list) {
                        customer.save();
                        progress++;
                        final int finalProgress = progress;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress((finalProgress * 100) / max);
                                progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
                                label.setText("Inserting Customer");
                            }
                        });
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                manager.setCustomerDone(true);
                LoadingThread(1000, KEY_PRODUCT, false);

            }
        }).start();
    }

    /*
    * Store Prstatid into SQLite3
    * */
    /*private void storePrstat(final JSONArray jsonArray) {
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

    *//*
    * Store Prstatid2 into SLQite3
    * *//*
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
    }*/

    /*
    * Insert Product into SQLite3
    * */
    private void storeProduct(final List<Product> list) {
        thread.interrupt();
        progressBar.setProgress(0);
        final int max = list.size();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ActiveAndroid.beginTransaction();
                try {
                    int progress = 0;
                    for (Product product : list) {

                        product.save();
                        progress++;
                        final int finalProgress = progress;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress((finalProgress * 100) / max);
                                progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
                                label.setText("Inserting Product");
                            }
                        });
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                manager.setProductDone(true);
                LoadingThread(1000, KEY_WAREHOUSE, false);

            }
        }).start();
    }

    /*
    * Store Warehouse into SQLite3
    * */
    private void storeWarehouse(final List<Warehouse> list) {
        thread.interrupt();
        progressBar.setProgress(0);
        final int max = list.size();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ActiveAndroid.beginTransaction();
                try {
                    int progress = 0;
                    for (Warehouse warehouse : list) {
                        warehouse.save();
                        progress++;
                        final int finalProgress = progress;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress((finalProgress * 100) / max);
                                progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
                                label.setText("Inserting Warehouse");
                            }
                        });
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                manager.setWarehouseDone(true);
                LoadingThread(1000, KEY_WAREHOUSE_STOCK, false);

            }
        }).start();
    }

    /*
    * Store WarehouseStock inti SLQite3
    * */
    private void storeWarehouseStock(final List<WarehouseStock> list) {
        thread.interrupt();
        progressBar.setProgress(0);
        final int max = list.size();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ActiveAndroid.beginTransaction();
                try {
                    int progress = 0;
                    for (WarehouseStock warehouseStock : list) {
                        warehouseStock.product = Product.find(warehouseStock.product_id);
                        warehouseStock.save();
                        progress++;
                        final int finalProgress = progress;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress((finalProgress * 100) / max);
                                progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
                                label.setText("Inserting WarehouseStock");
                            }
                        });
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
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
        /*} else if (!manager.hasPrstat()) {
            LoadingThread(INTERVAL, KEY_PRODUCT_STATUS, false);
        } else if (!manager.hasPrstat2()) {
            LoadingThread(INTERVAL, KEY_PRODUCT_STATUS2, false);*/
        } else if (!manager.hasProduct()) {
            LoadingThread(INTERVAL, KEY_PRODUCT, false);
        } else if (!manager.hasWarehouse()) {
            LoadingThread(INTERVAL, KEY_WAREHOUSE, false);
        } else if (!manager.hasWarehouseStock()) {
            LoadingThread(INTERVAL, KEY_WAREHOUSE_STOCK, false);
        } else {
            LoadingThread(10, 100, true);
        }
    }

}
