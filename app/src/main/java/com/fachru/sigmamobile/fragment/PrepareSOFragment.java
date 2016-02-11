package com.fachru.sigmamobile.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.adapters.AdapterSoHead;
import com.fachru.sigmamobile.model.SoHead;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fachru on 03/11/15.
 */
public class PrepareSOFragment extends Fragment {

    protected Activity activity;
    protected Bundle bundle;

    protected ListView lv_prepare_so;

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

        lv_prepare_so = (ListView) view.findViewById(R.id.lv_prepare_so);

        lv_prepare_so.setAdapter(adapterSoHeadItem);

        return view;
    }
}
