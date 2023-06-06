package com.example.platter1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.platter1.Public.Public_data;

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
            switch (msg.what){
                case 0:
                Intent intent=new Intent();
                intent.putExtra("path", Public_data.file);
                intent.setClass(LoadingActivity.this, WatchActivity_R.class);
                startActivityForResult(intent,123);
                break;
                case 1:
                    jindu.setText("搭建");
                    break;
                case 2:
                    jindu.setText(msg.arg1+"%");
                    break;
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
        if(Public_data.net_way.equals("WIFI")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    WindowManager windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
                    Display display = windowManager.getDefaultDisplay();
                    Point outPoint = new Point();
                    display.getRealSize(outPoint);
                    pixel_width=outPoint.x;
                    pixel_height=outPoint.y;
                    new more_client(mHandler);
                }
            }).start();
        }
        else{
            Intent intent=new Intent();
            intent.putExtra("path",ip);
            intent.setClass(LoadingActivity.this,Watch_R_2Activity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123){
            setResult(123);
            finish();
        }
    }
}