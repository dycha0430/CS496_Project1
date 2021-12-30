package com.example.project1.ui.phone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;

import java.io.File;
import java.util.ArrayList;

public class PhoneTabAdapter extends RecyclerView.Adapter<PhoneTabAdapter.ViewHolder>{
    private Context context;
    private ArrayList<ContactData> contactList;
    public PhoneTabAdapter(Context context, ArrayList<ContactData> contactList) {
        this.context = context;
        this.contactList = contactList;
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
        final ContactData contactData = contactList.get(position);

        holder.nameTextView.setText(contactData.getName());
        holder.numTextView.setText(contactData.getPhoneNum());

        if (contactData.getProfileRes() != null) {
            Log.d("Hello", "Dayun" + contactData.getProfileRes());
            holder.profileImage.setImageURI(Uri.parse(contactData.getProfileRes()));
        } else {
            //TODO
            int resourceId = R.drawable.user;
            holder.profileImage.setImageResource(resourceId);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                dialog.setMessage("연락처를 삭제하시겠습니까?");
                dialog.setCancelable(true);
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeItemView(position);
                    }
                });
                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                dialog.show();
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactData.getPhoneNum()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    private void removeItemView(int position) {
        Long contactId = contactList.get(position).getContactId();
        contactList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, contactList.size());

        context.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts.CONTACT_ID + "=" + contactId, null);
    }


    public void setContactList(ArrayList<ContactData> contactList) {
        this.contactList = contactList;
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
