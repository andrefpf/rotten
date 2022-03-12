package com.dist;

import org.jgroups.Message;

import java.io.*;
import java.util.List;
import java.util.Hashtable;
import java.util.LinkedList;


public class RottenClient extends FileTransfer {
    String outputPath = "out";
    Hashtable<String, Envelope> seeding = new Hashtable<String, Envelope>();

// public
    public String seed(String filename) {
        Envelope ev;
        String link = "";

        try {
            byte[] buffer = readFile(filename).getBuf();            
            ev = new Envelope(filename, buffer);
            seeding.put(ev.link, ev);
            link = ev.link;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return link;
    }

    public void stopSeed(String link) {
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

        try {
            File file = new File("out", ev.filename);
            file.getParentFile().mkdirs(); 
            file.createNewFile();
            FileWriter writer = new FileWriter(file);

            String s = new String(ev.data, "UTF-8");

            writer.write(s);
            writer.flush();
            writer.close();
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

        System.out.println("is in dict: " + ev);

        if (ev == null) {
            System.out.println("Não tenho o arquivo solicitado.");
        }
        else {
            System.out.println("Tenho o arquivo e estou mandando.");
            sendMessage(msg.dest(), ev);
        }
    }

    @Override
    protected void eventLoop() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        String tmp;

        while(true) {
            try {
                System.out.println("Select your option: 0: exit | 1: seed | 2: download");
                line = in.readLine();            

                switch (Integer.parseInt(line)) {
                    case 0:
                        return;
                    case 1:
                        System.out.println("select the file you want to seed");
                        line = in.readLine();
                        tmp = seed(line);
                        System.out.println("The file link is: " + tmp);
                        break;
                    case 2:
                        System.out.println("select the link you want to download");
                        line = in.readLine();
                        download(line);
                        break;
                    default:
                        System.out.println("Bad input.");
                }
            }
            catch(Exception e) {
            }
        }
    }


}