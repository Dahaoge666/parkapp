package com.example.parkapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.parkapp.Bean.NormalBean;
import com.example.parkapp.Thread.NormalThread;
import com.example.parkapp.Thread.PredictThread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class book_list extends Activity {
//    private ListPopupWindow mListPop;
//    private List<String> lists = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        Intent intent = getIntent();//声明一个对象，并获得跳转过来的Intent对象
        String name=intent.getStringExtra("name");
        Double latitude=intent.getDoubleExtra("latitude",'0');
        Double longtitude=intent.getDoubleExtra("longtitude",'0');
        TextView destination=findViewById(R.id.destination);
        destination.setText("   "+name);
//        lists.add("Comprehensive ranking");
//        lists.add("Price first");
//        lists.add("Distance first");
//        mListPop = new ListPopupWindow(this);
//        mListPop.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lists));
//        final Button select = findViewById(R.id.select);
//        mListPop.setWidth(ActionBar.LayoutParams.WRAP_CONTENT);
//        mListPop.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
//        mListPop.setAnchorView(select);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
//        mListPop.setModal(true);//设置是否是模式
//        mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                select.setText(lists.get(position));
//
//                if (lists.get(position)=="Distance first"){
//                    getList();
//                }
//                mListPop.dismiss();
//            }
//        });
//        select.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mListPop.show();
//            }
//        });
        setPage();
    }


    private void intentPage(final Integer dataNum){
        NormalThread normalThread = new NormalThread();
        try {
            normalThread.start();
            normalThread.join();
        } catch (Exception e) {
            System.out.println("Exception from main");
        }
        NormalBean[] normalBean = normalThread.normalBean;
        Intent intent = new Intent(book_list.this, book2.class);
        intent.putExtra("longtitude",Double.valueOf(normalBean[dataNum].getAtitude().split(",")[0]));
        intent.putExtra("latitude",Double.valueOf(normalBean[dataNum].getAtitude().split(",")[1]));
        intent.putExtra("capacity",normalBean[dataNum].getCapacity());
        intent.putExtra("distance",normalBean[dataNum].getDistance());
        intent.putExtra("name",normalBean[dataNum].getName());
        startActivity(intent);
    }



    private void setPage(){
        NormalThread normalThread = new NormalThread();
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


//    private void getList(){
//
//        for (Integer i=0;i<dbData.size();i++){
//            for (Integer j=i;j<dbData.size();j++) {
//                if (Double.parseDouble(dbData.get(j).get(4)) < Double.parseDouble(dbData.get(i).get(4))) {
//                    Collections.swap(dbData, j, i);
//                }
//            }
//        }
//
//        setPage();
//    }
}
