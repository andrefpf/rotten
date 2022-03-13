package com.dist;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Address;
import org.jgroups.View;
import org.jgroups.ReceiverAdapter;
import org.jgroups.util.Buffer;
import org.jgroups.util.ByteArrayDataOutputStream;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.*;
import java.io.*;


public class FileTransfer extends ReceiverAdapter {
    JChannel channel;
    int connections = 0;
    String user_name = System.getProperty("user.name", "n/a");

    public void start() {
        try {
            channel = new JChannel(); // use the default config, udp.xml
            channel.setReceiver(this);
            channel.setDiscardOwnMessages(true);
            channel.connect("RottenCluster");
            eventLoop();
            channel.close();
        } 
        catch (Exception e) {
        }
    }

    protected void eventLoop() {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            try {
                String line = in.readLine();

                if (line.toLowerCase().startsWith("exit"))
                    break;
                else
                    System.out.println("Not implemented");
            }
            catch(Exception e) {
            }
        }
    }

    public void sendRequest(String link) {
        sendRequest(null, link, "");
    }

    public void sendRequest(Address addr, String link) {
        sendRequest(addr, link, "");
    }

    public void sendRequest(Address addr, String link, String path) {        
        Envelope ev = new Envelope(link, path);
        // System.out.println("Sent: " + ev);
        sendMessage(addr, ev);
    }

    public void sendFile(Address addr, String filename) {
        try {
            byte[] buffer = readFile(filename).getBuf();            
            Envelope ev = new Envelope(filename, buffer);
            sendMessage(addr, ev);
        } 
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Address addr, Object obj) {
        try {
            Message msg = new Message(addr, obj);
            channel.send(msg);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void receive(Message msg) {
        Envelope ev = (Envelope) msg.getObject();

        // System.out.println("Received: " + ev);

        if (ev.type.equals("request")) {
            requestHandler(msg);
        }
        else if (ev.type.equals("file")) {
            fileHandler(msg);
        }
    }

    public void viewAccepted(View new_view) {
        connections++;
        // System.out.println("** connections: " + connections);
    }

    protected static Buffer readFile(String filename) throws Exception {
        File file;
        int  size;
        FileInputStream input;
        ByteArrayDataOutputStream out;
        byte[] read_buf;
        int bytes;
        
        file     = new File(filename);
        size     = (int) file.length();
        input    = new FileInputStream(file);
        out      = new ByteArrayDataOutputStream(size);
        read_buf = new byte[1024];

        while((bytes = input.read(read_buf)) > 0) {
            out.write(read_buf, 0, bytes);
        }

        return out.getBuffer();
    }

    protected void fileHandler(Message msg) {
        System.out.println("handling file");
    }

    protected void requestHandler(Message msg) {
        System.out.println("handling request");
    }
}