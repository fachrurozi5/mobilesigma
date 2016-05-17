package com.fachru.sigmamobile.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.adapters.AdapterSoHead;
import com.fachru.sigmamobile.model.SoHead;
import com.fachru.sigmamobile.utils.CommonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fachru on 03/11/15.
 */
public class PrepareSOFragment extends Fragment implements SearchView.OnQueryTextListener, AdapterView.OnItemLongClickListener {

    protected Activity activity;
    protected Bundle bundle;

    protected SearchView searchView;
    protected ListView lv_prepare_so;

    protected SoHead soHead;

    protected List<SoHead> soHeads;

    protected AdapterSoHead adapterSoHeadItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        bundle = getArguments();

        soHeads = new ArrayList<>();
        if (SoHead.getAllHasPrint().size() > 0)
            soHeads = SoHead.getAllHasPrint();

        adapterSoHeadItem = new AdapterSoHead(activity, soHeads);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_prepare_so, container, false);

        searchView = (SearchView) view.findViewById(R.id.sv_prepare_so);
        lv_prepare_so = (ListView) view.findViewById(R.id.lv_prepare_so);

        lv_prepare_so.setAdapter(adapterSoHeadItem);

        searchView.setOnQueryTextListener(this);

        lv_prepare_so.setOnItemLongClickListener(this);


        return view;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        soHead = (SoHead) parent.getItemAtPosition(position);
        File file = CommonUtil.getOutputMediaFile(soHead.so);
        openPDf(file);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapterSoHeadItem.getFilter().filter(newText);
        return false;
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
}
