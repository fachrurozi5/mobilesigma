package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.fachru.sigmamobile.utils.Constanta;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fachru on 31/12/15.
 */
@Table(name = "Products")
public class Product extends Model {

	public static final String ID = "product_id";
	public static final String NAME = "name";
	public static final String UNIT_ID = "unit_id";
	public static final String PRINCIPAL_ID = "principal_id";
	public static final String BASE_PRICE = "base_price";
	public static final String OLD_PRICE = "old_price";
	public static final String TEST_PRICE = "test_price";
	public static final String SELL_PRICE = "sell_price";
	public static final String PO_PRICE = "po_price";

	@Expose
	@SerializedName("id")
	@Column(name = ID, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public String prodid;

	@Expose
	@Column(name = NAME)
	public String name;

	@Expose
	@SerializedName(UNIT_ID)
	@Column(name = UNIT_ID)
	public String unitId;

	@Expose
	@SerializedName(PRINCIPAL_ID)
	@Column(name = PRINCIPAL_ID)
	public String principalId;

	@Expose
	@SerializedName(BASE_PRICE)
	@Column(name = BASE_PRICE)
	public double basePrice;

	@Expose
	@SerializedName(OLD_PRICE)
	@Column(name = OLD_PRICE)
	public double oldPrice;

	@Expose
	@SerializedName(TEST_PRICE)
	@Column(name = TEST_PRICE)
	public double testPrice;

	@Expose
	@SerializedName(PO_PRICE)
	@Column(name = PO_PRICE)
	public double poPrice;

	@Expose
	@SerializedName(SELL_PRICE)
	@Column(name = SELL_PRICE)
	public double sellPrice;


	public Product() {
		super();
	}

	public static List<Product> all() {
		return new Select().from(Product.class).execute();
	}

	public static List<HashMap<String, String>> toListHashMap() {
		List<HashMap<String, String>> hashMaps = new ArrayList<>();
		HashMap<String, String> map;
		for (Product product : all()) {
			map = new HashMap<>();
			map.put(Constanta.SIMPLE_LIST_ITEM_1, product.prodid);
			map.put(Constanta.SIMPLE_LIST_ITEM_2, product.name);
			hashMaps.add(map);
		}

		return hashMaps;
	}

	public static Product find(String product_id) {
		return new Select().from(Product.class)
				.where("product_id = ?", product_id)
				.executeSingle();
	}

	@Override
	public String toString() {
		return "Product{" +
				"prodid='" + prodid + '\'' +
				", name='" + name + '\'' +
				", unitId='" + unitId + '\'' +
				", principalId='" + principalId + '\'' +
				", basePrice=" + basePrice +
				", oldPrice=" + oldPrice +
				", testPrice=" + testPrice +
				", poPrice=" + poPrice +
				", sellPrice=" + sellPrice +
				'}';
	}
}
