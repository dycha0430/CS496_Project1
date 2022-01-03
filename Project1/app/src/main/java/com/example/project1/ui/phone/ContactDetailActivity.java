package com.example.project1.ui.phone;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.MainActivity;
import com.example.project1.R;
import com.example.project1.databinding.ActivityContactDetailBinding;
import com.example.project1.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ContactDetailActivity extends AppCompatActivity {

    private ActivityContactDetailBinding binding;
    EditText nameTv, phoneNumTv;
    ImageView profileImage;
    Button editContactBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nameTv = (EditText) findViewById(R.id.detailNameTextView);
        phoneNumTv = (EditText) findViewById(R.id.detailPhoneNumTextView);
        profileImage = (ImageView) findViewById(R.id.detailProfileImageView);
        editContactBtn = (Button) findViewById(R.id.editContactBtn);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phoneNum = intent.getStringExtra("phoneNum");
        String imageUriString = intent.getStringExtra("imageUri");

        Uri imageUri;
        if (imageUriString.equals("empty")) {
            profileImage.setImageResource(R.drawable.user);
        } else {
            imageUri = Uri.parse(imageUriString);
            profileImage.setImageURI(imageUri);
        }

        nameTv.setText(name);
        phoneNumTv.setText(phoneNum);

        editContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editContact();
                PhoneTabFragment phoneTabFragment = new PhoneTabFragment();
//                Intent intent = new Intent(this, phoneTabFragment.getActivity().getClass());

                Intent intent1 = new Intent(view.getContext(), MainActivity.class) ;
                startActivity(intent1);
//                getSupportFragmentManager().beginTransaction().replace(R.id.detailLinearLayout, phoneTabFragment).commit();
            }
        });
    }

    void editContact() {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        String editName = nameTv.getText().toString();
        String editPhoneNum = phoneNumTv.getText().toString();

        long contachId = -1;
//        contachId = getContactIdFromNameAndNumber()
        try {
            operations.add(
                    ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                            .build()
            );

            operations.add(
                    ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, editName)
                            .build()
            );

            operations.add(
                    ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, editPhoneNum)
                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                            .build()
            );


/*
            Bitmap bitmap = null;

            if (profileImageUri == null) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.user);
            } else {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.getContentResolver(), profileImageUri));
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), profileImageUri);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Bitmap to Byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byteArray = stream.toByteArray();
            bitmap.recycle();



            operations.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, byteArray)
                            .build()
            );
 */

            getContentResolver().applyBatch(ContactsContract.AUTHORITY, operations);
            operations.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
