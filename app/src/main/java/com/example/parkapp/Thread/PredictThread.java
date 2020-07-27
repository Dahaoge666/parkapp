package com.example.parkapp.Thread;

import android.util.Log;

import com.example.parkapp.Bean.PredictBean;
import com.example.parkapp.GsonUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.List;

public class PredictThread extends Thread {
    public Integer capacity;
    public Integer occupy;
    public Integer remain;
    public String atitude;
    public Double latitude;
    public Double longitude;
    public List<Integer> predict;

    private String params;
    public PredictThread(String params) {
        this.params = params;
    }

    public void run() {
        GsonUtils gsonUtils = new GsonUtils();
        try {
            Document doc = Jsoup.connect("http://182.92.219.51:8000/predict/"+params).get();
            Log.d("ceshi1", params);
            String body = doc.body().text();
            Log.d("ceshi1", body);
            PredictBean predictBean = gsonUtils.parserJsonToPredictData(body);
            capacity = predictBean.getCapacity();
            occupy = predictBean.getOccupy();
            remain = predictBean.getRemain();
            atitude = predictBean.getAtitude();
            predict = predictBean.getPredict();
            latitude = Double.valueOf(atitude.split(",")[1]);
            longitude = Double.valueOf(atitude.split(",")[0]);
        }catch(IOException e) {
            Log.d("ceshi1", e.toString());
        }
    }



}
