package com.example.platter1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.util.QrCodeGenerator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

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
    private TextView tv_jindu;
    private char Vision []=null;
    private Uri uri;
    private boolean isPlaying;
    private int pause_position=0;
    private int time=0;
    private static boolean visibility=true;
    private String file;
    private RelativeLayout r_control;
    private LinearLayout l_control;
    static int back_code=0;

    private Thread server=null;
    private Thread server_p=null;

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
    ImageView imgQrcode;
    private int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;

    public Handler mhandler=new Handler(new Handler.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 2:
                    ArrayList <Integer> screen_width=new ArrayList<>();
                    ArrayList <Integer> screen_height=new ArrayList<>();
                    for(String s:Public_data.screen){
                        System.out.println(s);
                        screen_width.add(Integer.valueOf(s.split("#")[0]));
                        screen_height.add(Integer.valueOf(s.split("#")[1]));
                    }
                    Intent intent=new Intent();
                    intent.setClass(WatchActivity.this,WatchActivity.class);
                    intent.putExtra("path",file);
                    intent.putExtra("connecting",true);
                    float width=1;
                    float height=1;
                    float x=0;
                    float y=0;
                    System.out.println(WatchActivity.pixel_width+"-"+Share_width+"\n"+WatchActivity.pixel_height+"-"+Share_height);
                    if(screen_height.get(1)<WatchActivity.pixel_width){
                        width=(float)screen_height.get(1)/WatchActivity.pixel_width;
                        x=(WatchActivity.pixel_width-Share_height)/2;
                    }
                    if(screen_width.get(1)<=WatchActivity.pixel_height){
                        height=(float)2*screen_width.get(1)/WatchActivity.pixel_height;
                        y=WatchActivity.pixel_height-screen_width.get(1);
                    }
                    else if(screen_width.get(1)>WatchActivity.pixel_height){
                        height=2;
                    }
                    change(width,height,x,y);
                    System.out.println("数量"+Public_data.socket_num);
                    if(Public_data.socket_num==0){
                        server_p=new Progress_Server(mhandler);
                        server_p.start();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(true){
                                    if(Public_data.back){
                                        try {
                                            System.out.println("结束");
                                            more_server.server.close();
                                            break;
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    try {
                                        sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();
                    }
                    break;
                case 3:
                    tv_jindu.setText("");
                    generateQrCode((String)msg.obj);
                    Toast.makeText(WatchActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    tv_jindu.setText("传输进度："+String.valueOf(msg.arg1)+"%");
                    break;
                case 5:
                    back_code=1;
                    if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                    break;
                case 6:
                    back_code=1;
                    if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                    break;
                case 7:
                    back_code=2;
                    if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                    break;
            }
            return false;
        }
    });

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);
        imgQrcode = findViewById(R.id.img_qrcode);
        Public_data.back=false;
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
        send= findViewById(R.id.btn_send);
        tv_jindu=findViewById(R.id.tv_jindu);
        progressBar1=findViewById(R.id.progressBar1);


        if(back_code==2){
            ArrayList <Integer> screen_width=new ArrayList<>();
            ArrayList <Integer> screen_height=new ArrayList<>();
            for(String s:Public_data.screen){
                System.out.println(s);
                screen_width.add(Integer.valueOf(s.split("#")[0]));
                screen_height.add(Integer.valueOf(s.split("#")[1]));
            }

            float width=1;
            float height=1;
            int x=0;
            int y=0;
            System.out.println(WatchActivity.pixel_width+"-"+Share_width+"\n"+WatchActivity.pixel_height+"-"+Share_height);
            if(screen_width.get(1)<WatchActivity.pixel_width){
                width=(float)screen_width.get(1)/WatchActivity.pixel_width;
                x=(WatchActivity.pixel_width-screen_width.get(1))/2;
            }
            if(screen_height.get(1)<=WatchActivity.pixel_height){
                height=(float)2*screen_height.get(1)/WatchActivity.pixel_height;
                y=WatchActivity.pixel_height-screen_height.get(1);
            }
            else if(screen_height.get(1)>WatchActivity.pixel_height){
                height=2;
            }
            change2(width,height,x,y);

        }

        if(Public_data.socket_num>1){
            ArrayList <Integer> screen_width=new ArrayList<>();
            ArrayList <Integer> screen_height=new ArrayList<>();
            for(String s:Public_data.screen){
                System.out.println(s);
                screen_width.add(Integer.valueOf(s.split("#")[0]));
                screen_height.add(Integer.valueOf(s.split("#")[1]));
            }

            switch (Public_data.socket_num){
                case 2:
                    float width=(float)sum_data(screen_width)/Public_data.screen_width;
                    float height=(float)min_data(screen_height)/Public_data.screen_height;
                    int x1=0,y1=0;
                    change2(width,height,x1,y1);
                    break;
                case 3:
                    int x=com_data_width(screen_height)/2;
                    int y=com_data_height(screen_width)/2;
                    float width1=(float)com_data_width(screen_height)/Public_data.screen_height;
                    float height1=(float)com_data_height(screen_width)/Public_data.screen_width;
                    change3(width1,height1,x,y,1,1);
                    break;

            }
        }



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
        ip = (ipAddress & 0xFF)   +"."+
                ((ipAddress >> 8 ) & 0xFF)  +"."+
                ((ipAddress >> 16 ) & 0xFF)  +"."+
                ( ipAddress >> 24 & 0xFF );

        if (back_code!=0){
            generateQrCode(ip);
            send.setVisibility(View.GONE);
            progressBar1.setVisibility(View.VISIBLE);
        }
        back_code=0;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
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
        if(savedInstanceState!=null){
            pause_position=savedInstanceState.getInt("pause_position",0);
        }
        file=getIntent().getStringExtra("path");
        connecting=getIntent().getBooleanExtra("connecting",false);
        MainActivity.width=getIntent().getIntExtra("width",WatchActivity.pixel_width);
        int width_left=getIntent().getIntExtra("width_left",0);
        MainActivity.height=getIntent().getIntExtra("height",WatchActivity.pixel_height);
        int height_top=getIntent().getIntExtra("height_top",0);
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
        System.out.println("视频："+file);
        player=new MediaPlayer();
        try {
            //player.reset();
            player.setDataSource(this,uri);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);

            lp =  new RelativeLayout.LayoutParams(MainActivity.width,MainActivity.height);
            lp.setMargins(width_left, height_top, 0, 0);
            surfaceView.setLayoutParams(lp);

            holder=surfaceView.getHolder();
            holder.addCallback(new MyCallBack());
            player.prepare();
            setlistener();
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
                Public_data.back=true;
                WatchActivity.this.finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send.setVisibility(View.GONE);
                progressBar1.setVisibility(View.VISIBLE);

                File file1=new File(file);
                if(Public_data.net_way.equals("WIFI")){
                    generateQrCode(ip);
                    server=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new more_server(file1,mhandler);
                        }
                    });
                    server.start();
                }
                else{
                    uploadFile(file);
                }

            }
        });

            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    progressBar.setVisibility(View.INVISIBLE);
                    player.seekTo(pause_position,MediaPlayer.SEEK_CLOSEST);
                    player.start();
                    time_duration.setText(getTime(mp.getDuration()));
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



    }

    private void generateQrCode(String msg) {
        Bitmap bitmap = QrCodeGenerator.getQrCodeImage(msg, imgQrcode.getWidth(), imgQrcode.getHeight());
        if (bitmap == null) {
            Toast.makeText(this, "生成二维码出错", Toast.LENGTH_SHORT).show();
            imgQrcode.setImageBitmap(null);
        } else {
            imgQrcode.setImageBitmap(bitmap);
        }
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(back_code==0){
            S_back=true;
            Public_data.back=true;
            player.release();
            for(int i=1;i<Public_data.screen.size();i++){
                Public_data.screen.remove(i);
            }
            Public_data.socket_num=0;
        }

        super.onDestroy();

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

    /* 上传文件至Server，uploadUrl：接收文件的处理页面 */
    private void uploadFile(String srcPath)
    {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    URL url = new URL("https://www.recycle11.top/re.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url
                            .openConnection();
                    httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
                    // 允许输入输出流
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setUseCaches(false);
                    // 使用POST方法
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                    httpURLConnection.setRequestProperty("Charset", "UTF-8");
                    httpURLConnection.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);

                    DataOutputStream dos = new DataOutputStream(
                            httpURLConnection.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + end);
                    String name=Public_data.getUniqueKey()+"."+srcPath.substring(srcPath.lastIndexOf("/") + 1).split("\\.",2)[1];
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""
                            + name
                            + "\""
                            + end);
                    dos.writeBytes(end);
                    FileInputStream fis = new FileInputStream(srcPath);

                    File file=new File(srcPath);

                    byte[] buffer = new byte[8192]; // 8k
                    int count = 0;
                    // 读取文件
                    int i=1;
                    while ((count = fis.read(buffer)) != -1)
                    {
                        dos.write(buffer, 0, count);
                        Message message=new Message();
                        message.what=4;
                        message.arg1= (int) ((long)(buffer.length)*100*i/file.length());
                        mhandler.sendMessage(message);
                        i++;
                    }
                    fis.close();

                    dos.writeBytes(end);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    dos.flush();

                    InputStream is = httpURLConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is, "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    String result = br.readLine();

//                    Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                    //handler.sendEmptyMessage(0);
                    Message message=new Message();
                    message.obj=name;
                    message.what=3;
                    mhandler.sendMessage(message);
                    dos.close();
                    is.close();

                } catch (Exception e)
                {
                    e.printStackTrace();
                    setTitle(e.getMessage());
                }
            }
        }).start();

    }

    void change(float width,float height,float x,float y){
        AnimationSet animationSet = new AnimationSet(true);//创建一个Animation对象  (1,1) --> (0.5,0.5)
        System.out.println("高度："+height);
        Animation animation = new ScaleAnimation(1, (float)width, 1,(float)height,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);//设置动画过程的时间
        animation.setDuration(1000);//设置动画开始时间偏移量
        animation.setStartOffset(1000);//设置重复播放的次数
        animation.setRepeatCount(0);//结束时的状态
        animation.setFillAfter(true);//将Animation对象添加到AnimationSet对象中
        animationSet.addAnimation(animation);
        surfaceView.startAnimation(animation);
        surfaceView.animate().translationX(x);
        surfaceView.animate().translationY(y);
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
        AnimationSet animationSet = new AnimationSet(true);
        Animation animation = new ScaleAnimation(1f, width, 1f, height,
                Animation.RELATIVE_TO_SELF, p1, Animation.RELATIVE_TO_SELF, p2);//设置动画过程的时间
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