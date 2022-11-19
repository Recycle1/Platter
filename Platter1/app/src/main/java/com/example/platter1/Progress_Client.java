package com.example.platter1;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Progress_Client extends Thread{
    private Socket socket;
    private DataInputStream dis;
    private byte[] bytes;
    String s_text[]=null;
    //private int thread_num=0;
    //private int socket_num=0;
    private int tmp_socket_num=0;
    private int cycle=0;
    private Handler handler;
    private String tmp_screen;

    Progress_Client(Handler handler){
        this.handler=handler;
    }

    @Override
    public void run() {
        try {
            sleep(2000);
            socket = new Socket(LoadingActivity.ip, 9992);
            dis=new DataInputStream(socket.getInputStream());
            WatchActivity_R.R_back=false;
            while(true){
                Public_data.c_socket_num=dis.readInt();
                Public_data.c_screen=dis.readUTF();
                if(cycle==0){
                    Public_data.thread_num=Public_data.c_socket_num;
                    tmp_screen=Public_data.c_screen;
                }
                if(tmp_screen.length()>Public_data.c_screen.length()){
                    String t_s[]=tmp_screen.split("%");
                    String s[]=Public_data.c_screen.split("%");

                    for(int i=1;i<=Public_data.socket_num;i++){
                        if(!t_s[i].equals(s[i])){
                            Public_data.thread_num--;
                        }
                        else if(i!=Public_data.thread_num){
                            Public_data.thread_num--;
                        }
                    }

                }
//                if((tmp_socket_num>Public_data.c_socket_num)&&(Public_data.c_socket_num<=Public_data.thread_num)){
//                    Public_data.thread_num--;
//                }
                if(cycle==0||(tmp_socket_num!=Public_data.c_socket_num)){
                    Message message=new Message();
                    message.what=1;
                    message.arg1=Public_data.c_socket_num;
                    message.arg2=Public_data.thread_num;
                    message.obj=Public_data.c_screen;
                    handler.sendMessage(message);
                }
                WatchActivity_R.R_back=dis.readBoolean();
                WatchActivity_R.R_position=dis.readInt();
                WatchActivity_R.R_pause=dis.readBoolean();

                tmp_socket_num=Public_data.c_socket_num;
                tmp_screen=Public_data.c_screen;

                cycle++;
                System.out.println("数量"+Public_data.c_socket_num);
                System.out.println("接收："+WatchActivity_R.R_position+"暂停："+WatchActivity_R.R_pause+"退出："+WatchActivity_R.R_back);
                if(Public_data.back){
                    System.out.println("退退退退退退退退退退退退退退退退退退退退");
                    break;
                }
            }

        } catch (IOException e) {
                e.printStackTrace();
        }catch (InterruptedException e) {
                e.printStackTrace();
        }
        finally {
            try {
                dis.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
