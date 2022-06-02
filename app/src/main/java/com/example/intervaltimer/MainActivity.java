package com.example.intervaltimer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    private int setsScore = 1;
    private int workSeconds = 0;
    private int workMinutes = 0;
    private int restSeconds = 0;
    private int restMinutes = 0;
    private boolean isRestTimerCompleted = true;
    private TextView txtTimeCountDown;
    private int workSecondsCopy;
    private int workMinutesCopy;
    private int restSecondsCopy;
    private int restMinutesCopy;
    private TextView txtSetsLeft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnSetsPlus = findViewById(R.id.setsPlus);
        Button btnSetsMinus = findViewById(R.id.setsMinus);
        Button btnWorkPlus = findViewById(R.id.workPlus);
        Button btnWorkMinus = findViewById(R.id.workMinus);
        Button btnRestPlus = findViewById(R.id.restPlus);
        Button btnRestMinus = findViewById(R.id.restMinus);
        Button btnStartTimer = findViewById(R.id.startTimer);
        TextView txtSetsCount = findViewById(R.id.setsCount);
        TextView txtWorkTime = findViewById(R.id.workTime);
        TextView txtRestTime = findViewById(R.id.restTime);
        txtTimeCountDown = findViewById(R.id.timeCountDown);
        txtSetsLeft = findViewById(R.id.setsLeft);



        btnSetsPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setsScore++;
                txtSetsCount.setText("" + setsScore);
            }
        });

        btnSetsMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setsScore<1){
                    Toast.makeText(MainActivity.this, "You can't have less than 1 set", Toast.LENGTH_SHORT).show();
                    setsScore = 1;
                }else{
                    txtSetsCount.setText("" + setsScore);
                    setsScore--;
                }
            }
        });

        btnWorkPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeFormated = String.format("%02d:%02d", workMinutes, workSeconds);
                txtWorkTime.setText(timeFormated);
                workSeconds++;
                if(workSeconds >= 60){
                    workMinutes++;
                    workSeconds = 0;
                }
            }
        });

        btnWorkMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(workSeconds > 0){
                    workSeconds--;
                }else if(workMinutes > 0){
                    workMinutes--;
                    workSeconds = 59;
                }else{
                    Toast.makeText(MainActivity.this, "You can't have less than 0 seconds", Toast.LENGTH_SHORT).show();
                }
                String timeFormated = String.format("%02d:%02d", workMinutes, workSeconds);
                txtWorkTime.setText(timeFormated);
            }
        });

        btnRestPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeFormated = String.format("%02d:%02d", restMinutes, restSeconds);
                txtRestTime.setText(timeFormated);
                restSeconds++;
                if(restSeconds >= 60){
                    restMinutes++;
                    restSeconds = 0;
                }
            }
        });

        btnRestMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(restSeconds > 0){
                    restSeconds--;
                }else if(restMinutes > 0){
                    restMinutes--;
                    restSeconds = 59;
                }else{
                    Toast.makeText(MainActivity.this, "You can't have less than 0 seconds", Toast.LENGTH_SHORT).show();
                }
                String timeFormated = String.format("%02d:%02d", restMinutes, restSeconds);
                txtRestTime.setText(timeFormated);
            }
        });


        btnStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runThreadWork();
            }
        });

    }

    private void runThreadRest() {
        if(isRestTimerCompleted == false) {
            restSecondsCopy = restSeconds;
            restMinutesCopy = restMinutes;
            while (restMinutesCopy > 0 || restSecondsCopy > 0) {
                while (restSecondsCopy > 0) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                restSecondsCopy--;
                                String timeFormatedCountDownRest = String.format("Rest Time: " + "%02d:%02d", restMinutesCopy, restSecondsCopy);
                                txtTimeCountDown.setText(timeFormatedCountDownRest);
                                if (restSecondsCopy == 0 && restMinutesCopy > 0) {
                                    restMinutesCopy--;
                                    restSecondsCopy = 60;
                                }
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(restSecondsCopy == 0 && restMinutesCopy == 0 && setsScore > 1){
                setsScore--;
                isRestTimerCompleted = true;
                runThreadWork();
            }
        }
    }

    private void runThreadWork() {
        new Thread() {
            public void run() {
                if (isRestTimerCompleted) {
                    workSecondsCopy = workSeconds;
                    workMinutesCopy = workMinutes;
                    while (workMinutesCopy > 0 || workSecondsCopy > 0) {
                        while (workSecondsCopy > 0) {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int setsScoreFixed = setsScore - 1;
                                        // setu skaita atnemsana
                                        txtSetsLeft.setText("Sets left: " + setsScoreFixed);
                                        //
                                        workSecondsCopy--;
                                        String timeFormatedCountDownWork = String.format("Work time: " + "%02d:%02d", workMinutesCopy, workSecondsCopy);
                                        txtTimeCountDown.setText(timeFormatedCountDownWork);
                                        if (workSecondsCopy == 0 && workMinutesCopy > 0) {
                                            workMinutesCopy--;
                                            workSecondsCopy = 60;
                                        }
                                    }
                                });
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (workMinutesCopy == 0 && workSecondsCopy == 0) {
                            isRestTimerCompleted = false;
                            runThreadRest();
                        }
                    }
                }
            }
        }.start();
    }
}

