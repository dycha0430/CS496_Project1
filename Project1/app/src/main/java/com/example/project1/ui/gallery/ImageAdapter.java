package com.example.project1.ui.gallery;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.project1.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    public ArrayList<Bitmap> images;

    public ImageAdapter(Context c, ArrayList<Bitmap> images) {
        context = c;
        this.images = images;
    }



    //Path(파일경로) -> Uri


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
        if (convertView != null) {
            imageview = (ImageView) convertView;
        }
        imageview = new ImageView(context);
        imageview.setLayoutParams(new GridView.LayoutParams(parent.getWidth()/3-15, parent.getWidth()/3-15));

        imageview.setImageBitmap(images.get(position));

        imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageview;
    }

    public void setImages(ArrayList<Bitmap> images) {
        this.images = images;
    }
}
