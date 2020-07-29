package com.example.parkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Introduction extends Activity {

    //定义图片
    private int[] resId = new int[]{
            R.mipmap.tu1, R.mipmap.tu2, R.mipmap.tu3, R.mipmap.tu4, R.mipmap.tu5, R.mipmap.tu6, R.mipmap.tu7, R.mipmap.tu8, R.mipmap.tu9, R.mipmap.tu10
    };
    //图片下标序号
    private int count = 0;
    //定义手势监听对象
    private GestureDetector gestureDetector;
    //定义ImageView对象
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);


        //显示提示语
        Toast.makeText(Introduction.this, "温馨提示：向左或向右滑动查看更多操作", Toast.LENGTH_SHORT).show();

        iv = (ImageView)findViewById(R.id.imageView);        //获取ImageView控件id
        gestureDetector = new GestureDetector(onGestureListener);  //设置手势监听由onGestureListener处理
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Introduction.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }


    //当Activity被触摸时回调
    public boolean onTouchEvent(MotionEvent event){
        return gestureDetector.onTouchEvent(event);
    }
    //自定义GestureDetector的手势识别监听器
    private GestureDetector.OnGestureListener onGestureListener
            = new GestureDetector.SimpleOnGestureListener(){
        //当识别的手势是滑动手势时回调onFinger方法
        public boolean onFling(MotionEvent e1,MotionEvent e2,float velocityX,float velocityY){
            //得到手触碰位置的起始点和结束点坐标 x , y ，并进行计算
            float x = e2.getX()-e1.getX();
            float y = e2.getY()-e1.getY();
            //通过计算判断是向左还是向右滑动
            if(x < 0){
                count++;
                if(count==resId.length){
                    Intent intent = new Intent(Introduction.this,MainActivity.class);
                    startActivity(intent);
                    count--;
                }
                count%=(resId.length);    //想显示多少图片，就把定义图片的数组长度-1
            }else if(x > 0){
                count--;
                count=(count+(resId.length))%(resId.length);
            }

            iv.setImageResource(resId[count]); //切换imageView的图片
            return true;
        }
    };
}
