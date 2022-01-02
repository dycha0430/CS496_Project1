package com.example.project1.ui.gallery;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.R;
import com.github.chrisbanes.photoview.PhotoView;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FullImageActivity extends AppCompatActivity {

    Button button;

    private String uri2path(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }
        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = { MediaStore.Files.FileColumns.DATA };
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
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



    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Intent i = getIntent();

        Uri position = Uri.parse(i.getExtras().getString("id"));
        String imagepath = uri2path(position);

        int orient = getOrientationOfImage(imagepath);
        Bitmap myBitmap = BitmapFactory.decodeFile(imagepath);
        Bitmap newBitmap = null;
        try {
            newBitmap = getRotatedBitmap(myBitmap, orient);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PhotoView photoView = (PhotoView) findViewById(R.id.photoView);
        photoView.setImageBitmap(newBitmap);


        button = (Button) findViewById(R.id.deleteImageBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BufferedReader br = null;
                String readStr = "";
                //--------read-------//
                try {
                    br = new BufferedReader(new FileReader(getFilesDir()+"gallery2.json"));
                    String str = null;
                    while(true){
                        if (!((str=br.readLine())!=null)) break;
                        JSONObject jsonobject2 = new JSONObject(str);
                        String imagePath = jsonobject2.getString("uri");
                        if (!imagePath.equals(imagepath))readStr+=str+"\n";
                        Log.d("@@imagePath11@@", imagePath);
                        Log.d("@@imagepath22@@", imagepath);
                    }
                    br.close();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                //--------read--------//
                //--------write temp images--------//
                String blank = "";
                BufferedWriter bm = null;
                try {
                    bm = new BufferedWriter(new FileWriter(getFilesDir() + "gallery2.json", false));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bm.write(blank);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    bm.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "gallery2.json", true));
                    bw.write(readStr);
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //--------write temp images--------//
                finish();


            }
        });
    }
}