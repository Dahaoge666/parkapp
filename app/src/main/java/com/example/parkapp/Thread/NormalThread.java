package com.example.parkapp.Thread;

import com.example.parkapp.Bean.NormalBean;
import com.example.parkapp.GsonUtils;
import com.example.parkapp.OkHttp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class NormalThread extends Thread {
    public NormalBean[] normalBean;
    private String params;
    public NormalThread(String params) {
        this.params = params;
    }
    public void run() {
        GsonUtils gsonUtils = new GsonUtils();
        try {
            Document doc = Jsoup.connect("http://182.92.219.51:8000/normal"+params).get();
            String body = doc.body().text();
            normalBean = gsonUtils.parserJsonToNormalData(body);
        }catch(IOException e) {

        }
    }
}
