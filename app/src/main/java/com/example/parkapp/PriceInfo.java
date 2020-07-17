package com.example.parkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PriceInfo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priceinfo);
        Intent intent = getIntent();
        String price_info = intent.getStringExtra("price_info");
        Log.d("ceshi", price_info);
        TextView info = findViewById(R.id.info);
        info.setText(price_info);
    }
}
