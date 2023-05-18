package com.PTKaBC;

/*
 * @authors: Seb White, Benji Cresswell
 * @year: 2023
 * This is the main class of the project.
 */
 import java.io.*;
 import java.net.*;

public class Main {
    public static void main(String[] args) {
        try {
            Socket mySocket = new Socket("localhost", 26695); // Create a new socket
            DataOutputStream ISay = new DataOutputStream(mySocket.getOutputStream()); // Create an output stream
            String msg = "!close";
            System.out.println("Sending: " + msg);
            ISay.writeUTF(msg); // write the message
            ISay.flush(); // send the message
            // Close the connection
            ISay.close();
            mySocket.close();
        } catch (Exception e) {
            System.out.println(e); // Oh no an error
        }
    }
}