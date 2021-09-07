package com.example.platter1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class WatchActivity_R extends AppCompatActivity {

    private Surface_View surfaceView;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private SeekBar seekBar;
    private ProgressBar progressBar;
    private Button fanhui;
    private int pause_position=0;
    boolean isPlaying=false;
    private static boolean visibility=true;
    static int R_position=0;
    static boolean R_pause=false;
    static boolean R_back=false;
    private RelativeLayout r_control;
    static TextView jindu;
    private int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_r);
        jindu=findViewById(R.id.jindu);
        surfaceView=findViewById(R.id.surfaceView);
        seekBar= findViewById(R.id.seekbar);
        progressBar=findViewById(R.id.progress1);
        fanhui=findViewById(R.id.btn_behind);
        r_control=findViewById(R.id.rcontrol);
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        View decorView = getWindow().getDecorView();
        uiFlags |= 0x00001000;
        decorView.setSystemUiVisibility(uiFlags);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                getWindow().getDecorView().setSystemUiVisibility(uiFlags);
            }
        });
        int vis = 0;
        char Vision[] = null;
        Vision = Build.VERSION.RELEASE.toCharArray();
        for (int i = 0, j = 1; (i < Vision.length) && (Vision[i] != '.'); i++, j = j * 10) {
            vis = vis * j + ((int) Vision[i] - 48);
        }
        if(vis<9){
            uri= Uri.parse("sdcard//"+"Platter/"+LoadingActivity.name);
        }
        else{
            uri= Uri.parse("file://"+ Environment.getExternalStorageDirectory().getAbsolutePath()+"/Platter/"+LoadingActivity.name);
        }
        int width_left=0;
        int height_top=0;
        MainActivity.width=LoadingActivity.pixel_height;
        MainActivity.height=LoadingActivity.pixel_width;
        if(LoadingActivity.Share_width<LoadingActivity.pixel_height){
            MainActivity.width=LoadingActivity.Share_width;
            width_left=(LoadingActivity.pixel_height-LoadingActivity.Share_width)/2;
        }
        if(LoadingActivity.Share_height<=LoadingActivity.pixel_width){
            MainActivity.height=2*LoadingActivity.Share_height;
            height_top=LoadingActivity.pixel_width-LoadingActivity.Share_height+LoadingActivity.Share_height;
        }
        else if(LoadingActivity.Share_height>LoadingActivity.pixel_width){
            MainActivity.height=2*LoadingActivity.pixel_width;
            height_top=LoadingActivity.Share_height-LoadingActivity.pixel_width+LoadingActivity.pixel_width;
        }
        player=new MediaPlayer();
        try {
            player.setDataSource(WatchActivity_R.this,uri);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            System.out.println(LoadingActivity.Share_height+"//"+LoadingActivity.Share_height);
            RelativeLayout.LayoutParams lp =  new RelativeLayout.LayoutParams(MainActivity.width, MainActivity.height);
            System.out.println(height_top+"lkndsfkdnsf");
            lp.setMargins(width_left, -height_top, 0, 0);
            System.out.println(MainActivity.height);
            surfaceView.setLayoutParams(lp);
            holder=surfaceView.getHolder();
            holder.addCallback(new MyCallBack());
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
                progressBar.setVisibility(View.INVISIBLE);
                player.seekTo(pause_position, MediaPlayer.SEEK_CLOSEST);
                player.start();
                new Thread() {

                    @Override
                    public void run() {
                        try {
                            isPlaying = true;
                            while (isPlaying) {
                                int current = player.getCurrentPosition();
                                int duration=player.getDuration();
                                seekBar.setProgress(current*100/duration);
                                if(Math.abs(R_position*duration/100-current)>700){
                                    seekBar.setProgress(R_position);
                                    player.seekTo(R_position*duration/100+500,MediaPlayer.SEEK_CLOSEST);
                                }
                                if(R_pause){
                                    pause_position=R_position;
                                    player.pause();
                                }
                                else{
                                    if(!player.isPlaying()){
                                        player.seekTo(pause_position+3500,MediaPlayer.SEEK_CLOSEST);
                                        player.start();
                                    }
                                }
                                if(R_back){
                                    player.release();
                                    finish();
                                }
                                sleep(500);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.release();
                LoadingActivity.name=null;
                Intent intent=new Intent();
                intent.setClass(WatchActivity_R.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
        new Progress_Client().start();
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
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}