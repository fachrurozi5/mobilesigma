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
import com.fachru.sigmamobile.utils.Constantas;

public class ChooseROActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    protected Context context = this;

    protected Toolbar toolbar;
    protected GridView gridview;
    protected TextView text_time;
    protected TextView text_date;
    protected TextView text_location;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if (intent.hasExtra(Constantas.RESULT_DATE)) {
                    text_time.setText(bundle.getString(Constantas.RESULT_TIME));
                    text_date.setText(bundle.getString(Constantas.RESULT_DATE));
                } else if (intent.hasExtra(Constantas.RESULT_ADDRESS)) {
                    text_location.setText(bundle.getString(Constantas.RESULT_ADDRESS));
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
        Intent intent = getIntent();
        initComp();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gridview.setAdapter(new ImageAdapter(this, getResources().getStringArray(R.array.sub_menu_c_ro),
                getResources().obtainTypedArray(R.array.icon_sub_menu_c_ro)));
        gridview.setOnItemClickListener(this);
        text_date.setText(intent.getStringExtra(Constantas.RESULT_DATE));
        text_time.setText(intent.getStringExtra(Constantas.RESULT_TIME));
        text_location.setText(intent.getStringExtra(Constantas.RESULT_ADDRESS));
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                receiver, new IntentFilter(Constantas.SERVICE_RECEIVER)
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
        switch (position) {
            case 2:
                startActivity(new Intent(ChooseROActivity.this, PointOfSaleActivity.class));
                break;
            case 4:
                startActivity(new Intent(ChooseROActivity.this, ReturnsProductActivity.class));
                break;
            default:
                break;
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
        intent.putExtra(Constantas.RESULT_DATE, text_date.getText().toString());
        intent.putExtra(Constantas.RESULT_TIME, text_time.getText().toString());
        intent.putExtra(Constantas.RESULT_ADDRESS, text_location.getText().toString());
        setResult(2, intent);
        finish();
    }

}
