package com.fachru.sigmamobile.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fachru.sigmamobile.R;
import com.fachru.sigmamobile.model.PicturesPath;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constantas;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by fachru on 11/12/15.
 */
public class PhotoAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static LayoutInflater inflater = null;
    private Context context;
    private List<PicturesPath> list = new ArrayList<>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    public PhotoAdapter(Context context) {
        this.context = context;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(PicturesPath picturesPath) {
        list.add(picturesPath);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(PicturesPath picturesPath) {
        list.add(picturesPath);
        sectionHeader.add(list.size() - 1);
        notifyDataSetChanged();

    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new Holder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = inflater.inflate(R.layout.layout_photo_view, null);
                    holder.tv_address = (TextView) convertView.findViewById(R.id.text_address);
                    holder.tv_time = (TextView) convertView.findViewById(R.id.text_time);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.imgPreview);
                    break;
                case TYPE_SEPARATOR:
                    convertView = inflater.inflate(R.layout.header_item_pictures, null);
                    holder.tv_date = (TextView) convertView.findViewById(R.id.text_header_date);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        PicturesPath p = (PicturesPath) getItem(position);

        if (rowType == TYPE_ITEM) {
            if (getItemViewType(position - 1) != TYPE_SEPARATOR)
                convertView.setPadding(0, CommonUtil.getPx(this.context, 40), 0, 0);
            else
                convertView.setPadding(0, 0, 0, 0);
            holder.imageView.setImageBitmap(createBitmap(p.picture_path));
            holder.tv_address.setText(p.picture_address);
            holder.tv_time.setText(CommonUtil.dateHelper(p.picture_date, Constantas.TIME));
        } else {
            holder.tv_date.setText(CommonUtil.dateHelper(p.picture_date, Constantas.ID));
        }

        return convertView;
    }

    public void update(List<PicturesPath> list) {
        this.list = list;
        notifyDataSetChanged();

    }

    private Bitmap createBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        return BitmapFactory.decodeFile(path, options);
    }

    private class Holder {
        TextView tv_address;
        TextView tv_date;
        TextView tv_time;
        ImageView imageView;
    }
}
