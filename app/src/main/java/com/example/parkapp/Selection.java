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
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Selection extends Activity {
    private MapView mMapView = null;
    private MapStatus.Builder builder;
    public String time;
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
        final LatLng parkPosition = new LatLng(22.525269,113.937374);
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


        final Button time_picker = findViewById(R.id.time_picker);


        final TimePickerView pvTime = new TimePickerBuilder(Selection.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Toast.makeText(Selection.this, getTime(date), Toast.LENGTH_SHORT).show();
                time_picker.setText(getTime(date));
            }
        })
                .setType(new boolean[]{true, true, true, true, true, true})
                .build();

        time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pvTime.show();
            }
        });


        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mBaiduMap.clear();
                LatLng parkPosition1 = new LatLng(point.latitude,point.longitude);

                OverlayOptions markOption1 = new MarkerOptions()
                        .position(parkPosition1)
                        .icon(bitmap);
                mBaiduMap.addOverlay(markOption1);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                mBaiduMap.clear();
                LatLng parkPosition2 = new LatLng(mapPoi.getPosition().latitude,mapPoi.getPosition().longitude);
                OverlayOptions markOption2 = new MarkerOptions()
                        .position(parkPosition2)
                        .icon(bitmap);
                mBaiduMap.addOverlay(markOption2);
                Toast.makeText(getApplicationContext(), "hhh"+mapPoi.getPosition(),Toast.LENGTH_LONG).show();
                return false;
            }
        });

    }





    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        return format.format(date);
    }

}
