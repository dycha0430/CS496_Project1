package com.example.project1.ui.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class SlideImageAdapter  extends RecyclerView.Adapter<ViewHolderPage>{

    private ArrayList<String> photos;
    SlideImageAdapter(ArrayList<String> list) {
            this.photos = list;
    }

    @NonNull
    @Override
    public ViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.photo_viewpager, parent, false);
        return new ViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPage holder, int position) {
        if (holder instanceof ViewHolderPage) {
            ViewHolderPage viewHolder = (ViewHolderPage) holder;
            viewHolder.onBind(photos.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
