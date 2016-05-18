package com.fachru.sigmamobile;

import android.content.Intent;
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
import com.fachru.sigmamobile.model.Discount;
import com.fachru.sigmamobile.model.DiscountStructure;
import com.fachru.sigmamobile.model.DiscountStructureLPD;
import com.fachru.sigmamobile.model.DiscountStructureMul;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.model.TableInfo;
import com.fachru.sigmamobile.model.UnitConverter;
import com.fachru.sigmamobile.model.Warehouse;
import com.fachru.sigmamobile.model.WarehouseStock;
import com.fachru.sigmamobile.utils.Constanta;
import com.fachru.sigmamobile.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashScreen extends AppCompatActivity {

    private static final int INTERVAL = 1000;
    private static final int KEY_CUSTOMER = 0;
    private static final int KEY_WAREHOUSE = 1;
    private static final int KEY_WAREHOUSE_STOCK = 2;
    private static final int KEY_PRODUCT = 3;
    private static final int KEY_UNIT_CONVERSION = 4;
    private static final int KEY_DISCOUNT = 5;
    private static final int KEY_TABLE_INFO = 6;

    /*
    * label
    * */
    String stringLabel;
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
    private Call<List<Customer>> customerCall;
    private Call<List<Product>> productCall;
    private Call<List<WarehouseStock>> whStockCall;
    private Call<List<Warehouse>> whouseCall;
    private Call<List<Discount>> discountCall;
    private Call<List<UnitConverter>> unitConverterCall;
    private Call<List<TableInfo>> tableInfoCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initComp();

        manager = new SessionManager(this);

        SyncOrContinue();

    }

    @Override
    public void onBackPressed() {
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
                        stringLabel = "Sync Customer";
                        downloadCustomer();
                        break;
                    case KEY_WAREHOUSE:
                        stringLabel = "Sync Warehouse";
                        downloadWarehouse();
                        break;
                    case KEY_WAREHOUSE_STOCK:
                        stringLabel = "Sync WarehouseStock";
                        downloadWarehouseStock();
                        break;
                    case KEY_PRODUCT:
                        stringLabel = "Sync Product";
                        downloadProduct();
                        break;
                    case KEY_UNIT_CONVERSION:
                        stringLabel = "Sync Unit Conversion";
                        downloadUnitConversion();
                        break;
                    case KEY_DISCOUNT:
                        stringLabel = "Sync Discount";
                        downloadDiscount();
                        break;
                    case KEY_TABLE_INFO:
                        stringLabel = "Sync Field";
                        downloadTableInfo();
                        break;
                    default:
                        stringLabel = manager.hasCustomer() ? "Finishing Sync" : "Starting";
                }

                while (progress < progressBar.getMax()) {
                    progress += 1;
                    final int progressFinal = progress;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressFinal);
                            progressBar.setSecondaryProgress(progressFinal + 5);
                            label.setText(stringLabel);
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
        // TODO: 10/05/16 SELECT NOW() as CurrentDate, ADDTIME(NOW(), '0:5:0') as NewDate ;
        Log.i(Constanta.TAG, "downloadCustomer");
        customerCall = apiManager.getCustomerAPI().Sync();
        customerCall.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccess() && response.body() != null) {
                    Log.e(Constanta.TAG, response.body().toString());
                    storeCustomer(response.body());
                } else {
                    LoadingThread(1000, KEY_PRODUCT, false);
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
        Log.i(Constanta.TAG, "downloadWarehouse");
        whouseCall = apiManager.getWarehouseAPI().Sync();
        whouseCall.enqueue(new Callback<List<Warehouse>>() {
            @Override
            public void onResponse(Call<List<Warehouse>> call, Response<List<Warehouse>> response) {
                if (response.isSuccess() && response.body() != null) {
                    Log.e(Constanta.TAG, response.body().toString());
                    storeWarehouse(response.body());
                } else {
                    LoadingThread(1000, KEY_WAREHOUSE_STOCK, false);
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
        Log.i(Constanta.TAG, "downloadWarehouseStock");
        whStockCall = apiManager.getWhStock().Sync();
        whStockCall.enqueue(new Callback<List<WarehouseStock>>() {
            @Override
            public void onResponse(Call<List<WarehouseStock>> call, Response<List<WarehouseStock>> response) {
                if (response.isSuccess() && response.body() != null) {
                    Log.e(Constanta.TAG, response.body().toString());
                    storeWarehouseStock(response.body());
                } else {
                    LoadingThread(1000, KEY_UNIT_CONVERSION, false);
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
        Log.i(Constanta.TAG, "downloadProduct");
        productCall = apiManager.getProduct().Sync();
        productCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccess() && response.body() != null) {
                    Log.e(Constanta.TAG, response.body().toString());
                    storeProduct(response.body());
                } else {
                    LoadingThread(1000, KEY_WAREHOUSE, false);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e(Constanta.TAG, "On Product Failure", t);
                showError("Prodcut", t.getMessage());
            }
        });
    }

    /*
    * Download UnitConversion
    * */
    private void downloadUnitConversion() {
        Log.i(Constanta.TAG, "downloadUnitConversion");
        unitConverterCall = apiManager.getUnitConverter().Sync();
        unitConverterCall.enqueue(new Callback<List<UnitConverter>>() {
            @Override
            public void onResponse(Call<List<UnitConverter>> call, Response<List<UnitConverter>> response) {
                if (response.isSuccess() && response.body() != null) {
                    Log.e(Constanta.TAG, response.body().toString());
                    storeUnitConverter(response.body());
                } else {
                    LoadingThread(1000, KEY_DISCOUNT, false);
                }
            }

            @Override
            public void onFailure(Call<List<UnitConverter>> call, Throwable t) {
                Log.e(Constanta.TAG, "On Unit Converter Failure", t);
                showError("Unit Converter", t.getMessage());
            }
        });
    }

    /*
    * Download Discount
    * */
    private void downloadDiscount() {
        Log.i(Constanta.TAG, "downloadDiscount");
        discountCall = apiManager.getDiscountAPI().Sync();
        discountCall.enqueue(new Callback<List<Discount>>() {
            @Override
            public void onResponse(Call<List<Discount>> call, Response<List<Discount>> response) {
                if (response.isSuccess() && response.body() != null) {
                    storeDiscount(response.body());
                } else {
                    LoadingThread(1000, KEY_TABLE_INFO, false);
                }
            }

            @Override
            public void onFailure(Call<List<Discount>> call, Throwable t) {
                Log.e(Constanta.TAG, "On Discount Failur", t);
                showError("Discount", t.getMessage());
            }
        });
    }

    private void downloadTableInfo() {
        Log.i(Constanta.TAG, "downloadTableInfo");
        tableInfoCall = apiManager.getTableInfoAPI().Sync();
        tableInfoCall.enqueue(new Callback<List<TableInfo>>() {
            @Override
            public void onResponse(Call<List<TableInfo>> call, Response<List<TableInfo>> response) {
                Log.d(Constanta.TAG, "Responses : " + response.message());
                if (response.isSuccess() && response.body() != null) {
                    Log.e(Constanta.TAG, "Response : " + response.body().toString());
                    storeTableInfo(response.body());
                } else {
                    LoadingThread(10, 100, true);
                }
            }

            @Override
            public void onFailure(Call<List<TableInfo>> call, Throwable t) {
                Log.e(Constanta.TAG, "On Discount Failur", t);
                showError("Discount", t.getMessage());
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
        Log.i(Constanta.TAG, "storeCustomer");
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
                                label.setText("Updating Customer");
                            }
                        });
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                LoadingThread(1000, KEY_PRODUCT, false);

            }
        }).start();
    }

    /*
    * Insert Product into SQLite3
    * */
    private void storeProduct(final List<Product> list) {
        Log.i(Constanta.TAG, "storeProduct");
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
                        Log.d(Constanta.TAG, product.toString());
                        product.save();
                        progress++;
                        final int finalProgress = progress;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress((finalProgress * 100) / max);
                                progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
                                label.setText("Updating Product");
                            }
                        });
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                LoadingThread(1000, KEY_WAREHOUSE, false);

            }
        }).start();
    }

    /*
    * Store Warehouse into SQLite3
    * */
    private void storeWarehouse(final List<Warehouse> list) {
        Log.i(Constanta.TAG, "storeWarehouse");
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
                                label.setText("Updating Warehouse");
                            }
                        });
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                LoadingThread(1000, KEY_WAREHOUSE_STOCK, false);

            }
        }).start();
    }

    /*
    * Store WarehouseStock inti SLQite3
    * */
    private void storeWarehouseStock(final List<WarehouseStock> list) {
        Log.i(Constanta.TAG, "storeWarehouseStock");
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
                        WarehouseStock stock = WarehouseStock.find(warehouseStock.whid, warehouseStock.product_id);
                        if (stock != null) {
                            Log.d(Constanta.TAG, "Stock != null");
                            stock.balance = warehouseStock.balance;
                            stock.product = Product.find(warehouseStock.product_id);
                            stock.save();
                        } else {
                            Log.d(Constanta.TAG, "Stock == null");
                            warehouseStock.product = Product.find(warehouseStock.product_id);
                            warehouseStock.save();
                        }
                        progress++;
                        final int finalProgress = progress;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress((finalProgress * 100) / max);
                                progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
                                label.setText("Updating WarehouseStock");
                            }
                        });
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                LoadingThread(1000, KEY_UNIT_CONVERSION, false);

            }
        }).start();
    }

    /*
    * Insert Unit Converter into SQLite3
    * */
    private void storeUnitConverter(final List<UnitConverter> list) {
        Log.i(Constanta.TAG, "storeUnitConverter");
        thread.interrupt();
        progressBar.setProgress(0);
        final int max = list.size();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ActiveAndroid.beginTransaction();
                try {
                    int progress = 0;
                    for (UnitConverter unitConverter : list) {
                        UnitConverter converter = UnitConverter.find(unitConverter.unitId, unitConverter.unitCon);
                        if (converter != null) {
                            converter.factor = unitConverter.factor;
                            Log.e(Constanta.TAG, converter.toString());
                            converter.save();
                        } else {
                            unitConverter.save();
                        }
                        progress++;
                        final int finalProgress = progress;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress((finalProgress * 100) / max);
                                progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
                                label.setText("Updating Unit Converter");
                            }
                        });
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                LoadingThread(1000, KEY_DISCOUNT, false);

            }
        }).start();
    }

    /*
    * Store discount into SQLite3
    * */
    private void storeDiscount(final List<Discount> list) {
        Log.i(Constanta.TAG, "storeDiscount");
        thread.interrupt();
        progressBar.setProgress(0);
        final int max = list.size();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ActiveAndroid.beginTransaction();
                try {
                    int progress = 0;
                    for (Discount discount : list) {
                        discount.save();
                        progress++;
                        for (DiscountStructure discountStructure : discount.structures) {
                            Log.e(Constanta.TAG, discountStructure.toString());
                            discountStructure.discount = discount;
                            discountStructure.save();
                        }

                        for (DiscountStructureLPD discountStructureLPD : discount.structuresLPD) {
                            Log.e(Constanta.TAG, discountStructureLPD.toString());
                            discountStructureLPD.discount = discount;
                            discountStructureLPD.save();
                        }

                        for (DiscountStructureMul discountStructureMul : discount.structuresMul) {
                            Log.e(Constanta.TAG, discountStructureMul.toString());
                            discountStructureMul.discount = discount;
                            discountStructureMul.save();
                        }
                        final int finalProgress = progress;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress((finalProgress * 100) / max);
                                progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
                                label.setText("Updating Discount");
                            }
                        });
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                LoadingThread(1000, KEY_TABLE_INFO, false);
            }
        }).start();
    }

    private void storeTableInfo(final List<TableInfo> list) {
        Log.i(Constanta.TAG, "storeTableInfo");
        thread.interrupt();
        progressBar.setProgress(0);
        final int max = list.size();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ActiveAndroid.beginTransaction();
                try {
                    int progress = 0;
                    for (TableInfo tableInfo : list) {
                        TableInfo.save(tableInfo);
                        progress++;
                        final int finalProgress = progress;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress((finalProgress * 100) / max);
                                progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
                                label.setText("Updating Field");
                            }
                        });
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                LoadingThread(10, 100, true);
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
                        SyncOrContinue();
                    }
                })
                .show();
    }

    /*
    * method for continue download
    * */
    private void SyncOrContinue() {
        if (manager.hasCustomer()) {
            LoadingThread(INTERVAL, KEY_CUSTOMER, false);
        } else {
            LoadingThread(10, 100, true);
        }
    }

}
