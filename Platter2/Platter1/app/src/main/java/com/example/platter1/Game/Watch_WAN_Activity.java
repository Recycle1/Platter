package com.example.platter1.Game;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.platter1.MainActivity;
import com.example.platter1.Public.Public_data;
import com.example.platter1.Public.Public_method;
import com.example.platter1.Public.WebTool;
import com.example.platter1.R;
import com.example.platter1.Surface_View;
import com.example.platter1.WatchActivity;
import com.google.zxing.util.QrCodeGenerator;

import java.io.IOException;
import java.util.Random;

public class Watch_WAN_Activity extends AppCompatActivity {


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
    private ImageView imgQrcode;
    private TextView time_postion;
    private TextView time_duration;
    private TextView tv_jindu;
    private RelativeLayout r_control;
    private LinearLayout l_control;
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

    String video_id;
    String path;
    String tel;

    String connect_code=null;
    Boolean connected=false;
    Boolean wait_connect=true;

    static int S_position=0;
    static boolean S_pause=false;
    static boolean S_back=false;
    static RelativeLayout.LayoutParams lp;
    static int Share_width;
    static int Share_height;
    static boolean Read_All=false;
    static int pixel_width;
    static int pixel_height;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case 1:
                    connected=true;
                    imgQrcode.setVisibility(View.GONE);
                    send.setVisibility(View.GONE);
                    String screen_width_height[]=((String)msg.obj).split("#");
                    int screen_width=Integer.valueOf(screen_width_height[0]);
                    int screen_height=Integer.valueOf(screen_width_height[1]);

                    System.out.println("          '''''''''''''''''''''");
                    System.out.println(screen_height);
                    System.out.println(screen_width);

                    float width=1;
                    float height=1;
                    float x=0;
                    float y=0;

                    //比较长边
                    if(screen_height<Public_data.screen_height){
                        width=(float)screen_height/Public_data.screen_height;
                        x=(Public_data.screen_height-Share_height)/2;
                    }
                    if(screen_width<=Public_data.screen_width){
                        height=(float)2*screen_width/Public_data.screen_width;
                        y=Public_data.screen_width-screen_width;
                    }
                    else if(screen_width>Public_data.screen_width){
                        height=2;
                    }
                    Public_method.change_watch(width,height,x,y,surfaceView);
                    break;
                case 2:
                    generateQrCode(connect_code+"#"+Public_data.tel);
                    break;
            }
            return false;
        }
    });


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_wan);

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
        tv_jindu=findViewById(R.id.tv_jindu);
        progressBar1=findViewById(R.id.progressBar1);
        imgQrcode=findViewById(R.id.img_qrcode);
        send=findViewById(R.id.btn_send);

        View decorView = getWindow().getDecorView();
        uiFlags |= 0x00001000;
        decorView.setSystemUiVisibility(uiFlags);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                getWindow().getDecorView().setSystemUiVisibility(uiFlags);
            }
        });

