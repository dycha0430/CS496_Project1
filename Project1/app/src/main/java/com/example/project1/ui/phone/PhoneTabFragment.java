package com.example.project1.ui.phone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.ScriptGroup;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;

import java.util.ArrayList;

public class PhoneTabFragment extends Fragment {

    RecyclerView recyclerView;
    PhoneTabAdapter phoneTabAdapter;
    Button addPhoneNumBtn;
    private ArrayList<ContactData> contactData;
    private Uri profileImageUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_phone, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.phoneList);
        addPhoneNumBtn = (Button) rootView.findViewById(R.id.addPhoneNumBtn);

        contactData = getContacts(getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        phoneTabAdapter = new PhoneTabAdapter(getActivity(), contactData);

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
                        // 이거 addContact 할때 써야함.. TODO
                        Log.d("GOOOOOD", "HANYANG" + profileImageUri.toString());
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

        final EditText numInput = new EditText(getActivity());
        numInput.setInputType(InputType.TYPE_CLASS_PHONE);
        numInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        int maxLength = 13;
        numInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        numInput.setHint("전화번호를 입력하세요");

        LinearLayout linearLayout1 = new LinearLayout(getActivity());
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);

        final Button imageBtn = new Button(getActivity());
        imageBtn.setText("프로필 이미지");
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                profileImageUri = null;
                getImageActivityResultLauncher.launch(intent);
            }
        });

        final ImageView imageView = new ImageView(getActivity());

        imageView.setMaxWidth(30);
        linearLayout1.addView(imageBtn);
        linearLayout1.addView(imageView);

        linearLayout.addView(nameInput);
        linearLayout.addView(numInput);
        linearLayout.addView(linearLayout1);
        alert.setView(linearLayout);

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = nameInput.getText().toString();
                String phoneNum = numInput.getText().toString();

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

        alert.show();
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


            // TODO 이게 맞나??? 아닌듯

/*
            operations.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Phone.PHOTO_URI, profileImageUri.toString())
                            .build()
            );
*/

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