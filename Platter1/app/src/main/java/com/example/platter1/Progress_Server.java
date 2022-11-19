package com.example.platter1;

import android.os.Handler;
import android.os.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Progress_Server extends Thread{
    private Socket client;
    private ServerSocket serverSocket;
    Handler handler;
    //private DataOutputStream dos;
    //private int socket_num=0;

    Progress_Server(Handler handler){
        this.handler=handler;
    }

    @Override
    public void run() {
        try {
            serverSocket=new ServerSocket(9992);
            System.out.println("是否退出："+WatchActivity.S_back);
            WatchActivity.S_back=false;
            Public_data.back=false;

            while(true){
                client=serverSocket.accept();
                if(Public_data.back){
                    break;
                }
                Public_data.socket_num++;
                if(Public_data.socket_num==3){
                    System.out.println();
                }
                ChildTh child=new ChildTh(client,Public_data.socket_num);
                Thread t=new Thread(child);


                t.start();
                if(Public_data.socket_num==2){
                    handler.sendEmptyMessage(5);
                }
                else if(Public_data.socket_num==3){
                    handler.sendEmptyMessage(6);
                }
                System.out.println("退出了："+Public_data.back);
                System.out.println("连接数量:"+Public_data.socket_num);

//                dos.writeBoolean(WatchActivity.S_back);
//                System.out.println("发送："+WatchActivity.S_position);
//                dos.writeInt(WatchActivity.S_position);
//                dos.writeBoolean(WatchActivity.S_pause);
//                dos.flush();


                //dos.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            try {
                //dos.close();
//                if(client!=null){
//                    client.close();
//                }
//                serverSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    class ChildTh implements Runnable {
        private Socket client;
        private boolean tmp_back;
        DataOutputStream dos;
        private int thread_num=0;
        private int tmp_socket_num=0;
        private ArrayList<String> tmp_screen;

        public ChildTh(Socket client,int socket_num) {
            this.client = client;
            thread_num=socket_num;
            tmp_socket_num=socket_num;
            tmp_screen=Public_data.screen;
        }

        @Override
        public void run() {
            try {
                dos=new DataOutputStream(client.getOutputStream());
                tmp_back=false;
                while(true){
                    tmp_back=Public_data.back;
                    //System.out.println("退出："+Public_data.back);

                    if(thread_num ==3){
                        System.out.println(Public_data.socket_num);
                    }

//                    if((tmp_socket_num>Public_data.socket_num)&&(Public_data.socket_num<=thread_num)){
//                        thread_num--;
//                    }

                    System.out.println("连接"+Public_data.socket_num+"h"+tmp_socket_num+"h"+thread_num);




                    if((Public_data.socket_num<tmp_socket_num)){

                        for(int i=1;i<=Public_data.socket_num;i++){
                            if(!tmp_screen.get(i).equals(Public_data.screen.get(i))){
                                thread_num--;
                            }
                            else if(i!=thread_num){
                                thread_num--;
                            }
                        }

//                        if(Public_data.socket_num<=thread_num){
//                            thread_num--;
//                        }
                        if(Public_data.socket_num==1){
                            handler.sendEmptyMessage(7);
                        }
                        if(Public_data.socket_num==2){
                            handler.sendEmptyMessage(5);
                        }
                        else if(Public_data.socket_num==3){
                            handler.sendEmptyMessage(6);
                        }
                    }

                    dos.writeInt(Public_data.socket_num);
                    String screen_info=new String();
                    for(int i=0;i<Public_data.screen.size();i++){
                        screen_info+=Public_data.screen.get(i);
                        if(i<Public_data.screen.size()-1){
                            screen_info+="%";
                        }
                    }
//                    for(String s:Public_data.screen){
//                        screen_info+=s+"%";
//                    }
                    dos.writeUTF(screen_info);
                    System.out.println("所有值和："+screen_info);
                    dos.writeBoolean(Public_data.back);
                    dos.writeInt(WatchActivity.S_position);
                    dos.writeBoolean(WatchActivity.S_pause);
                    System.out.println("线程"+Public_data.socket_num+"发送："+WatchActivity.S_position);
                    dos.flush();

                    tmp_socket_num=Public_data.socket_num;
                    tmp_screen=Public_data.screen;

                    if(tmp_back){
                        break;
                    }
                    sleep(500);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    dos.close();
                    client.close();
                    Public_data.socket_num--;
                    Public_data.screen.remove(thread_num);
                    System.out.println("连接数量"+Public_data.socket_num);
                    if(Public_data.socket_num==0){
                        serverSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
