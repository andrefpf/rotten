package com.dist;

import java.util.Map;
import java.io.*;
import java.util.List;
import java.util.ArrayList;


public class Frontend {
    RottenClient rotten;

    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));


    public Frontend(RottenClient r) {
        rotten = r;
    }

    public void mainMenu() {
        while(true) {
            try {
                clearScreen();

                System.out.print(
                    "ROTTEN             \n" +
                    "                   \n" +
                    "[0] Exit           \n" + 
                    "[1] New Seed       \n" +
                    "[2] Remove Seed    \n" +
                    "[3] Download       \n" +
                    "                   \n" + 
                    "> "
                );

                // System.out.flush();

                String line = in.readLine();            
                int opt = Integer.parseInt(line);
                
                switch (opt) {
                    case 0:
                        clearScreen();
                        return;
                    case 1:
                        newSeedScreen();
                        break;
                    case 2:
                        removeSeedScreen();
                        break;
                    case 3:
                        downloadScreen();
                        break;
                    default:
                        badInput();
                }
            }
            catch(Exception e) {
            }
        }
    }

    public void newSeedScreen() {
        String line;

        try {
            clearScreen();

            System.out.print(
                "ROTTEN                     \n" +
                "                           \n" +
                "Type the path to the seed: \n" +
                "(let it empty to cancel)   \n" +
                "> "
            );

            line = in.readLine();     
            if (line.isEmpty())
                return;
            rotten.seed(line);

            System.out.println();
            showSeedList(rotten);

            System.out.print(
                "                         \n" +
                "Press enter to continue: \n"
            );
            line = in.readLine();
        }
        catch(Exception e) {
        }
    }

    public void removeSeedScreen() {
        int opt;
        String line;
        Envelope ev;

        try {
            clearScreen();

            List<Envelope> links = new ArrayList<Envelope>(rotten.seeding.values());
            
            for (int i = 0; i < links.size(); i++) {
                ev = links.get(i);
                System.out.println(i + " - [filename = \"" + ev.filename + "\", link = \"" + ev.link + "\"]");
            }

            System.out.print(
                "                                       \n" +
                "Select the seed you want to remove:    \n" +
                "(let it empty to cancel)               \n" +
                "> "
            );
 
            line = in.readLine();     
            if (line.isEmpty())
                return;

            opt = Integer.parseInt(line);

            if (opt < 0 || opt >= links.size())
                badInput();
            rotten.removeSeed(links.get(opt).link);

            System.out.print(
                "                         \n" +
                "Press enter to continue: \n"
            );
            line = in.readLine();

        }
        catch(Exception e) {
        }
    }

    public void downloadScreen() {
        String line;

        try {
            clearScreen();

            System.out.print(
                "ROTTEN                                 \n" +
                "                                       \n" +
                "Type the link you want to download:    \n" +
                "(let it empty to cancel)               \n" +
                "> "
            );

            line = in.readLine();     
            if (line.isEmpty())
                return;
            rotten.download(line);

        }
        catch(Exception e) {
        }
    }

    public static void showSeedList(RottenClient r) {
        Envelope ev;
        System.out.println("Seed list:");
        for (Map.Entry<String, Envelope> set : r.seeding.entrySet()) {
            ev = set.getValue();
            System.out.println("[filename = \"" + ev.filename + "\", link = \"" + ev.link + "\"]");
        }
    }

    public static void badInput() {
        System.out.println("Bad input.");
    }

    public static void clearScreen() {  
        // I have no idea if it is reliable, but it works in my manjaro linux 
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  
}