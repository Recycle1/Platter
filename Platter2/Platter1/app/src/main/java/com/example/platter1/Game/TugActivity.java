package com.example.platter1.Game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.platter1.Public.Public_data;
import com.example.platter1.Public.WebTool;
import com.example.platter1.R;

public class TugActivity extends AppCompatActivity {

    private TextView tv_time;
    private TextView tv_score1;
    private TextView tv_score2;
    private Button btn_tug;
    int score1;
    int score2;
    String s1_and_s2;

    String connect_code;
    private ProgressBar progressBar;

    boolean flag=true;

    int number;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    System.out.println(s1_and_s2);
                    score1=Integer.valueOf(s1_and_s2.split("-")[0]);
                    score2=Integer.valueOf(s1_and_s2.split("-")[1]);
                    if(number==1){
                        tv_score1.setText(s1_and_s2.split("-")[0]);
                        tv_score2.setText(s1_and_s2.split("-")[1]);
                        progressBar.setProgress(score1);
                    }
                    else if(number==2){
                        tv_score1.setText(s1_and_s2.split("-")[1]);
                        tv_score2.setText(s1_and_s2.split("-")[0]);
                        progressBar.setProgress(score2);
                    }
                    break;
                case 2:

                    break;
            }
            return false;
        }
    });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tug);

        tv_time=findViewById(R.id.tv_time);
        btn_tug=findViewById(R.id.btn_tug);
        tv_score1=findViewById(R.id.tv_score1);
        tv_score2=findViewById(R.id.tv_score2);
        progressBar=findViewById(R.id.progressBar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (flag){
                        s1_and_s2=WebTool.touchHtml(Public_data.url+"get_score.php?connect_code="+connect_code);
                        handler.sendEmptyMessage(1);
                        Thread.sleep(500);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        number=getIntent().getIntExtra("number",1);
        connect_code=getIntent().getStringExtra("connect_code");
        if(number==1){
            tv_score1.setTextColor(Color.parseColor("#8E9AE8"));
            tv_score2.setTextColor(Color.parseColor("#EC5C5C"));
            btn_tug.setBackground(getDrawable(R.drawable.round3));
        }
        else{
            tv_score1.setTextColor(Color.parseColor("#EC5C5C"));
            tv_score2.setTextColor(Color.parseColor("#8E9AE8"));
            btn_tug.setBackground(getDrawable(R.drawable.round4));
        }
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                tv_time.setText("倒计时：" + millisUntilFinished / 1000);
            }
            public void onFinish() {
                flag=false;
                btn_tug.setClickable(false);
                if(number==1){
                    if(score1>=score2){
                        Intent intent=new Intent(TugActivity.this,WaitingActivity.class);
                        intent.putExtra("connect_code",connect_code);
                        startActivityForResult(intent,1);
                    }
                    else {
                        Intent intent=new Intent(TugActivity.this,ConnectActivity.class);
                        intent.putExtra("connect_code",connect_code);
                        startActivityForResult(intent,1);
                    }
                }
                else if(number==2){
                    if(score1>=score2){
                        Intent intent=new Intent(TugActivity.this,ConnectActivity.class);
                        intent.putExtra("connect_code",connect_code);
                        startActivityForResult(intent,1);
                    }
                    else{
                        Intent intent=new Intent(TugActivity.this,WaitingActivity.class);
                        intent.putExtra("connect_code",connect_code);
                        startActivityForResult(intent,1);
                    }
                }
            }
        }.start();

        btn_tug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number==1){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println(Public_data.url+"add_score1.php?connect_code="+connect_code);
                                s1_and_s2=WebTool.touchHtml(Public_data.url+"add_score1.php?connect_code="+connect_code);
                                handler.sendEmptyMessage(1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else if(number==2){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println(Public_data.url+"add_score2.php?connect_code="+connect_code);
                                s1_and_s2=WebTool.touchHtml(Public_data.url+"add_score2.php?connect_code="+connect_code);
                                handler.sendEmptyMessage(1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });







    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            TugActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        flag=false;
        super.onDestroy();
    }
}