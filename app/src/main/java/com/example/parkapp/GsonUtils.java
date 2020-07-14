package com.example.parkapp;

import com.example.parkapp.Bean.*;
import com.google.gson.Gson;

public class GsonUtils {
    public static GetBean parserJsonToGetData(String jsonStr) {
        Gson gson = new Gson();
        GetBean data = gson.fromJson(jsonStr, GetBean.class);
        return data;
    }

    public static ListBean parserJsonToListData(String jsonStr) {
        Gson gson = new Gson();
        ListBean data = gson.fromJson(jsonStr, ListBean.class);
        return data;
    }

    public static NormalBean parserJsonToNormalData(String jsonStr) {
        Gson gson = new Gson();
        NormalBean data = gson.fromJson(jsonStr, NormalBean.class);
        return data;
    }

    public static ParkdataBean parserJsonToParkData(String jsonStr) {
        Gson gson = new Gson();
        ParkdataBean data= gson.fromJson(jsonStr, ParkdataBean.class);
        return data;
    }

    public static PredictBean parserJsonToPredictData(String jsonStr) {
        Gson gson = new Gson();
        PredictBean data= gson.fromJson(jsonStr, PredictBean.class);
        return data;
    }

    public static ReserveBean parserJsonToReserveData(String jsonStr) {
        Gson gson = new Gson();
        ReserveBean data= gson.fromJson(jsonStr, ReserveBean.class);
        return data;
    }


}
