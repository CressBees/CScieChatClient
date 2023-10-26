package com.PTKaBC;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;


public class MessageReceiver implements Runnable{
    //this controls if debug statements and the like are enabled, might make a in program control, but for now, just manually control on or off
    boolean debugMode = false;

    DataInputStream inputStream;
    public MessageReceiver(DataInputStream IHear){
        inputStream = IHear;
    }

    @Override
    public void run() {
        try {
            if(debugMode){System.out.println("Debug_ThreadStarted");}

            //Read message and then print it
            //TODO: make this good
            while(true){
                if(debugMode){System.out.println("Debug_PrintMessage");}
                String receivedMessage = inputStream.readUTF();
                System.out.println(receivedMessage);
            }
        }
        catch (Exception e){
            System.out.println("Debug_MessageReceiveError");
            System.out.println(e);
        }
    }
}
