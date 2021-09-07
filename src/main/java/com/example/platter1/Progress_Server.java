package com.example.platter1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Progress_Server extends Thread{
    private Socket socket;
    private ServerSocket serverSocket;
    private DataOutputStream dos;
    private DataInputStream dis;
    @Override
    public void run() {
        try {
            serverSocket=new ServerSocket(9992);
            while(true){
                socket=serverSocket.accept();
                dos=new DataOutputStream(socket.getOutputStream());
                //dis=new DataInputStream(socket.getInputStream());
                dos.writeBoolean(WatchActivity.S_back);
                dos.writeInt(WatchActivity.S_position);
                dos.writeBoolean(WatchActivity.S_pause);
                dos.flush();
                dos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            try {
//                dos.close();
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }
}
