package com.example.parkapp.Thread;

import org.jsoup.Jsoup;
import java.io.IOException;

public class SetThread extends Thread {
    private String params;
    public SetThread(String params) {
        this.params = params;
    }
    public void run() {
        try {
            Jsoup.connect("http://182.92.219.51:8000/set"+params).get();
        }catch(IOException e) {

        }
    }
}
