package com.fachru.sigmamobile.model;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by fachru on 09/05/16.
 */
@Table(name = "Discount")
public class Discount extends Model {

	private static final String TAG = "toknowdiscount";


	@Expose
	@SerializedName("id")
	@Column(name = "ita_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public String _id;

	@Expose
	@Column(name = "description")
	public String description;

	@Expose
	@SerializedName("principal")
	@Column(name = "principal_active")
	public int principal_active;

	@Expose
	@SerializedName("nusantara")
	@Column(name = "nusantara_active")
	public int nusantara_active;

	@Expose
	@SerializedName("started")
	@Column(name = "started")
	public Date started;

	@Expose
	@Column(name = "expired")
	public Date expired;

	@Expose
	@Column(name = "clause")
	public String clause;

	@Expose
	@SerializedName("satuan")
	@Column(name = "satuan")
	public String unit;

	@Expose
	@SerializedName("structures")
	public List<DiscountStructure> structures = new ArrayList<>();

	@Expose
	@SerializedName("structure_lpd")
	public List<DiscountStructureLPD> structuresLPD = new ArrayList<>();

	@Expose
	@SerializedName("structure_mul")
	public List<DiscountStructureMul> structuresMul = new ArrayList<>();

	public Discount() {
		super();
	}

	public static List<Discount> getDiscountsLpd() {
		return new Select()
				.from(Discount.class)
				.where("ita_id like ?", "%LPD-%")
				.execute();
	}

	public static List<Discount> getDiscountsVal() {
		return new Select()
				.from(Discount.class)
				.where("ita_id like ?", "%VAL-%")
				.execute();
	}

	public static List<Discount> getDiscountsPsd() {
		return new Select()
				.from(Discount.class)
				.where("ita_id like ?", "%PSD-%")
				.execute();
	}

	public static List<Discount> getDiscountsSub() {
		return new Select()
				.from(Discount.class)
				.where("ita_id like ?", "%SUB-%")
				.execute();
	}

	public static List<Discount> getDiscountsMul() {
		return new Select()
				.from(Discount.class)
				.where("ita_id like ?", "%MUL-%")
				.execute();
	}

	public static long getDiscountValAsString(long initialPrice) {
		long priceDiscounts = 0;
		double n_percent = 0;
		double p_percent = 0;
		for (Discount discount : getDiscountsVal()) {
			for (DiscountStructure discountStructure : discount.structures()) {
				if (initialPrice >= discountStructure.min &&
						initialPrice <= discountStructure.max) {
					n_percent += getNspPercent(discountStructure);
					p_percent += getPrincPercent(discountStructure);
					break;
				}
			}
		}

		priceDiscounts = (long) (initialPrice * ((n_percent + p_percent) / 100));

		return priceDiscounts;
	}

	public static double[] getDiscounts(String product_id, long initialPrice, String unit_converter) {
		double[] priceDiscounts = new double[2];

		double n_percent = 0;
		double p_percent = 0;

		Product product = Product.find(product_id);

		for (Discount discount : getDiscountsPsd()) {
			if (discount.expired.after(new Date()) && discount.started.before(new Date())) {
				if (discount.unit.equalsIgnoreCase(unit_converter) ||
						(product.unitId.equalsIgnoreCase(unit_converter) && !discount.unit.equalsIgnoreCase("pcs"))) {
					for (String s : discount.getClause()) {
						if (s.equalsIgnoreCase(product.prodid)) {
							for (DiscountStructure discountStructure : discount.structures()) {
								if (initialPrice >= discountStructure.min &&
										initialPrice <= discountStructure.max) {
									n_percent += getNspPercent(discountStructure);
									p_percent += getPrincPercent(discountStructure);
									break;
								}
							}
							break;
						}
					}
				}
			}
		}

		for (Discount discount : getDiscountsSub()) {
			if (discount.expired.after(new Date()) && discount.started.before(new Date())) {
				if (discount.unit.equalsIgnoreCase(unit_converter) ||
						(product.unitId.equalsIgnoreCase(unit_converter) && !discount.unit.equalsIgnoreCase("pcs"))) {
					for (String s : discount.getClause()) {
						if (s.equalsIgnoreCase(product.principalId)) {
							for (DiscountStructure discountStructure : discount.structures()) {
								if (initialPrice >= discountStructure.min &&
										initialPrice <= discountStructure.max) {
									n_percent += getNspPercent(discountStructure);
									p_percent += getPrincPercent(discountStructure);
									break;
								}
							}
							break;
						}
					}
				}
			}
		}

		for (Discount discount : getDiscountsLpd()) {
			if (discount.expired.after(new Date()) && discount.started.before(new Date())) {
				if (discount.unit.equalsIgnoreCase(unit_converter) ||
						(product.unitId.equalsIgnoreCase(unit_converter) && !discount.unit.equalsIgnoreCase("pcs"))) {
					for (DiscountStructureLPD discountStructureLPD : discount.structureLPDs()) {
						if (discountStructureLPD.product_id.equalsIgnoreCase(product_id)) {
							n_percent += getNspPercent(discountStructureLPD);
							p_percent += getPrincPercent(discountStructureLPD);
							break;
						}
					}
					break;
				}
			}
		}

		priceDiscounts[0] = n_percent;
		priceDiscounts[1] = p_percent;

		return priceDiscounts;
	}

	public static int getMulDiscount(String productId, int qty, String unitConverter) {
		Product product = Product.find(productId);
		Log.e("getMulDiscount", product.toString());
		for (Discount discount : getDiscountsMul()) {
			Log.e("getMulDiscount", discount.toString());
			if (discount.expired.after(new Date()) && discount.started.before(new Date())) {
				return checkDiscount(discount, product, unitConverter, qty);
			}
		}

		return 0;
	}

	private static int checkDiscount(Discount discount, Product product, String unitConverter, int qty) {
		if (discount.unit.equalsIgnoreCase(unitConverter) ||
				(product.unitId.equalsIgnoreCase(unitConverter) && !discount.unit.equalsIgnoreCase("pcs"))) {
			for (DiscountStructureMul discountStructureMul : discount.structureMuls()) {
				if (discountStructureMul.productId.equalsIgnoreCase(product.prodid)) {
					if (qty >= discountStructureMul.minMultiples) {
						return getMulBonus(discountStructureMul) * (qty / discountStructureMul.minMultiples);
					}
				}
			}
		}

		return 0;
	}

	public static double[] getProductDiscount(String productId, long initialPrice, String unit_converter) {
		double[] priceDiscounts = new double[2];

		double n_percent = 0;
		double p_percent = 0;

		Product product = Product.find(productId);

		for (Discount discount : getDiscountsLpd()) {
			if (discount.unit.equalsIgnoreCase(unit_converter) ||
					(product.unitId.equalsIgnoreCase(unit_converter) && !discount.unit.equalsIgnoreCase("pcs"))) {
				for (DiscountStructure discountStructure : discount.structures()) {
					n_percent += getNspPercent(discountStructure);
					p_percent += getPrincPercent(discountStructure);
					break;
				}
				break;
			}
		}

		priceDiscounts[0] = n_percent;
		priceDiscounts[1] = p_percent;

		return priceDiscounts;
	}

	public static double[] getDiscounts(String product_id, long initialPrice) {
		double[] priceDiscounts = new double[2];

		double n_percent = 0;
		double p_percent = 0;

		Product product = Product.find(product_id);

		for (Discount discount : getDiscountsPsd()) {
			for (String s : discount.getClause()) {
				if (s.equalsIgnoreCase(product.prodid)) {
					for (DiscountStructure discountStructure : discount.structures()) {
						if (initialPrice >= discountStructure.min &&
								initialPrice <= discountStructure.max) {
							n_percent += getNspPercent(discountStructure);
							p_percent += getPrincPercent(discountStructure);
							break;
						}
					}
					break;
				}
			}
		}

		for (Discount discount : getDiscountsSub()) {
			Log.d(TAG, "getDiscounts: " + discount.toString());
			for (String s : discount.getClause()) {
				Log.e(TAG, "getDiscounts: Clause " + s + " principalId " + product.principalId);
				if (s.equalsIgnoreCase(product.principalId)) {
					for (DiscountStructure discountStructure : discount.structures()) {
						Log.i(TAG, "getDiscounts: min " + discountStructure.min);
						if (initialPrice >= discountStructure.min &&
								initialPrice <= discountStructure.max) {
							n_percent += getNspPercent(discountStructure);
							p_percent += getPrincPercent(discountStructure);
							Log.d(TAG, "getDiscounts: n_percent " + n_percent + " p_percent " + p_percent);
							break;
						}
					}
					break;
				}
			}
		}

		priceDiscounts[0] = n_percent;
		priceDiscounts[1] = p_percent;

		return priceDiscounts;
	}

	private static double getPrincPercent(DiscountStructure discountStructure) {
		if (discountStructure.discount.principal_active == 1)
			return discountStructure.principal_percent;

		return 0;
	}

	private static double getNspPercent(DiscountStructure discountStructure) {
		if (discountStructure.discount.nusantara_active == 1)
			return discountStructure.nusantara_percent;

		return 0;
	}

	private static double getPrincPercent(DiscountStructureLPD discountStructureLPD) {
		if (discountStructureLPD.discount.principal_active == 1)
			return discountStructureLPD.principal_percent;

		return 0;
	}

	private static double getNspPercent(DiscountStructureLPD discountStructureLPD) {
		if (discountStructureLPD.discount.nusantara_active == 1)
			return discountStructureLPD.nusantara_percent;

		return 0;
	}

	private static int getPrincipalBonus(DiscountStructureMul discountStructureMul) {
		if (discountStructureMul.discount.principal_active == 1)
			if (discountStructureMul.discount.unit.equals("Pcs"))
				return discountStructureMul.princQtyPcs;
			else return discountStructureMul.princQtyCarton;
		else return 0;
	}

	private static int getNspBonust(DiscountStructureMul discountStructureMul) {
		if (discountStructureMul.discount.nusantara_active == 1)
			if (discountStructureMul.discount.unit.equals("Pcs"))
				return discountStructureMul.npsQtyPcs;
			else return discountStructureMul.nspQtyCarton;
		else return 0;
	}

	private static int getMulBonus(DiscountStructureMul discountStructureMul) {
		return getPrincipalBonus(discountStructureMul) + getNspBonust(discountStructureMul);
	}


	public List<DiscountStructure> structures() {
		return getMany(DiscountStructure.class, "discount");
	}

	public List<DiscountStructureLPD> structureLPDs() {
		return getMany(DiscountStructureLPD.class, "discount");
	}

	public List<DiscountStructureMul> structureMuls() {
		return getMany(DiscountStructureMul.class, "discount");
	}

	public List<String> getClause() {
		if (this.clause != null) return Arrays.asList(this.clause.split("\\,"));

		return new ArrayList<>();
	}

	@Override
	public String toString() {
		return "Discount{" +
				"ita_id='" + _id + '\'' +
				", description='" + description + '\'' +
				", principal_active=" + principal_active +
				", nusantara_active=" + nusantara_active +
				", expired=" + expired +
				", clause='" + clause + '\'' +
				", unit='" + unit + '\'' +
				", structures=" + structures +
				'}';
	}
}
