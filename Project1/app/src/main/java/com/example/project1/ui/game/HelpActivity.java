package com.example.project1.ui.game;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.databinding.ActivityHelpBinding;

public class HelpActivity extends AppCompatActivity {
    private ActivityHelpBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
