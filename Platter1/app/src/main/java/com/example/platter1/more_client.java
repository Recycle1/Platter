package com.example.platter1;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class more_client
{
    //static Socket server;
    private DataOutputStream dos = null; // 创建流对象
    private DataInputStream dis = null; // 创建流对象
    private Socket socket; // 声明Socket对象socket
    private long lengths = -1; // 图片文件的大小
    static boolean chuanshu1=false;
    int lot=500*1024;

    public more_client(Handler mHandler)
    {
        try {
            socket = new Socket(LoadingActivity.ip,9999);
            dos = new DataOutputStream(socket.getOutputStream());// 获得输出流对象
            dis = new DataInputStream(socket.getInputStream());// 获得输入流对象
            //LoadingActivity.jindu.setText("搭建");
            mHandler.sendEmptyMessage(1);
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

            for (int i = 0; i < bt.length; i = i + lot, p = p + lot) {
                if (i + lot > bt.length) {
                    dis.readFully(bt, i, bt.length - i);
                    Message message=new Message();
                    message.what=2;
                    message.arg1= (int) (p * 100 / bt.length);
                    mHandler.sendMessage(message);
                    System.out.println(p * 100 / bt.length + "%");
                } else {
                    dis.readFully(bt, i, lot);
                    Message message=new Message();
                    message.what=2;
                    message.arg1= (int) (p * 100 / bt.length);
                    mHandler.sendMessage(message);
                }
            }

            dos.writeBoolean(true);
            dos.flush();
            File file;
            String name=Public_data.getUniqueKey()+"."+LoadingActivity.name.split("\\.",2)[1];

            if (MainActivity.vis > 9) {
                System.out.println("名称"+name);
                file = new File("sdcard//"+ "/Platter/" + name);
            } else {
                file = new File("sdcard//" + "Platter/" + name);
            }
            File file_lib=new File("sdcard//"+Environment.getExternalStorageDirectory().getAbsolutePath(),"Platter");
            File filename=new File("sdcard//"+Environment.getExternalStorageDirectory().getAbsolutePath(),"Platter/"+name);
            if(!file_lib.exists()){
                file_lib.mkdirs();
                try {
                    if(!filename.exists()){
                        filename.createNewFile();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(!file.exists()){
                File file_lib1=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"Platter");
                File filename1=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"Platter/"+name);
                if(!file_lib1.exists()){
                    file_lib1.mkdirs();
                    try {
                        if(!filename1.exists()){
                            filename1.createNewFile();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            FileOutputStream fOut = new FileOutputStream(file.getAbsoluteFile());
            fOut.write(bt);
            fOut.flush();
            fOut.close();
            System.out.println("文件接收完毕。");
            Public_data.file=name;

            Message message=new Message();
            message.what=2;
            message.arg1=100;
            mHandler.sendMessage(message);
            mHandler.sendEmptyMessage(0);
        } catch(IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                dis.close();// 关闭流
                dos.close();
                socket.close(); // 关闭套接字
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}