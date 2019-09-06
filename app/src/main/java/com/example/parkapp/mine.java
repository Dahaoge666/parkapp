package com.example.parkapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class mine extends AppCompatActivity {
    private TextView mineBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        TextView mybook=findViewById(R.id.mybook);


        mybook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mine.this,mybook.class);
                intent.putExtra("name",0);
                startActivity(intent);
            }
        });


        TextView mywallet=findViewById(R.id.mywallet);


        mywallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mine.this,mywallet.class);
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
                Intent i1 = new Intent(mine.this,detail_intro.class);
                startActivity(i1);
            }
        });
    }
    public void onBackPressed() {
        Intent intent = new Intent(mine.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
