package com.dist;

import org.jgroups.Message;

import java.io.*;
import java.util.Hashtable;
import java.util.LinkedList;
import java.nio.file.Paths;
import java.io.FileNotFoundException;

public class RottenClient extends FileTransfer {
    String outputPath = "./out/";
    Hashtable<String, Envelope> seeding = new Hashtable<String, Envelope>();
    LinkedList<String> downloaded = new LinkedList<String>();

// public
    public String seed(String path) {
        Envelope ev;
        String link = "";

        try {
            byte[] buffer = readFile(path).getBuf();      
            String filename = Paths.get(path).getFileName().toString();
            
            System.out.println("Path: " + path);
            System.out.println("Filename: " + filename);

            ev = new Envelope(filename, buffer);
            seeding.put(ev.link, ev);
            link = ev.link;
        }
        catch(Exception FileNotFoundException) {
            System.out.println("File \"" + path + "\" not found.");
        }
        
        return link;
    }

    public void removeSeed(String link) {
        seeding.remove(link);
    }

    public void download(String link) {
        sendRequest(link);
    }

    public void setOutputPath(String newOutput) {
        outputPath = newOutput;
    }

// protected
    @Override
    protected void fileHandler(Message msg) {
        // aqui eu decido onde salvar meu arquivo 
        Envelope ev = (Envelope) msg.getObject();
        
        if (downloaded.contains(ev.link)) {
            return;
        }

        try {
            File file = new File(outputPath, ev.filename);
            file.getParentFile().mkdirs(); 
            file.createNewFile();

            FileOutputStream output = new FileOutputStream(file);
            output.write(ev.data);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void requestHandler(Message msg) {
        // aqui eu vejo se tenho o arquivo de quem pediu e envio de volta 
        Envelope ev; 
        
        ev = (Envelope) msg.getObject();        // request
        ev = seeding.get(ev.link);              // file

        if (ev != null) {
            sendMessage(msg.dest(), ev);
        }
    }

    @Override
    protected void eventLoop() {
        Frontend f = new Frontend(this);
        f.mainMenu();
    }


}