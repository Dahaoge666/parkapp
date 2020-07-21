package com.example.parkapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import overlayutil.DrivingRouteOverlay;

public class NormalDetails extends AppCompatActivity {
    private TextView longBookBack;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private MapStatus.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_details);

        Intent intent = getIntent();//声明一个对象，并获得跳转过来的Intent对象
        final String name=intent.getExtras().getString("name");
        final Double latitude=intent.getDoubleExtra("latitude",0);
        final Double longitude=intent.getDoubleExtra("longitude",0);
        final Integer capacity=intent.getIntExtra("capacity",0);
        final Double distance=intent.getDoubleExtra("distance",0);
        final String time=intent.getStringExtra("time");
        final String price_info = intent.getStringExtra("price_info");


//        longBookBack=findViewById(R.id.longBookBack)
        //获取地图控件引用
        mMapView = findViewById(R.id.mapViewLongBook);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        builder = new MapStatus.Builder();
        builder.zoom(14.0f);
        LatLng parkPosition = new LatLng(latitude,longitude);
        builder.target(parkPosition);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        OverlayOptions markOption = new MarkerOptions()
                .position(parkPosition)
                .perspective(true)
                .icon(bitmap)
                .alpha(0.8f);
        mBaiduMap.addOverlay(markOption);



        //定义Maker坐标点
        LatLng point = new LatLng(22.525269, 113.937374);
//构建Marker图标
        BitmapDescriptor bitmap1 = BitmapDescriptorFactory
                .fromResource(R.drawable.park_icon);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point) //必传参数
                .icon(bitmap1) //必传参数
                .draggable(true)
//设置平贴地图，在地图中双指下拉查看效果
                .flat(true)
                .alpha(0.5f);
//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);



        TextView parkName = findViewById(R.id.parkName);
        parkName.setText(name);

        TextView parkCapacity = findViewById(R.id.parkCapacity);
        parkCapacity.setText(capacity.intValue()+"");
        TextView parkDistance = findViewById(R.id.parkDistance);
        parkDistance.setText(distance+"米");

        findViewById(R.id.info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NormalDetails.this, PriceInfo.class);
                intent.putExtra("price_info",price_info);
                startActivity(intent);
            }
        });
        String nameStr=name.replaceAll("辅道","");

        String[] name1 = nameStr.split("\\)");
        String[] name2 = name1[0].split("\\(");
        nameStr = name2[0];
        Log.d("nameStr", nameStr);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
}
