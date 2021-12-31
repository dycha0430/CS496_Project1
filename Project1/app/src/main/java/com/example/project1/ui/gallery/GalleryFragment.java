package com.example.project1.ui.gallery;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.project1.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private ArrayList<Uri> images;

    private static final int PERMISSION_REQUEST = 0;
    private static final int RESULT_LOAD_IMAGE = 1;

    protected Context context;
    protected ImageAdapter imageAdapter;
    Button button;
    private Uri addImageUri;
    private ImageView addImageView;
    JSONObject jsonObject;



    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        images = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(getActivity().getFilesDir()+"gallery.json"));
            String readStr = "";
            String str = null;
            while(true){
                if (!((str=br.readLine())!=null)) break;
                readStr+=str+"\n";
                JSONObject jsonobject2 = new JSONObject(str);
                Uri imageUri = Uri.parse(jsonobject2.getString("uri"));
                images.add(imageUri);
            }
            br.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

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
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            addImageUri = data.getData();
                            addImageView = new ImageView(context);
                            addImageView.setImageURI(addImageUri);
                            images.add(addImageUri);
                            imageAdapter.setImages(images);
                            imageAdapter.notifyDataSetChanged();
                            jsonObject = new JSONObject();
                            try {
                                jsonObject.put("uri",addImageUri);
                                String jsonString = jsonObject.toString() + "\n";
                                BufferedWriter bw = new BufferedWriter(new FileWriter(context.getFilesDir() + "gallery.json", true));
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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(), FullImageActivity.class);
                i.putExtra("id", images.get(position).toString());
                startActivity(i);
            }
        });



        return rootView;
    }
}