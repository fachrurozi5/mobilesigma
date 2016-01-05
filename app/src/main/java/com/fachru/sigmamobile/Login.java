package com.fachru.sigmamobile;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.fachru.sigmamobile.controller.EmployeeController;
import com.fachru.sigmamobile.controller.interfaces.OnEmployeeCallbackListener;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.utils.Constanta;
import com.fachru.sigmamobile.utils.SessionManager;

public class Login extends AppCompatActivity implements OnClickListener, OnEmployeeCallbackListener {

    public static final String EMPLID = "key_emplid";
    private Context context = this;

    /*
    * utils
    * */
    private SessionManager sessionManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(context);
        controller = new EmployeeController(this);
        initComp();

        btn_login.setOnClickListener(this);
    }

    @Override
    public void onFetchProgress(Employee employee) {
        Log.e(Constanta.TAG, employee.toString());
        sessionManager.setEmployee(employee.getId());
    }

    @Override
    public void onFetchStart() {
        Log.e(Constanta.TAG, "Employee Start");
    }

    @Override
    public void onFetchComplete() {
        Log.e(Constanta.TAG, "Employee Complite");
    }

    @Override
    public void onFetchFailed(Throwable t) {
        Log.e(Constanta.TAG, "FetchFailed", t);
    }

    @Override
    public void onClick(View v) {
        if (!isMissing()) {
            String username = et_username.getText().toString();
            String password = et_password.getText().toString();
            controller.startFetch(username, password);
        }
    }

    private void initComp() {
        et_username = (EditText) findViewById(R.id.input_username);
        et_password = (EditText) findViewById(R.id.input_password);
        btn_login   = (Button) findViewById(R.id.btn_login);
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


}
