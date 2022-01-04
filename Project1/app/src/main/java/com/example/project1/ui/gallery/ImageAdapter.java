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
    public ArrayList<String> images;

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

    public Bitmap getRotatedBitmap(Bitmap bitmap, int degrees) throws Exception {
        if(bitmap == null) return null;
        if (degrees == 0) return bitmap;

        Matrix m = new Matrix();
        m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }

    public int getOrientationOfImage(String filepath) {
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

        if (orientation != -1) {
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }
        }

        return 0;
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
        imageview = new ImageView(context);
        imageview.setLayoutParams(new GridView.LayoutParams(parent.getWidth()/3-15, parent.getWidth()/3-15));
        int orient = getOrientationOfImage(images.get(position));
        Bitmap myBitmap = BitmapFactory.decodeFile(images.get(position));
        Bitmap newBitmap = null;
        try {
            newBitmap = getRotatedBitmap(myBitmap, orient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageview.setImageBitmap(myBitmap);

        imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageview;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
