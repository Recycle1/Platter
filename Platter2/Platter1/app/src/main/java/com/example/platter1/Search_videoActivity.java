package com.example.platter1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.platter1.Game.Watch_WAN_Activity;
import com.example.platter1.Public.Video_info;
import com.example.platter1.Public.WebTool;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Search_videoActivity extends AppCompatActivity {

    private ImageView iv_search;
    private EditText et_search;

    private TextView tv_all;
    private TextView tv_movie;
    private TextView tv_teleplay;
    private TextView tv_document;
    private TextView tv_variety_show;
    private TextView tv_list[];

    private RecyclerView rv;
    private VideoListAdapter adapter;
    private ArrayList<Video_info> list;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    adapter.setVideolist(list);
                    rv.setAdapter(adapter);
                    break;
                case 2:
                    adapter.setVideolist(list);
                    adapter.notifyDataSetChanged();
                    break;
                case 3:
                    ((TextView)msg.obj).setTextColor(Color.parseColor("#8A8383"));
                    ((TextView)msg.obj).setTextSize(15);
                    break;
                case 4:
                    ((TextView)msg.obj).setTextColor(Color.parseColor("#353333"));
                    ((TextView)msg.obj).setTextSize(17);
                    break;
            }
            return false;
        }
    });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_video);
        rv=findViewById(R.id.rv);
        iv_search=findViewById(R.id.iv);
        et_search=findViewById(R.id.et);
        tv_all=findViewById(R.id.tv_all);
        tv_movie=findViewById(R.id.tv_movie);
        tv_teleplay=findViewById(R.id.tv_teleplay);
        tv_document=findViewById(R.id.tv_document);
        tv_variety_show=findViewById(R.id.tv_variety_show);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new VideoListAdapter(this, new VideoListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

                Intent intent=new Intent(Search_videoActivity.this, Watch_WAN_Activity.class);
                intent.putExtra("video_id",list.get(position).video_id);
                startActivity(intent);

            }
        });
        tv_list= new TextView[]{tv_all, tv_movie, tv_teleplay, tv_document, tv_variety_show};
        set_listener();
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String text=et_search.getText().toString();
                        if(text.trim().length()!=0){
                            try {
                                list=WebTool.get_video_search(text);
                                handler.sendEmptyMessage(2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list=WebTool.get_video_list();
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    void set_TextView(int i){
        for(int j=0;j<tv_list.length;j++){
            tv_list[j].setTextColor(Color.parseColor("#8A8383"));
            tv_list[j].setTextSize(15);
            tv_list[j].setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        tv_list[i].setTextColor(Color.parseColor("#353333"));
        tv_list[i].setTextSize(17);
        tv_list[i].setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }
    void set_listener(){
        tv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_TextView(0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            list=WebTool.get_video_list();
                            handler.sendEmptyMessage(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        tv_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_TextView(1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            list=WebTool.get_video_class(0);
                            handler.sendEmptyMessage(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        tv_teleplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_TextView(2);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            list=WebTool.get_video_class(1);
                            handler.sendEmptyMessage(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        tv_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_TextView(3);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            list=WebTool.get_video_class(2);
                            handler.sendEmptyMessage(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        tv_variety_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_TextView(4);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            list=WebTool.get_video_class(3);
                            handler.sendEmptyMessage(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}