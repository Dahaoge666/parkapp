package com.example.parkapp.Thread;

import com.example.parkapp.Bean.ParkdataBean;
import com.example.parkapp.GsonUtils;
import com.example.parkapp.OkHttp;

import java.io.IOException;

public class ParkDataThread extends Thread {
    public ParkdataBean[] parkDataBean;
    public void run() {
        OkHttp http = new OkHttp();
        GsonUtils gsonUtils = new GsonUtils();
        try {
            String responseData = http.run("http://mock-api.com/NnQ0W5gY.mock/park_data");
            parkDataBean = gsonUtils.parserJsonToParkData(responseData);
        }catch(IOException e) {

        }
    }




}
