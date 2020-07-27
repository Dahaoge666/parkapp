package com.example.parkapp.Thread;

import com.example.parkapp.Bean.ParkdataBean;
import com.example.parkapp.GsonUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
public class ParkDataThread extends Thread {

    public ParkdataBean[] parkDataBean;
    public void run() {
        GsonUtils gsonUtils = new GsonUtils();
        try {
            Document doc = Jsoup.connect("http://182.92.219.51:8000/park_data").get();
            String body = doc.body().text();
            parkDataBean = gsonUtils.parserJsonToParkData(body);
        }catch(IOException e) {

        }
    }
}
