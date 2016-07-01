package com.fachru.sigmamobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fachru.sigmamobile.adapters.PhotoAdapter;
import com.fachru.sigmamobile.model.PicturesPath;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraActivity extends AppCompatActivity
		implements Animation.AnimationListener {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	private Context context = this;

	private Uri fileUri;
	private PhotoAdapter photoAdapter;
	private PicturesPath picturesPath;
	private Animation animSlideUp;
	private Animation animSlideUp2;

	/*
	* widget
	* */
	private ListView listView;
	private TextView textView;

	private List<PicturesPath> picturesPathList = new ArrayList<>();

	private int thisPosition = -1;
	private double latitude = 0;
	private double longitude = 0;
	private String label_address;
	private String prevDate = null;
	private String thisDate;

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				if (intent.hasExtra(Constanta.LATITUDE)) {
					latitude = bundle.getDouble(Constanta.LATITUDE, 0);
					longitude = bundle.getDouble(Constanta.LONGITUDE, 0);
					label_address = bundle.getString(Constanta.RESULT_ADDRESS);
					Log.d(Constanta.TAG, "Receiver : " + latitude + " " + longitude + " " + label_address);
				}
			} else {
				Toast.makeText(CameraActivity.this, "FAILED",
						Toast.LENGTH_LONG).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		initComponent();

		Intent bundle = getIntent();

		if (bundle != null) {
			latitude = bundle.getDoubleExtra(Constanta.LATITUDE, 0);
			longitude = bundle.getDoubleExtra(Constanta.LONGITUDE, 0);
			label_address = bundle.getStringExtra(Constanta.RESULT_ADDRESS);
			Log.d(Constanta.TAG, "Bundle not null " + latitude + " " + longitude + " " + label_address);
		}

		animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
		animSlideUp2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up2);
		animSlideUp.setAnimationListener(this);

		picturesPathList = PicturesPath.all();

		photoAdapter = new PhotoAdapter(context);

		int i = 0;
		for (PicturesPath path : picturesPathList) {
			thisDate = CommonUtil.dateHelper(picturesPathList.get(i++).picture_date, Constanta.ID);
			if (prevDate == null || !prevDate.equals(thisDate)) {
				prevDate = thisDate;
				if (i != 1)
					photoAdapter.addSectionHeaderItem(path);
				photoAdapter.addItem(path);
			} else {
				photoAdapter.addItem(path);
			}
		}
		prevDate = null;

		listView.setAdapter(photoAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PicturesPath path = (PicturesPath) parent.getItemAtPosition(position);
				Log.d(Constanta.TAG, "Position " + position);
			}
		});

		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (picturesPathList.size() != 0)
					if (thisPosition != firstVisibleItem) {
						thisPosition = firstVisibleItem;

						PicturesPath path = (PicturesPath) listView.getItemAtPosition(thisPosition);

						thisDate = CommonUtil.dateHelper(path.picture_date, Constanta.ID);

						if (prevDate == null || !prevDate.equals(thisDate)) {
							prevDate = thisDate;
							textView.startAnimation(animSlideUp);
							textView.setText(thisDate);
						}

					}

			}
		});

//        takePicture();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				File file = new File(fileUri.getPath());
				// Image captured and saved to fileUri specified in the Intent
				Date lastModified = new Date(file.lastModified());

				Log.d(Constanta.TAG, "Lat " + latitude + " | Long " + longitude);

				picturesPath = new PicturesPath();
				picturesPath.picture_name = file.getName();
				picturesPath.picture_date = lastModified;
				picturesPath.picture_lat = latitude;
				picturesPath.picture_long = longitude;
				picturesPath.picture_path = fileUri.getPath();
				picturesPath.picture_address = label_address;
				picturesPath.save();

				photoAdapter.update(PicturesPath.all());

			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_photo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_camera) {
			takePicture();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void initComponent() {
		textView = (TextView) findViewById(R.id.text_header_date);
		listView = (ListView) findViewById(R.id.lv_picture_view);
	}

	private void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = CommonUtil.getOutputMediaFileUri(Constanta.MEDIA_TYPE_IMAGE); // create a file to save the image
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

		// start the image capture Intent
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		textView.startAnimation(animSlideUp2);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}
}
