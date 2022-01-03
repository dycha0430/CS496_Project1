package com.example.project1.ui.phone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    Uri profileImageUri;
    ImageView profileImage;
    Button editContactBtn, removeProfileBtn;

    ActivityResultLauncher<Intent> getImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        profileImageUri = data.getData();
                        profileImage.setImageURI(profileImageUri);
                    }
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nameTv = (EditText) findViewById(R.id.detailNameTextView);
        phoneNumTv = (EditText) findViewById(R.id.detailPhoneNumTextView);
        profileImage = (ImageView) findViewById(R.id.detailProfileImageView);
        editContactBtn = (Button) findViewById(R.id.editContactBtn);
        removeProfileBtn = (Button) findViewById(R.id.removeProfileBtn);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phoneNum = intent.getStringExtra("phoneNum");
        Long contactId = intent.getLongExtra("contactId", 0);
        String imageUriString = intent.getStringExtra("imageUri");

        Uri imageUri;
        if (imageUriString.equals("empty")) {
            profileImage.setImageResource(R.drawable.user);
            profileImageUri = null;
        } else {
            imageUri = Uri.parse(imageUriString);
            profileImage.setImageURI(imageUri);
            profileImageUri = imageUri;
        }

        nameTv.setText(name);
        phoneNumTv.setText(phoneNum);
        phoneNumTv.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        editContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editContact(contactId);

                Intent intent1 = new Intent(view.getContext(), MainActivity.class) ;
                startActivity(intent1);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                getImageActivityResultLauncher.launch(intent);
            }
        });

        removeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileImageUri = null;
                profileImage.setImageResource(R.drawable.user);
            }
        });
    }

    void editContact(long contactId) {
        getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts.CONTACT_ID + "=" + contactId, null);
        addContacts(nameTv.getText().toString(), phoneNumTv.getText().toString(), profileImageUri);
    }

    void addContacts(String name, String phoneNum, Uri profileImageUri) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        try {
            operations.add(
                    ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                            .build()
            );

            operations.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                            .build()
            );

            operations.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNum)
                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                            .build()
            );



            Bitmap bitmap = null;

            if (profileImageUri == null) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user);
            } else {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), profileImageUri));
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), profileImageUri);
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


            getContentResolver().applyBatch(ContactsContract.AUTHORITY, operations);
            operations.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
