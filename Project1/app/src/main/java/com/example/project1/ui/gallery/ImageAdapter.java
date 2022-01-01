package com.example.project1.ui.gallery;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> images;

    public ImageAdapter(Context c, ArrayList<String> images) {
        context = c;
        this.images = images;
    }

    //Uri -> Path(파일경로)
    private String uri2path(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }
        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = { MediaStore.Files.FileColumns.DATA };
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) { return cursor.getString(columnIndex); }
        } finally {
            cursor.close();
        }
        return null;
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
        if (null != convertView) {
            imageview = (ImageView) convertView;
        }
        int a = parent.getWidth();
        imageview = new ImageView(context);
        imageview.setLayoutParams(new GridView.LayoutParams(parent.getWidth()/3-15, parent.getWidth()/3-15));
        Bitmap myBitmap = BitmapFactory.decodeFile(images.get(position));
        imageview.setImageBitmap(myBitmap);
        Log.d("set", images.get(position));
        imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageview;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
