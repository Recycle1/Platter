package com.example.platter1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class WatchActivity_R extends AppCompatActivity {

    private Surface_View surfaceView;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private SeekBar seekBar;
    private Button fanhui;
    private int pause_position=0;
    boolean isPlaying=false;
    private static boolean visibility=false;
    static int R_position=0;
    static boolean R_pause=false;
    static boolean R_back=false;
    private RelativeLayout r_control;
    private String file;

    static int back_code=0;

    File d_file;
    private int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;
    private Uri uri;
    public Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    String sreen=(String)msg.obj;
                    Public_data.c_screen=(String)msg.obj;
                    System.out.println("屏幕："+sreen);
                    String [] single_screen=sreen.split("%");
                    ArrayList <Integer> screen_width=new ArrayList<>();
                    ArrayList <Integer> screen_height=new ArrayList<>();
                    for(String s:single_screen){
                        System.out.println(s);
                        screen_width.add(Integer.valueOf(s.split("#")[0]));
                        screen_height.add(Integer.valueOf(s.split("#")[1]));
                    }
                    switch (msg.arg1){
                        case 1:
                            if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
                                back_code=1;
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            }
                            float width=1;
                            float height=1;
                            float x=0;
                            float y=0;
                            if(LoadingActivity.Share_width<Public_data.screen_height){
                                width=(float)LoadingActivity.Share_width/Public_data.screen_height;
                                x=(Public_data.screen_height-LoadingActivity.Share_width)/2;
                            }
                            if(LoadingActivity.Share_height<=Public_data.screen_width){
                                height=(float)2*LoadingActivity.Share_height/Public_data.screen_width;
                                y=LoadingActivity.Share_height;
                            }
                            else if(LoadingActivity.Share_height>Public_data.screen_width){
                                height=2;
                                y=Public_data.screen_width;
                            }
                            change(width,height,x,y);
                            break;
                        case 2:
                            back_code=1;
                            if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            }
                            break;
                        case 3:
                            if(Public_data.thread_num==3){
                                x=com_data_width(screen_height)/2;
                                y=com_data_height(screen_width)/2;
                                width=(float)com_data_width(screen_height)/Public_data.screen_height;
                                height=(float)com_data_height(screen_width)/Public_data.screen_width;
                                switch (Public_data.thread_num) {
                                    case 3:
                                        change3(width, height, 0, 0, 0, 0);
                                        break;
                                }
                            }

                            back_code=1;
                            if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){

                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            }
                            break;
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_r);
        surfaceView=findViewById(R.id.surfaceView);
        seekBar= findViewById(R.id.seekbar);
        fanhui=findViewById(R.id.btn_behind);
        r_control=findViewById(R.id.rcontrol);

        Public_data.back=false;

        file=getIntent().getStringExtra("path");
        View decorView = getWindow().getDecorView();
        uiFlags |= 0x00001000;
        decorView.setSystemUiVisibility(uiFlags);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                getWindow().getDecorView().setSystemUiVisibility(uiFlags);
            }
        });

        System.gc();

        int vis = 0;
        char Vision[] = null;
        Vision = Build.VERSION.RELEASE.toCharArray();
        for (int i = 0, j = 1; (i < Vision.length) && (Vision[i] != '.'); i++, j = j * 10) {
            vis = vis * j + ((int) Vision[i] - 48);
        }

        if(Public_data.vis<9){
            d_file=new File("sdcard//"+"Platter/"+file);
        }
        else{
            d_file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Platter/"+file);
        }

        uri=Uri.parse("file://"+d_file.getAbsolutePath());

        int width_left=0;
        int height_top=0;
        float width=1;
        float height=1;
        int x=0;
        int y=0;
        ArrayList <Integer> screen_width=new ArrayList<>();
        ArrayList <Integer> screen_height=new ArrayList<>();

        System.out.println("所有的内容："+Public_data.c_screen);

        if(back_code==1&&Public_data.c_socket_num==1){
            String [] single_screen=Public_data.c_screen.split("%");
            for(String s:single_screen){
                System.out.println(s);
                screen_width.add(Integer.valueOf(s.split("#")[0]));
                screen_height.add(Integer.valueOf(s.split("#")[1]));
            }
            float width1=1;
            float height1=1;
            float x1=0;
            float y1=0;
            if(screen_height.get(1)<Public_data.screen_height){
                width1=(float)screen_height.get(1)/Public_data.screen_height;
                x1=(Public_data.screen_height-screen_height.get(1))/2;
            }
            if(screen_width.get(1)<=Public_data.screen_width){
                height1=(float)2*screen_width.get(1)/Public_data.screen_width;
                y1=screen_width.get(1);
            }
            else if(screen_width.get(1)>Public_data.screen_width){
                height1=2;
                y1=Public_data.screen_width;
            }
            change(width1,height1,x1,y1);
        }

        player=new MediaPlayer();
        try {
            player.reset();
            player.setDataSource(WatchActivity_R.this,uri);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            System.out.println(LoadingActivity.Share_height+"//"+LoadingActivity.Share_height);
            holder=surfaceView.getHolder();
            holder.addCallback(new MyCallBack());
            if(back_code==0){
                new Progress_Client(handler).start();
            }
            back_code=0;


            if(Public_data.c_socket_num>1){
                String [] single_screen=Public_data.c_screen.split("%");
                for(String s:single_screen){
                    System.out.println(s);
                    screen_width.add(Integer.valueOf(s.split("#")[0]));
                    screen_height.add(Integer.valueOf(s.split("#")[1]));
                }
                if(Public_data.c_socket_num==2){
                    width=(float)sum_data(screen_width)/Public_data.screen_width;
                    height=(float)min_data(screen_height)/Public_data.screen_height;
                }
//                else if(Public_data.c_socket_num>2){
//                    width=(float)com_data_width(screen_height)/Public_data.screen_height;
//                    height=(float)com_data_height(screen_width)/Public_data.screen_width;
//                }
            }

            switch (Public_data.c_socket_num){
                case 2:
                    switch (Public_data.thread_num){
                        case 1:
                            x=screen_width.get(0);
                            break;
                        case 2:
                            x=screen_width.get(0)+screen_width.get(1);
                            break;
                    }
                    if(min_data(screen_height)<Public_data.screen_height){
                        y=(Public_data.screen_height-min_data(screen_height))/2;
                    }
                    else{
                        y=0;
                    }
                    change2(width,height,-x,y);
                    break;
                case 3:
                    x=com_data_width(screen_height)/2;
                    y=com_data_height(screen_width)/2;
                    width=(float)com_data_width(screen_height)/Public_data.screen_height;
                    height=(float)com_data_height(screen_width)/Public_data.screen_width;
                    switch (Public_data.thread_num){
                        case 1:
                            change3(width,height,0,0,1,0);
                            break;
                        case 2:
                            change3(width,height,0,0,0,1);
                            break;
                        case 3:
                            change3(width,height,0,0,0,0);
                            break;
                    }
                    break;
            }



            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.seekTo(pause_position, MediaPlayer.SEEK_CLOSEST);
                player.start();

                isPlaying = true;
                System.gc();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sleep(500);
                            R_back=false;
                            while (isPlaying) {
                                int current = player.getCurrentPosition();
                                int duration=player.getDuration();
                                seekBar.setProgress(current*100/duration);
                                System.out.println("位置："+R_position+"暂停："+R_pause+"退出："+R_back);
                                if(R_back){
                                    player.release();
                                    finish();
                                    isPlaying=false;
                                    break;
                                }
                                if(R_pause){
                                    pause_position=R_position;
                                    if(player.isPlaying()){
                                        player.pause();
                                    }
                                }
                                else{
                                    if(!player.isPlaying()&&R_position!=100){
                                        player.seekTo(pause_position*duration/100,MediaPlayer.SEEK_CLOSEST);
                                        player.start();
                                    }

                                }
                                if(Math.abs(R_position*duration/100-current)>500){
                                    seekBar.setProgress(R_position);
                                    player.seekTo(R_position*duration/100+600,MediaPlayer.SEEK_CLOSEST);
                                }

                                sleep(500);
                                System.gc();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.release();
                LoadingActivity.name=null;
                if(d_file.exists()){
                    d_file.delete();
                }
                file=null;
                R_back=true;
                isPlaying=false;
                finish();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visibility){
                    r_control.setVisibility(View.GONE);
                    seekBar.setVisibility(View.GONE);
                    visibility=false;
                }
                else{
                    r_control.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                    visibility=true;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(d_file.exists()){
                d_file.delete();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    }

    @Override
    protected void onDestroy() {

        if(back_code==0){
            R_back=true;
            player.release();

            if(d_file.exists()){
                d_file.delete();
            }
            file=null;
            setResult(123);
            System.gc();
            Public_data.back=true;
            Public_data.c_socket_num=0;
            Public_data.thread_num=0;
            Public_data.c_screen=null;
        }
        isPlaying=false;

        super.onDestroy();
    }

    void change(float width,float height,float x,float y){
        AnimationSet animationSet = new AnimationSet(true);//创建一个Animation对象  (1,1) --> (0.5,0.5)
        System.out.println("高度："+height);
        Animation animation = new ScaleAnimation(1, (float)width, 1,(float)height,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);//设置动画过程的时间
        animation.setDuration(1000);//设置动画开始时间偏移量
        animation.setStartOffset(1000);//设置重复播放的次数
        animation.setRepeatCount(0);//结束时的状态
        animation.setFillAfter(true);//将Animation对象添加到AnimationSet对象中
        animationSet.addAnimation(animation);
        surfaceView.startAnimation(animation);
        surfaceView.animate().translationX(x);
    }

    void change2(float width,float height,int x,int y){
        AnimationSet animationSet = new AnimationSet(true);
        Animation animation = new ScaleAnimation(1f, width, 1f, height,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);//设置动画过程的时间
        animation.setDuration(1000);//设置动画开始时间偏移量
        animation.setFillAfter(true);//将Animation对象添加到AnimationSet对象中
        animationSet.addAnimation(animation);
        animationSet.setFillAfter(true);
        animationSet.setFillEnabled(true);
        surfaceView.startAnimation(animationSet);
        RelativeLayout.LayoutParams lp =  new RelativeLayout.LayoutParams(Public_data.screen_width,Public_data.screen_height);
        lp.setMargins(x, y, 0, 0);
        surfaceView.setLayoutParams(lp);
    }

    void change3(float width,float height,int x,int y,int p1,int p2){
        RelativeLayout.LayoutParams lp =  new RelativeLayout.LayoutParams(Public_data.screen_height,Public_data.screen_width);
        lp.setMargins(x, y, 0, 0);
        surfaceView.setLayoutParams(lp);
        AnimationSet animationSet = new AnimationSet(true);
        Animation animation = new ScaleAnimation(1f, width, 1f, height,
                Animation.RELATIVE_TO_SELF, p1, Animation.RELATIVE_TO_SELF, p2);//设置动画过程的时间
        animation.setDuration(1000);//设置动画开始时间偏移量
        animation.setFillAfter(true);//将Animation对象添加到AnimationSet对象中
        animationSet.addAnimation(animation);
        animationSet.setFillAfter(true);
        animationSet.setFillEnabled(true);
        surfaceView.startAnimation(animationSet);

    }

    int min_data(ArrayList<Integer> l){
        int min=l.get(0);
        for(int obj:l){
            if(obj<min){
                min=obj;
            }
        }
        return min;
    }

    int sum_data(ArrayList<Integer> l){
        int sum=0;
        for(int obj:l){
            sum+=obj;
        }
        return sum;
    }

    int com_data_width(ArrayList<Integer> l){
        int s1=l.get(0)+l.get(2);
        int s2=l.get(1)+l.get(3);
        if(s1>s2){
            return s2;
        }
        else{
            return s1;
        }
    }

    int com_data_height(ArrayList<Integer> l){
        int s1=l.get(0)+l.get(1);
        int s2=l.get(2)+l.get(3);
        if(s1>s2){
            return s2;
        }
        else{
            return s1;
        }
    }

}