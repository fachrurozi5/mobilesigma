package com.fachru.sigmamobile.app;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.model.Discount;
import com.fachru.sigmamobile.model.DiscountLv1;
import com.fachru.sigmamobile.model.DiscountStructure;
import com.fachru.sigmamobile.model.DiscountStructureLPD;
import com.fachru.sigmamobile.model.DiscountStructureMul;
import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.model.DoItem;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.model.IdleTime;
import com.fachru.sigmamobile.model.IdleTimeAnalysis;
import com.fachru.sigmamobile.model.OSCManagement;
import com.fachru.sigmamobile.model.OSCManagementDetail;
import com.fachru.sigmamobile.model.Outlet;
import com.fachru.sigmamobile.model.OutletSize;
import com.fachru.sigmamobile.model.OutletType;
import com.fachru.sigmamobile.model.PicturesPath;
import com.fachru.sigmamobile.model.Principal;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.model.ReturnsProduct;
import com.fachru.sigmamobile.model.SoHead;
import com.fachru.sigmamobile.model.SoItem;
import com.fachru.sigmamobile.model.TableInfo;
import com.fachru.sigmamobile.model.Tolerance;
import com.fachru.sigmamobile.model.Unit;
import com.fachru.sigmamobile.model.UnitConverter;
import com.fachru.sigmamobile.model.Warehouse;
import com.fachru.sigmamobile.model.WarehouseStock;
import com.fachru.sigmamobile.utils.UtilDateSerializer;

/**
 * Created by fachru on 07/03/16.
 */
public class MyApplication extends Application {

	private static Context context;

	public static Context getAppContext() {
		return MyApplication.context;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Configuration.Builder builder = new Configuration.Builder(this);
		builder.setDatabaseName("sigma.db");
		builder.setDatabaseVersion(1);
		builder.setTypeSerializers(UtilDateSerializer.class);
		builder.setModelClasses(Customer.class,
				DiscountLv1.class,
				Discount.class,
				DiscountStructure.class,
				DiscountStructureLPD.class,
				DiscountStructureMul.class,
				DoHead.class,
				DoItem.class,
				Employee.class,
				IdleTime.class,
				IdleTimeAnalysis.class,
				OSCManagement.class,
				OSCManagementDetail.class,
				Outlet.class,
				OutletSize.class,
				OutletType.class,
				PicturesPath.class,
				Principal.class,
				Product.class,
				ReturnsProduct.class,
				SoHead.class,
				SoItem.class,
				TableInfo.class,
				Tolerance.class,
				Unit.class,
				UnitConverter.class,
				Warehouse.class,
				WarehouseStock.class);
		Configuration configurationBuilder = builder.create();

		ActiveAndroid.initialize(configurationBuilder);
		MyApplication.context = getApplicationContext();
	}
}
