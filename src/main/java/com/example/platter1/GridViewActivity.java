package com.example.platter1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GridViewActivity extends AppCompatActivity {

    private GridView mGv;
    private ProgressBar progressBar;
    private TextView textView;
    private List<MediaBean> mList;
    private ScannerAnsyTask mAnsyTask;
    private MyGridViewAdapter myGridViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);
        mGv=findViewById(R.id.gv);
        progressBar=findViewById(R.id.progress);
        textView=findViewById(R.id.progress_text);
        //mGv.setAdapter(new com.example.platter.MyGridViewAdapter(GridViewActivity.this,mList));
        startScanTack();
        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(GridViewActivity.this, "点击 pos:"+position, Toast.LENGTH_SHORT).show();
                myGridViewAdapter=new MyGridViewAdapter(GridViewActivity.this,mList);
                System.out.println(myGridViewAdapter.getItem(position).getPath());
                Intent intent=new Intent();
                intent.setClass(GridViewActivity.this, WatchActivity.class);
                intent.putExtra("path",myGridViewAdapter.getItem(position).getPath());
                startActivity(intent);
            }
        });
    }
    private void startScanTack() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mAnsyTask = new ScannerAnsyTask(getParent(), progressBar,textView);
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

//    private Handler mHandler = new Handler(){
//        public void handleMessage(android.os.Message msg) {
//            if(msg.what == 0x101 ){
//                mGv.setVisibility(View.VISIBLE);
//                mGv.setAdapter(new com.example.platter.MyGridViewAdapter(GridViewActivity.this,mList));
//            }
//        };
//    };
    Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if(msg.what == 0x101 ){
                mGv.setVisibility(View.VISIBLE);
                mGv.setAdapter(new MyGridViewAdapter(GridViewActivity.this,mList));
            }
            return false;
        }
    });
}
