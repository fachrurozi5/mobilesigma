package com.fachru.sigmamobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.controller.EmployeeController;
import com.fachru.sigmamobile.controller.interfaces.OnEmployeeCallbackListener;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.model.Discount;
import com.fachru.sigmamobile.model.DiscountStructure;
import com.fachru.sigmamobile.model.DiscountStructureLPD;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.model.Unit;
import com.fachru.sigmamobile.model.UnitConverter;
import com.fachru.sigmamobile.model.Warehouse;
import com.fachru.sigmamobile.model.WarehouseStock;
import com.fachru.sigmamobile.utils.Constanta;
import com.fachru.sigmamobile.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements OnClickListener, OnEmployeeCallbackListener {

    public static final String EMPLID = "key_emplid";
    private static final int INTERVAL = 1000;
    private static final int KEY_CUSTOMER = 0;
    private static final int KEY_WAREHOUSE = 1;
    private static final int KEY_WAREHOUSE_STOCK = 2;
    private static final int KEY_PRODUCT = 3;
    private static final int KEY_UNIT_CONVERSION = 4;
    private static final int KEY_DISCOUNT = 5;
    private static final int KEY_UNIT = 6;
    /*
    * label
    * */
    String stringLabel;
    String username;
    String password;
    private RestApiManager apiManager = new RestApiManager();
    private Context context = this;
    /*
    * utils
    * */
    private SessionManager manager;
    /*
    * controller
    * */
    private EmployeeController controller;
    /*
    * widget
    * */
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private MaterialDialog materialDialog;
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
    private Call<List<Discount>> discountCall;
    private Call<List<Customer>> customerCall;
    private Call<List<Product>> productCall;
    private Call<List<WarehouseStock>> whStockCall;
    private Call<List<Warehouse>> whouseCall;
    private Call<List<UnitConverter>> unitConverterCall;
    private Call<List<Unit>> unitCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        manager = new SessionManager(context);
        controller = new EmployeeController(this);
        initComp();

        et_username.setText(manager.getUsername());

        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == 1001) {
                    goLogin();
                    handled = true;
                }
                return handled;
            }
        });

        btn_login.setOnClickListener(this);
    }

    @Override
    public void onFetchProgress(Employee employee) {
        manager.setEmployee(employee.getId());
    }

    @Override
    public void onFetchStart() {
        showProgressHorizontalIndeterminateDialog();
    }

    @Override
    public void onFetchComplete() {
        manager.setUsername(et_username.getText().toString());
        materialDialog.dismiss();
        et_username.setVisibility(View.GONE);
        et_password.setVisibility(View.GONE);
        btn_login.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        label.setVisibility(View.VISIBLE);
        downloadOrContinue();
    }

    @Override
    public void onFetchFailed(Throwable t) {
        Log.e(Constanta.TAG, "FetchFailed", t);
        materialDialog.dismiss();
        new MaterialDialog.Builder(this)
                .title("Error")
                .iconRes(android.R.drawable.ic_dialog_alert)
                .content("Server tidak meresponse (timeout) ")
                .show();
    }

    @Override
    public void onFailureShowMessage(String message) {
        materialDialog.dismiss();
        new MaterialDialog.Builder(this)
                .title("Login Fail")
                .iconRes(android.R.drawable.ic_dialog_alert)
                .content(message)
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        et_password.setText("");
                    }
                })
                .show();
    }

    @Override
    public void onClick(View v) {
        goLogin();
    }

    private void initComp() {
        et_username = (EditText) findViewById(R.id.input_username);
        et_password = (EditText) findViewById(R.id.input_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        label = (TextView) findViewById(R.id.tv_loading_info);
    }

    private void goLogin() {
        if (!isMissing()) {
            username = et_username.getText().toString();
            password = et_password.getText().toString();
            controller.startFetch(username, password);
        }
    }

    private boolean isMissing() {
        if (et_username.getText().toString().equals("")
                || et_username.getText() == null) {
            et_username.setError("Username tidak boleh kosong");
            return true;
        } else if (et_password.getText().toString().equals("")
                || et_password.getText() == null) {
            et_password.setError("Password tidak boleh kosong");
            return true;
        } else {
            return false;
        }
    }

    private void setVisibility(boolean visibility) {
        if (visibility) {

        }
    }

    public void showProgressHorizontalIndeterminateDialog() {
        materialDialog = showIndeterminateProgressDialog(true);
    }

    private MaterialDialog showIndeterminateProgressDialog(boolean horizontal) {
        return new MaterialDialog.Builder(this)
                .title("Login")
                .content("Please Wait")
                .progress(true, 0)
                .progressIndeterminateStyle(horizontal)
                .show();
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
                    case KEY_PRODUCT:
                        stringLabel = "Load Product";
                        downloadProduct();
                        break;
                    case KEY_UNIT_CONVERSION:
                        stringLabel = "Load Unit Conversion";
                        downloadUnitConversion();
                        break;
                    case KEY_DISCOUNT:
                        stringLabel = "Load Discount";
                        downloadDiscount();
                        break;
                    case KEY_UNIT:
                        stringLabel = "Load Unit";
                        downloadUnit();
                        break;
                    default:
                        stringLabel = "Login";
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
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    if (customerCall != null) customerCall.cancel();

                    if (productCall != null) productCall.cancel();

                    if (whStockCall != null) whStockCall.cancel();

                    if (whouseCall != null) whouseCall.cancel();

                    if (unitConverterCall != null) unitConverterCall.cancel();

                    if (discountCall != null) discountCall.cancel();
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
                } else {
                    showError("Customer", response.message());
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
    * Download Discount
    * */
    private void downloadDiscount() {
        discountCall = apiManager.getDiscountAPI()._Records();
        discountCall.enqueue(new Callback<List<Discount>>() {
            @Override
            public void onResponse(Call<List<Discount>> call, Response<List<Discount>> response) {
                if (response.isSuccess() && response.body() != null) {
                    storeDiscount(response.body());
                } else {
                    showError("Discount", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Discount>> call, Throwable t) {
                Log.e(Constanta.TAG, "On Discount Failur", t);
                showError("Discount", t.getMessage());
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
                } else {
                    showError("Warehouse", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Warehouse>> call, Throwable t) {
                Log.e(Constanta.TAG, "On Warehouse Failure", t);
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
                    showError("Warehouse Stock", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<WarehouseStock>> call, Throwable t) {
                Log.e(Constanta.TAG, "On Warehouse Stock Failure", t);
                showError("Warehouse Stock", t.getMessage());
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
                    showError("Prodcut", response.message());
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
        unitConverterCall = apiManager.getUnitConverter()._Records();
        unitConverterCall.enqueue(new Callback<List<UnitConverter>>() {
            @Override
            public void onResponse(Call<List<UnitConverter>> call, Response<List<UnitConverter>> response) {

                if (response.isSuccess() && response.body() != null) {
                    storeUnitConverter(response.body());
                } else {
                    showError("Unit Converter", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<UnitConverter>> call, Throwable t) {
                Log.e(Constanta.TAG, "On Unit Converter Failure", t);
                showError("Unit Converter", t.getMessage());
            }
        });
    }

    private void downloadUnit() {
        unitCall = apiManager.getUnitAPI()._Records();
        unitCall.enqueue(new Callback<List<Unit>>() {
            @Override
            public void onResponse(Call<List<Unit>> call, Response<List<Unit>> response) {
                if (response.isSuccess() && response.body() != null) {
                    Log.d(Constanta.TAG, response.body().toString());
                    storeUnit(response.body());
                } else {
                    showError("Unit", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Unit>> call, Throwable t) {
                Log.e(Constanta.TAG, "On Unit Failure", t);
                showError("Unit Converter", t.getMessage());
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
                downloadOrContinue();
                /*LoadingThread(1000, KEY_PRODUCT, false);*/

            }
        }).start();
    }

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
                downloadOrContinue();
                /*LoadingThread(1000, KEY_WAREHOUSE, false);*/

            }
        }).start();
    }

    /*
    * Insert Unit Converter into SQLite3
    * */
    private void storeUnitConverter(final List<UnitConverter> list) {
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
                        unitConverter.save();
                        progress++;
                        final int finalProgress = progress;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress((finalProgress * 100) / max);
                                progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
                                label.setText("Inserting Unit Converter");
                            }
                        });
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                manager.setUnitConverterDone(true);
                downloadOrContinue();
                /*LoadingThread(1000, KEY_WAREHOUSE, false);*/

            }
        }).start();
    }

    private void storeUnit(final List<Unit> list) {
        thread.interrupt();
        progressBar.setProgress(0);
        final int max = list.size();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ActiveAndroid.beginTransaction();
                try {
                    int progress = 0;
                    for (Unit unit : list) {
                        unit.save();
                        progress++;
                        final int finalProgress = progress;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress((finalProgress * 100) / max);
                                progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
                                label.setText("Inserting Unit");
                            }
                        });
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                manager.setUnitDone(true);
                downloadOrContinue();
            }
        }).start();
    }

    /*
    * Store discount into SQLite3
    * */
    private void storeDiscount(final List<Discount> list) {
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
                            discountStructure.discount = discount;
                            discountStructure.save();
                        }

                        for (DiscountStructureLPD discountStructureLPD : discount.structuresLPD) {
                            discountStructureLPD.discount = discount;
                            discountStructureLPD.save();
                        }
                        final int finalProgress = progress;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress((finalProgress * 100) / max);
                                progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
                                label.setText("Inserting Discount");
                            }
                        });
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                manager.setDiscountDone(true);
                downloadOrContinue();
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
                downloadOrContinue();
                /*LoadingThread(1000, KEY_WAREHOUSE_STOCK, false);*/

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
                downloadOrContinue();
                /*startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();*/

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
                .cancelable(false)
                .positiveText("Load kembali")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        downloadOrContinue();
                    }
                })
                .negativeText("Keluar")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        finish();
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
        } else if (!manager.hasProduct()) {
            LoadingThread(INTERVAL, KEY_PRODUCT, false);
        } else if (!manager.hasWarehouse()) {
            LoadingThread(INTERVAL, KEY_WAREHOUSE, false);
        } else if (!manager.hasWarehouseStock()) {
            LoadingThread(INTERVAL, KEY_WAREHOUSE_STOCK, false);
        } else if (!manager.hasUnitConversion()) {
            LoadingThread(INTERVAL, KEY_UNIT_CONVERSION, false);
        } else if (!manager.hasDiscount()) {
            LoadingThread(INTERVAL, KEY_DISCOUNT, false);
        } else if (!manager.hasUnit()) {
            LoadingThread(INTERVAL, KEY_UNIT, false);
        } else {
            LoadingThread(10, 100, true);
        }
    }

}
