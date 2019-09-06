package com.example.parkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class booked extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);
        Intent intent = getIntent();//声明一个对象，并获得跳转过来的Intent对象
        final String name = intent.getExtras().getString("name");
        final String time = intent.getExtras().getString("time");
        final String used = intent.getExtras().getString("used");
        TextView book_name = findViewById(R.id.book_name);
        TextView book_time = findViewById(R.id.book_time);
        TextView book_used = findViewById(R.id.book_used);
        TextView book_price = findViewById(R.id.book_price);
        TextView book_money = findViewById(R.id.book_money);

        book_name.setText(name);
        book_time.setText("" + time);
        book_used.setText("" + used + "小时");
        book_price.setText("" + "5元/小时");
        final Integer money;
        money = Integer.valueOf(used) * 5;
        book_money.setText("" + money + "元");

        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_POSITIVE:
                                break;
                            case Dialog.BUTTON_NEUTRAL:
                                Intent intent1 = new Intent(booked.this, mybook.class);
                                intent1.putExtra("name", 1);
                                startActivity(intent1);
                                break;
                        }
                    }
                };
                //dialog参数设置
                AlertDialog.Builder builder = new AlertDialog.Builder(booked.this);  //先得到构造器
                builder.setTitle("结束使用"); //设置标题
                builder.setMessage("您本次消费总金额为：" + money + "元，是否确认结束使用。");
                builder.setIcon(R.drawable.park);//设置图标，图片id即可
                builder.setPositiveButton("返回", dialogOnclicListener);
                builder.setNeutralButton("确认", dialogOnclicListener);
                builder.create().show();

            }
        });

    }
}
