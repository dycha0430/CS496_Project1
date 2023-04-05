package com.example.project1.ui.game;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.project1.MainActivity;
import com.example.project1.R;
import com.example.project1.ui.gallery.GalleryFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class CustomDialog extends Dialog {

    private Context mContext;
    Activity context;
    private int score;
    private static final String CAPTURE_PATH = "CAPTURE_DIR";
    View currentView;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_dialog);

        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        currentView = null;
        currentView = getWindow().getDecorView();

        Log.d("DDDDDDDDD@@@@",  currentView + "");
        // 커스텀 다이얼로그의 각 위젯들을 정의한다.

        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView messageTextView = findViewById(R.id.messageTextView);
        Button positiveButton = findViewById(R.id.positiveButton);
        Button saveButton = findViewById(R.id.saveButton);

        titleTextView.setText("Score");
        messageTextView.setText(score + "");

        // 버튼 리스너 설정
        positiveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시
                // ...코드..
                // Custom Dialog 종료
                dismiss();
            }
        });
        Context thisContext = this.getContext();

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //captureActivity(context);
                // TODO 캡쳐할 뷰를 인자로 넣어줘야 함
                captureView(currentView);
                Intent intent = new Intent(thisContext, MainActivity.class);
                thisContext.startActivity(intent);
            }
        });
    }

    public void captureView(View view) {
        view.buildDrawingCache();
        Bitmap captureView = view.getDrawingCache();

        String path = "/storage/emulated/0/DCIM/Screenshots";
        String filename = "peachGame" + System.currentTimeMillis() + ".jpg";
        File file = new File(path, filename);

        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            captureView.compress(Bitmap.CompressFormat.JPEG, 10, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GalleryFragment gf = new GalleryFragment();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("uri", path + '/' + filename);
                    String jsonString = jsonObject.toString() + "\n";
                    BufferedWriter bw = new BufferedWriter(new FileWriter(context.getFilesDir() + "gallery2.json", true));
                    bw.write(jsonString);
                    bw.close();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 5000);
    }

    public static void captureActivity(Activity context) {
        if(context == null) return;
        View root = context.getWindow().getDecorView().getRootView();
        root.setDrawingCacheEnabled(true);
        root.buildDrawingCache();
// 루트뷰의 캐시를 가져옴
        Bitmap screenshot = root.getDrawingCache();

// get view coordinates
        int[] location = new int[2];
        root.getLocationInWindow(location);

// 이미지를 자를 수 있으나 전체 화면을 캡쳐 하도록 함
        Bitmap bmp = Bitmap.createBitmap(screenshot, location[0], location[1], root.getWidth(), root.getHeight(), null, false);

        MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, "Peach Game Result", "복숭아 게임 결과 캡쳐");
    }

    public CustomDialog(Context mContext, int score, Activity context) {
        super(mContext);
        this.mContext = mContext;
        this.score = score;
        this.context = context;

    }
}
