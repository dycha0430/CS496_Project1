package com.example.project1.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.project1.R;
import com.example.project1.databinding.FragmentNotificationsBinding;

import org.intellij.lang.annotations.Identifier;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private final int GAME_WIDTH = 10;
    private final int GAME_HEIGHT = 12;
    ImageButton[][] peaches;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        peaches = new ImageButton[12][10];
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_notifications, container, false);

        getPeaches(rootView);
        initGame();
        return rootView;
    }

    void getPeaches(ViewGroup rootView) {
        for (int i = 0; i < GAME_HEIGHT; i++) {
            for (int j = 0; j < GAME_WIDTH; j++) {
                String peach_id_string = "peach" + (i+1) + "_" + (j+1);
                String packageName = getActivity().getPackageName();
                int peach_id = getActivity().getResources().getIdentifier(peach_id_string, "id", packageName);

                ImageButton peach = (ImageButton) rootView.findViewById(peach_id);

                peaches[i][j] = peach;
            }
        }
    }

    void initGame() {
        for (int i = 0; i < GAME_HEIGHT; i++) {
            for (int j = 0; j < GAME_WIDTH; j++) {
                int randomNum = (int)(Math.random() * 9 + 1);

                if (randomNum == 8 || randomNum == 9) {
                    if ((int)(Math.random() * 10) < 4) {
                        randomNum = (int)(Math.random() * 9 + 1);
                    }
                }

                String packageName = getActivity().getPackageName();
                String peach_image_id_string = "peach" + randomNum;
                int peach_image_id = getActivity().getResources().getIdentifier(peach_image_id_string, "drawable", packageName);

                peaches[i][j].setImageResource(peach_image_id);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}