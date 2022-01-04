package com.example.project1.ui.gallery;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.project1.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private ArrayList<String> images;

    private static final int PERMISSION_REQUEST = 0;
    private static final int RESULT_LOAD_IMAGE = 1;

    Uri addImageUri;
    protected Context context;
    protected ImageAdapter imageAdapter;
    Button button;
    Button cmbutton;
    private ImageView addImageView;
    JSONObject jsonObject;

    //Uri -> Path(파일경로)
    private String uri2path(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }
        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = { MediaStore.Files.FileColumns.DATA };
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) { return cursor.getString(columnIndex); }
        } finally {
            cursor.close();
        }
        return null;
    }




    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        images = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(getActivity().getFilesDir()+"gallery2.json"));
            String readStr = "";
            String str = null;

            while(true){
                if (!((str=br.readLine())!=null)) break;
                readStr+=str+"\n";
                JSONObject jsonobject2 = new JSONObject(str);
                String imagePath = jsonobject2.getString("uri");
                File file = new File(imagePath);
                if (file.exists() && !images.contains(imagePath)) {
                    images.add(imagePath);
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        images = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(getActivity().getFilesDir()+"gallery2.json"));
            String readStr = "";
            String str = null;
            while(true){
                if (!((str=br.readLine())!=null)) break;
                readStr+=str+"\n";
                JSONObject jsonobject2 = new JSONObject(str);
                String imagePath = jsonobject2.getString("uri");
                File file = new File(imagePath);
                if (file.exists() && !images.contains(imagePath)) {
                    images.add(imagePath);
                    Log.d("@222loadimage222", imagePath);
                }
            }
            br.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        imageAdapter.images=images;
        imageAdapter.notifyDataSetChanged();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gallery, container, false);
        context = container.getContext();
        GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        imageAdapter = new ImageAdapter(context, images);
        gridView.setAdapter(imageAdapter);


        ActivityResultLauncher<Intent> getImageActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            addImageUri = data.getData();
                            addImageView = new ImageView(context);
                            addImageView.setImageURI(addImageUri);
                            String x = uri2path(addImageUri);
                            if (!images.contains(x)) images.add(x);
                            else {
                                Toast myToast = Toast.makeText(context, "The image is already in the gallery!", Toast.LENGTH_SHORT);
                                myToast.show();
                                return;
                            }
                            imageAdapter.setImages(images);
                            imageAdapter.notifyDataSetChanged();
                            jsonObject = new JSONObject();
                            try {
                                jsonObject.put("uri",x);
                                String jsonString = jsonObject.toString() + "\n";
                                BufferedWriter bw = new BufferedWriter(new FileWriter(context.getFilesDir() + "gallery2.json", true));
                                bw.write(jsonString);
                                bw.close();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        button = (Button) rootView.findViewById(R.id.addImageBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                addImageUri = null;
                getImageActivityResultLauncher.launch(intent);
            }
        });

//        cmbutton = (Button) rootView.findViewById(R.id.cameraBtn);
//        cmbutton.setOnClickListener(new View. OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//               startActivity(takePictureIntent);
//
//           }
//        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(), FullImageActivity.class);
                i.putExtra("id", position);
                i.putStringArrayListExtra("id2", images);
                startActivity(i);
            }
        });

        return rootView;
    }
}