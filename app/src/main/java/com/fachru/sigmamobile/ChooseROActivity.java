package com.fachru.sigmamobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.fachru.sigmamobile.adapter.ImageAdapter;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.utils.Constanta;
import com.fachru.sigmamobile.utils.SessionManager;

public class ChooseROActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private Context context = this;

    /*
    * widget
    * */
    private Toolbar toolbar;
    private GridView gridview;
    private TextView text_time;
    private TextView text_date;
    private TextView text_location;

    /*
    * utils
    * */
    private SessionManager manager;

    /*
    * label
    * */
    private long custid;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if (intent.hasExtra(Constanta.RESULT_DATE)) {
                    text_time.setText(bundle.getString(Constanta.RESULT_TIME));
                    text_date.setText(bundle.getString(Constanta.RESULT_DATE));
                } else if (intent.hasExtra(Constanta.RESULT_ADDRESS)) {
                    text_location.setText(bundle.getString(Constanta.RESULT_ADDRESS));
                }
            } else {
                Toast.makeText(context, "FAILED",
                        Toast.LENGTH_LONG).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_ro);
        manager = new SessionManager(context);
        Intent intent = getIntent();
        initComp();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridview.setAdapter(new ImageAdapter(this, getResources().getStringArray(R.array.sub_menu_c_ro),
                getResources().obtainTypedArray(R.array.icon_sub_menu_c_ro)));
        gridview.setOnItemClickListener(this);

        text_date.setText(intent.getStringExtra(Constanta.RESULT_DATE));
        text_time.setText(intent.getStringExtra(Constanta.RESULT_TIME));
        text_location.setText(intent.getStringExtra(Constanta.RESULT_ADDRESS));

        custid = intent.getLongExtra(CustomerActivity.CUSTID, -1);

        if (custid >= 0)
            manager.setCustomer(custid);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                receiver, new IntentFilter(Constanta.SERVICE_RECEIVER)
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        activityResult();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                activityResult();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = null;

        switch (position) {
            case 2:
                intent = new Intent(context, PointOfSaleActivity.class);
                break;
            case 3:
                intent = new Intent(context, SalesOrderActivity.class);
                break;
            case 4:
                intent = new Intent(context, ReturnsProductActivity.class);
                break;
            default:
                break;
        }

        if (intent != null) {
            intent.putExtra(CustomerActivity.CUSTID, manager.getCustomer());
            intent.putExtra(Login.EMPLID, manager.getEmployee());
            startActivity(intent);
        }
    }

    private void initComp() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        gridview = (GridView) findViewById(R.id.gridmenu);
        text_time = (TextView) findViewById(R.id.text_time);
        text_date = (TextView) findViewById(R.id.text_date);
        text_location = (TextView) findViewById(R.id.text_locality);
    }

    private void activityResult() {
        Intent intent = new Intent();
        intent.putExtra(Constanta.RESULT_DATE, text_date.getText().toString());
        intent.putExtra(Constanta.RESULT_TIME, text_time.getText().toString());
        intent.putExtra(Constanta.RESULT_ADDRESS, text_location.getText().toString());
        setResult(2, intent);
        finish();
    }

}
