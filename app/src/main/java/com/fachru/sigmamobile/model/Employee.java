package com.fachru.sigmamobile.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by fachru on 29/12/15.
 */
@Table(name = "Employees")
public class Employee extends Model {

    @SerializedName("EMPID")
    @Column(name = "employee_id")
    public String employee_id;

    @SerializedName("EMPNAME")
    @Column(name = "name")
    public String name;

    @SerializedName("DATECREATE")
    @Column(name = "created_at")
    public Date created_at;

    @SerializedName("DATEUPDATE")
    @Column(name = "updated_at")
    public Date updated_at;

    public Employee() {
    }

    public Employee(Builder builder) {
        super();
        employee_id = builder.employee_id;
        name = builder.name;
        created_at = builder.created_at;
        updated_at = builder.updated_at;
    }

    public Employee(String employee_id, String name, Date created_at, Date updated_at) {
        super();
        this.employee_id = employee_id;
        this.name = name;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public static Employee findOrCreateFromJson(JSONObject json) throws JSONException {
        String empid = json.getString("EMPID");
        Employee existingEmployee = new Select().from(Employee.class).where("employee_id = ?", empid).executeSingle();
        if (existingEmployee != null) {
            return existingEmployee;
        } else {
            Employee employee = Employee.fromJson(json);
            employee.save();
            return employee;
        }
    }

    public static Employee fromJson(JSONObject json) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls();
        Gson gson = gsonBuilder.create();

        return gson.fromJson(json.toString(), Employee.class);
    }

    public static Employee getEmployee(String empid) {
        return new Select()
                .from(Employee.class)
                .where("employee_id = ?", empid)
                .executeSingle();
    }

    public static String getEmployeeName(String employee_id) {
        Employee employee = getEmployee(employee_id);

        if (employee != null)
            return employee.name;
        return "";
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employee_id='" + employee_id + '\'' +
                ", name='" + name + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }

    /*
    * class builder
    * */
    public static class Builder {

        private String employee_id;
        private String name;
        private Date created_at;
        private Date updated_at;

        public Builder setEmployee_id(String employee_id) {
            this.employee_id = employee_id;
            return Builder.this;
        }

        public Builder setName(String name) {
            this.name = name;
            return Builder.this;
        }

        public Builder setCreated_at(Date created_at) {
            this.created_at = created_at;
            return Builder.this;
        }

        public Builder setUpdated_at(Date updated_at) {
            this.updated_at = updated_at;
            return Builder.this;
        }

        public Employee builde() {
            return new Employee(Builder.this);
        }
    }
}
