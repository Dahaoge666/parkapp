package com.example.parkapp.Thread;

import android.util.Log;

import com.example.parkapp.Bean.PredictBean;
import com.example.parkapp.Bean.ReserveBean;
import com.example.parkapp.GsonUtils;
import com.example.parkapp.OkHttp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ReserveThread extends Thread {
    public String name;
    public Integer capacity;
    public Integer occupy;
    public Integer remain;
    public String atitude;
    public Double latitude;
    public Double longitude;
    public String price_info;
    public String time_use;
    public Double distance;
    private String params;
    public ReserveThread(String params) {
        this.params = params;
    }
    public void run() {
        OkHttp http = new OkHttp();
        GsonUtils gsonUtils = new GsonUtils();
        try {
            Document doc = Jsoup.connect("http://182.92.219.51:8000/reserve"+params).get();
            String body = doc.body().text();
            ReserveBean reserveBean = gsonUtils.parserJsonToReserveData(body);
            name = reserveBean.getName();
            capacity = reserveBean.getCapacity();
            occupy = reserveBean.getOccupy();
            remain = reserveBean.getRemain();
            atitude = reserveBean.getAtitude();
            price_info = reserveBean.getPrice_info();
            time_use = reserveBean.getTime_use();
            distance = reserveBean.getDistance();
            latitude = Double.valueOf(atitude.split(",")[0]);
            longitude = Double.valueOf(atitude.split(",")[1]);
        }catch(IOException e) {
            Log.d("ceshi1", e.toString());
        }
    }

}
