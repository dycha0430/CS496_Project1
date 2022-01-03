package com.example.project1.ui.phone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PhoneTabAdapter extends RecyclerView.Adapter<PhoneTabAdapter.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<ContactData> contactList;
    private ArrayList<ContactData> filteredContactList;
    public PhoneTabAdapter(Context context, ArrayList<ContactData> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.filteredContactList = contactList;
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
        final ContactData contactData = filteredContactList.get(position);

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

        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactData.getPhoneNum()));
                context.startActivity(intent);
            }
        });

        holder.messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.putExtra("sms_body", "");
                intent.setData(Uri.parse("smsto:" + contactData.getPhoneNum()));
                context.startActivity(intent);
            }
        });

        // Long click으로 삭제
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
                Intent intent = new Intent(view.getContext(), ContactDetailActivity.class);
                intent.putExtra("name", contactData.getName());
                intent.putExtra("phoneNum", contactData.getPhoneNum());
                intent.putExtra("contactId", contactData.getContactId());
                if (contactData.getProfileRes() != null) {
                    intent.putExtra("imageUri", contactData.getProfileRes().toString());
                } else {
                    intent.putExtra("imageUri", "empty");
                }

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredContactList.size();
    }

    private void removeItemView(int position) {
        Long contactId = filteredContactList.get(position).getContactId();
        filteredContactList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, filteredContactList.size());

        context.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts.CONTACT_ID + "=" + contactId, null);
    }


    public void setContactList(ArrayList<ContactData> contactList) {
        this.contactList = contactList;
        this.filteredContactList = contactList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredContactList = contactList;
                } else {
                    ArrayList<ContactData> filteringList = new ArrayList<>();
                    for (ContactData data : contactList) {
                        if (data.getName().contains(charString)) {
                            filteringList.add(data);
                        }
                    }
                    filteredContactList = filteringList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredContactList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredContactList = (ArrayList<ContactData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView nameTextView, numTextView;
        ImageButton callBtn, messageBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            profileImage = (ImageView)itemView.findViewById(R.id.imageView);
            nameTextView = (TextView)itemView.findViewById(R.id.nameTextView);
            numTextView = (TextView)itemView.findViewById(R.id.numTextView);
            callBtn = (ImageButton) itemView.findViewById(R.id.callBtn);
            messageBtn = (ImageButton) itemView.findViewById(R.id.messageBtn);
        }
    }


}
