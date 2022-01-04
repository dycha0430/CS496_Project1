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
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
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
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.project1.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    public static ArrayList<Bitmap> images;

    private static final int PERMISSION_REQUEST = 0;
    private static final int RESULT_LOAD_IMAGE = 1;

    Uri addImageUri;
    protected Context context;
    protected ImageAdapter imageAdapter;
    Button button;
    Button cmbutton;
    private ImageView addImageView;
    JSONObject jsonObject;

    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    /*
     * Bitmap을 String형으로 변환
     * */
    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }

    private void getImages() {
        images = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(getActivity().getFilesDir()+"gallery_new.json"));
            String readStr = "";
            String str = null;
            while(true){
                if (!((str=br.readLine())!=null)) break;
                readStr+=str+"@";
                //[B@8ddc290
                JSONObject jsonobject2 = new JSONObject(str);
                String tmpBitmapString = jsonobject2.getString("bitmap");
                Bitmap bitmap = StringToBitmap(tmpBitmapString);
                if (!images.contains(bitmap)) {
                    images.add(bitmap);
                }
            }
            br.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        images = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(getActivity().getFilesDir()+"gallery_new.json"));
            String readStr = "";
            String str = null;
            while(true){
                if (!((str=br.readLine())!=null)) break;
                readStr+=str+"@";
                //[B@8ddc290
                JSONObject jsonobject2 = new JSONObject(str);
                String tmpBitmapString = jsonobject2.getString("bitmap");
                Bitmap bitmap = StringToBitmap(tmpBitmapString);
                if (!images.contains(bitmap)) {
                    images.add(bitmap);
                }
            }
            br.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        images = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(getActivity().getFilesDir() + "gallery2.json"));
            String readStr = "";
            String str = null;
            while (true) {
                if (!((str = br.readLine()) != null)) break;
                readStr += str + "\n";
                JSONObject jsonobject2 = new JSONObject(str);
                String tmpBitmapString = jsonobject2.getString("bitmap");
                Bitmap bitmap = StringToBitmap(tmpBitmapString);
                if (!images.contains(bitmap)) {
                    images.add(bitmap);
                }
            }
            br.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        imageAdapter.images = images;
        imageAdapter.notifyDataSetChanged();
    }





    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getOrientationOfImage(Uri uri) {
        ExifInterface exif = null;

        try {
            InputStream in = getActivity().getContentResolver().openInputStream(uri);
            exif = new ExifInterface(in);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

        if (orientation != -1) {
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }
        }

        return 0;
    }

    public Bitmap getRotatedBitmap(Bitmap bitmap, int degrees) throws Exception {
        if(bitmap == null) return null;
        if (degrees == 0) return bitmap;

        Matrix m = new Matrix();
        m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gallery, container, false);
        context = container.getContext();
        GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        imageAdapter = new ImageAdapter(context, images);
        gridView.setAdapter(imageAdapter);

        imageAdapter.images=images;
        imageAdapter.notifyDataSetChanged();


        ActivityResultLauncher<Intent> getImageActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            addImageUri = data.getData();

                            int orient = getOrientationOfImage(addImageUri);
                            Bitmap myBitmap = null;
                            try {
                                myBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), addImageUri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            Bitmap newBitmap = null;
                            try {
                                newBitmap = getRotatedBitmap(myBitmap, orient);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            /*
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            newBitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                            byte[] byteArray = stream.toByteArray();
                            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                             */
                            Log.d("ADD BITMAP", myBitmap + "");
                            addImageView = new ImageView(context);
                            addImageView.setImageURI(addImageUri);

                            //if (!images.contains(compressedBitmap)) images.add(compressedBitmap);
                            if (!images.contains(myBitmap)) images.add(myBitmap);
                            else {
                                Toast myToast = Toast.makeText(context, "이미 갤러리에 추가된 사진입니다.", Toast.LENGTH_SHORT);
                                myToast.show();
                                return;
                            }

                            imageAdapter.setImages(images);
                            imageAdapter.notifyDataSetChanged();
                            jsonObject = new JSONObject();
                            try {
                                Log.d("ADD BITMAP^^^^^^^", BitmapToString(myBitmap));
                                jsonObject.put("bitmap", BitmapToString(myBitmap));
                                String jsonString = jsonObject.toString() + "@";
                                Log.d("JSON STRING&&&&&&&&&&&", jsonString);
                                BufferedWriter bw = new BufferedWriter(new FileWriter(context.getFilesDir() + "gallery_new.json", true));
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
                Intent intent = new Intent(getActivity().getApplicationContext(), FullImageActivity.class);
                intent.putExtra("id", position);

                startActivity(intent);
            }
        });

        return rootView;
    }
}