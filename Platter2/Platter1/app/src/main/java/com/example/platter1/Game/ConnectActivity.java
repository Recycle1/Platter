package com.example.platter1.Game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.example.platter1.GridAdapter;
import com.example.platter1.GridRecyclerViewActivity;
import com.example.platter1.MediaBean;
import com.example.platter1.Public.Public_data;
import com.example.platter1.Public.Public_method;
import com.example.platter1.Public.WebTool;
import com.example.platter1.R;
import com.example.platter1.ScannerAnsyTask;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class ConnectActivity extends AppCompatActivity {

    String select_member;
    int identity;

    String connect_code;

    private List<MediaBean> mList;
    private ScannerAnsyTask mAnsyTask;

    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0x101:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Random random=new Random();
                            int length=mList.size();
                            int index=random.nextInt(length);
                            System.out.println(mList.get(index).getPath().split(".", 2)[1]);
//                            while(!mList.get(index).getPath().split(".",2)[1].equals("flv")){
//                                index=random.nextInt(length);
//                            }
//                            System.out.println(mList.get(index).getPath());
                            WebTool.uploadFile(mList.get(0).getPath(), Public_data.url+"upload.php", "video_"+Public_data.tel+".mp4");
                            mHandler.sendEmptyMessage(2);
                        }
                    }).start();
                    break;
                case 2:
                    Intent intent=new Intent(ConnectActivity.this,Watch_WAN_Activity.class);
                    intent.putExtra("connect_code",connect_code);
                    intent.putExtra("tel",Public_data.tel);
                    startActivityForResult(intent,1);
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        select_member=getIntent().getStringExtra("select_member");
        identity=getIntent().getIntExtra("identity",0);
        connect_code=getIntent().getStringExtra("connect_code");
        startScanTack();
    }
    private void startScanTack() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mAnsyTask = new ScannerAnsyTask(2,getParent());
                    mAnsyTask.execute();
                    mList = mAnsyTask.get();
                    if(mList != null && mList.size()>0){
                        mHandler.sendEmptyMessage(0x101);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            ConnectActivity.this.finish();
        }
    }
}