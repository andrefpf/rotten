package com.dist;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Address;
import org.jgroups.View;
import org.jgroups.ReceiverAdapter;
import org.jgroups.util.*;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.*;
import java.io.*;


public class FileTransfer extends ReceiverAdapter {

    JChannel channel;

    String user_name = System.getProperty("user.name", "n/a");

    public void start() {
        try {
            channel = new JChannel(); // use the default config, udp.xml
            channel.setReceiver(this);
            channel.setDiscardOwnMessages(true);
            channel.connect("ChatCluster");
            eventLoop();
            channel.close();
        } 
        catch (Exception e) {
        }
    }

    private void eventLoop() {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            try {
                String line = in.readLine();

                if (line.toLowerCase().startsWith("exit"))
                    break;

                requestFile(line);
            }
            catch(Exception e) {
            }
        }
    }

    public void requestFile(String link) {
        String message;
        message = "(request)" + "(" + link + ")";
        sendMessage(null, message);
    }

    public void sendMessage(Address addr, String str) {
        try {
            Message msg = new Message(addr, str);
            channel.send(msg);
        }
        catch(Exception e) {
        }
    }

    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    public void receive(Message msg) {
        System.out.println(msg.getSrc() + ": " + msg.getObject());
        String data = msg.getObject().toString();
        

        // List<String> tokens = extractTokens(data);
        // System.out.println(tokens);
        // for (int i = 0; i < tokens.size(); i++) {
        //     System.out.println(tokens.get(i));
        // }

        if (data.startsWith("(request)")) {
            requestHandler(msg);
        }

        if (data.startsWith("(file)")) {
            fileHandler(msg);
        }
    }

    public static void main(String[] args) throws Exception {
        new FileTransfer().start();
    }


    public static void fileHandler(Message msg) {
        System.out.println("handling file");
    }

    public static void requestHandler(Message msg) {
        System.out.println("handling request");
    }


    public void sendFile(String filename) {
        try {
            Buffer buffer = readFile(filename);
            Message msg = new Message(null, "(file)" + buffer);
            channel.send(msg);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> extractTokens(String str) {
        List<String> tokens = new ArrayList<String>();
        Matcher m = Pattern.compile("\\((.*?)\\)").matcher(str);
      
        while(m.find()) {   
            tokens.add(m.group(1));
        }

        return tokens;
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
}