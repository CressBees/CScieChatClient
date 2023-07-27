package com.PTKaBC;
import java.io.*;
import java.net.*;


public class MessageReceiver implements Runnable{
    //this socket will correspond with another socket on the server side in the client obj
    //serversockets on both sides, this is an unbearably stupid way of doing this, but java has forced my hand
    public MessageReceiver(ServerSocket listenOn){
        try {
            System.out.println("Debug_ThreadStarted");
            Socket mainSocket = listenOn.accept();
            DataInputStream readFromListenOn = new DataInputStream(mainSocket.getInputStream());

            String receivedMessage;

            //Read message and then print it
            //TODO: make this good
            while(true){
                System.out.println("Debug_PrintMessage");
                receivedMessage = readFromListenOn.readUTF();
                System.out.println(receivedMessage);
            }
        }
        catch (Exception e){
            System.out.println("Debug_MessageReceiveError");
            System.out.println(e);
        }
    }
    public void run(){

    }
}
