package com.example.platter1;

import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Progress_Client extends Thread{
    private Socket socket;
    private DataInputStream dis;
    @Override
    public void run() {
        try {
            sleep(4000);
            while(true){
                socket = new Socket("192.168."+LoadingActivity.ip.substring(0,1)+"."+LoadingActivity.ip.substring(1,4), 9992);
                //dos=new DataOutputStream(socket.getOutputStream());
                dis=new DataInputStream(socket.getInputStream());
                WatchActivity_R.R_back=dis.readBoolean();
                WatchActivity_R.R_position=dis.readInt();
                WatchActivity_R.R_pause=dis.readBoolean();
                dis.close();
            }
        } catch (IOException e) {
                e.printStackTrace();
        }catch (InterruptedException e) {
                e.printStackTrace();
        }
        finally {
//            try {
//                dis.close();
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }
}
