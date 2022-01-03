package com.example.project1.ui.game;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.R;
import com.example.project1.databinding.ActivityHelpBinding;

import java.util.Timer;
import java.util.TimerTask;

public class HelpActivity extends AppCompatActivity {
    private ActivityHelpBinding binding;
    Timer timer;
    Timer timer2;
    ImageView peachExample;
    ImageView peachExample2;
    int firstExampleCycle;
    int secondExampleCycle;
    private final Handler handler;

    public HelpActivity() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (secondExampleCycle) {
                    case 0:
                        peachExample2.setImageResource(R.drawable.peaches_1_7);
                        secondExampleCycle = 1;
                        peachExample.setImageResource(R.drawable.peaches_1_1);
//                        firstExampleCycle = 1;
                        break;
                    case 1:
                        peachExample2.setImageResource(R.drawable.peaches_1_8);
                        secondExampleCycle = 2;
                        peachExample.setImageResource(R.drawable.peaches_1_2);
//                        firstExampleCycle = 2;
                        break;
                    case 2:
                        peachExample2.setImageResource(R.drawable.peaches_1_9);
                        secondExampleCycle = 3;
                        peachExample.setImageResource(R.drawable.peaches_1_3);
//                        firstExampleCycle = 3;
                        break;
                    case 3:
                        peachExample2.setImageResource(R.drawable.peaches_100);
                        secondExampleCycle = 4;
                        peachExample.setImageResource(R.drawable.peaches_1_4);
//                        firstExampleCycle = 4;
                        break;
                    case 4:
                        peachExample2.setImageResource(R.drawable.peaches_1_7);
                        secondExampleCycle = 5;
                        peachExample.setImageResource(R.drawable.peaches_1_4);
//                        firstExampleCycle = 5;
                        break;
                    case 5:
                        peachExample2.setImageResource(R.drawable.peaches_1_8);
                        secondExampleCycle = 6;
                        peachExample.setImageResource(R.drawable.peaches_1_5);
//                        firstExampleCycle = 7;
                        break;
                    case 6:
                        peachExample2.setImageResource(R.drawable.peaches_1_9);
                        secondExampleCycle = 7;
                        peachExample.setImageResource(R.drawable.peaches_1_6);
//                        firstExampleCycle = 8;
                        break;
                    case 7:
                        peachExample2.setImageResource(R.drawable.peaches_100);
                        secondExampleCycle = 8;
                        peachExample.setImageResource(R.drawable.peaches_1_7);
//                        firstExampleCycle = 9;
                        break;
                    case 8:
                        peachExample2.setImageResource(R.drawable.peaches_100);
                        secondExampleCycle = 0;
                        peachExample.setImageResource(R.drawable.peaches_1_7);
//                        firstExampleCycle = 0;
                        break;
                }
            }
        };
    }

//    void startTimer() {
//        timer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                switch (firstExampleCycle) {
//                    case 0:
//                        peachExample.setImageResource(R.drawable.peaches_1_1);
//                        firstExampleCycle = 1;
//                        break;
//                    case 1:
//                        peachExample.setImageResource(R.drawable.peaches_1_2);
//                        firstExampleCycle = 2;
//                        break;
//                    case 2:
//                        peachExample.setImageResource(R.drawable.peaches_1_3);
//                        firstExampleCycle = 3;
//                        break;
//                    case 3:
//                        peachExample.setImageResource(R.drawable.peaches_1_4);
//                        firstExampleCycle = 4;
//                        break;
//                    case 4:
//                        peachExample.setImageResource(R.drawable.peaches_1_4);
//                        firstExampleCycle = 5;
//                    case 5:
//                        peachExample.setImageResource(R.drawable.peaches_1_4);
//                        firstExampleCycle = 6;
//                        break;
//                    case 6:
//                        peachExample.setImageResource(R.drawable.peaches_1_5);
//                        firstExampleCycle = 7;
//                        break;
//                    case 7:
//                        peachExample.setImageResource(R.drawable.peaches_1_6);
//                        firstExampleCycle = 8;
//                        break;
//                    case 8:
//                        peachExample.setImageResource(R.drawable.peaches_1_7);
//                        firstExampleCycle = 9;
//                        break;
//                    case 9:
//                        peachExample.setImageResource(R.drawable.peaches_1_7);
//                        firstExampleCycle = 0;
//                        break;
//                }
//            }
//        };
//        timer.schedule(timerTask, 0, 800);
//    }

        void startTimer2() {
            timer2 = new Timer();
            TimerTask timerTask2 = new TimerTask() {
                @Override
                public void run() {
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                }
            };
            timer2.schedule(timerTask2, 0, 800);
        }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        peachExample = findViewById(R.id.helpimage);
        peachExample2 = findViewById(R.id.helpimage2);

//        firstExampleCycle = 0;
        secondExampleCycle = 0;

        peachExample.setImageResource(R.drawable.peaches_1_1);
        peachExample2.setImageResource(R.drawable.peaches_1_7);
//        startTimer();
        startTimer2();
    }
}
