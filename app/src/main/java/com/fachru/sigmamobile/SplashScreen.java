package com.fachru.sigmamobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.api.RestApiManager;
import com.fachru.sigmamobile.model.Discount;
import com.fachru.sigmamobile.model.DiscountLv1;
import com.fachru.sigmamobile.model.DiscountStructure;
import com.fachru.sigmamobile.model.DiscountStructureLPD;
import com.fachru.sigmamobile.model.DiscountStructureMul;
import com.fachru.sigmamobile.model.Outlet;
import com.fachru.sigmamobile.model.OutletType;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.model.TableInfo;
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


public class SplashScreen extends AppCompatActivity {

	private static final String TAG = "MyProblemSync";
	boolean syncProcess = true;
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
	* label
    * */
	private String stringLabel;

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

	/*************
	 * Fetching data from server
	 ******************/

	private void initComp() {
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		label = (TextView) findViewById(R.id.tv_loading_info);
	}

	/**
	 * http://new-neosolusi.ddns.net:8000/sigmamobile/outlets/sync
	 */
	private void downloadOutlet() {
		Log.i(TAG, "downloadOutlet: start");
		Call<List<Outlet>> outletCall = apiManager.getOutletAPI().Sync();
		outletCall.enqueue(new Callback<List<Outlet>>() {
			@Override
			public void onResponse(Call<List<Outlet>> call, Response<List<Outlet>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeOutlet(response.body());
				} else {
					Log.e(TAG, response.message());
					downloadOutletType();
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
	 * http://new-neosolusi.ddns.net:8000/sigmamobile/outlets/type/sync
	 */
	private void downloadOutletType() {
		Log.i(TAG, "downloadOutletType: start");
		Call<List<OutletType>> outletTypeCall = apiManager.getOutletAPI().TypeSync();
		outletTypeCall.enqueue(new Callback<List<OutletType>>() {
			@Override
			public void onResponse(Call<List<OutletType>> call, Response<List<OutletType>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeOutletType(response.body());
				} else {
					Log.e(TAG, response.message());
					downloadTolerance();
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
	 * http://new-neosolusi.ddns.net:8000/sigmamobile/tolerances/sync
	 */
	private void downloadTolerance() {
		Log.i(TAG, "downloadTolerance: start");
		Call<List<Tolerance>> toleranceCall = apiManager.getToleranceAPI().Sync();
		toleranceCall.enqueue(new Callback<List<Tolerance>>() {
			@Override
			public void onResponse(Call<List<Tolerance>> call, Response<List<Tolerance>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeTolerance(response.body());
				} else {
					Log.e(TAG, response.message());
					downloadUnit();
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
	 * http://new-neosolusi.ddns.net:8000/sigmamobile/units/sync
	 */
	private void downloadUnit() {
		Log.i(TAG, "downloadUnit: start");
		Call<List<Unit>> unitCall = apiManager.getUnitAPI().Sync();
		unitCall.enqueue(new Callback<List<Unit>>() {
			@Override
			public void onResponse(Call<List<Unit>> call, Response<List<Unit>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeUnit(response.body());
				} else {
					Log.e(TAG, response.message());
					downloadUnitConversion();
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
	* http://new-neosolusi.ddns.net:8000/sigmamobile/unit-converter/sync
	* */
	private void downloadUnitConversion() {
		Log.i(TAG, "downloadUnitConversion: start");
		Call<List<UnitConverter>> unitConverterCall = apiManager.getUnitConverter().Sync();
		unitConverterCall.enqueue(new Callback<List<UnitConverter>>() {
			@Override
			public void onResponse(Call<List<UnitConverter>> call, Response<List<UnitConverter>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeUnitConverter(response.body());
				} else {
					Log.e(TAG, response.message());
					downloadDiscount();
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
    * http://new-neosolusi.ddns.net:8000/sigmamobile/discounts/sync
    * */
	private void downloadDiscount() {
		Log.i(TAG, "downloadDiscount: start");
		Call<List<Discount>> discountCall = apiManager.getDiscountAPI().Sync();
		discountCall.enqueue(new Callback<List<Discount>>() {
			@Override
			public void onResponse(Call<List<Discount>> call, Response<List<Discount>> response) {
				if (response.isSuccessful() && response.body() != null) {
					Log.e(TAG, response.body().toString());
					storeDiscount(response.body());
				} else {
					Log.e(TAG, response.message());
					downloadDiscountLv1();
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
	 * http://new-neosolusi.ddns.net:8000/sigmamobile/discountslv1/sync
	 */
	private void downloadDiscountLv1() {
		Log.i(TAG, "downloadDiscountLv1: start");
		Call<List<DiscountLv1>> discountLv1Call = apiManager.getDiscountAPI().Lv1Sync();
		discountLv1Call.enqueue(new Callback<List<DiscountLv1>>() {
			@Override
			public void onResponse(Call<List<DiscountLv1>> call, Response<List<DiscountLv1>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeDiscountLv1(response.body());
				} else {
					Log.e(TAG, response.message());
					downloadProduct();
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
    * http://new-neosolusi.ddns.net:8000/sigmamobile/products/sync
    * */
	private void downloadProduct() {
		Log.i(TAG, "downloadProduct: start");
		Call<List<Product>> productCall = apiManager.getProduct().Sync();
		productCall.enqueue(new Callback<List<Product>>() {
			@Override
			public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeProduct(response.body());
				} else {
					Log.e(TAG, response.message());
					downloadWarehouse();
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
		Call<List<Warehouse>> whouseCall = apiManager.getWarehouseAPI().Sync();
		whouseCall.enqueue(new Callback<List<Warehouse>>() {
			@Override
			public void onResponse(Call<List<Warehouse>> call, Response<List<Warehouse>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeWarehouse(response.body());
				} else {
					Log.e(TAG, response.message());
					downloadWarehouseStock();
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
		Call<List<WarehouseStock>> whStockCall = apiManager.getWhStock().Sync();
		whStockCall.enqueue(new Callback<List<WarehouseStock>>() {
			@Override
			public void onResponse(Call<List<WarehouseStock>> call, Response<List<WarehouseStock>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeWarehouseStock(response.body());
				} else {
					Log.e(TAG, response.message());
					downloadTableInfo();
				}
			}

			@Override
			public void onFailure(Call<List<WarehouseStock>> call, Throwable t) {
				Log.e(TAG, "On Warehouse Stock Failure", t);
				showError("Warehouse Stock", t.getMessage());
			}
		});
	}

	private void downloadTableInfo() {
		Call<List<TableInfo>> tableInfoCall = apiManager.getTableInfoAPI().Sync();
		tableInfoCall.enqueue(new Callback<List<TableInfo>>() {
			@Override
			public void onResponse(Call<List<TableInfo>> call, Response<List<TableInfo>> response) {
				if (response.isSuccessful() && response.body() != null) {
					storeTableInfo(response.body());
				} else {
					showLoading();
				}
			}

			@Override
			public void onFailure(Call<List<TableInfo>> call, Throwable t) {
				showError("Table Info", t.getMessage());
			}
		});
	}

	/*************
	 * Store data into sqlite
	 ******************/

	private void storeOutlet(final List<Outlet> list) {
		Log.i(TAG, "storeOutlet: start");
		progressBar.setProgress(0);
		final int max = list.size();

		new Thread(new Runnable() {
			@Override
			public void run() {
				ActiveAndroid.beginTransaction();
				try {
					int progress = 0;
					new Delete().from(Outlet.class);
					for (Outlet outlet : list) {
						outlet.save();
						progress++;
						final int finalProgress = progress;
						handler.post(new Runnable() {
							@Override
							public void run() {
								progressBar.setProgress((finalProgress * 100) / max);
								progressBar.setSecondaryProgress(((finalProgress * 100) / max) + 5);
								label.setText("Updating Outlet");
							}
						});
					}
					ActiveAndroid.setTransactionSuccessful();
				} finally {
					ActiveAndroid.endTransaction();
				}
				downloadOutletType();
			}
		}).start();
	}

	private void storeOutletType(final List<OutletType> list) {
		Log.i(TAG, "storeOutletType: start");
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
								label.setText("Updating Outlet Type");
							}
						});
					}
					ActiveAndroid.setTransactionSuccessful();
				} finally {
					ActiveAndroid.endTransaction();
				}
				downloadTolerance();
			}
		}).start();
	}

	private void storeTolerance(final List<Tolerance> list) {
		Log.i(TAG, "storeTolerance: start");
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
								label.setText("Updating Tolerance");
							}
						});
					}
					ActiveAndroid.setTransactionSuccessful();
				} finally {
					ActiveAndroid.endTransaction();
				}
				downloadUnit();
			}
		}).start();
	}

	private void storeUnit(final List<Unit> list) {
		Log.i(TAG, "storeUnit: start");
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
								label.setText("Updating Unit");
							}
						});
					}
					ActiveAndroid.setTransactionSuccessful();
				} finally {
					ActiveAndroid.endTransaction();
				}
				downloadUnitConversion();
			}
		}).start();
	}

	private void storeUnitConverter(final List<UnitConverter> list) {
		Log.i(TAG, "storeUnitConverter: start");
		progressBar.setProgress(0);
		final int max = list.size();

		new Thread(new Runnable() {
			@Override
			public void run() {
				ActiveAndroid.beginTransaction();
				try {
					int progress = 0;
					for (UnitConverter unitConverter : list) {
						UnitConverter converter = UnitConverter.find(unitConverter.unitFrom, unitConverter.toUnit);
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
				downloadDiscount();

			}
		}).start();
	}

	private void storeDiscount(final List<Discount> list) {
		Log.i(TAG, "storeDiscount: start");
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
								label.setText("Updating Discount");
							}
						});
					}
					ActiveAndroid.setTransactionSuccessful();
				} finally {
					ActiveAndroid.endTransaction();
				}
				downloadDiscountLv1();
			}
		}).start();
	}

	private void storeDiscountLv1(final List<DiscountLv1> list) {
		Log.i(TAG, "storeDiscountLv1: start");
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
								label.setText("Updating Discount Lv1");
							}
						});
					}
					ActiveAndroid.setTransactionSuccessful();
				} finally {
					ActiveAndroid.endTransaction();
				}
				downloadProduct();
			}
		}).start();
	}

	private void storeProduct(final List<Product> list) {
		Log.i(TAG, "storeProduct: start");
		progressBar.setProgress(0);
		final int max = list.size();

		new Thread(new Runnable() {
			@Override
			public void run() {
				ActiveAndroid.beginTransaction();
				try {
					int progress = 0;
					new Delete().from(Product.class).execute();
					for (Product product : list) {
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
				downloadWarehouse();
			}
		}).start();
	}

	private void storeWarehouse(final List<Warehouse> list) {
		Log.i(TAG, "storeWarehouse: start");
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
				downloadWarehouseStock();

			}
		}).start();
	}

	private void storeWarehouseStock(final List<WarehouseStock> list) {
		Log.i(TAG, "storeWarehouseStock: start");
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
				downloadTableInfo();
			}
		}).start();
	}

	private void storeTableInfo(final List<TableInfo> list) {
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
				showLoading();
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
		new MaterialDialog.Builder(this)
				.title(title)
				.iconRes(android.R.drawable.ic_dialog_alert)
				.content(message)
				.positiveText("Try again")
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
						SyncOrContinue();
					}
				})
				.show();
	}

	/*
	* method for continue download
	* */
	private void SyncOrContinue() {
		Log.e(Constanta.TAG, "SyncOrContinue");
		if (manager.hasOutlet()) {
			downloadOutlet();
		} else {
			showLoading();
		}
	}

	private void showLoading() {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				int progress = 0;

				while (progress < progressBar.getMax() && syncProcess) {
					progress += 1;
					final int progressFinal = progress;
					handler.post(new Runnable() {
						@Override
						public void run() {
							progressBar.setProgress(progressFinal);
							progressBar.setSecondaryProgress(progressFinal + 5);
							label.setText(manager.hasOutlet() ? "Finishing Sync" : "Starting");
						}
					});

					try {
						Thread.sleep(100);
						Log.i(TAG, "run: " + thread.getId() + " sleep");
					} catch (InterruptedException e) {
						thread.interrupt();
					}
				}

				syncProcess = false;

				startActivity(new Intent(getApplicationContext(), Login.class));
				finish();

			}
		});

		thread.start();
		thread.interrupt();
	}
}
