package com.example.parkapp;

import java.io.IOException;

import okhttp3.*;


public class OkHttp {

    OkHttpClient client = new OkHttpClient();

    public String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }




}
