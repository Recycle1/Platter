package com.example.platter1;

import java.util.ArrayList;
import java.util.Random;

public class Public_data {

    static String file;
    static int vis;
    static String net_way="WIFI";
    static boolean back=false;
    static int screen_width;
    static int screen_height;
    static int socket_num=0;
    static int c_socket_num=0;
    static int thread_num=0;
    static String c_screen;
    static ArrayList<String> screen=new ArrayList<>();

    public static synchronized String  getUniqueKey(){

        Random random = new Random();
        Integer number = random.nextInt(9000)+1000;

        return "video_"+System.currentTimeMillis()+ String.valueOf(number);
    }
}
