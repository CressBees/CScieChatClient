package com.PTKaBC;
import java.io.*;
import java.net.*;


public class MessageReceiver implements Runnable{
    public MessageReceiver(DataInputStream IHear, DataOutputStream ISay, boolean clientActive){
        try {
            System.out.println("Debug_ThreadStarted");

            String receivedMessage;

            //Read message and then print it
            //TODO: make this good
            while(!clientActive){
                System.out.println("Debug_PrintMessage");
                receivedMessage = IHear.readUTF();
                System.out.println(receivedMessage);
            }
        }
        catch (Exception e){
            System.out.println("Debug_MessageReceiveError");
            System.out.println(e);
        }
    }

    @Override
    public void run() {

    }
}
