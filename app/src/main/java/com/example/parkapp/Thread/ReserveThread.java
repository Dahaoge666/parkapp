package com.example.parkapp.Thread;

import com.example.parkapp.Bean.ReserveBean;
import com.example.parkapp.GsonUtils;
import com.example.parkapp.OkHttp;
import java.io.IOException;

public class ReserveThread {
    public Integer capacity;
    public Integer occupy;
    public Integer remain;
    public String atitude;
    public Double latitude;
    public Double longtitude;
    public String price_info;
    public String time_use;
    public Integer distance;
    public void run() {
        OkHttp http = new OkHttp();
        GsonUtils gsonUtils = new GsonUtils();
        try {
            String responseData = http.run("http://mock-api.com/NnQ0W5gY.mock/short");
            ReserveBean reserveBean = gsonUtils.parserJsonToReserveData(responseData);
            capacity = reserveBean.getCapacity();
            occupy = reserveBean.getOccupy();
            remain = reserveBean.getRemain();
            atitude = reserveBean.getAtitude();
            price_info = reserveBean.getPrice_info();
            latitude = Double.valueOf(atitude.split(",")[0]);
            longtitude = Double.valueOf(atitude.split(",")[1]);
            time_use = reserveBean.getTime_use();
            distance = reserveBean.getDistance();
        }catch(IOException e) {

        }
    }


}
