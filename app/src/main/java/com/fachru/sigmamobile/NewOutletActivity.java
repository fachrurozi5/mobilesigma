package com.fachru.sigmamobile;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fachru.sigmamobile.model.Outlet;
import com.fachru.sigmamobile.model.OutletType;
import com.fachru.sigmamobile.model.Warehouse;

public class NewOutletActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener{

	private Context context = this;

	private EditText editTextId;
	private EditText editTextName;
	private EditText editTextAddress;
	private EditText editTextOwner;
	private EditText editTextSecondOwner;
	private EditText editTextThirdOwner;
	private EditText editTextTelephone;
	private EditText editTextMobile;
	private EditText editTextLatitude;
	private EditText editTextLongitude;

	private RadioGroup radioGroupPayment;

	private Spinner spinnerTypeOutlet;

	private Button buttonSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_outlet);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		initComponent();

		editTextId.setOnFocusChangeListener(this);
		editTextName.setOnFocusChangeListener(this);
		editTextAddress.setOnFocusChangeListener(this);
		editTextOwner.setOnFocusChangeListener(this);
		editTextSecondOwner.setOnFocusChangeListener(this);
		editTextThirdOwner.setOnFocusChangeListener(this);
		editTextTelephone.setOnFocusChangeListener(this);
		editTextMobile.setOnFocusChangeListener(this);
		editTextLatitude.setOnFocusChangeListener(this);
		editTextLongitude.setOnFocusChangeListener(this);

		ArrayAdapter<OutletType> adapter = new ArrayAdapter<OutletType>(context,android.R.layout.simple_dropdown_item_1line,android.R.id.text1,OutletType.all()){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text1 = (TextView) view.findViewById(android.R.id.text1);
				OutletType outletType = getItem(position);
				text1.setText(outletType.getName());
				return view;
			}

			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent) {
				View view =  super.getDropDownView(position, convertView, parent);
				TextView text1 = (TextView) view.findViewById(android.R.id.text1);
				OutletType outletType = getItem(position);
				text1.setText(outletType.getName());
				return view;
			}
		};

		adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

		spinnerTypeOutlet.setAdapter(adapter);
		buttonSave.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
			case R.id.buttonSave:
				validation();
				break;
			default:
				Toast.makeText(context, "ID IS NOT VALID", Toast.LENGTH_LONG).show();
				break;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				v.setBackground(getResources().getDrawable(R.drawable.edittext_focus_style,context.getTheme()));
			} else {
				v.setBackground(getResources().getDrawable(R.drawable.edittext_focus_style));
			}
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				v.setBackground(getResources().getDrawable(R.drawable.edittext_lost_focus_style,context.getTheme()));
			} else {
				v.setBackground(getResources().getDrawable(R.drawable.edittext_lost_focus_style));
			}
		}
	}

	private void initComponent() {
		editTextId          = (EditText) findViewById(R.id.editTextId);
		editTextName        = (EditText) findViewById(R.id.editTextName);
		editTextAddress     = (EditText) findViewById(R.id.editTextAddress);
		editTextOwner       = (EditText) findViewById(R.id.editTextOwner);
		editTextSecondOwner = (EditText) findViewById(R.id.editTextSecondOwner);
		editTextThirdOwner  = (EditText) findViewById(R.id.editTextThirdOwner);
		editTextTelephone   = (EditText) findViewById(R.id.editTextTelp);
		editTextMobile      = (EditText) findViewById(R.id.editTextMobile);
		editTextLatitude    = (EditText) findViewById(R.id.editTextLat);
		editTextLongitude   = (EditText) findViewById(R.id.editTextLng);
		radioGroupPayment   = (RadioGroup) findViewById(R.id.radioGroupPayment);
		spinnerTypeOutlet   = (Spinner) findViewById(R.id.spinnerTypeOutlet);
		buttonSave          = (Button) findViewById(R.id.buttonSave);
	}

	private boolean validation() {

		if (!isInputValid(editTextId)) {
			editTextId.setError("Please Enter ID");
			return false;
		} else if (!isInputValid(editTextName)) {
			editTextName.setError("Please Enter Name");
			return false;
		} else if (!isInputValid(editTextOwner)) {
			editTextOwner.setError("Please Enter Owner");
			return false;
		}

		return true;
	}

	private boolean isInputValid(EditText editText) {
		if (editText.getText().toString().equals("") || editText.getText() == null) return false;

		return true;
	}
}
