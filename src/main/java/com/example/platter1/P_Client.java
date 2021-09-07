package com.example.platter1;

import android.os.Environment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class P_Client{
    private DataOutputStream dos = null; // 创建流对象
    private DataInputStream dis = null; // 创建流对象
    private Socket socket; // 声明Socket对象socket
    private long lengths = -1; // 图片文件的大小
    static boolean chuanshu=false;

    P_Client(){
        try {
            System.out.println("192.168."+LoadingActivity.ip.substring(0,1)+"."+LoadingActivity.ip.substring(1,4));
                socket = new Socket("192.168."+LoadingActivity.ip.substring(0,1)+"."+LoadingActivity.ip.substring(1,4), 9999);
                dos = new DataOutputStream(socket.getOutputStream());// 获得输出流对象
                dis = new DataInputStream(socket.getInputStream());// 获得输入流对象
                LoadingActivity.jindu.setText("搭建");
                LoadingActivity.name = dis.readUTF();// 读取文件名
                long lengths = dis.readLong();
                String str;
                String strs[]=new String[2];
                str=dis.readUTF();
                strs=str.split("%",2);
                LoadingActivity.Share_width=Integer.valueOf(strs[0]);
                LoadingActivity.Share_height=Integer.valueOf(strs[1]);
                dos.writeUTF(LoadingActivity.pixel_width+"%"+LoadingActivity.pixel_height);
                System.out.println(LoadingActivity.pixel_width+"%"+LoadingActivity.pixel_height);
                dos.flush();
                byte[] bt = new byte[(int) lengths];// 创建字节数组
                System.out.println((int) lengths);
                long p = 0;
                for (int i = 0; i < bt.length; i = i + 2 * 1024 * 1024, p = p + 2 * 1024 * 1024) {
                    if (i + 2 * 1024 * 1024 > bt.length) {
                        dis.readFully(bt, i, bt.length - i);
                        //MainActivity.v4.setText(p*100/bt.length+"%");
                        LoadingActivity.jindu.setText(p * 100 / bt.length + "%");
                        System.out.println(p * 100 / bt.length + "%");
                    } else {
                        dis.readFully(bt, i, 2 * 1024 * 1024);
                        //MainActivity.v4.setText(p*100/bt.length+"%");
                        //System.out.println(p * 100 / bt.length + "%");
                        LoadingActivity.jindu.setText(p * 100 / bt.length + "%");
                    }
                }
                System.out.println(1235);
                dos.writeBoolean(true);
                dos.flush();
                File file;
                if (MainActivity.vis > 9) {
                    file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Platter/" + LoadingActivity.name);
                } else {
                    file = new File("sdcard//" + "Platter/" + LoadingActivity.name);
                }
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                FileOutputStream fOut = new FileOutputStream(file.getAbsoluteFile());
                fOut.write(bt);
                System.out.println(1235);
                fOut.flush();
                fOut.close();
                System.out.println("文件接收完毕。");
                dis.close();// 关闭流
                dos.close();
                socket.close(); // 关闭套接字
                //MainActivity.v4.setText(100+"%");
                LoadingActivity.jindu.setText(100 + "%");
                chuanshu=false;
        } catch(IOException e){
            e.printStackTrace();
            chuanshu=true;
        }
    }
}
