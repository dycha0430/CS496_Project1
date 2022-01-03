package com.example.project1.ui.phone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.R;
import com.example.project1.databinding.ActivityContactDetailBinding;
import com.example.project1.databinding.ActivityMainBinding;

public class ContactDetailActivity extends AppCompatActivity {

    private ActivityContactDetailBinding binding;
    TextView nameTv, phoneNumTv;
    ImageView profileImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nameTv = (TextView) findViewById(R.id.detailNameTextView);
        phoneNumTv = (TextView) findViewById(R.id.detailPhoneNumTextView);
        profileImage = (ImageView) findViewById(R.id.detailProfileImageView);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phoneNum = intent.getStringExtra("phoneNum");
        String imageUriString = intent.getStringExtra("imageUri");

        Uri imageUri;
        if (imageUriString.equals("empty")) {
            profileImage.setImageResource(R.drawable.user);
        } else {
            imageUri = Uri.parse(imageUriString);
            profileImage.setImageURI(imageUri);
        }

        nameTv.setText(name);
        phoneNumTv.setText(phoneNum);

    }
}
