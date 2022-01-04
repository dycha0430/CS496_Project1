package com.example.project1.ui.game;

import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.example.project1.MainActivity;
import com.example.project1.R;
import com.example.project1.ui.phone.ContactDetailActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class GameFragment extends Fragment {

    private final int GAME_WIDTH = 10;
    private final int GAME_HEIGHT = 12;
    Button gameBtn;
    ImageButton helpBtn;
    TextView timerTextView, scoreTextView;
    boolean playing;
    int score;
    TableLayout gameTable;
    ImageView readyImageView;

    ViewGroup rootView;

    /* Timer */
    private final int TOTAL_TIME = 10;
    int leftTime = TOTAL_TIME;
    private Timer timer;
    private final Handler handler;
    Handler handler2 = new Handler();

    /* Game board item */
    class PeachItem {
        private int num;
        private ImageButton peach;
        private boolean selected;
        private boolean removed;

        public PeachItem(ImageButton peach) {
            this.peach = peach;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public ImageButton getPeach() {
            return peach;
        }

        public boolean getSelected() {
            return selected;
        }

        public boolean isRemoved() {
            return removed;
        }

        public void setRemoved(boolean removed) {
            this.removed = removed;
        }
    }
    PeachItem[][] peaches;

    /* Click peach */
    class PeachLoc {
        int row;
        int col;

        public PeachLoc(int row, int col) {
            this.row = row;
            this.col = col;
        }
    };

    private PeachLoc[] clickedPeaches = new PeachLoc[2];
    private int clickedPeachNum;



    public GameFragment() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                // Timer task
                leftTime -= 1;
                setTimerView(leftTime);
                if (leftTime == 0) {
                    // TODO 점수 띄워주기
                    CustomDialog customDialog = new CustomDialog(getActivity(), score, getActivity());
                    customDialog.setCancelable(false);


                    customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    WindowManager.LayoutParams params = customDialog.getWindow().getAttributes();
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    customDialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
                    customDialog.show();
                    resetGame();
                }
            }
        };
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_game, container, false);

        gameBtn = (Button) rootView.findViewById(R.id.gameBtn);
        helpBtn = (ImageButton) rootView.findViewById(R.id.helpBtn);
        timerTextView = (TextView) rootView.findViewById(R.id.timerTextView);
        scoreTextView = (TextView) rootView.findViewById(R.id.scoreTextView);
        gameTable = (TableLayout) rootView.findViewById(R.id.gameTable);
        readyImageView = (ImageView) rootView.findViewById(R.id.readyImageView);
        final LinearLayout gameBar = (LinearLayout) rootView.findViewById(R.id.gameBar);

        gameBar.post(new Runnable() {
            @Override
            public void run() {
                adjustGameMapSize(gameBar.getHeight());
            }
        });

        init();

        gameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playing) {
                    resetGame();
                } else {
                    readyImageView.setImageResource(R.drawable.peach3);
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            readyImageView.setImageResource(R.drawable.peach2);
                            handler2.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    readyImageView.setImageResource(R.drawable.peach1);
                                    handler2.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            readyImageView.setImageResource(R.drawable.start_peach);
                                            handler2.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    startGame();
                                                }
                                            }, 500);
                                        }
                                    }, 1000);
                                }
                            }, 1000);
                        }
                    }, 1000);
                }
            }
        });
        return rootView;
    }

    void adjustGameMapSize(int gameBarHeight) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float width = size.x;

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int dpi = dm.densityDpi;
        float density = dm.density;

        int status_bar_size = 0;
        int resourceId;
        resourceId = getActivity().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            status_bar_size = (int) getActivity().getResources().getDimension(resourceId);
        }

        BottomNavigationView bnv = getActivity().findViewById(R.id.nav_view);
        int bottom_bar_size = bnv.getHeight();

        int other_heights = Math.round((float) density * (10)) + bottom_bar_size * 2 + gameBarHeight + status_bar_size;
        float height = size.y - other_heights;

        int width_padding = Math.round((float) density * 10);
        width -= width_padding;

        if (height / width < 1.2) {
            // hi
            ViewGroup.LayoutParams layoutParams = (FrameLayout.LayoutParams) gameTable.getLayoutParams();
//            TableLayout.LayoutParams layoutParams = (TableLayout.LayoutParams) gameTable.getLayoutParams();
            layoutParams.width = (int) ((double) height / 1.2);
            gameTable.setLayoutParams(layoutParams);
        }
    }


    void startTimer() {
        timer = new Timer();
        gameBtn.setText("RESET");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }
        };

        timer.schedule(timerTask, 0, 1000);
    }

    void setTimerView(int time) {
        int minute = time / 60;
        int second = time % 60;
        String minute_display = minute + "";
        String second_display = second + "" ;
        if (minute < 10) minute_display = "0" + minute_display;
        if (second < 10) second_display = "0" + second_display;

        timerTextView.setText("남은 시간\n" + minute_display + " : "  + second_display);
    }

    void setScoreView(int score) {
        scoreTextView.setText("점수\n" + score);
    }

    void stopTimer() {
        timer.cancel();
    }

    void startGame() {
        for (int i = 0; i < GAME_HEIGHT; i++) {
            for (int j = 0; j < GAME_WIDTH; j++) {
                peaches[i][j].getPeach().setEnabled(true);
            }
        }

        readyImageView.setVisibility(View.INVISIBLE);
        gameTable.setVisibility(View.VISIBLE);

        playing = true;
        gameBtn.setText("RESET");
        startTimer();
    }

    void initVars() {
        peaches = new PeachItem[12][10];
        getPeaches(rootView);

        score = 0;
        clickedPeachNum = 0;
        clickedPeaches[0] = null;
        clickedPeaches[1] = null;
        playing = false;
        gameBtn.setText("START");
        leftTime = TOTAL_TIME;
        setTimerView(TOTAL_TIME);
        setScoreView(0);

        readyImageView.setVisibility(View.VISIBLE);
        gameTable.setVisibility(View.INVISIBLE);
        readyImageView.setImageResource(R.drawable.ready_peach);

        initGame();
    }

    void resetGame() {
        peaches = new PeachItem[12][10];
        getPeaches(rootView);
        stopTimer();
        initVars();
    }

    void helpBtnClicked() {
        Intent intent = new Intent(getActivity(), HelpActivity.class);

        startActivity(intent);
    }

    void init() {
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpBtnClicked();
            }
        });

        initVars();
        setPeachOnClickListener();
    }

    void getPeaches(ViewGroup rootView) {
        for (int i = 0; i < GAME_HEIGHT; i++) {
            for (int j = 0; j < GAME_WIDTH; j++) {
                String peach_id_string = "peach" + (i+1) + "_" + (j+1);
                String packageName = getActivity().getPackageName();
                int peach_id = getActivity().getResources().getIdentifier(peach_id_string, "id", packageName);

                ImageButton peach = (ImageButton) rootView.findViewById(peach_id);

                peaches[i][j] = new PeachItem(peach);
            }
        }
    }

    void setPeachOnClickListener() {
        for (int i = 0; i < GAME_HEIGHT; i++) {
            for (int j = 0; j < GAME_WIDTH; j++) {
                int finalI = i;
                int finalJ = j;
                peaches[i][j].getPeach().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        peachClicked(finalI, finalJ);
                    }
                });
            }
        }
    }

    void peachClicked(int row, int col) {
        PeachItem peach = peaches[row][col];
        if (peach.getSelected()) {
            clickedPeachNum = 0;
            peach.setSelected(false);
            setPeachImage(peach);

            return;
        }

        clickedPeaches[clickedPeachNum++] = new PeachLoc(row, col);
        if (clickedPeachNum == 1) {
            peach.setSelected(true);
            setPeachImage(peach);
        } else if (clickedPeachNum == 2) {
            PeachLoc selected1 = clickedPeaches[0];
            PeachLoc selected2 = clickedPeaches[1];

            int row_start = Math.min(selected1.row, selected2.row);
            int row_end = Math.max(selected1.row, selected2.row);
            int col_start = Math.min(selected1.col, selected2.col);
            int col_end = Math.max(selected1.col, selected2.col);

            int sum = 0;
            int num = 0;
            for (int i = row_start; i <= row_end; i++) {
                for (int j = col_start; j <= col_end; j++) {
                    if (!peaches[i][j].isRemoved()) {
                        peaches[i][j].setSelected(true);
                        peaches[i][j].getPeach().setEnabled(false);
                        setPeachImage(peaches[i][j]);
                        sum += peaches[i][j].getNum();
                        num++;
                    }
                }
            }

            // TODO sum handling
            if (sum == 10) {
                score += num;
                setScoreView(score);

                for (int i = row_start; i <= row_end; i++) {
                    for (int j = col_start; j <= col_end; j++) {
                        peaches[i][j].getPeach().setImageResource(R.drawable.ic_baseline_circle_24);
                        peaches[i][j].setRemoved(true);
                    }
                }
            }

            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = row_start; i <= row_end; i++) {
                        for (int j = col_start; j <= col_end; j++) {
                            if (!peaches[i][j].isRemoved()) {
                                peaches[i][j].getPeach().setEnabled(true);
                                peaches[i][j].setSelected(false);
                                setPeachImage(peaches[i][j]);
                            }                        }
                    }
                }
            }, 300);

            clickedPeachNum = 0;
        }
    }

    void setPeachImage(PeachItem peachItem) {
        String packageName = getActivity().getPackageName();

        String peach_image_id_string;
        if (peachItem.getSelected()) {
            // 진한 peach
            peach_image_id_string = "peach_a_" + peachItem.getNum();
        } else {
            // 연한 peach
            peach_image_id_string = "peach" + peachItem.getNum();
        }

        int peach_image_id = getActivity().getResources().getIdentifier(peach_image_id_string, "drawable", packageName);

        peachItem.getPeach().setImageResource(peach_image_id);
    }

    void initGame() {
        for (int i = 0; i < GAME_HEIGHT; i++) {
            for (int j = 0; j < GAME_WIDTH; j++) {
                int randomNum = (int)(Math.random() * 9 + 1);

                if (randomNum == 8 || randomNum == 9) {
                    randomNum = (int)(Math.random() * 9 + 1);
                }

                if (randomNum == 6 || randomNum == 7) {
                    if ((int)(Math.random() * 10) < 8) {
                        randomNum = (int)(Math.random() * 9 + 1);
                    }
                }

                peaches[i][j].setNum(randomNum);
                peaches[i][j].setSelected(false);
                setPeachImage(peaches[i][j]);
                peaches[i][j].getPeach().setEnabled(false);
            }
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}