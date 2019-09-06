package com.example.parkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class mybook extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybook);

        Intent intent = getIntent();//声明一个对象，并获得跳转过来的Intent对象
        int name=intent.getExtras().getInt("name");



        LinearLayout dingdan1 = findViewById(R.id.dingdan1);
        LinearLayout dingdan2 = findViewById(R.id.dingdan2);
        LinearLayout dingdan3 = findViewById(R.id.dingdan3);
        LinearLayout dingdan4 = findViewById(R.id.dingdan4);
//        LinearLayout dingdan5 = findViewById(R.id.dingdan5);
        if (name==1){
            dingdan1.setVisibility(View.GONE);
            dingdan4.setVisibility(View.VISIBLE);
        }
        if (name==2){
            dingdan2.setVisibility(View.GONE);
//            dingdan4.setVisibility(View.VISIBLE);
        }


//        LinearLayout dingdan4 = findViewById(R.id.dingdan4);

        dingdan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mybook.this,booked.class);
                intent.putExtra("name","文心二路");
                intent.putExtra("time","2019年9月3日 15:10");
                intent.putExtra("used","3");
                startActivity(intent);
            }
        });

        dingdan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mybook.this,bookd_info.class);
                intent.putExtra("name","深圳大学");
                intent.putExtra("time","2019年9月1日 13:10");
                intent.putExtra("used","0");
                startActivity(intent);
            }
        });

        dingdan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mybook.this,booked2.class);
                intent.putExtra("name","文心公园");
                intent.putExtra("time","2019年9月3日 13:10");
                intent.putExtra("used","5");
                startActivity(intent);
            }
        });

        dingdan4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mybook.this,booked2.class);
                intent.putExtra("name","文心二路");
                intent.putExtra("time","2019年9月3日 15:10");
                intent.putExtra("used","3");
                startActivity(intent);
            }
        });

//        dingdan5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mybook.this,booked2.class);
//                intent.putExtra("name","深圳大学");
//                intent.putExtra("time","2019年9月1日 13:10");
//                intent.putExtra("used","0");
//                startActivity(intent);
//            }
//        });

    }

    public void onBackPressed() {
        Intent intent = new Intent(mybook.this, mine.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
