package com.PTKaBC;

/*
 * @authors: Seb White, Benji Cresswell
 * @year: 2023
 * This is the main class of the project.
 */
 import java.io.*;
 import java.net.*;
import java.util.Scanner;

public class Main {
    static boolean clientActive = true; //making this static might cause unexpected errors later, keep an eye out.
    static final short attemptsAllowed = 5; // how many attempts for things like the for loop
    public static void main(String[] args) {
        System.out.println("Starting Client...");
        try {
            //this server socket handles msgs received by the server
            ServerSocket listenOn = new ServerSocket(0);

            //Make a message receiver on a thread so you can send/receive messages independently
            //IMPORTANT, there should only be one of these at any time, having multiple will cause strange errors
            System.out.println("Debug_MessageReceiver");
            Thread messageReceiverThread = new Thread();
            messageReceiverThread.start();

            System.out.println("Client active");
            //TODO: do readme
            System.out.println("See Readme.txt for details and guide");

            Socket mySocket = new Socket("localhost", 26695); // Create a new socket
            DataOutputStream ISay = new DataOutputStream(mySocket.getOutputStream()); // Create an output stream, sends to server
            DataInputStream IHear = new DataInputStream(mySocket.getInputStream()); //create input stream, gets response from server
            sendInitialData(ISay, listenOn);
            while(clientActive) {
                //Set the sent message to be the one the user wants.
                String msg = getMessage();
                System.out.println("Sending: " + msg);
                ISay.writeUTF(msg); // write the message
                ISay.flush(); // send the message

                String test = IHear.readUTF();
                System.out.println(test);

                //if the user inputs a close command, ask them if they want to quit
                if(msg.equals("!exit")||(msg.equals("/exit"))){
                    clientActive = endSession();
                    mySocket.close();
                    //close the connection
                    ISay.close();
                }
            }
            System.out.println("Shutting Down...");

        } catch (Exception e) {
            System.out.println("Debug_ClientError");
            System.out.println(e); // Oh no, an error
        }
    }

    //send initial info to the server so the server knows who this is
    //rn it only includes client pn, but in future will have username and whatever else
    private static void sendInitialData(DataOutputStream ISay, ServerSocket listenOn) throws IOException {
        //send the value of the port the client will receive messages from to the server
        int port = listenOn.getLocalPort();
        String msg = String.valueOf(port);
        System.out.println("Debug_SendingInfo...");
        ISay.writeUTF("/initial:"+msg); // write the message
        ISay.flush(); // send the message
    }

    //gets a message from the user
    private static String getMessage() {
        Scanner messageScanner = null;
        for (int i = 0; i < attemptsAllowed; i++) {
            messageScanner = new Scanner(System.in);
            System.out.println("Input Message");
            String msg = messageScanner.nextLine();

            if (msg.startsWith("/initial")) {
                System.out.println("Error, /initial is for server use only, please try again");
                msg = null;
            } else {
                messageScanner.close();
                return msg;
            }
        }
        messageScanner.close();
        System.out.println("Error: Too many failed attempts");
        return ("FailedAttemptsError");
    }
    //asks if clients wants to stop
    private static boolean endSession(){
        Scanner messageScanner = new Scanner(System.in); // create scanner
        for(int i = 0; i <= attemptsAllowed; i++){
            System.out.println("Do you want to quit? (Y/N)"); //y for yes, n for no.
            String clientInput = messageScanner.nextLine();
            if(clientInput.equalsIgnoreCase("y")){
                return false; //exit
            }
            else if(clientInput.equalsIgnoreCase("n")){
                return true; //don't exit
            }
            else{
                System.out.println("Error, Input not recognised, please try again");
            }
        }
        //if you enter something unrecognized more than 5 times, it auto logs you out.
        System.out.println("Error, Automatically logging out");
        return true; //exit
    }
}