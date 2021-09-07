package com.example.platter1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    private char Vision[] = null;
    static int width;
    static int height;
    static int vis;
    private Button send;
    private Button fasong;
    private Button receive;
    private Button jieshou;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        send=findViewById(R.id.btn_s);
        receive=findViewById(R.id.btn_r);
        fasong=findViewById(R.id.btn_send);
        jieshou=findViewById(R.id.btn_receive);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        Vision = Build.VERSION.RELEASE.toCharArray();
        for (int i = 0, j = 1; (i < Vision.length) && (Vision[i] != '.'); i++, j = j * 10) {
            vis = vis * j + ((int) Vision[i] - 48);
        }
        setlistener();
    }
    private void setlistener(){
        OnClick onClick=new OnClick();
        send.setOnClickListener(onClick);
        receive.setOnClickListener(onClick);
        fasong.setOnClickListener(onClick);
        jieshou.setOnClickListener(onClick);
    }
    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_s:
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this,GridRecyclerViewActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_r:
                    P_Client.chuanshu=false;
                    Intent intent1=new Intent();
                    intent1.setClass(MainActivity.this,ShareActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.btn_send:
                    send.setVisibility(VISIBLE);
                    receive.setVisibility(GONE);
                    fasong.setBackgroundColor(Color.parseColor("#C1C5EA"));
                    jieshou.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    break;
                case R.id.btn_receive:
                    send.setVisibility(GONE);
                    receive.setVisibility(VISIBLE);
                    fasong.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    jieshou.setBackgroundColor(Color.parseColor("#C1C5EA"));
                    break;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(0!=(Intent.FLAG_ACTIVITY_CLEAR_TOP&intent.getFlags())){
            finish();
            System.exit(0);
        }
    }
}