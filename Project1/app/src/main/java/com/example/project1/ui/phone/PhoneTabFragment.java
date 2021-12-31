package com.example.project1.ui.phone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PhoneTabFragment extends Fragment {

    RecyclerView recyclerView;
    PhoneTabAdapter phoneTabAdapter;
    Button addPhoneNumBtn;
    private ArrayList<ContactData> contactData;
    private Uri profileImageUri;
    private ImageView profileImageView;
    private ItemTouchHelper itemTouchHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_phone, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.phoneList);
        addPhoneNumBtn = (Button) rootView.findViewById(R.id.addPhoneNumBtn);

        contactData = getContacts(getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        phoneTabAdapter = new PhoneTabAdapter(getActivity(), contactData);

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(phoneTabAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(phoneTabAdapter);

        addPhoneNumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBtnClicked(getActivity());
            }
        });


        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    ActivityResultLauncher<Intent> getImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        profileImageUri = data.getData();
                        profileImageView.setImageURI(profileImageUri);
                    }
                }
            });

    public void addBtnClicked(Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle("연락처 추가");
        alert.setMessage("추가할 연락처를 입력해주세요");

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(60, 0, 60, 0);

        final EditText nameInput = new EditText(getActivity());
        nameInput.setHint("이름을 입력하세요");
        nameInput.setPadding(0, 0, 0, 50);

        final EditText numInput = new EditText(getActivity());
        numInput.setInputType(InputType.TYPE_CLASS_PHONE);
        numInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        int maxLength = 13;
        numInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        numInput.setHint("전화번호를 입력하세요");
        numInput.setPadding(0, 0, 0, 50);

        LinearLayout linearLayout1 = new LinearLayout(getActivity());
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setGravity(Gravity.FILL_HORIZONTAL);
        linearLayout1.setWeightSum(3);
        linearLayout1.setPadding(0, 50, 0, 0);

        final Button imageBtn = new Button(getActivity());
        imageBtn.setText("프로필 이미지");
        imageBtn.setTextSize(18);
        imageBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.radius));

        imageBtn.setSingleLine(true);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                profileImageUri = null;
                getImageActivityResultLauncher.launch(intent);
            }
        });

        profileImageView = new ImageView(getActivity());
        profileImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        profileImageView.setAdjustViewBounds(true);
        profileImageView.setMaxWidth(150);
        profileImageView.setMaxHeight(150);
        profileImageView.setBackground(ContextCompat.getDrawable(context, R.drawable.profile));
        profileImageView.setClipToOutline(true);

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp1.weight = 2;
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1;
        imageBtn.setLayoutParams(lp1);
        profileImageView.setLayoutParams(lp2);

        int resourceId = R.drawable.user;
        profileImageView.setImageResource(resourceId);
        linearLayout1.addView(imageBtn);
        linearLayout1.addView(profileImageView);

        linearLayout.addView(nameInput);
        linearLayout.addView(numInput);
        linearLayout.addView(linearLayout1);
        alert.setView(linearLayout);

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = nameInput.getText().toString();
                String phoneNum = numInput.getText().toString();

                if (name == "" || phoneNum == "") {

                }
                addContacts(context, name, phoneNum);
                contactData = getContacts(context);

                phoneTabAdapter.setContactList(contactData);
                phoneTabAdapter.notifyDataSetChanged();
            }
        });

        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog tmpAlert = alert.show();

        // alertdialog에 폰트 적용 실패
//        TextView textView = (TextView) tmpAlert.findViewById(R.id.message);
//        Typeface face = Typeface.createFromAsset(context.getAssets(), "font/nanumfont.ttf");
//        textView.setTypeface(face);
    }

    public void addContacts(Context context, String name, String phoneNum) {
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
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.getContentResolver(), profileImageUri));
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), profileImageUri);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            /* Bitmap to Byte array */
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            bitmap.recycle();

            operations.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, byteArray)
                            .build()
            );


            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operations);
            operations.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ContactData> getContacts(Context context) {
        ArrayList<ContactData> datas = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        };

        Cursor cursor = resolver.query(phoneUri, projection, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int imageUriIndex = cursor.getColumnIndex(projection[0]);
                int nameIndex = cursor.getColumnIndex(projection[1]);
                int numIndex = cursor.getColumnIndex(projection[2]);
                int contactIdIndex = cursor.getColumnIndex(projection[3]);

                String imageUri = cursor.getString(imageUriIndex);
                String name = cursor.getString(nameIndex);
                String phoneNum = cursor.getString(numIndex);
                Long contactId = Long.parseLong(cursor.getString(contactIdIndex));

                ContactData data = new ContactData();

                data.setProfileRes(imageUri);
                data.setName(name);
                data.setPhoneNum(phoneNum);
                data.setContactId(contactId);

                Log.d("PhoneTabFragment", "Hello" + name + imageUri);
                datas.add(data);
            }
        }

        cursor.close();

        return datas;
    }
}