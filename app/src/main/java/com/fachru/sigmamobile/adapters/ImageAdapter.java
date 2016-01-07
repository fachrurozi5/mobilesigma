package com.fachru.sigmamobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fachru.sigmamobile.R;

/**
 * Created by fachru on 28/10/15.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    // references to our images
    private TypedArray mThumbIds;
    private String[] mStrings;

    public ImageAdapter(Context c, String[] strings, TypedArray thumbIds) {
        mContext = c;
        mStrings = strings;
        mThumbIds = thumbIds;
    }

    public int getCount() {
        return mStrings.length;
    }

    public Object getItem(int position) {
        return mStrings[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = mInflater.inflate(R.layout.layout_menu, null);
            holder = new ViewHolder();
            holder.menu_icon = (ImageView) convertView.findViewById(R.id.menu_icon);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.menu_icon.setImageResource(mThumbIds.getResourceId(position, -1));
        holder.tv_title.setText(mStrings[position]);

        return convertView;
    }

    private class ViewHolder {
        ImageView menu_icon;
        TextView tv_title;
    }
}