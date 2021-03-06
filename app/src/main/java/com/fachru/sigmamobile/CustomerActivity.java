package com.fachru.sigmamobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.adapters.AdapterCustomer;
import com.fachru.sigmamobile.controller.CustomerController;
import com.fachru.sigmamobile.controller.interfaces.OnCustomerCallbackListener;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity implements
		OnQueryTextListener, OnCustomerCallbackListener, OnItemLongClickListener, AdapterView.OnItemClickListener,
		SwipeRefreshLayout.OnRefreshListener {


	public static final String CUSTID = "key_custid";
	private static final int limit = 5000;
	private static int offset = 0;
	private static int list_position = 0;
	private Context context = this;
	private LoadCustomerTask customerTask;
	/*
	* label
	* */
	private String text_time = "";
	private String text_date = "";
	private String text_location = "";
	private boolean swipe_refresh = true;

	/*
	* widget
	* */
	private CoordinatorLayout coordinatorLayout;
	private Toolbar toolbar;
	private SearchView searchView;
	private SwipeRefreshLayout swipeRefreshLayout;
	private ListView listview;
	private MenuItem menuItem;
	private MenuItem miToogleSwipte;
	private TextView tv_name;
	private TextView tv_csstatid1;
	private TextView tv_csstatid2;
	private TextView tv_csstatid3;
	private TextView tv_csstatid4;
	private TextView tv_csstatid5;
	private TextView tv_type_group;
	private TextView tv_cs_group;
	private TextView tv_channel;
	private TextView tv_invadd1;
	private TextView tv_invadd2;
	private TextView tv_invadd3;
	private TextView tv_invadd4;
	private TextView tv_invzip;
	private TextView tv_deladdid;
	private TextView tv_deladd1;
	private TextView tv_deladd2;
	private TextView tv_deladd3;
	private TextView tv_deladd4;
	private TextView tv_delzip;
	private TextView tv_created;
	private TextView tv_updated;
	private EditText et_id;
	private EditText et_name;
	private EditText et_csstatid1;
	private EditText et_csstatid2;
	private EditText et_csstatid3;
	private EditText et_csstatid4;
	private EditText et_csstatid5;
	private EditText et_type_group;
	private EditText et_cs_group;
	private EditText et_channel;
	private EditText et_invadd1;
	private EditText et_invadd2;
	private EditText et_invadd3;
	private EditText et_invadd4;
	private EditText et_invzip;
	private EditText et_deladdid;
	private EditText et_deladd1;
	private EditText et_deladd2;
	private EditText et_deladd3;
	private EditText et_deladd4;
	private EditText et_delzip;
	private EditText et_created;
	private EditText et_updated;
	/*
	* adapter
	* */
	private AdapterCustomer adapter;
	/*
	* controller
	* */
	private CustomerController controller;
	/*
	* arraylist
	* */
	private List<Customer> list = new ArrayList<>();

	/*
	* reciever datetime
	* */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				if (intent.hasExtra(Constanta.RESULT_DATE)) {
					text_time = bundle.getString(Constanta.RESULT_TIME);
					text_date = bundle.getString(Constanta.RESULT_DATE);
				}

				if (intent.hasExtra(Constanta.RESULT_ADDRESS)) {
					text_location = bundle.getString(Constanta.RESULT_ADDRESS);
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
		setContentView(R.layout.activity_customer);
		initComp();

		customerTask = new LoadCustomerTask();

		controller = new CustomerController(this);

		adapter = new AdapterCustomer(context, list);

		searchView.setOnQueryTextListener(this);

		swipeRefreshLayout.setOnRefreshListener(this);

		swipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(true);

				customerTask.execute(limit, offset);
			}
		});

		listview.setAdapter(adapter);
		listview.setOnItemLongClickListener(this);
		listview.setOnItemClickListener(this);
		listview.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				list_position = firstVisibleItem + visibleItemCount;
			}
		});

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				listview.smoothScrollToPosition(list_position - 50);
			}
		});
		fab.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				listview.smoothScrollToPosition(0);
				return false;
			}
		});


	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Constanta.RESULT_OK
				&& requestCode == Constanta.REQUEST_CODE) {
			if (data.hasExtra(Constanta.RESULT_DATE)) {
				text_date = data.getStringExtra(Constanta.RESULT_DATE);
				text_time = data.getStringExtra(Constanta.RESULT_TIME);
			}

			if (data.hasExtra(Constanta.RESULT_ADDRESS))
				text_location = data.getStringExtra(Constanta.RESULT_ADDRESS);
		}
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
		customerTask.onCancelled();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		activityResult();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_customer, menu);
		menuItem = menu.findItem(R.id.action_sync);
		miToogleSwipte = menu.findItem(R.id.action_swipe);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
			case android.R.id.home:
				activityResult();
				return true;
			case R.id.action_sync:
				controller.startFetching();
				menuItem = item;
				menuItem.setActionView(R.layout.progressbar);
				menuItem.expandActionView();
				return true;
			case R.id.action_swipe:
				if (swipe_refresh) {
					swipe_refresh = false;
					swipeRefreshLayout.setEnabled(false);
					miToogleSwipte.setIcon(android.R.drawable.button_onoff_indicator_off);
					showSnackBar("Swipe Refresh Off", true).show();
				} else {
					swipe_refresh = true;
					swipeRefreshLayout.setEnabled(true);
					miToogleSwipte.setIcon(android.R.drawable.button_onoff_indicator_on);
					showSnackBar("Swipe Refresh On", false).show();
				}
			case R.id.action_settings:
				return true;
		}

		return super.onOptionsItemSelected(item);

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		Customer customer = (Customer) parent.getItemAtPosition(position);
		showDetailCustomer(customer);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Customer customer = (Customer) parent.getItemAtPosition(position);
		showDetailCustomer(customer);
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		adapter.getFilter().filter(newText);
		return false;
	}

	@Override
	public void onFetchStart() {
		menuItem.setActionView(R.layout.progressbar);
		menuItem.expandActionView();
	}

	@Override
	public void onFetchProgress(Customer customer) {

	}

	@Override
	public void onFetchProgress(List<Customer> list) {
		adapter.update(list);
	}

	@Override
	public void onFetchComplete() {
		menuItem.collapseActionView();
		menuItem.setActionView(null);
	}

	@Override
	public void onFetchFailed(Throwable t) {
		menuItem.collapseActionView();
		menuItem.setActionView(null);
		Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onRefresh() {
		new LoadCustomerTask().execute(limit, offset);
	}

	private void initComp() {

		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		searchView = (SearchView) findViewById(R.id.search_view);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
		listview = (ListView) findViewById(R.id.lv_customer);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	}


	private void showDetailCustomer(final Customer customer) {
		MaterialDialog dialog = new MaterialDialog.Builder(CustomerActivity.this)
				.title("Customer")
				.customView(R.layout.detail_customer, true)
				.negativeText("Tutup")
				.positiveText("OK")
				.cancelable(false)
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
						Intent intent = new Intent(context, ChooseROActivity.class);
						intent.putExtra(CUSTID, customer.getId());
						intent.putExtra(Constanta.RESULT_DATE, text_date);
						intent.putExtra(Constanta.RESULT_TIME, text_time);
						intent.putExtra(Constanta.RESULT_ADDRESS, text_location);
						startActivityForResult(intent, Constanta.REQUEST_CODE);
					}
				})
				.build();

		View view = dialog.getCustomView();

		et_id = (EditText) view.findViewById(R.id.et_id);
		et_name = (EditText) view.findViewById(R.id.et_name);
		et_csstatid1 = (EditText) view.findViewById(R.id.et_csstatid1);
		et_csstatid2 = (EditText) view.findViewById(R.id.et_csstatid2);
		et_csstatid3 = (EditText) view.findViewById(R.id.et_csstatid3);
		et_csstatid4 = (EditText) view.findViewById(R.id.et_csstatid4);
		et_csstatid5 = (EditText) view.findViewById(R.id.et_csstatid5);
		et_type_group = (EditText) view.findViewById(R.id.et_type_group);
		et_cs_group = (EditText) view.findViewById(R.id.et_cs_group);
		et_channel = (EditText) view.findViewById(R.id.et_channel);
		et_invadd1 = (EditText) view.findViewById(R.id.et_invadd1);
		et_invadd2 = (EditText) view.findViewById(R.id.et_invadd2);
		et_invadd3 = (EditText) view.findViewById(R.id.et_invadd3);
		et_invadd4 = (EditText) view.findViewById(R.id.et_invadd4);
		et_invzip = (EditText) view.findViewById(R.id.et_invzip);
		et_deladdid = (EditText) view.findViewById(R.id.et_deladdid);
		et_deladd1 = (EditText) view.findViewById(R.id.et_deladd1);
		et_deladd2 = (EditText) view.findViewById(R.id.et_deladd2);
		et_deladd3 = (EditText) view.findViewById(R.id.et_deladd3);
		et_deladd4 = (EditText) view.findViewById(R.id.et_deladd4);
		et_delzip = (EditText) view.findViewById(R.id.et_deladdzip);
		et_created = (EditText) view.findViewById(R.id.et_created);
		et_updated = (EditText) view.findViewById(R.id.et_updated);

		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_csstatid1 = (TextView) view.findViewById(R.id.tv_csstatid1);
		tv_csstatid2 = (TextView) view.findViewById(R.id.tv_csstatid2);
		tv_csstatid3 = (TextView) view.findViewById(R.id.tv_csstatid3);
		tv_csstatid4 = (TextView) view.findViewById(R.id.tv_csstatid4);
		tv_csstatid5 = (TextView) view.findViewById(R.id.tv_csstatid5);
		tv_type_group = (TextView) view.findViewById(R.id.tv_type_group);
		tv_cs_group = (TextView) view.findViewById(R.id.tv_cs_group);
		tv_channel = (TextView) view.findViewById(R.id.tv_channel);
		tv_invadd1 = (TextView) view.findViewById(R.id.tv_invadd1);
		tv_invadd2 = (TextView) view.findViewById(R.id.tv_invadd2);
		tv_invadd3 = (TextView) view.findViewById(R.id.tv_invadd3);
		tv_invadd4 = (TextView) view.findViewById(R.id.tv_invadd4);
		tv_invzip = (TextView) view.findViewById(R.id.tv_invzip);
		tv_deladdid = (TextView) view.findViewById(R.id.tv_deladdid);
		tv_deladd1 = (TextView) view.findViewById(R.id.tv_deladd1);
		tv_deladd2 = (TextView) view.findViewById(R.id.tv_deladd2);
		tv_deladd3 = (TextView) view.findViewById(R.id.tv_deladd3);
		tv_deladd4 = (TextView) view.findViewById(R.id.tv_deladd4);
		tv_delzip = (TextView) view.findViewById(R.id.tv_deladdzip);
		tv_created = (TextView) view.findViewById(R.id.tv_created);
		tv_updated = (TextView) view.findViewById(R.id.tv_updated);

		setDataCustomer(customer);

		dialog.show();
	}


	private void setDataCustomer(Customer customer) {

		et_id.setText(customer.custid);

		if (!(customer.name.equals("") || customer.name.equals("-"))) {
			et_name.setText(customer.name);
		} else {
			et_name.setVisibility(View.GONE);
			tv_name.setVisibility(View.GONE);
		}

		if (!(customer.csstatid1.equals("") || customer.csstatid1.equals("-"))) {
			et_csstatid1.setText(customer.csstatid1);
		} else {
			et_csstatid1.setVisibility(View.GONE);
			tv_csstatid1.setVisibility(View.GONE);
		}

		if (!(customer.csstatid2.equals("") || customer.csstatid2.equals("-"))) {
			et_csstatid2.setText(customer.csstatid2);
		} else {
			et_csstatid2.setVisibility(View.GONE);
			tv_csstatid2.setVisibility(View.GONE);
		}

		if (!(customer.csstatid3.equals("") || customer.csstatid3.equals("-"))) {
			et_csstatid3.setText(customer.csstatid3);
		} else {
			et_csstatid3.setVisibility(View.GONE);
			tv_csstatid3.setVisibility(View.GONE);
		}

		if (!(customer.csstatid4.equals("") || customer.csstatid4.equals("-"))) {
			et_csstatid4.setText(customer.csstatid4);
		} else {
			et_csstatid4.setVisibility(View.GONE);
			tv_csstatid4.setVisibility(View.GONE);
		}

		if (!(customer.csstatid5.equals("") || customer.csstatid5.equals("-"))) {
			et_csstatid5.setText(customer.csstatid5);
		} else {
			et_csstatid5.setVisibility(View.GONE);
			tv_csstatid5.setVisibility(View.GONE);
		}

		if (!(customer.group_type.equals("") || customer.group_type.equals("-"))) {
			et_type_group.setText(customer.group_type);
		} else {
			et_type_group.setVisibility(View.GONE);
			tv_type_group.setVisibility(View.GONE);
		}

		if (!(customer.cs_group.equals("") || customer.cs_group.equals("-"))) {
			et_cs_group.setText(customer.cs_group);
		} else {
			et_cs_group.setVisibility(View.GONE);
			tv_cs_group.setVisibility(View.GONE);
		}

		if (!(customer.channel.equals("") || customer.channel.equals("-"))) {
			et_channel.setText(customer.channel);
		} else {
			et_channel.setVisibility(View.GONE);
			tv_channel.setVisibility(View.GONE);
		}

		if (!(customer.invadd1.equals("") || customer.invadd1.equals("-"))) {
			et_invadd1.setText(customer.invadd1);
		} else {
			et_invadd1.setVisibility(View.GONE);
			tv_invadd1.setVisibility(View.GONE);
		}

		if (!(customer.invadd2.equals("") || customer.invadd2.equals("-"))) {
			et_invadd2.setText(customer.invadd2);
		} else {
			et_invadd2.setVisibility(View.GONE);
			tv_invadd2.setVisibility(View.GONE);
		}

		if (!(customer.invadd3.equals("") || customer.invadd3.equals("-"))) {
			et_invadd3.setText(customer.invadd3);
		} else {
			et_invadd3.setVisibility(View.GONE);
			tv_invadd3.setVisibility(View.GONE);
		}

		if (!(customer.invadd4.equals("") || customer.invadd4.equals("-"))) {
			et_invadd4.setText(customer.invadd4);
		} else {
			et_invadd4.setVisibility(View.GONE);
			tv_invadd4.setVisibility(View.GONE);
		}

		if (!(customer.invzip.equals("") || customer.invzip.equals("-"))) {
			et_invzip.setText(customer.invzip);
		} else {
			et_invzip.setVisibility(View.GONE);
			tv_invzip.setVisibility(View.GONE);
		}

		if (!(customer.deladdid.equals("") || customer.deladdid.equals("-"))) {
			et_delzip.setText(customer.deladdid);
		} else {
			et_delzip.setVisibility(View.GONE);
			tv_delzip.setVisibility(View.GONE);
		}

		if (!(customer.deladd1.equals("") || customer.deladd1.equals("-"))) {
			et_deladd1.setText(customer.deladd1);
		} else {
			et_deladd1.setVisibility(View.GONE);
			tv_deladd1.setVisibility(View.GONE);
		}

		if (!(customer.deladd2.equals("") || customer.deladd2.equals("-"))) {
			et_deladd2.setText(customer.deladd2);
		} else {
			et_deladd2.setVisibility(View.GONE);
			tv_deladd2.setVisibility(View.GONE);
		}

		if (!(customer.deladd3.equals("") || customer.deladd3.equals("-"))) {
			et_deladd3.setText(customer.deladd3);
		} else {
			et_deladd3.setVisibility(View.GONE);
			tv_deladd3.setVisibility(View.GONE);
		}

		if (!(customer.deladd4.equals("") || customer.deladd4.equals("-"))) {
			et_deladd4.setText(customer.deladd4);
		} else {
			et_deladd4.setVisibility(View.GONE);
			tv_deladd4.setVisibility(View.GONE);
		}

		if (!(customer.delzip.equals("") || customer.delzip.equals("-"))) {
			et_delzip.setText(customer.delzip);
		} else {
			et_delzip.setVisibility(View.GONE);
			tv_delzip.setVisibility(View.GONE);
		}

		if (customer.created_at != null) {
			et_created.setText(CommonUtil.dateHelper(customer.created_at, Constanta.ID_LONG));
		} else {
			et_created.setVisibility(View.GONE);
			tv_created.setVisibility(View.GONE);
		}


		if (customer.updated_at != null) {
			et_updated.setText(CommonUtil.dateHelper(customer.updated_at, Constanta.ID_LONG));
		} else {
			et_updated.setVisibility(View.GONE);
			tv_updated.setVisibility(View.GONE);
		}

	}

	private void activityResult() {
		Intent intent = new Intent();
		intent.putExtra(Constanta.RESULT_DATE, text_date);
		intent.putExtra(Constanta.RESULT_TIME, text_time);
		intent.putExtra(Constanta.RESULT_ADDRESS, text_location);
		setResult(Constanta.RESULT_OK, intent);
		finish();
	}

	private Snackbar showSnackBar(String message, final boolean b) {
		return Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG)
				.setAction("BATAL", new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						swipe_refresh = b;
						swipeRefreshLayout.setEnabled(swipe_refresh);
						if (swipe_refresh) {
							miToogleSwipte.setIcon(android.R.drawable.button_onoff_indicator_on);
						} else {
							miToogleSwipte.setIcon(android.R.drawable.button_onoff_indicator_off);

						}
					}
				});
	}

	private class LoadCustomerTask extends AsyncTask<Integer, Customer, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			searchView.clearFocus();
			searchView.setFocusable(false);
			searchView.setEnabled(false);
		}

		@Override
		protected Integer doInBackground(Integer... params) {

			for (Customer customer : Customer.getAll(params[0], params[1])) {
				offset++;
				publishProgress(customer);
	            /*try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
			}
			Log.e(Constanta.TAG, "Offset " + offset);
			return 0;
		}

		@Override
		protected void onProgressUpdate(Customer... values) {
			adapter.add(values[0]);
			listview.setSelection(adapter.getCount() - 1);
		}

		@Override
		protected void onPostExecute(Integer integer) {
			menuItem.collapseActionView();
			menuItem.setActionView(null);
			searchView.setEnabled(true);
			searchView.setFocusable(true);
			searchView.requestFocus();
			adapter.getFilter().filter(searchView.getQuery().toString());
			swipeRefreshLayout.setRefreshing(false);
		}

		@Override
		protected void onCancelled() {
			Log.e(Constanta.TAG, "OnCancelled");
			offset = 0;
		}
	}
}
