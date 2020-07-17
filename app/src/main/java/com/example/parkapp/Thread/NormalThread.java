package com.example.parkapp.Thread;

import com.example.parkapp.Bean.NormalBean;
import com.example.parkapp.GsonUtils;
import com.example.parkapp.OkHttp;
import java.io.IOException;

public class NormalThread extends Thread {
    private String params;
    public NormalThread(String params) {
        this.params = params;
    }
    public NormalBean[] normalBean;
    public void run() {
        OkHttp http = new OkHttp();
        GsonUtils gsonUtils = new GsonUtils();
        try {
            String responseData = http.run("http://mock-api.com/NnQ0W5gY.mock/normal"+params);
            normalBean = gsonUtils.parserJsonToNormalData(responseData);
        }catch(IOException e) {

        }
    }
}
