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
            System.out.println("Client active");
            System.out.println("See Readme.txt for details and guide");
            Socket mySocket = new Socket("localhost", 26695); // Create a new socket
            DataOutputStream ISay = new DataOutputStream(mySocket.getOutputStream()); // Create an output stream
            while(clientActive) {
                //Set the sent message to be the one the user wants.
                String msg = getMessage();
                System.out.println("Sending: " + msg);
                ISay.writeUTF(msg); // write the message
                ISay.flush(); // send the message
                // Close the connection

                //if the user inputs a close command, ask them if they want to quit
                if(msg.equals("!exit")||(msg.equals("/exit"))){
                    clientActive = endSession();
                    mySocket.close();
                    ISay.close();
                }
            }
            System.out.println("Shutting Down...");

        } catch (Exception e) {
            System.out.println("Debug_ClientError");
            System.out.println(e); // Oh no, an error
        }
    }

    //gets a message from the user
    private static String getMessage(){
        Scanner messageScanner = new Scanner(System.in); // create scanner
        System.out.println("Input Message");
        return messageScanner.nextLine();
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