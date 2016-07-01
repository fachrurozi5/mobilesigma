package com.fachru.sigmamobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.fachru.sigmamobile.model.Discount;
import com.fachru.sigmamobile.model.DiscountLv1;
import com.fachru.sigmamobile.model.DiscountStructure;
import com.fachru.sigmamobile.model.DiscountStructureLPD;
import com.fachru.sigmamobile.model.DiscountStructureMul;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.model.Outlet;
import com.fachru.sigmamobile.model.OutletType;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.model.Tolerance;
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

	public static final String TAG = "MyProblemInLogin";
	public static final String EMPLID = "keyEmployee";

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
	private ProgressBar progressBar;
	private TextView label;

	/*
	* android.os
    * */
	private Handler handler = new Handler();
	private Thread thread;

	/*
	* label
	* */
	private boolean loginProcess;

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
	protected void onPause() {
		super.onPause();
		try {
			thread.interrupt();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy: Login Activity");
	}

	@Override
	public void onFetchProgress(Employee employee) {
		manager.setEmployee(employee.getId());
	}

	@Override
	public void onFetchStart() {
		loginProcess = true;
		et_username.setVisibility(View.GONE);
		et_password.setVisibility(View.GONE);
		btn_login.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		label.setVisibility(View.VISIBLE);
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				int progress = 0;

				while (progress < progressBar.getMax() && loginProcess) {
					progress += 1;
					final int progressFinal = progress;
					handler.post(new Runnable() {
						@Override
						public void run() {
							progressBar.setProgress(progressFinal);
							progressBar.setSecondaryProgress(progressFinal + 5);
							label.setText("Login");
						}
					});

					try {
						Thread.sleep(100);
						Log.i(TAG, "run: " + thread.getId() + " sleep");
					} catch (InterruptedException e) {
						thread.interrupt();
					}
				}
			}
		});

		thread.start();
		thread.interrupt();
	}

	@Override
	public void onFetchComplete() {
		loginProcess = false;
		manager.setUsername(et_username.getText().toString());
		downloadOrContinue();
	}

	@Override
	public void onFetchFailed(Throwable t) {
		Log.e(Constanta.TAG, "FetchFailed", t);
		thread.interrupt();
		new MaterialDialog.Builder(this)
				.title("Error")
				.iconRes(android.R.drawable.ic_dialog_alert)
				.content("Server tidak meresponse (timeout) ")
				.canceledOnTouchOutside(false)
				.positiveText("OK")
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						et_username.setVisibility(View.VISIBLE);
						et_password.setVisibility(View.VISIBLE);
						btn_login.setVisibility(View.VISIBLE);
						progressBar.setVisibility(View.GONE);
						label.setVisibility(View.GONE);
						dialog.dismiss();
					}
				})
				.show();
	}

	@Override
	public void onFailureShowMessage(String message) {
		thread.interrupt();
		new MaterialDialog.Builder(this)
				.title("Login Fail")
				.iconRes(android.R.drawable.ic_dialog_alert)
				.content(message)
				.positiveText("OK")
				.canceledOnTouchOutside(false)
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
						et_password.setText("");
						et_username.setVisibility(View.VISIBLE);
						et_password.setVisibility(View.VISIBLE);
						btn_login.setVisibility(View.VISIBLE);
						progressBar.setVisibility(View.GONE);
						label.setVisibility(View.GONE);
						materialDialog.dismiss();
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
			String username = et_username.getText().toString();
			String password = et_password.getText().toString();
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

	/*************
	 * Fetching data from server
	 ******************/

	/**
	 * http://new-neosolusi.ddns.net:8000/sigmamobile/outlets
	 */
	private void downloadOutlet() {
		Log.i(TAG, "downloadOutlet: start");
		Call<List<Outlet>> outletCall = apiManager.getOutletAPI().Records();
		outletCall.enqueue(new Callback<List<Outlet>>() {
			@Override
			public void onResponse(Call<List<Outlet>> call, Response<List<Outlet>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeOutlet(response.body());
				} else {
					Log.e(TAG, response.message());
					showError("Outlet", response.message());
				}
			}

			@Override
			public void onFailure(Call<List<Outlet>> call, Throwable t) {
				Log.e(TAG, "On Outlet Failure", t);
				showError("Outlet", t.getMessage());
			}
		});
	}

	/**
	 * http://new-neosolusi.ddns.net:8000/sigmamobile/outlets/type
	 */
	private void downloadOutletType() {
		Log.i(TAG, "downloadOutletType: start");
		Call<List<OutletType>> outletTypeCall = apiManager.getOutletAPI().TypeRecords();
		outletTypeCall.enqueue(new Callback<List<OutletType>>() {
			@Override
			public void onResponse(Call<List<OutletType>> call, Response<List<OutletType>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeOutletType(response.body());
				} else {
					Log.e(TAG, response.message());
					showError("Outlet Type", response.message());
				}
			}

			@Override
			public void onFailure(Call<List<OutletType>> call, Throwable t) {
				Log.e(TAG, "On Outlet Failure", t);
				showError("Outlet Type", t.getMessage());
			}
		});
	}

	/**
	 * http://new-neosolusi.ddns.net:8000/sigmamobile/tolerances
	 */
	private void downloadTolerance() {
		Log.i(TAG, "downloadTolerance: start");
		Call<List<Tolerance>> toleranceCall = apiManager.getToleranceAPI()._Records();
		toleranceCall.enqueue(new Callback<List<Tolerance>>() {
			@Override
			public void onResponse(Call<List<Tolerance>> call, Response<List<Tolerance>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeTolerance(response.body());
				} else {
					Log.e(TAG, response.message());
					showError("Tolerance", response.message());
				}
			}

			@Override
			public void onFailure(Call<List<Tolerance>> call, Throwable t) {
				Log.e(TAG, "On Tolerance Failure", t);
				showError("Tolerance", t.getMessage());
			}
		});
	}

	/**
	 * http://new-neosolusi.ddns.net:8000/sigmamobile/units
	 */
	private void downloadUnit() {
		Log.i(TAG, "downloadUnit: start");
		Call<List<Unit>> unitCall = apiManager.getUnitAPI()._Records();
		unitCall.enqueue(new Callback<List<Unit>>() {
			@Override
			public void onResponse(Call<List<Unit>> call, Response<List<Unit>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeUnit(response.body());
				} else {
					Log.e(TAG, response.message());
					showError("Unit", response.message());
				}
			}

			@Override
			public void onFailure(Call<List<Unit>> call, Throwable t) {
				Log.e(TAG, "On Unit Failure", t);
				showError("Unit", t.getMessage());
			}
		});
	}

	/*
	* http://new-neosolusi.ddns.net:8000/sigmamobile/unit-converter
	* */
	private void downloadUnitConversion() {
		Log.i(TAG, "downloadUnitConversion: start");
		Call<List<UnitConverter>> unitConverterCall = apiManager.getUnitConverter()._Records();
		unitConverterCall.enqueue(new Callback<List<UnitConverter>>() {
			@Override
			public void onResponse(Call<List<UnitConverter>> call, Response<List<UnitConverter>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeUnitConverter(response.body());
				} else {
					Log.e(TAG, response.message());
					showError("Unit Converter", response.message());
				}
			}

			@Override
			public void onFailure(Call<List<UnitConverter>> call, Throwable t) {
				Log.e(TAG, "On Unit Converter Failure", t);
				showError("Unit Converter", t.getMessage());
			}
		});
	}

	/*
    * http://new-neosolusi.ddns.net:8000/sigmamobile/discounts/structures
    * */
	private void downloadDiscount() {
		Log.i(TAG, "downloadDiscount: start");
		Call<List<Discount>> discountCall = apiManager.getDiscountAPI()._Records();
		discountCall.enqueue(new Callback<List<Discount>>() {
			@Override
			public void onResponse(Call<List<Discount>> call, Response<List<Discount>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeDiscount(response.body());
				} else {
					Log.e(TAG, response.message());
					showError("Discount", response.message());
				}
			}

			@Override
			public void onFailure(Call<List<Discount>> call, Throwable t) {
				Log.e(TAG, "On Discount Failure", t);
				showError("Discount", t.getMessage());
			}
		});
	}

	/**
	 * http://new-neosolusi.ddns.net:8000/sigmamobile/discountslv1
	 */
	private void downloadDiscountLv1() {
		Log.i(TAG, "downloadDiscountLv1: start");
		Call<List<DiscountLv1>> discountLv1Call = apiManager.getDiscountAPI().Lv1Records();
		discountLv1Call.enqueue(new Callback<List<DiscountLv1>>() {
			@Override
			public void onResponse(Call<List<DiscountLv1>> call, Response<List<DiscountLv1>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeDiscountLv1(response.body());
				} else {
					Log.e(TAG, response.message());
					showError("Discount Lv1", response.message());
				}
			}

			@Override
			public void onFailure(Call<List<DiscountLv1>> call, Throwable t) {
				Log.e(TAG, "On Discount Lv1 Failure", t);
				showError("Discount Lv1", t.getMessage());
			}
		});
	}

	/*
    * http://new-neosolusi.ddns.net:8000/sigmamobile/products/picking
    * */
	private void downloadProduct() {
		Log.i(TAG, "downloadProduct: start");
		Call<List<Product>> productCall = apiManager.getProduct().Records();
		productCall.enqueue(new Callback<List<Product>>() {
			@Override
			public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeProduct(response.body());
				} else {
					Log.e(TAG, response.message());
					showError("Prodcut", response.message());
				}
			}

			@Override
			public void onFailure(Call<List<Product>> call, Throwable t) {
				Log.e(TAG, "On Product Failure", t);
				showError("Prodcut", t.getMessage());
			}
		});
	}

	/*
    * http://new-neosolusi.ddns.net:8000/sigmamobile/whouses
    * */
	private void downloadWarehouse() {
		Log.i(TAG, "downloadWarehouse: start");
		Call<List<Warehouse>> whouseCall = apiManager.getWarehouseAPI()._Records();
		whouseCall.enqueue(new Callback<List<Warehouse>>() {
			@Override
			public void onResponse(Call<List<Warehouse>> call, Response<List<Warehouse>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeWarehouse(response.body());
				} else {
					Log.e(TAG, response.message());
					showError("Warehouse", response.message());
				}
			}

			@Override
			public void onFailure(Call<List<Warehouse>> call, Throwable t) {
				Log.e(TAG, "On Warehouse Failure", t);
				showError("Warehouse", t.getMessage());
			}
		});
	}

	/*
    * http://new-neosolusi.ddns.net:8000/sigmamobile/whstocks
    * */
	private void downloadWarehouseStock() {
		Log.i(TAG, "downloadWarehouseStock: start");
		Call<List<WarehouseStock>> whStockCall = apiManager.getWhStock()._Records();
		whStockCall.enqueue(new Callback<List<WarehouseStock>>() {
			@Override
			public void onResponse(Call<List<WarehouseStock>> call, Response<List<WarehouseStock>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeWarehouseStock(response.body());
				} else {
					Log.e(TAG, response.message());
					showError("Warehouse Stock", response.message());
				}
			}

			@Override
			public void onFailure(Call<List<WarehouseStock>> call, Throwable t) {
				Log.e(TAG, "On Warehouse Stock Failure", t);
				showError("Warehouse Stock", t.getMessage());
			}
		});
	}

	/*************
	 * Store data into sqlite
	 ******************/

	private void storeOutlet(final List<Outlet> list) {
		Log.i(TAG, "storeOutlet: start");
		thread.interrupt();
		progressBar.setProgress(0);
		final int max = list.size();

		new Thread(new Runnable() {
			@Override
			public void run() {
				ActiveAndroid.beginTransaction();
				try {
					int progress = 0;
					for (Outlet outlet : list) {
						outlet.save();
						progress++;
						final int finalProgress = progress;
						handler.post(new Runnable() {
							@Override
							public void run() {
								progressBar.setProgress((finalProgress * 100) / max);
								progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
								label.setText("Inserting Outlet");
							}
						});
					}
					ActiveAndroid.setTransactionSuccessful();
				} finally {
					ActiveAndroid.endTransaction();
				}
				manager.setOutletDone(true);
				downloadOrContinue();
			}
		}).start();
	}

	private void storeOutletType(final List<OutletType> list) {
		Log.i(TAG, "storeOutletType: start");
		thread.interrupt();
		progressBar.setProgress(0);
		final int max = list.size();

		new Thread(new Runnable() {
			@Override
			public void run() {
				ActiveAndroid.beginTransaction();
				try {
					int progress = 0;
					for (OutletType outletType : list) {
						outletType.save();
						progress++;
						final int finalProgress = progress;
						handler.post(new Runnable() {
							@Override
							public void run() {
								progressBar.setProgress((finalProgress * 100) / max);
								progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
								label.setText("Inserting Outlet Type");
							}
						});
					}
					ActiveAndroid.setTransactionSuccessful();
				} finally {
					ActiveAndroid.endTransaction();
				}
				manager.setOutletTypeDone(true);
				downloadOrContinue();
			}
		}).start();
	}

	private void storeTolerance(final List<Tolerance> list) {
		Log.i(TAG, "storeTolerance: start");
		thread.interrupt();
		progressBar.setProgress(0);
		final int max = list.size();

		new Thread(new Runnable() {
			@Override
			public void run() {
				ActiveAndroid.beginTransaction();
				try {
					int progress = 0;
					for (Tolerance tolerance : list) {
						tolerance.save();
						progress++;
						final int finalProgress = progress;
						handler.post(new Runnable() {
							@Override
							public void run() {
								progressBar.setProgress((finalProgress * 100) / max);
								progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
								label.setText("Inserting Tolerance");
							}
						});
					}
					ActiveAndroid.setTransactionSuccessful();
				} finally {
					ActiveAndroid.endTransaction();
				}
				manager.setToleranceDown(true);
				downloadOrContinue();
			}
		}).start();
	}

	private void storeUnit(final List<Unit> list) {
		Log.i(TAG, "storeUnit: start");
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

	private void storeUnitConverter(final List<UnitConverter> list) {
		Log.i(TAG, "storeUnitConverter: start");
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

			}
		}).start();
	}

	private void storeDiscount(final List<Discount> list) {
		Log.i(TAG, "storeDiscount: start");
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

						for (DiscountStructureMul discountStructureMul : discount.structuresMul) {
							discountStructureMul.discount = discount;
							discountStructureMul.save();
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

	private void storeDiscountLv1(final List<DiscountLv1> list) {
		Log.i(TAG, "storeDiscountLv1: start");
		thread.interrupt();
		progressBar.setProgress(0);
		final int max = list.size();

		new Thread(new Runnable() {
			@Override
			public void run() {
				ActiveAndroid.beginTransaction();
				try {
					int progress = 0;
					for (DiscountLv1 discount : list) {
						discount.save();
						progress++;
						final int finalProgress = progress;
						handler.post(new Runnable() {
							@Override
							public void run() {
								progressBar.setProgress((finalProgress * 100) / max);
								progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
								label.setText("Inserting Discount Lv1");
							}
						});
					}
					ActiveAndroid.setTransactionSuccessful();
				} finally {
					ActiveAndroid.endTransaction();
				}
				manager.setDiscountLv1Done(true);
				downloadOrContinue();
			}
		}).start();
	}

	private void storeProduct(final List<Product> list) {
		Log.i(TAG, "storeProduct: start");
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
			}
		}).start();
	}

	private void storeWarehouse(final List<Warehouse> list) {
		Log.i(TAG, "storeWarehouse: start");
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

			}
		}).start();
	}

	private void storeWarehouseStock(final List<WarehouseStock> list) {
		Log.i(TAG, "storeWarehouseStock: start");
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
		Log.e(TAG, "downloadOrContinue: ");
		if (!manager.hasOutlet()) {
			downloadOutlet();
		} else if (!manager.hasOutletType()) {
			downloadOutletType();
		} else if (!manager.hasTolerance()) {
			downloadTolerance();
		} else if (!manager.hasUnit()) {
			downloadUnit();
		} else if (!manager.hasUnitConversion()) {
			downloadUnitConversion();
		} else if (!manager.hasDiscount()) {
			downloadDiscount();
		} else if (!manager.hasDiscountLv1()) {
			downloadDiscountLv1();
		} else if (!manager.hasProduct()) {
			downloadProduct();
		} else if (!manager.hasWarehouse()) {
			downloadWarehouse();
		} else if (!manager.hasWarehouseStock()) {
			downloadWarehouseStock();
		} else {
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			finish();
		}
	}

}
