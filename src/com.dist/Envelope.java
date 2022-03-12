package com.dist;

import java.io.Serializable;


public class Envelope implements Serializable {
    String type;
    String link;
    String path;
    String filename;
    byte[] data;

    public Envelope(String path, byte[] data) {
        this.type = "file";
        this.link = path; // criar uma espécie de hash ou coisa assim
        this.path = path;
        this.data = data;
    }

    public Envelope(String link, String path) {
        this.type = "request";
        this.link = link;
        this.path = path;
        this.data = null;
    }

    public Envelope(String link) {
        this.type = "request";
        this.link = link;
        this.path = "";
        this.data = null;
    }

    @Override
    public String toString() {
        return "Envelope [type=" + type + ", link=" + link + ", path=\"" + path + "\"]";
    }

}