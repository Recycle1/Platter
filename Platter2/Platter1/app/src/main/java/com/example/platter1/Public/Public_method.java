package com.example.platter1.Public;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.example.platter1.Surface_View;

import java.util.Random;

public class Public_method {

    static public String get_ip(Context context){
        WifiManager wifiManager =(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip=null;
        ip = (ipAddress & 0xFF)   +"."+
                ((ipAddress >> 8 ) & 0xFF)  +"."+
                ((ipAddress >> 16 ) & 0xFF)  +"."+
                ( ipAddress >> 24 & 0xFF );
        return ip;
    }

    public static synchronized String getUniqueKey(){

        Random random = new Random();
        Integer number = random.nextInt(9000)+1000;

        return "video_"+System.currentTimeMillis()+ String.valueOf(number);
    }

    public static String getRandom(){

        Random random = new Random();
        Integer number1 = random.nextInt(1000)+2;
        Integer number2 = random.nextInt(1000)+3;

        return String.valueOf(number1)+String.valueOf(number2);
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

    public static void change_watch(float width, float height, float x, float y, Surface_View surfaceView){
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
    public static void change_watch_r(float width,float height,float x,float y,Surface_View surfaceView){
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

}
