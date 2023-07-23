package com.PTKaBC;
import java.io.*;
import java.net.*;
import java.util.*;


public class MessageReceiver extends Thread {

    //this socket will correspond with another socket on the server side in the client obj
    //serversockets on both sides, this is an unbearably stupid way of doing this, but java has forced my hand
    public void getMessage(){
        try {
            ServerSocket listenOn = new ServerSocket(78123);
            Socket mainSocket = listenOn.accept();
            String receivedMessage;
            while(clientActive){
                receivedMessage = mainSocket.
            }
        }
        catch (Exception e){
            System.out.println("Debug_MessageReceiveError");
            System.out.println(e);
        }
    }
}
