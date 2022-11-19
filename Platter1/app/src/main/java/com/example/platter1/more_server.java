package com.example.platter1;

import android.os.Handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class more_server{

    static ServerSocket server; // 声明ServerSocket对象
    File file = null;
    byte[] bytes;
    Handler mhandler;
    Socket client;
    boolean tmp_back;
    private int socket_num=0;

    public more_server(File file, android.os.Handler mhandler){
        try {
            this.file=file;
            this.mhandler=mhandler;
            server = new ServerSocket(9999);
            while(true) {
                tmp_back=Public_data.back;
                System.out.println("连接哈"+tmp_back);
                if(tmp_back){
                    break;
                }
                else{
                    client=server.accept();//监听
                    ChildTh child=new ChildTh(client);
                    Thread t=new Thread(child);
                    socket_num++;
                    t.start();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(client!=null){
                    client.close();
                }
                //server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    class stopthread extends Thread{

        @Override
        public void run() {
            while(true){
                if(Public_data.back){
                    try {
                        System.out.println("结束");
                        server.close();
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
    }

    class ChildTh implements Runnable {
        private Socket client;
        DataOutputStream dos;
        FileInputStream fis;
        DataInputStream dis;
        public ChildTh(Socket client) {
            this.client=client;
        }

        public void run() {
            try {
                dos = new DataOutputStream(client.getOutputStream());
                fis=   new FileInputStream(file);
                dis = new DataInputStream(client.getInputStream());
                while(true) {
                    if(!client.isClosed()){
                        System.out.println(1235);
                        bytes = new byte[(int)file.length()];
                        dos.writeUTF(file.getName());
                        dos.flush();
                        dos.writeLong((int)file.length());
                        dos.flush();
                        dos.writeUTF(WatchActivity.pixel_width+"%"+WatchActivity.pixel_height);
                        dos.flush();
                        String str;
                        String strs[]=new String[2];
                        str=dis.readUTF();
                        strs=str.split("%",2);
                        WatchActivity.Share_width=Integer.valueOf(strs[0]);
                        WatchActivity.Share_height=Integer.valueOf(strs[1]);
                        Public_data.screen.add(WatchActivity.Share_width+"#"+WatchActivity.Share_height);
                        System.out.println("共享尺寸"+WatchActivity.Share_width+"\n"+WatchActivity.Share_height);
                        int len=-1;
                        while ((len = fis.read(bytes)) != -1) {// 将视频文件读取到字节数组
                            dos.write(bytes);// 将字节数组写入输出流
                        }
                        dos.flush();
                        //WatchActivity.Read_All=dis.readBoolean();
                        if(dis.readBoolean()){
                            mhandler.sendEmptyMessage(2);
                            break;
                        }


                    }

                }

            }
            catch(Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    fis.close();
                    dos.close();
                    dis.close();
                    client.close();
                    socket_num--;
//                    if(socket_num==0){
//                        server.close();
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}