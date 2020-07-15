package com.example.parkapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class ChangePark extends Activity {
    private TextView shortBookBack;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private MapStatus.Builder builder;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        Intent intent = getIntent();//声明一个对象，并获得跳转过来的Intent对象
        final String currentLatitude = intent.getStringExtra("latitude");//从intent对象中获得数据
        final String currentLongtitude = intent.getStringExtra("longtitude");//从intent对象中获得数据


        shortBookBack=findViewById(R.id.shortBookBack);
        //获取地图控件引用
        mMapView = findViewById(R.id.mapViewBook);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        builder = new MapStatus.Builder();
        builder.zoom(20.0f);
        LatLng parkPosition = new LatLng(22.493113,113.948789);
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


        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        shortBookBack.setTypeface(font);
        shortBookBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePark.this,MainActivity.class);
                startActivity(intent);
            }
        });
        Button parkingButton = findViewById(R.id.parkingButton);
        parkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent();
                i1.setData(Uri.parse("baidumap://map/direction?region=shenzhen&origin=22.534088,113.919806&destination="+"文心一路"+"&coord_type=bd09ll&mode=driving&src=andr.baidu.openAPIdemo"));
                try {
                    startActivity(i1);
                }catch (Exception e){
                    Toast.makeText(ChangePark.this,"请安装百度地图",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button roadBook1 = findViewById(R.id.roadBook1);
        roadBook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent();
                i1.setData(Uri.parse("baidumap://map/direction?region=shenzhen&origin=22.534088,113.919806&destination="+"文心一路"+"&coord_type=bd09ll&mode=driving&src=andr.baidu.openAPIdemo"));
                try {
                    startActivity(i1);
                }catch (Exception e){
                    Toast.makeText(ChangePark.this,"请安装百度地图",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }


}
