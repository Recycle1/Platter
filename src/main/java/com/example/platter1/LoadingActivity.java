package com.example.platter1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    static TextView jindu;
    static int Share_width;
    static int Share_height;
    static int pixel_width;
    static int pixel_height;
    static String ip;
    static String name = null;

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if(msg.what == 0){
                Intent intent=new Intent();
//                if(Share_height<MainActivity.pixel_height){
//                    intent.putExtra("width",Share_height);
//                    intent.putExtra("width_left",(MainActivity.pixel_height-Share_height)/2);
//                }
//                if(Share_width<=MainActivity.pixel_width){
//                    intent.putExtra("height",2*Share_width);
//                    intent.putExtra("height_top",MainActivity.pixel_width-Share_width);
//                }
//                else if(Share_width>MainActivity.pixel_width){
//                    intent.putExtra("height",2*MainActivity.pixel_width);
//                    intent.putExtra("height_top",Share_width-MainActivity.pixel_width);
//                }
                intent.setClass(LoadingActivity.this, WatchActivity_R.class);
                startActivity(intent);
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading2);
        jindu=findViewById(R.id.jindu);
        progressBar=findViewById(R.id.progress1);
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        ip=getIntent().getStringExtra("ip");
        new Thread(new Runnable() {
            @Override
            public void run() {
                WindowManager windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();
                Point outPoint = new Point();
                display.getRealSize(outPoint);
                pixel_width=outPoint.x;
                pixel_height=outPoint.y;
                new P_Client();
                if(!P_Client.chuanshu)mHandler.sendEmptyMessage(0);
            }
        }).start();
    }
}