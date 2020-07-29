package com.example.parkapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.parkapp.Bean.MyBookBean;
import com.example.parkapp.Bean.NormalBean;
import com.example.parkapp.Thread.NormalThread;

import overlayutil.DrivingRouteOverlay;

public class Normal extends Activity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private MapStatus.Builder builder;
    NormalBean[] normalBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        Intent intent = getIntent();//声明一个对象，并获得跳转过来的Intent对象
        final String name=intent.getStringExtra("name");
        final String destination=intent.getStringExtra("destination");
        final Double historyLatitude = intent.getDoubleExtra("historyLatitude",0);
        final Double historyLongitude = intent.getDoubleExtra("historyLongitude",0);
        final String type=intent.getStringExtra("type");

        final MyBookBean myBookBean = new MyBookBean();



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
                DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
                if (drivingRouteResult.getRouteLines().size() > 0) {
                    overlay.setData(drivingRouteResult.getRouteLines().get(0));
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

        //获取地图控件引用
        mMapView = findViewById(R.id.mapViewBook);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        builder = new MapStatus.Builder();
        builder.zoom(16.0f);
        final LatLng parkPosition = new LatLng(historyLatitude,historyLongitude);
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
        LatLng currentPoint = new LatLng(myBookBean.currentLatitude, myBookBean.currentLongitude);
        NormalThread normalThread = new NormalThread("/"+historyLongitude+"/"+historyLatitude+"/"+type);
        try {
            normalThread.start();
            normalThread.join();
        } catch (Exception e) {
            System.out.println("Exception from main");
        }
        normalBean = normalThread.normalBean;
        LatLng point = new LatLng(Double.valueOf(normalBean[0].getAtitude().split(",")[0]),Double.valueOf(normalBean[0].getAtitude().split(",")[1]));
        PlanNode stNode = PlanNode.withLocation(currentPoint);
        PlanNode enNode = PlanNode.withLocation(point);
        try {
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));
            mSearch.destroy();
        }catch (Exception e){
            Toast.makeText(Normal.this,"无法规划路线",Toast.LENGTH_SHORT);
        }

        myBookBean.parkName = normalBean[0].getName();
        myBookBean.travelTime = normalBean[0].getTime_use();
        myBookBean.parkingOccupancy = normalBean[0].getOccupy()+"/"+normalBean[0].getCapacity();
        myBookBean.priceInfo = normalBean[0].getPrice_info();
        myBookBean.parkLatitude = Double.valueOf(normalBean[0].getAtitude().split(",")[0]);
        myBookBean.parkLongitude = Double.valueOf(normalBean[0].getAtitude().split(",")[1]);
        final Button roadBook1 = findViewById(R.id.roadBook1);
        roadBook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent();
                i1.setData(Uri.parse("baidumap://map/direction?region=shenzhen&origin=22.534088,113.919806&destination="+"深圳市"+normalBean[0].getName()+"&coord_type=bd09ll&mode=driving&src=andr.baidu.openAPIdemo"));
                try {
                    startActivity(i1);
                    if(type.equals("normal")){
                        initNotify(destination,name,historyLatitude,historyLongitude );}
                }catch (Exception e){
                    Toast.makeText(Normal.this,"请安装百度地图",Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myBookBean.parkName = "";
                Intent intent = new Intent(Normal.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"取消成功",Toast.LENGTH_SHORT).show();
            }
        });

        setPage();
    }


    private void intentPage(final Integer dataNum){
        Log.d("ceshi", normalBean[dataNum].getDistance()+"");

        Intent intent = new Intent(Normal.this, NormalDetails.class);
        intent.putExtra("longitude",Double.valueOf(normalBean[dataNum].getAtitude().split(",")[1]));
        intent.putExtra("latitude",Double.valueOf(normalBean[dataNum].getAtitude().split(",")[0]));
        intent.putExtra("capacity",normalBean[dataNum].getCapacity());
        intent.putExtra("distance",Double.valueOf(normalBean[dataNum].getDistance()));
        intent.putExtra("name",normalBean[dataNum].getName());
        intent.putExtra("occupy",normalBean[dataNum].getOccupy());
        intent.putExtra("time_use",normalBean[dataNum].getTime_use());
        intent.putExtra("price_info",normalBean[dataNum].getPrice_info());
        startActivity(intent);
    }



    private void setPage(){
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
            RelativeLayout rela3 =findViewById(R.id.rela3);
            rela3.setVisibility(View.VISIBLE);
            Button parking3=findViewById(R.id.parking3);
            parking3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentPage(2);
                }
            });
        }

    }

    private void initNotify(String destination,String historyName, Double historyLatitude, Double historyLongitude) {
        Log.d("notify", "initNotify: ");
        String id = "8.0";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;
        Intent intent = new Intent(this, Normal.class);
        intent.putExtra("destination",destination);
        intent.putExtra("name",historyName);
        intent.putExtra("historyLatitude",historyLatitude);
        intent.putExtra("historyLongitude",historyLongitude);
        intent.putExtra("type","change");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationChannel mChannel = new NotificationChannel(id, "提示", NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(mChannel);
        notification = new Notification.Builder(this, "default")
                .setChannelId(id)
                .setContentTitle("Better parking spot is detected")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.hongyan)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);
    }
}
