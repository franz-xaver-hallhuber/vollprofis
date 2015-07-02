package com.example.carol.inozzle;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Carol on 28.06.2015.
 */
public class Client implements Runnable {
    String ip = "192.168.188.27";
    int port = 5005;

    Socket client;
    BufferedReader inS;

    ByteArrayOutputStream bos;

    String inLine;

    @Override
    public void run() {
        try {
            Log.i("Client","Attempt Connect");

            bos = new ByteArrayOutputStream(512);
            client = new Socket(ip, port);
            Log.i("Client", "Created Socket");

            //inS = new BufferedReader(new InputStreamReader(client.getInputStream()));
            InputStream inStr = client.getInputStream();
            Log.i("Client", "Create Buffered Reader");

            Log.i("Client", "Start receiving data");

            byte[] temp = new byte[512];
            int bytesread;

            while ((bytesread = inStr.read(temp)) != -1) {
                bos.write(temp, 0, bytesread);
                Log.i("Received from Server:", bos.toString("utf-8"));
                bos.reset();
            }

            Log.i("Client","Connection CLose");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
