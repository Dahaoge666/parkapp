package com.example.parkapp;

import android.app.Activity;
import android.content.Intent;
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
import com.example.parkapp.Thread.ReserveThread;

import overlayutil.DrivingRouteOverlay;

public class Parking extends Activity {

    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private MapStatus.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);



        //获取地图控件引用
        mMapView = findViewById(R.id.mapViewBook);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        builder = new MapStatus.Builder();
        builder.zoom(14.0f);
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

        //定义Maker坐标点
        LatLng point = new LatLng(22.525269, 113.937374);
//构建Marker图标
        BitmapDescriptor bitmap1 = BitmapDescriptorFactory
                .fromResource(R.drawable.park_icon);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point) //必传参数
                .icon(bitmap1) ;//必传参数
//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

        final Intent intent = getIntent();//声明一个对象，并获得跳转过来的Intent对象
        final Double latitude = intent.getDoubleExtra("latitude",0);
        final Double longitude = intent.getDoubleExtra("longitude",0);
        final String name = intent.getStringExtra("name");
        final String type = intent.getStringExtra("type");
        final String destination = intent.getStringExtra("destination");

        final ReserveThread reserveThread = new ReserveThread("");
        try {
            reserveThread.start();
            reserveThread.join();
        } catch (Exception e) {
            System.out.println("Exception from main");
        }

        TextView travelTime = findViewById(R.id.travelTime);
        travelTime.setText(reserveThread.time_use);
        TextView parkingOccupancy = findViewById(R.id.parkingOccupancy);
        parkingOccupancy.setText(reserveThread.occupy+"/"+reserveThread.capacity);
        final String price_info = reserveThread.price_info;
        Button info = findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoIntent = new Intent(Parking.this, PriceInfo.class);
                infoIntent.putExtra("price_info",price_info);
                startActivity(infoIntent);
            }
        });


        final Button change = findViewById(R.id.change);
        if (type.equals( "change")){
            change.setVisibility(View.VISIBLE);
        }
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent();
                i1.setData(Uri.parse("baidumap://map/direction?region=shenzhen&origin=22.534088,113.919806&destination="+"深圳市"+reserveThread.name+"&coord_type=bd09ll&mode=driving&src=andr.baidu.openAPIdemo"));
                try {
                    startActivity(i1);
                }catch (Exception e){
                    Toast.makeText(Parking.this,"请安装百度地图",Toast.LENGTH_SHORT).show();
                }


                if (type.equals( "change")){
                    change.setVisibility(View.GONE);
                }


            }
        });


        findViewById(R.id.cancelButon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Parking.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"取消成功",Toast.LENGTH_SHORT).show();
            }
        });
















        //设置路线规划的监听器
        final RoutePlanSearch mSearch = RoutePlanSearch.newInstance();
        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                //创建DrivingRouteOverlay实例
                DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
                if (drivingRouteResult.getRouteLines().size() > 0) {
                    //获取路径规划数据,(以返回的第一条路线为例）
                    //为DrivingRouteOverlay实例设置数据
                    overlay.setData(drivingRouteResult.getRouteLines().get(0));
                    //在地图上绘制DrivingRouteOverlay
                    overlay.addToMap();
                }
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };
        mSearch.setOnGetRoutePlanResultListener(listener);
        //按钮触发器
        Button road = findViewById(R.id.road);
        road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlanNode stNode = PlanNode.withCityNameAndPlaceName("深圳", name);
                PlanNode enNode = PlanNode.withCityNameAndPlaceName("深圳", reserveThread.name);
                try {
                    mSearch.drivingSearch((new DrivingRoutePlanOption())
                            .from(stNode)
                            .to(enNode));
                    mSearch.destroy();
                }catch (Exception e){
                    stNode = PlanNode.withCityNameAndPlaceName("深圳", "文心二路");
                    enNode = PlanNode.withCityNameAndPlaceName("深圳", "东角头");
                    mSearch.drivingSearch((new DrivingRoutePlanOption())
                            .from(stNode)
                            .to(enNode));
                    mSearch.destroy();
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
}
