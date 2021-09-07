package com.example.platter1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;

public class WatchActivity extends AppCompatActivity {

    private Surface_View surfaceView;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private ProgressBar progressBar;
    private ProgressBar progressBar1;
    private SeekBar seekBar;
    private Button pause;
    private Button go;
    private Button back;
    private Button behind;
    private Button send;
    private TextView time_postion;
    private TextView time_duration;
    private char Vision []=null;
    private Uri uri;
    private boolean isPlaying;
    private int pause_position=0;
    private int time=0;
    private static boolean visibility=true;
    private TextView share;
    private String file;
    private RelativeLayout r_control;
    private LinearLayout l_control;
    static int S_position=0;
    static boolean S_pause=false;
    static boolean S_back=false;
    static RelativeLayout.LayoutParams lp;
    static int Share_width;
    static int Share_height;
    static boolean Read_All=false;
    static int pixel_width;
    static int pixel_height;
    String ip;
    boolean connecting;
    private int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;

    Handler mhandler=new Handler(new Handler.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if(msg.what==2){
                player.release();
                Intent intent=new Intent();
                intent.setClass(WatchActivity.this,WatchActivity.class);
                intent.putExtra("path",file);
                intent.putExtra("connecting",true);
                System.out.println(WatchActivity.pixel_width+"-"+Share_width+"\n"+WatchActivity.pixel_height+"-"+Share_height);
                if(Share_height<WatchActivity.pixel_width){
                    intent.putExtra("width",Share_height);
                    intent.putExtra("width_left",(WatchActivity.pixel_width-Share_height)/2);
                }
                if(Share_width<=WatchActivity.pixel_height){
                    intent.putExtra("height",2*Share_width);
                    intent.putExtra("height_top",WatchActivity.pixel_height-Share_width);
                }
                else if(Share_width>WatchActivity.pixel_height){
                    intent.putExtra("height",2*WatchActivity.pixel_width);
                    intent.putExtra("height_top",Share_width-WatchActivity.pixel_height);
                }
                startActivity(intent);
            }
            return false;
        }
    });

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        surfaceView = findViewById(R.id.surfaceView);
        progressBar= findViewById(R.id.progressBar);
        seekBar= findViewById(R.id.seekbar);
        pause= findViewById(R.id.btn_pause);
        go= findViewById(R.id.btn_go);
        back= findViewById(R.id.btn_back);
        time_postion= findViewById(R.id.tv_position);
        time_duration= findViewById(R.id.tv_duration);
        r_control=findViewById(R.id.rcontrol);
        l_control=findViewById(R.id.lcontrol);
        behind= findViewById(R.id.btn_behind);
        share=findViewById(R.id.tv_share);
        send= findViewById(R.id.btn_send);
        progressBar1=findViewById(R.id.progressBar1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        WindowManager windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        display.getRealSize(outPoint);
        pixel_width=outPoint.x;
        pixel_height=outPoint.y;
        WifiManager wifiManager =(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        ip = (ipAddress & 0xFF)   +""+
                ((ipAddress >> 8 ) & 0xFF)  +""+
                ((ipAddress >> 16 ) & 0xFF)  +""+
                ( ipAddress >> 24 & 0xFF );
        new Thread() {
            @Override
            public void run() {
                try {
                    while(true){
                        if(Read_All){
                            mhandler.sendEmptyMessage(2);
                            break;
                        }
                        sleep(3000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        //System.out.println(MainActivity.vis);
        View decorView = getWindow().getDecorView();
        uiFlags |= 0x00001000;
        decorView.setSystemUiVisibility(uiFlags);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                getWindow().getDecorView().setSystemUiVisibility(uiFlags);
            }
        });
        if(savedInstanceState!=null){
            pause_position=savedInstanceState.getInt("pause_position",0);
        }
        file=getIntent().getStringExtra("path");
        connecting=getIntent().getBooleanExtra("connecting",false);
        MainActivity.width=getIntent().getIntExtra("width",WatchActivity.pixel_width);
        int width_left=getIntent().getIntExtra("width_left",0);
        MainActivity.height=getIntent().getIntExtra("height",WatchActivity.pixel_height);
        int height_top=getIntent().getIntExtra("height_top",0);
//        System.out.println(width_left+"\n"+height_top+"1234567890");
//        System.out.println(MainActivity.height);
        int vis = 0;
        char Vision[] = null;
        Vision = Build.VERSION.RELEASE.toCharArray();
        for (int i = 0, j = 1; (i < Vision.length) && (Vision[i] != '.'); i++, j = j * 10) {
            vis = vis * j + ((int) Vision[i] - 48);
        }
        if(vis<9){
            uri= Uri.parse(file);
        }
        else{
            uri= Uri.parse("file://"+file);
        }
        System.out.println(file);
        player=new MediaPlayer();
        System.out.println("1235");
        try {
            player.reset();
            player.setDataSource(this,uri);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            lp =  new RelativeLayout.LayoutParams(MainActivity.width,MainActivity.height);
            //lp =  new RelativeLayout.LayoutParams(surfaceView.getLayoutParams().width, surfaceView.getLayoutParams().height);
            lp.setMargins(width_left, height_top, 0, 0);
            surfaceView.setLayoutParams(lp);
            holder=surfaceView.getHolder();
            holder.addCallback(new MyCallBack());
            player.prepare();
            System.out.println("1235");
            setlistener();
            if(connecting){
                send.setBackground(getResources().getDrawable(R.drawable.bg_connecting));
                send.setText("S-R");
                send.setEnabled(false);
                new Progress_Server().start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setlistener(){
        behind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S_back=true;
                WatchActivity.this.finish();
                System.exit(0);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send.setVisibility(View.GONE);
                progressBar1.setVisibility(View.VISIBLE);
                //获取wifi服务
                share.setText("共享码:"+ip.substring(6));
                File file1=new File(file);
                new P_Server(file1).start();
            }
        });
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    progressBar.setVisibility(View.INVISIBLE);
                    player.seekTo(pause_position,MediaPlayer.SEEK_CLOSEST);
                    player.start();
                    time_duration.setText(getTime(player.getDuration()));
                    pause.setBackground(getResources().getDrawable(R.drawable.zanting));
                    //player.setLooping(true);
                    new Thread() {

                        @Override
                        public void run() {
                            try {
                                isPlaying = true;
                                while (isPlaying) {
                                    int current = player.getCurrentPosition();
                                    int duration=player.getDuration();
                                    S_position=current*100/duration;
                                    seekBar.setProgress(current*100/duration);
                                    sleep(500);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            time_postion.setText(getTime(player.getCurrentPosition()));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress=seekBar.getProgress();
                int position=progress*player.getDuration()/100;
                player.seekTo(position,MediaPlayer.SEEK_CLOSEST);
                pause_position=position;
            }
        });
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visibility){
                    r_control.setVisibility(View.GONE);
                    l_control.setVisibility(View.GONE);
                    visibility=false;
                }
                else{
                    r_control.setVisibility(View.VISIBLE);
                    l_control.setVisibility(View.VISIBLE);
                    visibility=true;
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
                if(player.isPlaying()){
                    pause_position=player.getCurrentPosition();
                    player.pause();
                    S_pause=true;
                    pause.setBackground(getResources().getDrawable(R.drawable.bofang));
                }
                else{
                    player.seekTo(pause_position,MediaPlayer.SEEK_CLOSEST);
                    player.start();
                    S_pause=false;
                    pause.setBackground(getResources().getDrawable(R.drawable.zanting));
                }
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause_position=player.getCurrentPosition()+3000;
                player.seekTo(pause_position,MediaPlayer.SEEK_CLOSEST);
                if(!player.isPlaying())pause.setBackground(getResources().getDrawable(R.drawable.zanting));
                player.start();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause_position=player.getCurrentPosition()-3000;
                player.seekTo(pause_position,MediaPlayer.SEEK_CLOSEST);
                if(!player.isPlaying())pause.setBackground(getResources().getDrawable(R.drawable.zanting));
                player.start();
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                S_back=true;
            }
        });
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

    class Mymedia implements MediaPlayer.OnSeekCompleteListener{

        @Override
        public void onSeekComplete(MediaPlayer mp) {
            player.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
        //player.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

    public static String getTime(long time){
        long hours = time / (1000 * 60 * 60);
        long minutes = (time-hours*(1000 * 60 * 60 ))/(1000* 60);
        long second = (time-hours*(1000 * 60 * 60 )-minutes*(1000 * 60 ))/1000;
        String diffTime="";
        if(hours<1){
            if(minutes<10){
                diffTime="0"+minutes;
            }else{
                diffTime=String.valueOf(minutes);
            }
            if(second<10){
                diffTime=diffTime+":0"+second;
            }else{
                diffTime=diffTime+":"+second;
            }
        }
        else{
            if(minutes<10){
                diffTime=hours+":0"+minutes;
            }else{
                diffTime=hours+":"+minutes;
            }
            if(second<10){
                diffTime=diffTime+":0"+second;
            }else{
                diffTime=diffTime+":"+second;
            }
        }
        return diffTime;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        pause_position=player.getCurrentPosition();
        outState.putInt("pause_position",pause_position);
    }
}