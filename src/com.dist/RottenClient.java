package com.dist;

import org.jgroups.Message;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.LinkedList;


public class RottenClient extends FileTransfer {
    List<String> seeding     = new LinkedList<String>();

    public void seed(String filename) {
        seeding.add(filename);
    }

    public void requestDownload(String link) {
        sendRequest(link);
    }

    @Override
    public void fileHandler(Message msg) {
        // aqui eu decido onde salvar meu arquivo 
        Envelope f = (Envelope) msg.getObject();

        try {
            File file = new File("out", f.path);
            file.getParentFile().mkdirs(); 
            file.createNewFile();
            FileWriter writer = new FileWriter(file);

            String s = new String(f.data, "UTF-8");

            writer.write(s);
            writer.flush();
            writer.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestHandler(Message msg) {
        // aqui eu vejo se tenho o arquivo de quem pediu e envio de volta 
        Envelope f = (Envelope) msg.getObject();

        if (seeding.contains(f.link)) {
            System.out.println("Tenho o arquivo e estou mandando.");
            sendFile(msg.dest(), f.link);
        }
        else {
            System.out.println("Não tenho o arquivo solicitado.");
        }
    }

    @Override
    protected void eventLoop() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        // Scanner myInput = new Scanner(System.in);
        // int opt;
        String line;

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
                        seed(line);
                        break;
                    case 2:
                        System.out.println("select the link you want to download");
                        line = in.readLine();
                        requestDownload(line);
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