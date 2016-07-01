package com.fachru.sigmamobile;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fachru.sigmamobile.utils.Constanta;
import com.fachru.sigmamobile.utils.SessionManager;
import com.venmo.android.pin.PinFragment;
import com.venmo.android.pin.PinFragmentConfiguration;
import com.venmo.android.pin.PinSaver;
import com.venmo.android.pin.Validator;

public class PinActivity extends Activity implements PinFragment.Listener {

	private Context context = this;
	private SessionManager manager;

	private String lastApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pin);
		manager = new SessionManager(context);
		lastApp = getIntent().getStringExtra("extra");

		PinFragmentConfiguration config =
				new PinFragmentConfiguration(context)
						.pinSaver(new PinSaver() {
							@Override
							public void save(String s) {
								Log.d(Constanta.TAG, "Save " + s);
								manager.savePin(s);
							}
						}).validator(new Validator() {
					public boolean isValid(String submission) {
						return manager.getPin().equals(submission);
					}
				});

		Fragment toShow = manager.isPinSaved() ?
				PinFragment.newInstanceForVerification(config) :
				PinFragment.newInstanceForCreation(config);

		getFragmentManager().beginTransaction()
				.replace(R.id.root, toShow)
				.commit();

	}

	@Override
	public void onValidated() {
		manager.saveLastAppPn(lastApp);
		recreate();
		Intent intent = getPackageManager().getLaunchIntentForPackage(lastApp);
		startActivity(intent);
	}

	@Override
	public void onPinCreated() {
		Toast.makeText(this, "Created PIN!", Toast.LENGTH_SHORT).show();
		recreate();
	}
}
