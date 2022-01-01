package com.example.project1.ui.game;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.R;

import java.util.Objects;

public class CustomDialog extends Dialog {

    private Context mContext;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_dialog);

        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.

        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView messageTextView = findViewById(R.id.messageTextView);
        Button positiveButton = findViewById(R.id.positiveButton);

        titleTextView.setText("Score");
        messageTextView.setText(score + "");
        positiveButton.setText("OK");

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
    }

    public CustomDialog(Context mContext, int score) {
        super(mContext);
        this.mContext = mContext;
        this.score = score;
    }
}