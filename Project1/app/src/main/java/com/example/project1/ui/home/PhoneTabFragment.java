package com.example.project1.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;
import com.example.project1.databinding.FragmentHomeBinding;

public class PhoneTabFragment extends Fragment {

    private FragmentHomeBinding binding;
    RecyclerView recyclerView;
    PhoneTabAdapter phoneTabAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.phoneList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        phoneTabAdapter = new PhoneTabAdapter(getActivity());

        recyclerView.setAdapter(phoneTabAdapter);

        return rootView;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}