package com.example.project1.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.project1.R;
import com.example.project1.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    protected Context context;
    protected ImageAdapter imageAdapter;

    @Override
    public void onCreate(Bundle savedInstance) {super.onCreate(savedInstance);}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);

        context = container.getContext();

        GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        imageAdapter = new ImageAdapter(context);
        gridView.setAdapter(imageAdapter);
        return rootView;
    }
}