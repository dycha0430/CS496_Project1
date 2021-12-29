package com.example.project1.ui.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;

public class PhoneTabAdapter extends RecyclerView.Adapter<PhoneTabAdapter.ViewHolder>{
    private Context context;
    public PhoneTabAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PhoneTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_num_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneTabAdapter.ViewHolder holder, int position) {
        holder.nameTextView.setText("Name" + position);
        holder.numTextView.setText(("Num" + position));
        holder.profileImage.setImageDrawable(Drawable.createFromPath("@drawable/ic_launcher_foreground"));


    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView nameTextView, numTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            profileImage = (ImageView)itemView.findViewById(R.id.imageView);
            nameTextView = (TextView)itemView.findViewById(R.id.nameTextView);
            numTextView = (TextView)itemView.findViewById(R.id.numTextView);
        }
    }
}
