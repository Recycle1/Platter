package com.example.platter1;

import androidx.appcompat.app.AppCompatActivity;


import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;


public class Watch_R_2Activity extends AppCompatActivity {

    private String path;
    private VideoView mVideoView;
    private Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_r2);
        mVideoView =findViewById(R.id.videoView);
        btn1=findViewById(R.id.btn1);
        path="https://www.recycle11.top/upload/video_16506460493325341.mp4";
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.setVideoPath(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Mp3"+"/123.mp4");
                //mVideoView.setVideoURI(Uri.parse(path));
                mVideoView.setMediaController(new MediaController(Watch_R_2Activity.this));
                mVideoView.requestFocus();
            }
        });
        //path = "rtmp://101.201.109.91:1935/live/livestream";//这里写你自己的拉流地址
        //path="https://www.recycle11.top/upload/"+getIntent().getStringExtra("path");

        //path="http://101.201.109.91/upload/video_16506331791675855.mp4";
        //mVideoView.setVideoPath(path);
//        mVideoView.setVideoURI(Uri.parse(path));
//        mVideoView.setMediaController(new MediaController(this));
//        mVideoView.requestFocus();
//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.start();
//            }
//        });

        try {
            URL url=new URL(path);
            DownLoad(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        mVideoView.setOnPreparedListener(new io.vov.vitamio.MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(io.vov.vitamio.MediaPlayer mp) {
                mp.setPlaybackSpeed(1.0f);
                //mp.start();
            }
        });
    }
    public void DownLoad(final URL url)
    {

        //开启线程下载数据
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(2000);
                    //获取SDCard的路径
                    String path= Environment.getExternalStorageDirectory().getAbsolutePath().toString();
                    //新建文件
                    File file=new File(path+"/Mp3");
                    //判段文件是否存在
                    if(!file.exists())
                    {
                        //创建目录
                        file.mkdir();
                    }
                    //创建文件，并起名为a.mp3
                    File files=new File(file.getAbsolutePath(),"123.mp4");
                    @SuppressWarnings("resource")
                    //用来保存读取到的网络数据，保存到文件中
                    FileOutputStream fileoutputstream=new FileOutputStream(files) ;
                    //获取httpURLConnection
                    HttpURLConnection http=(HttpURLConnection) url.openConnection();
                    //设置请求方式
                    http.setRequestMethod("GET");
                    //设置连接超时时间
                    http.setConnectTimeout(5000);
                    //如果连接成功读取网络数据
                    //System.out.println("下载成功");
                    if(http.getResponseCode()==200)
                    {
                        System.out.println("下载成功");
                        //得到HttpURLConnection的输入流对象，用拿来读取网络中的数据
                        InputStream inputstream=http.getInputStream();
                        //调用WebTools中的getData方法并得到数据
                       byte[] data=WebTools.getData(inputstream);
                       System.out.println(data.length);
                        //把数据写入到文件中
                        fileoutputstream.write(data);
                        System.out.println("下载成功");
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }).start();


    }

}