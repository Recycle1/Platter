package com.example.platter1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.example.platter1.Game.Watch_WAN_Activity;
import com.example.platter1.Public.Public_data;
import com.example.platter1.Public.Record;
import com.example.platter1.Public.Video_info;
import com.example.platter1.Public.WebTool;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {

    private RecyclerView rv;
    private RecordAdapter adapter;
    private ArrayList<Record> list;
    private Button btn_back;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    adapter.setRecordlist(list);
                    rv.setAdapter(adapter);
                    break;
            }
            return false;
        }
    });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        btn_back=findViewById(R.id.btn_back);
        rv=findViewById(R.id.rv);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordActivity.this.finish();
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new RecordAdapter(this, new RecordAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent=new Intent(RecordActivity.this, Watch_WAN_Activity.class);
                intent.putExtra("video_id",list.get(position).video_info.video_id);
                startActivity(intent);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list=WebTool.get_record();
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}