package com.example.platter1.Game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.platter1.Public.Public_data;
import com.example.platter1.Public.WebTool;
import com.example.platter1.R;

public class WaitingActivity extends AppCompatActivity {

    String connect_code;
    boolean waiting=true;
    String tel;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    Intent intent=new Intent(WaitingActivity.this,Watch_WAN_R_Activity.class);
                    intent.putExtra("connect_code",connect_code);
                    intent.putExtra("tel",tel);
                    startActivityForResult(intent,1);
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        connect_code=getIntent().getStringExtra("connect_code");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (waiting){
                    try {
                        tel=WebTool.touchHtml(Public_data.url+"get_video_prepared.php?connect_code="+connect_code);
                        if(!tel.equals("0")){
                            handler.sendEmptyMessage(1);
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        waiting=false;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            WaitingActivity.this.finish();
        }
    }
}