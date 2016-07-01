package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

/**
 * Created by fachru on 29/12/15.
 */
@Table(name = "Employees")
public class Employee extends Model {

	public static final String ID = "employeeId";
	public static final String NAME = "name";
	public static final String OUTLET_ID = "outlet_id";
	public static final String PRODUCT_ID = "product_id";

	@Expose
	@SerializedName("id")
	@Column(name = ID)
	private String employeeId;

	@Expose
	@Column(name = NAME)
	private String name;

	@Expose
	@Column(name = OUTLET_ID)
	private String outlet_id;

	@Expose
	@Column(name = PRODUCT_ID)
	private String product_id;

	public Employee() {
		super();
	}

	public static Employee getEmployee(String empid) {
		return new Select()
				.from(Employee.class)
				.where(ID + " = ?", empid)
				.executeSingle();
	}

	public static String getEmployeeName(String employee_id) {
		Employee employee = getEmployee(employee_id);

		return employee != null ? employee.name : "";
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public String getName() {
		return name;
	}

	public String getOutlet_id() {
		return outlet_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public List<String> getProductIdList() {
		return Arrays.asList(this.product_id.split("\\,"));
	}

	public List<String> getOutletIdList() {
		return Arrays.asList(this.outlet_id.split("\\,"));
	}

	@Override
	public String toString() {
		return "Employee{" +
				"employeeId='" + employeeId + '\'' +
				", name='" + name + '\'' +
				", outlet_id='" + outlet_id + '\'' +
				", product_id='" + product_id + '\'' +
				'}';
	}
}
