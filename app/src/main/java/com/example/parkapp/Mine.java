package com.example.parkapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkapp.Bean.MyBookBean;

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

        final MyBookBean myBookBean = new MyBookBean();

        mybook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myBookBean.parkName!=""){
                    Intent intent = new Intent(Mine.this, Parking.class);
                    startActivity(intent);}
                else {
                    Toast.makeText(Mine.this,"No Park Has Been Reserved",Toast.LENGTH_LONG).show();
                }
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
                Intent i1 = new Intent(Mine.this, Introduction1.class);
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
