package com.example.parkapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.parkapp.Bean.MyBookBean;

public class Mybook extends Activity {

    static MyBookBean normalBook = new MyBookBean();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybook);


        TextView name = findViewById(R.id.name);
        name.setText(normalBook.name);
        TextView info = findViewById(R.id.info);
        info.setText("订单类型：");

        LinearLayout dingdan1 = findViewById(R.id.dingdan1);
        LinearLayout dingdan2 = findViewById(R.id.dingdan2);
//        LinearLayout dingdan5 = findViewById(R.id.dingdan5);

//        LinearLayout dingdan4 = findViewById(R.id.dingdan4);

        dingdan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mybook.this,Parking.class);
                intent.putExtra("name","文心二路");
                intent.putExtra("time","2019年9月3日 15:10");
                intent.putExtra("used","3");
                startActivity(intent);
            }
        });

        dingdan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mybook.this, Parking.class);
                intent.putExtra("name","深圳大学");
                intent.putExtra("time","2019年9月1日 13:10");
                intent.putExtra("used","0");
                startActivity(intent);
            }
        });


    }

    public void onBackPressed() {
        Intent intent = new Intent(Mybook.this, Mine.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
