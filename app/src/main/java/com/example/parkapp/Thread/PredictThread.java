package com.example.parkapp.Thread;

import com.example.parkapp.Bean.PredictBean;
import com.example.parkapp.GsonUtils;
import com.example.parkapp.OkHttp;

import java.io.IOException;
import java.util.List;

public class PredictThread extends Thread {
    public Integer capacity;
    public Integer occupy;
    public Integer remain;
    public String atitude;
    public Double latitude;
    public Double longtitude;
    public List<Integer> predict;
    public void run() {
        OkHttp http = new OkHttp();
        GsonUtils gsonUtils = new GsonUtils();
        try {
            String responseData = http.run("http://mock-api.com/NnQ0W5gY.mock/predict");



            PredictBean predictBean = gsonUtils.parserJsonToPredictData(responseData);
            capacity = predictBean.getCapacity();
            occupy = predictBean.getOccupy();
            remain = predictBean.getRemain();
            atitude = predictBean.getAtitude();
            predict = predictBean.getPredict();
            latitude = Double.valueOf(atitude.split(",")[1]);
            longtitude = Double.valueOf(atitude.split(",")[0]);
        }catch(IOException e) {

        }
    }



}
