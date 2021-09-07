package com.example.platter1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GridRecyclerViewActivity extends AppCompatActivity {

    private RecyclerView mRvGrid;
    private ProgressBar progressBar;
    private TextView textView;
    private List<MediaBean> mList;
    private ScannerAnsyTask mAnsyTask;
    private MyGridViewAdapter myGridViewAdapter;
    static GridAdapter gridAdapter;
    GridAdapter.OnItemClickListener clickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_recycler_view);
        mRvGrid=findViewById(R.id.rv_grid);
        progressBar=findViewById(R.id.progress);
        textView=findViewById(R.id.progress_text);
        startScanTack();
        mRvGrid.setLayoutManager(new GridLayoutManager(GridRecyclerViewActivity.this,3));
        clickListener=new GridAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                //Toast.makeText(GridRecyclerViewActivity.this, "click:"+position, Toast.LENGTH_SHORT).show();
                myGridViewAdapter=new MyGridViewAdapter(GridRecyclerViewActivity.this,mList);
                System.out.println(myGridViewAdapter.getItem(position).getPath());
                Intent intent=new Intent();
                intent.setClass(GridRecyclerViewActivity.this,WatchActivity.class);
                intent.putExtra("path",myGridViewAdapter.getItem(position).getPath());
                startActivity(intent);
            }
        };
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
    Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if(msg.what == 0x101 ){
                mRvGrid.setVisibility(View.VISIBLE);
                gridAdapter=new GridAdapter(GridRecyclerViewActivity.this,mList);
                mRvGrid.setAdapter(gridAdapter);
                gridAdapter.setMlistener(clickListener);
            }
            return false;
        }
    });
}