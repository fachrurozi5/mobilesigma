package com.fachru.sigmamobile.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.adapters.AdapterDoHead;
import com.fachru.sigmamobile.adapters.AdapterDoItem;
import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.model.DoItem;
import com.fachru.sigmamobile.utils.CommonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fachru on 03/11/15.
 */
public class DoneOrderFragment extends Fragment implements SearchView.OnQueryTextListener, AdapterView.OnItemLongClickListener {

	protected Activity activity;
	protected Bundle bundle;

	protected ListView lv_done_order;
	protected SearchView searchView;
	protected EditText et_total;

	protected List<DoHead> doHeads;

	protected AdapterDoHead adapterDoHeadItem;
	protected AdapterDoItem adapterDoItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		bundle = getArguments();

		doHeads = new ArrayList<>();
		if (DoHead.getAllHasPrint().size() > 0)
			doHeads = DoHead.getAllHasPrint();

		adapterDoHeadItem = new AdapterDoHead(activity, doHeads);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_done_order, container, false);

		lv_done_order = (ListView) view.findViewById(R.id.lv_done_order);
		searchView = (SearchView) view.findViewById(R.id.sv_order_done);

		lv_done_order.setAdapter(adapterDoHeadItem);

		lv_done_order.setOnItemLongClickListener(this);
		searchView.setOnQueryTextListener(this);

		return view;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		adapterDoHeadItem.getFilter().filter(newText);
		return false;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        /*showDoItems((DoHead) parent.getItemAtPosition(position));*/
		DoHead doHead = (DoHead) parent.getItemAtPosition(position);
		File file = CommonUtil.getOutputMediaFile(doHead.doc_no);
		openPDf(file);
		return false;
	}

	private void showDoItems(DoHead doHead) {

		LayoutInflater inflater = LayoutInflater.from(activity);
		View view = inflater.inflate(R.layout.dialog_done_oreder, null);
		view.setPadding(getPx(5), getPx(5), getPx(5), getPx(5));

		ListView listView = (ListView) view.findViewById(R.id.lv_do_items);
		et_total = (EditText) view.findViewById(R.id.et_total);
		adapterDoItem = new AdapterDoItem(activity, doHead.doItems());

		listView.setAdapter(adapterDoItem);

		calcTotal();

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setView(view);
		AlertDialog dialog = builder.create();
		dialog.show();

	}

	private void openPDf(File file) {
		if (CommonUtil.canDisplayPdf(activity)) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file), "application/pdf");
			startActivity(intent);
		} else {
			new MaterialDialog.Builder(activity)
					.title("Information")
					.content("there's not pdf reader in this device, please install them")
					.positiveText(R.string.agree)
					.negativeText(R.string.disagree)
					.onPositive(new MaterialDialog.SingleButtonCallback() {
						@Override
						public void onClick(MaterialDialog dialog, DialogAction which) {
							try {
								startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.adobe.reader")));
							} catch (android.content.ActivityNotFoundException anfe) {
								startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.adobe.reader")));
							}
						}
					})
					.show();
		}
	}

	private void calcTotal() {
		double total = 0;
		if (adapterDoItem.getList().size() != 0) {
			for (DoItem item : adapterDoItem.getList()) {
				total += item.sub_total;
			}
		}
		et_total.setText(CommonUtil.priceFormat2Decimal(total));
	}

	private int getPx(int dimensionDp) {
		float density = getResources().getDisplayMetrics().density;
		return (int) (dimensionDp * density + 0.5f);
	}

}
