package com.dist;

import java.io.Serializable;
import java.security.MessageDigest;


public class Envelope implements Serializable {
    String type;
    String link;
    String filename;
    byte[] data;

    public Envelope(String filename, byte[] data) {
        this.type       = "file";
        this.filename   = filename;
        this.data       = data;
        this.link       = getLink(this);
    }

    public Envelope(String link, String filename) {
        this.type       = "request";
        this.filename   = filename;
        this.data       = null;
        this.link       = link;
    }

    public Envelope(String link) {
        this.type       = "request";
        this.filename   = "";
        this.data       = null;
        this.link       = link;
    }

    @Override
    public String toString() {
        return "Envelope [type=" + type + ", filename=\"" + filename + "\", link=\"" + link + "\"]";
    }

    public static String getLink(Envelope ev) {
        String tmp = "";
        MessageDigest md;        
        StringBuffer sb = new StringBuffer();

        if (ev.type != "file") {
            return tmp;
        }

        try {
            tmp = ev.data + ev.filename;
            md = MessageDigest.getInstance("MD5");
            md.update(tmp.getBytes("UTF-8"));

            for (byte b : md.digest())
                sb.append(String.format("%02x", b & 0xff));

            tmp = "rottenlink:" + sb.toString();
        }
        catch (Exception e) {
        }

        return tmp;
    } 

}