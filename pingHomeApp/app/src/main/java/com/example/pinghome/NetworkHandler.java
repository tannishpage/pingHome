package com.example.pinghome;

import android.net.Network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * NetworkHandler is a singleton class that is supposed to handle all the network communications
 * to the server.
 */
public class NetworkHandler {

    Socket s;
    DataOutputStream out;
    BufferedReader input;
    private int sendInterval = 60;

    private static NetworkHandler instance;

    private NetworkHandler(){}

    public static NetworkHandler getInstance(){
        if (instance == null){
            instance = new NetworkHandler();
        }
        return instance;
    }

    public int getSendInterval() {
        return sendInterval;
    }

    public void setSendInterval(int sendInterval) {
        this.sendInterval = sendInterval;
    }

    public void connect(String ipaddress, int port){

    }

    public void disconnect(){

    }

    public void send(StringBuffer data){

    }
}
