package com.example.parkapp;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.parkapp.Bean.MyBookBean;
import com.example.parkapp.Thread.ReserveThread;
import com.example.parkapp.Thread.SetThread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Selection extends Activity {
    private MapView mMapView = null;
    private MapStatus.Builder builder;

    public static String time = "2018-09-07-07:00:00";
    public static String date_str = "2018-09-07";
    public static String time_str = "7:00";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        //获取地图控件引用
        mMapView = findViewById(R.id.mapViewBook);
        final BaiduMap mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        builder = new MapStatus.Builder();
        builder.zoom(16.0f);
        final LatLng parkPosition = new LatLng(22.585269,113.937374);
        builder.target(parkPosition);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        final BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        OverlayOptions markOption = new MarkerOptions()
                .position(parkPosition)
                .perspective(true)
                .icon(bitmap)
                .alpha(0.8f);
        mBaiduMap.addOverlay(markOption);


        final Button date_picker = findViewById(R.id.date_picker);
        final MyBookBean myBookBean = new MyBookBean();



        Calendar startDate = Calendar.getInstance();
        startDate.set(2018,12,11);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019,1,1);
        final TimePickerView pvTime = new TimePickerBuilder(Selection.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                date_picker.setText(getTime(date));
                date_str = getTime(date);
                time = date_str+"-0"+time_str+":00";
                myBookBean.time = time;
                SetThread setThread = new SetThread("/"+myBookBean.currentLatitude+"/"+myBookBean.currentLongitude+"/"+time);
                try {
                    setThread.start();
                    setThread.join();
                } catch (Exception e) {
                    System.out.println("Exception from main");
                }
            }
        })
                .setType(new boolean[]{true, true, true,false,false,false})
                .setLabel("年","月","日","时","分","秒")
                .setRangDate(startDate,endDate)
                .setTitleText("Select Date")
                .build();

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pvTime.show();
            }
        });

        final Button time_picker = findViewById(R.id.time_picker);
        final List<String> content = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            if(i==0){content.add("7:00");}
            else if(i==1){content.add("7:05");}
            else {content.add("7:"+5*i);}

        }
        final OptionsPickerView pvNoLinkOptions = new OptionsPickerBuilder(Selection.this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                time_picker.setText(content.get(options1));
                time_str = content.get(options1);
                time = date_str+"-0"+time_str+":00";
                myBookBean.time = time;
                final SetThread setThread = new SetThread("/"+myBookBean.currentLatitude+"/"+myBookBean.currentLongitude+"/"+time);
                try {
                    setThread.start();
                    setThread.join();
                } catch (Exception e) {
                    System.out.println("Exception from main");
                }

            }
        })
                .setTitleText("Select Time")
                .build();

        pvNoLinkOptions.setPicker(content);

        time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvNoLinkOptions.show();
            }
        });




        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                if(point.latitude>22.613468 || point.latitude<22.56){
                    Toast.makeText(Selection.this,"Out of the label range.",Toast.LENGTH_LONG).show();return; }
                if(point.longitude>114.03242 || point.longitude<113.85242){
                    Toast.makeText(Selection.this,"Out of the label range.",Toast.LENGTH_LONG).show();return; }
                mBaiduMap.clear();

                LatLng parkPosition1 = new LatLng(point.latitude,point.longitude);
                myBookBean.currentLatitude = point.latitude;
                myBookBean.currentLongitude = point.longitude;
                SetThread setThread = new SetThread("/"+point.latitude+"/"+point.longitude+"/"+time);
                try {
                    setThread.start();
                    setThread.join();
                } catch (Exception e) {
                    System.out.println("Exception from main");
                }

                OverlayOptions markOption1 = new MarkerOptions()
                        .position(parkPosition1)
                        .icon(bitmap);
                mBaiduMap.addOverlay(markOption1);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {

                if(mapPoi.getPosition().latitude>22.613468 || mapPoi.getPosition().latitude<22.56){
                    Toast.makeText(Selection.this,"Out of the label range.",Toast.LENGTH_LONG).show();return false; }
                if(mapPoi.getPosition().longitude>114.03242 || mapPoi.getPosition().longitude<113.85242){
                    Toast.makeText(Selection.this,"Out of the label range.",Toast.LENGTH_LONG).show();return false; }
                mBaiduMap.clear();
                LatLng parkPosition2 = new LatLng(mapPoi.getPosition().latitude,mapPoi.getPosition().longitude);
                myBookBean.currentLatitude = mapPoi.getPosition().latitude;
                myBookBean.currentLongitude = mapPoi.getPosition().longitude;
                SetThread setThread = new SetThread("/"+mapPoi.getPosition().latitude+"/"+mapPoi.getPosition().longitude+"/"+time);
                try {
                    setThread.start();
                    setThread.join();
                } catch (Exception e) {
                    System.out.println("Exception from main");
                }

                OverlayOptions markOption2 = new MarkerOptions()
                        .position(parkPosition2)
                        .icon(bitmap);
                mBaiduMap.addOverlay(markOption2);
                return false;
            }
        });

    }





    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

}
