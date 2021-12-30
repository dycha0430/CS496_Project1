package com.example.project1.ui.dashboard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.project1.R;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    public Integer[] images = {
            R.drawable.gallery_01,
            R.drawable.gallery_02,
            R.drawable.gallery_03,
            R.drawable.gallery_04,
            R.drawable.gallery_05,
            R.drawable.gallery_06,
            R.drawable.gallery_07,
            R.drawable.gallery_08,
            R.drawable.gallery_09,
            R.drawable.gallery_10,
            R.drawable.gallery_11,
            R.drawable.gallery_12,
            R.drawable.gallery_13,
            R.drawable.gallery_14,
            R.drawable.gallery_15,
            R.drawable.gallery_16,
            R.drawable.gallery_17,
            R.drawable.gallery_18,
            R.drawable.gallery_19,
            R.drawable.gallery_20,
            R.drawable.gallery_21,
            R.drawable.gallery_22,
            R.drawable.gallery_23,
            R.drawable.gallery_24,
    };

    public ImageAdapter(Context c) {
        context = c;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageview;
        if (null != convertView) {
            imageview = (ImageView) convertView;
        }
        int a = parent.getWidth();
        imageview = new ImageView(context);
        imageview.setLayoutParams(new GridView.LayoutParams(parent.getWidth()/3-7, parent.getWidth()/3-7));
        imageview.setImageResource(images[position]);
        imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageview;
    }
}
