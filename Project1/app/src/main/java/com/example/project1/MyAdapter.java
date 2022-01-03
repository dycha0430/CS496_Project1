package com.example.project1;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.project1.ui.gallery.GalleryFragment;
import com.example.project1.ui.game.GameFragment;
import com.example.project1.ui.phone.PhoneTabFragment;

public class MyAdapter extends FragmentStateAdapter {
    public int mCount;
    public MyAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if (index==0) return new PhoneTabFragment();
        else if (index==1) return new GalleryFragment();
        else return new GameFragment();
    }

    @Override
    public int getItemCount() {
        return 2000;
    }
    public int getRealPosition(int position) {
        return position % mCount;
    }
}
