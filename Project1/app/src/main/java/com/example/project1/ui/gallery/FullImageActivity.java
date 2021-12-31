package com.example.project1.ui.gallery;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.R;
import com.github.chrisbanes.photoview.PhotoView;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Intent i = getIntent();

        Uri position = Uri.parse(i.getExtras().getString("id"));

        PhotoView photoView = (PhotoView) findViewById(R.id.photoView);
        photoView.setImageURI(position);
    }
}