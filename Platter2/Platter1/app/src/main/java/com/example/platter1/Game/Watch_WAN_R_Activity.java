package com.example.platter1.Game;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.platter1.Public.Public_data;
import com.example.platter1.Public.Public_method;
import com.example.platter1.Public.WebTool;
import com.example.platter1.R;
import com.example.platter1.Surface_View;

import java.io.IOException;

public class Watch_WAN_R_Activity extends AppCompatActivity {


    private Surface_View surfaceView;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private SeekBar seekBar;
    private Button behind;
    private Button send;
    String video_id;
    private RelativeLayout r_control;
    private int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;

    private char Vision []=null;
    private Uri uri;
    private boolean isPlaying;
    private int pause_position=0;
    private int time=0;
    private static boolean visibility=true;
    private String file;
    static int back_code=0;

    String connect_code=null;
    //另一部手机的电话号
    String tel=null;

    static int R_position=0;
    static boolean R_pause=false;
    static boolean R_back=false;
    static RelativeLayout.LayoutParams lp;
    static int Share_width;
    static int Share_height;
    static boolean Read_All=false;
    static int pixel_width;
    static int pixel_height;

    private Handler handler=new Handler(new Handler.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case 1:
                    String screen_width_height[]=((String)msg.obj).split("#");
                    int screen_width=Integer.valueOf(screen_width_height[0]);
                    int screen_height=Integer.valueOf(screen_width_height[1]);

                    float width=1;
                    float height=1;
                    float x=0;
                    float y=0;

                    //比较长边
                    if(screen_height<Public_data.screen_height){
                        width=(float)screen_height/Public_data.screen_height;
                        x=(Public_data.screen_height-Share_height)/2;
                    }
                    if(screen_width<Public_data.screen_width){
                        height=(float)2*screen_width/Public_data.screen_width;
                        y=Public_data.screen_width-screen_width;
                    }
                    else if(screen_width>=Public_data.screen_width){
                        height=2;
                    }
                    Public_method.change_watch_r(width,height,x,y,surfaceView);
                    break;
                case 2:
                    seekBar.setProgress((int) msg.arg1);
                    break;
                case 3:
                    int current=(int)msg.arg1;
                    int duration=(int)msg.arg2;
                    Video_status vs=(Video_status) msg.obj;
                    if(R_back!=vs.back){
                        R_back=vs.back;
                        if(R_back==true){
                            Watch_WAN_R_Activity.this.finish();
                        }
                    }
                    if(R_pause!=vs.pause){
                        R_pause=vs.pause;
                        if(R_pause){
                            pause_position=R_position;
                            if(player.isPlaying()){
                                player.pause();
                            }
                        }
                        else if(!player.isPlaying()&&R_position!=100){
                                player.seekTo(pause_position*duration/100,MediaPlayer.SEEK_CLOSEST);
                                player.start();
                        }
                    }
                    System.out.println(vs.position*duration/100+"            -           "+current);
                    if(Math.abs(vs.position*duration/100-current)>500){
                        seekBar.setProgress(vs.position);
                        player.seekTo(vs.position*duration/100+600,MediaPlayer.SEEK_CLOSEST);
                    }

                    break;
            }
            return false;
        }
    });


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_wan_r);

        surfaceView=findViewById(R.id.surfaceView);
        seekBar= findViewById(R.id.seekbar);
        behind=findViewById(R.id.btn_behind);
        r_control=findViewById(R.id.rcontrol);

        connect_code=getIntent().getStringExtra("connect_code");
        tel=getIntent().getStringExtra("tel");
        video_id=getIntent().getStringExtra("video_id");

        View decorView = getWindow().getDecorView();
        uiFlags |= 0x00001000;
        decorView.setSystemUiVisibility(uiFlags);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                getWindow().getDecorView().setSystemUiVisibility(uiFlags);
            }
        });

        player=new MediaPlayer();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WebTool.touchHtml(Public_data.url+"send_p2_screen.php?connect_code="+connect_code+"&p2_width="+Public_data.screen_width+"&p2_height="+Public_data.screen_height);
                    String screen=WebTool.touchHtml(Public_data.url+"get_p1_screen.php?connect_code="+connect_code);
                    Message message=new Message();
                    message.what=1;
                    message.obj=screen;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //player.reset();
        try {
//            uri=Uri.parse("https://www.recycle11.top/Platter/video/video_16506460493325341.mp4");
            if(tel!=null){
                player.setDataSource(Public_data.url+"video/video_"+tel+".mp4");
            }
            else{
                player.setDataSource(Public_data.url+"cloud_video/"+video_id+".mp4");
            }
//            player.setDataSource("https://www.recycle11.top/Platter/video/video_16506460493325341.mp4");
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);

            lp =  new RelativeLayout.LayoutParams(Public_data.screen_height,Public_data.screen_width);
            surfaceView.setLayoutParams(lp);

            holder=surfaceView.getHolder();
            holder.addCallback(new MyCallBack());
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }



        behind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                isPlaying=false;
//                player.release();
                Watch_WAN_R_Activity.this.finish();
            }
        });

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.seekTo(pause_position,MediaPlayer.SEEK_CLOSEST);
                player.start();
                player.setLooping(true);
                new Thread() {

                    @Override
                    public void run() {
                        try {
                            isPlaying = true;
                            while (isPlaying) {
                                int current = player.getCurrentPosition();
                                int duration=player.getDuration();
                                //System.out.println("位置："+S_position+"暂停："+S_pause+"退出："+S_back);
                                Message message1=new Message();
                                message1.what=2;
                                message1.arg1=current*100/duration;
                                handler.sendMessage(message1);
                                System.out.println("R_position:"+R_position);
                                Video_status vs=WebTool.get_video_status(connect_code);
                                Message message=new Message();
                                message.what=3;
                                message.obj=vs;
                                message.arg1=current;
                                message.arg2=duration;
                                handler.sendMessage(message);
                                sleep(500);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
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

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                time_postion.setText(Public_method.getTime(player.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
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

    @Override
    protected void onDestroy() {
        isPlaying=false;
        player.release();
        super.onDestroy();
    }
}