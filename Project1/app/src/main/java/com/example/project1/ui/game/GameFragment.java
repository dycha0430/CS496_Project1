package com.example.project1.ui.game;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project1.R;

import java.util.Timer;
import java.util.TimerTask;

public class GameFragment extends Fragment {

    private final int GAME_WIDTH = 10;
    private final int GAME_HEIGHT = 12;
    ImageButton[][] peaches;
    Button gameBtn;
    ImageButton helpBtn;
    TextView timerTextView, scoreTextView;
    boolean playing;
    private final int TOTAL_TIME = 80;
    int leftTime = TOTAL_TIME;

    private Timer timer;
    private final Handler handler;

    public GameFragment() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                // Timer task
                leftTime -= 1;
                setTimerView(leftTime);
                if (leftTime == 0) {
                    stopTimer();
                }
            }
        };
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_game, container, false);

        gameBtn = (Button) rootView.findViewById(R.id.gameBtn);
        helpBtn = (ImageButton) rootView.findViewById(R.id.helpBtn);
        timerTextView = (TextView) rootView.findViewById(R.id.timerTextView);
        scoreTextView = (TextView) rootView.findViewById(R.id.scoreTextView);

        init(rootView);

        gameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playing) {
                    resetGame();
                } else {
                    startGame();
                }
            }
        });
        return rootView;
    }

    void startTimer() {
        timer = new Timer();
        gameBtn.setText("RESET");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                // TODO
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }
        };

        timer.schedule(timerTask, 0, 1000);
    }

    void setTimerView(int time) {
        int minute = time / 60;
        int second = time % 60;

        timerTextView.setText("남은 시간\n" + minute + " : "  + second);
    }

    void setScoreView(int score) {
        scoreTextView.setText("점수\n" + score);
    }

    void stopTimer() {
        timer.cancel();
    }

    void startGame() {
        playing = true;
        gameBtn.setText("RESET");
        startTimer();
    }

    void resetGame() {
        playing = false;
        gameBtn.setText("START");
        leftTime = TOTAL_TIME;
        setTimerView(TOTAL_TIME);
        setScoreView(0);
        stopTimer();
        initGame();
    }

    void helpBtnClicked() {

    }

    void init(ViewGroup rootView) {
        setTimerView(TOTAL_TIME);
        setScoreView(0);
        playing = false;
        peaches = new ImageButton[12][10];
        getPeaches(rootView);
        initGame();
    }

    void getPeaches(ViewGroup rootView) {
        for (int i = 0; i < GAME_HEIGHT; i++) {
            for (int j = 0; j < GAME_WIDTH; j++) {
                String peach_id_string = "peach" + (i+1) + "_" + (j+1);
                String packageName = getActivity().getPackageName();
                int peach_id = getActivity().getResources().getIdentifier(peach_id_string, "id", packageName);

                ImageButton peach = (ImageButton) rootView.findViewById(peach_id);

                peaches[i][j] = peach;
            }
        }
    }

    void initGame() {
        for (int i = 0; i < GAME_HEIGHT; i++) {
            for (int j = 0; j < GAME_WIDTH; j++) {
                int randomNum = (int)(Math.random() * 9 + 1);

                if (randomNum == 8 || randomNum == 9) {
                    if ((int)(Math.random() * 10) < 4) {
                        randomNum = (int)(Math.random() * 9 + 1);
                    }
                }

                String packageName = getActivity().getPackageName();
                String peach_image_id_string = "peach" + randomNum;
                int peach_image_id = getActivity().getResources().getIdentifier(peach_image_id_string, "drawable", packageName);

                peaches[i][j].setImageResource(peach_image_id);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}