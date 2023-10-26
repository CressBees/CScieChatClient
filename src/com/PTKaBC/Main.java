package com.PTKaBC;

/*
 * @authors: Seb White, Benji Cresswell
 * @year: 2023
 * This is the main class of the project.
 */
import java.io.*;
import java.net.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main implements Runnable{
    //this controls if debug statements and the like are enabled, might make a in program control, but for now, just manually control on or off
    static boolean debugMode = false;
    static boolean clientActive = true; //making this static might cause unexpected errors later, keep an eye out.
    static final short attemptsAllowed = 5; // how many attempts for things like the for loop

    static String name;
    public static void main(String[] args) throws IOException {
        Scanner keyboardInput = new Scanner(System.in);
        System.out.println("Starting Client...");
        Socket mySocket = null;
        try {
            System.out.println("Client active");
            System.out.println("Enter !exit to quit");
            System.out.println("See Readme.txt for details and guide");

            System.out.println("Please choose your name");
            name = keyboardInput.nextLine();

            //initialise some stuff
            mySocket = new Socket("localhost", 26695);
            DataOutputStream ISay = new DataOutputStream(mySocket.getOutputStream()); // Create an output stream, sends to server
            DataInputStream IHear = new DataInputStream(mySocket.getInputStream()); //create input stream, gets response from server

            //Make a message receiver on a thread, so you can send/receive messages independently
            //IMPORTANT, there should only be one of these at any time, having multiple will cause strange errors
            System.out.println("Debug_MessageReceiver");
            Thread messageReceiverThread = new Thread(new MessageReceiver(IHear, clientActive));
            messageReceiverThread.start();

            sendInitialData(name, ISay);

            while (clientActive) {
                //Set the sent message to be the one the user wants.
                if(debugMode){System.out.println("Debug_ClientActive");}
                String msg = getMessage(keyboardInput);
                if(debugMode){System.out.println("Debug_GotMessage");}


                System.out.println("Sending: " + msg);
                ISay.writeUTF(msg); // write the message
                ISay.flush(); // send the message

                //if the user inputs a close command, ask them if they want to quit
                if (msg.equals("!exit") || (msg.equals("/exit"))) {
                    System.out.println("Do you want to quit?");
                    clientActive = endSession();
                    if(debugMode){System.out.println("Debug_Exiting");}
                    mySocket.close();
                    //close the connection
                    ISay.close();
                }
            }
            System.out.println("Shutting Down...");

        } catch (Exception e) {
            System.out.println("Debug_ClientError");
            if(mySocket != null){
                mySocket.close();
            }
            //close the connection
            System.out.println(e); // Oh no, an error
        }
    }

    //send initial info to the server so the server knows who this is
    //sends name only right now, but will send other stuff
    private static void sendInitialData(String name, DataOutputStream ISay) throws IOException {
        //send info to server
        if(debugMode){System.out.println("Debug_SendingInfo...");}
        ISay.writeUTF("/initial:"+name); // write the message
        ISay.flush(); // send the message
    }

    //gets a message from the user
    private static String getMessage(Scanner keyboardInput) {
        for (int i = 0; i < attemptsAllowed; i++) {
            if(debugMode){System.out.println("Debug_WaitingForMessage");}
            String clientMessage = keyboardInput.nextLine();

            //if client tries to use /initial, don't let them, and make them try again
            if (clientMessage.startsWith("/initial")) {
                System.out.println("Error, /initial is for server use only, please try again");
            } else {
                return clientMessage;
            }
        }
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
                messageScanner.close();
                return false; //exit
            }
            else if(clientInput.equalsIgnoreCase("n")){
                messageScanner.close();
                return true; //don't exit
            }
            else{
                System.out.println("Error, Input not recognised, please try again");
            }
        }
        //if you enter something unrecognized more than 5 times, it auto logs you out.
        System.out.println("Error, Automatically logging out");
        messageScanner.close();
        return true; //exit
    }

    @Override
    public void run() {

    }
}