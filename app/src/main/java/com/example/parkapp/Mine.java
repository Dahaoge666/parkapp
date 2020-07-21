package com.example.parkapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

public class Mine extends AppCompatActivity {
    private TextView mineBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        TextView time_place_picker=findViewById(R.id.time_place_picker);


        time_place_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mine.this, Selection.class);
                startActivity(intent);
            }
        });


        TextView mybook=findViewById(R.id.mybook);


        mybook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mine.this, Parking.class);
                intent.putExtra("destination","深圳公园");
                intent.putExtra("name","文心三路");
                intent.putExtra("latitude",22.525269);
                intent.putExtra("longitude",113.937374);
                intent.putExtra("type","detail");
                startActivity(intent);
            }
        });


        TextView mywallet=findViewById(R.id.mywallet);


        mywallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mine.this, Mywallet.class);
                startActivity(intent);
            }
        });
//        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.exit(0);
//            }
//        });
        TextView introduct=findViewById(R.id.introduct);
        introduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(Mine.this, Introduction.class);
                startActivity(i1);
            }
        });
    }
    public void onBackPressed() {
        Intent intent = new Intent(Mine.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
