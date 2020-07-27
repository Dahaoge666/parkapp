package com.example.parkapp.Thread;

import org.jsoup.Jsoup;
import java.io.IOException;

public class InitThread extends Thread {
    public void run() {
        try {
            Jsoup.connect("http://182.92.219.51:8000/init_setting").get();
        }catch(IOException e) {

        }
    }
}