package com.example.platter1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class P_Server extends Thread{
    Socket socket = null;
    private ServerSocket server; // 声明ServerSocket对象
//    private DataOutputStream out = null; // 创建流对象
//    private DataInputStream in = null; // 创建流对象
    OutputStream os = null;
    File file = null;
    FileInputStream fis = null;
    byte[] bytes;
    DataOutputStream dos=null;
    DataInputStream dis=null;
    //boolean chuanshu=false;
    P_Server(File file){
        this.file=file;
    }
    public void run() {
        try {
            server = new ServerSocket(9999); // 实例化Socket对象
            System.out.println("服务器套接字已经创建成功\n"); // 输出信息
            while (true) { // 如果套接字是连接状态
                socket = server.accept(); // 实例化Socket对象
                System.out.println("客户机连接成功......\n"); // 输出信息
                System.out.println(1235);
                //out = new DataOutputStream(socket.getOutputStream());// 获得输出流对象
                //in = new DataInputStream(socket.getInputStream());// 获得输入流对象
//                getClient(); // 调用getClient()方法
//                socket.close();
                getClient(); // 调用getClient()方法
                //in.close();
                //out.close();
                socket.close();
                //chuanshu=true;
            }

        } catch (Exception e) {
            e.printStackTrace(); // 输出异常信息
        }
    }

    private void getClient() {
        try {
            System.out.println(1235);
            //socket = new Socket("192.168.2.236", 9999);
            os = socket.getOutputStream();
            dos = new DataOutputStream(os);
            fis=   new FileInputStream(file);
            dis = new DataInputStream(socket.getInputStream());
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
            System.out.println(WatchActivity.Share_width+"\n"+WatchActivity.Share_height);
            int len=-1;
            while ((len = fis.read(bytes)) != -1) {// 将视频文件读取到字节数组
                dos.write(bytes);// 将字节数组写入输出流
            }
            dos.flush();
            WatchActivity.Read_All=dis.readBoolean();
            fis.close();
            dos.close();
            dis.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}
