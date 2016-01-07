package com.fachru.sigmamobile.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.adapters.adapter.AdapterDoHeadItem;
import com.fachru.sigmamobile.model.model.DoHead;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fachru on 03/11/15.
 */
public class DoneOrderFragment extends Fragment {

    protected Activity activity;
    protected Bundle bundle;

    protected ListView lv_done_order;

    protected List<DoHead> doHeads;

    protected AdapterDoHeadItem adapterDoHeadItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        bundle = getArguments();

        doHeads = new ArrayList<>();
        if (DoHead.allDone().size() > 0)
            doHeads = DoHead.allDone();

        adapterDoHeadItem = new AdapterDoHeadItem(activity, doHeads);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_done_order, container, false);

        lv_done_order = (ListView) view.findViewById(R.id.lv_done_order);

        lv_done_order.setAdapter(adapterDoHeadItem);

        return view;
    }
}
