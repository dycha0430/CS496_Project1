package com.example.project1.ui.phone;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.databinding.ActivityContactDetailBinding;
import com.example.project1.databinding.ActivityMainBinding;

public class ContactDetailActivity extends AppCompatActivity {

    private ActivityContactDetailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContactDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
