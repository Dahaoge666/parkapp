package com.example.parkapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.parkapp.Bean.NormalBean;
import com.example.parkapp.Thread.NormalThread;

public class Normal extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        Intent intent = getIntent();//声明一个对象，并获得跳转过来的Intent对象
        String name=intent.getStringExtra("name");
        Double latitude=intent.getDoubleExtra("latitude",'0');
        Double longtitude=intent.getDoubleExtra("longtitude",'0');
        TextView destination=findViewById(R.id.destination);
        destination.setText("   "+name);
        setPage();
    }


    private void intentPage(final Integer dataNum){
        NormalThread normalThread = new NormalThread("");
        try {
            normalThread.start();
            normalThread.join();
        } catch (Exception e) {
            System.out.println("Exception from main");
        }
        NormalBean[] normalBean = normalThread.normalBean;
        Log.d("ceshi", normalBean[dataNum].getDistance()+"");

        Intent intent = new Intent(Normal.this, NormalDetails.class);
        intent.putExtra("longitude",Double.valueOf(normalBean[dataNum].getAtitude().split(",")[0]));
        intent.putExtra("latitude",Double.valueOf(normalBean[dataNum].getAtitude().split(",")[1]));
        intent.putExtra("capacity",normalBean[dataNum].getCapacity());
        intent.putExtra("distance",Double.valueOf(normalBean[dataNum].getDistance()));
        intent.putExtra("name",normalBean[dataNum].getName());
        intent.putExtra("price_info",normalBean[dataNum].getPrice_info());
        startActivity(intent);
    }



    private void setPage(){
        NormalThread normalThread = new NormalThread("");
        try {
            normalThread.start();
            normalThread.join();
        } catch (Exception e) {
            System.out.println("Exception from main");
        }
        NormalBean[] normalBean = normalThread.normalBean;
        Integer dbNum = normalBean.length;
        if (dbNum==0){return;}
        if (dbNum>0){
            TextView parkingName1=findViewById(R.id.parkingName1);
            parkingName1.setText(normalBean[0].getName());
            LinearLayout linear1 =findViewById(R.id.linear1);
            linear1.setVisibility(View.VISIBLE);
            Button parking1=findViewById(R.id.parking1);
            parking1.setVisibility(View.VISIBLE);
            findViewById(R.id.parking1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentPage(0);
                }
            });
        }
        if (dbNum>1) {
            TextView parkingName2 = findViewById(R.id.parkingName2);
            parkingName2.setText(normalBean[1].getName());
            LinearLayout linear2 =findViewById(R.id.linear2);
            linear2.setVisibility(View.VISIBLE);
            Button parking2=findViewById(R.id.parking2);
            parking2.setVisibility(View.VISIBLE);
            findViewById(R.id.parking2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentPage(1);
                }
            });
        }
        if (dbNum>2) {
            TextView parkingName3 = findViewById(R.id.parkingName3);
            parkingName3.setText(normalBean[2].getName());
            LinearLayout linear3 =findViewById(R.id.linear3);
            linear3.setVisibility(View.VISIBLE);
            Button parking3=findViewById(R.id.parking3);
            parking3.setVisibility(View.VISIBLE);
            parking3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentPage(2);
                }
            });
        }

    }

}