//        connect_code=getIntent().getStringExtra("connect_code");
        video_id=getIntent().getStringExtra("video_id");
        path=getIntent().getStringExtra("path");
        tel=getIntent().getStringExtra("tel");
        connect_code="cc_"+Public_method.getRandom();
        if(video_id!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        WebTool.touchHtml(Public_data.url+"add_record.php?tel="+Public_data.tel+"&video_id="+video_id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        if(getIntent().getStringExtra("connect_code")!=null){
            connect_code=getIntent().getStringExtra("connect_code");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Public_data.url+"send_p1_screen.php?connect_code="+connect_code+"&tel="+Public_data.tel+"&p1_width="+Public_data.screen_width+"&p1_height="+Public_data.screen_height);
                        WebTool.touchHtml(Public_data.url+"send_p1_screen.php?connect_code="+connect_code+"&tel="+Public_data.tel+"&p1_width="+Public_data.screen_width+"&p1_height="+Public_data.screen_height);
                        String screen="0#0";
                        while(wait_connect){
                            System.out.println(Public_data.url+"get_p2_screen.php?connect_code="+connect_code);
                            screen=WebTool.touchHtml(Public_data.url+"get_p2_screen.php?connect_code="+connect_code);
                            if(!screen.equals("0#0")){
                                Message message=new Message();
                                message.what=1;
                                message.obj=screen;
                                handler.sendMessage(message);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        player=new MediaPlayer();

            //player.reset();
        try {
            if(video_id!=null){
                player.setDataSource(Public_data.url+"cloud_video/"+video_id+".mp4");
            }
            else if(tel!=null){
                player.setDataSource(Public_data.url+"video/video_"+tel+".mp4");
            }
            else{
                Uri uri=Uri.parse(path);
                player.setDataSource(this,uri);
            }
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);

            lp =  new RelativeLayout.LayoutParams(Public_data.screen_height,Public_data.screen_width);
            surfaceView.setLayoutParams(lp);

            holder=surfaceView.getHolder();
            holder.addCallback(new MyCallBack());
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Public_data.tel.equals("")){
                    Toast.makeText(Watch_WAN_Activity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(video_id==null){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                WebTool.uploadFile(path,Public_data.url+"upload.php", "video_"+Public_data.tel+".mp4");
                                handler.sendEmptyMessage(2);
                            }
                        }).start();
                    }
                    else{
                        generateQrCode(connect_code+"#"+video_id);
                    }
                    send.setVisibility(View.GONE);
                    progressBar1.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println(Public_data.url+"send_p1_screen.php?connect_code="+connect_code+"&tel="+Public_data.tel+"&p1_width="+Public_data.screen_width+"&p1_height="+Public_data.screen_height);
                                WebTool.touchHtml(Public_data.url+"send_p1_screen.php?connect_code="+connect_code+"&tel="+Public_data.tel+"&p1_width="+Public_data.screen_width+"&p1_height="+Public_data.screen_height);
                                String screen="0#0";
                                while(wait_connect){
                                    System.out.println(Public_data.url+"get_p2_screen.php?connect_code="+connect_code);
                                    screen=WebTool.touchHtml(Public_data.url+"get_p2_screen.php?connect_code="+connect_code);
                                    if(!screen.equals("0#0")){
                                        Message message=new Message();
                                        message.what=1;
                                        message.obj=screen;
                                        handler.sendMessage(message);
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

            }
        });

        behind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                isPlaying=false;
//                player.release();
                Watch_WAN_Activity.this.finish();
            }
        });

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.INVISIBLE);
                player.seekTo(pause_position,MediaPlayer.SEEK_CLOSEST);
                player.start();
                time_duration.setText(Public_method.getTime(mp.getDuration()));
                pause.setBackground(getResources().getDrawable(R.drawable.zanting));
                player.setLooping(true);
                new Thread() {

                    @Override
                    public void run() {
                        try {
                            isPlaying = true;
                            while (isPlaying) {
                                int current = player.getCurrentPosition();
                                int duration=player.getDuration();
                                S_position=current*100/duration;
                                //System.out.println("位置："+S_position+"暂停："+S_pause+"退出："+S_back);
                                seekBar.setProgress(S_position);
                                if(connected){
                                    WebTool.touchHtml(Public_data.url+"send_status.php?connect_code="+connect_code+"&position="+S_position+"&pause="+S_pause+"&back="+Public_data.back);
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
            @RequiresApi(api = Build.VERSION_CODES.O)
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                pause_position=player.getCurrentPosition()+3000;
                player.seekTo(pause_position,MediaPlayer.SEEK_CLOSEST);
                if(!player.isPlaying())pause.setBackground(getResources().getDrawable(R.drawable.zanting));
                player.start();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                pause_position=player.getCurrentPosition()-3000;
                player.seekTo(pause_position,MediaPlayer.SEEK_CLOSEST);
                if(!player.isPlaying())pause.setBackground(getResources().getDrawable(R.drawable.zanting));
                player.start();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                pause_position=player.getCurrentPosition()-3000;
                player.seekTo(pause_position,MediaPlayer.SEEK_CLOSEST);
                if(!player.isPlaying())pause.setBackground(getResources().getDrawable(R.drawable.zanting));
                player.start();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                time_postion.setText(Public_method.getTime(player.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress=seekBar.getProgress();
                int position=progress*player.getDuration()/100;
                player.seekTo(position,MediaPlayer.SEEK_CLOSEST);
                pause_position=position;
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

    private void generateQrCode(String msg) {
        Bitmap bitmap = QrCodeGenerator.getQrCodeImage(msg, imgQrcode.getWidth(), imgQrcode.getHeight());
        if (bitmap == null) {
            Toast.makeText(this, "生成二维码出错", Toast.LENGTH_SHORT).show();
            imgQrcode.setImageBitmap(null);
        } else {
            imgQrcode.setImageBitmap(bitmap);
            imgQrcode.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        S_back=true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WebTool.touchHtml(Public_data.url+"send_back.php?connect_code="+connect_code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        wait_connect=false;
        isPlaying=false;
        player.release();
        super.onDestroy();
    }
}