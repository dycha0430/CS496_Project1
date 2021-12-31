package com.example.project1.ui.gallery;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.project1.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Uri> images;

    public ImageAdapter(Context c, ArrayList<Uri> images) {
        context = c;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
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
        imageview.setLayoutParams(new GridView.LayoutParams(parent.getWidth()/3-15, parent.getWidth()/3-15));
        imageview.setImageURI(images.get(position));
        imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageview;
    }

    public void setImages(ArrayList<Uri> images) {
        this.images = images;
    }
}
