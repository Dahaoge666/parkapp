package com.example.parkapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.example.parkapp.Bean.MyBookBean;
import com.example.parkapp.Thread.NormalThread;
import com.example.parkapp.Thread.ReserveThread;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import overlayutil.DrivingRouteOverlay;

public class Reserve extends AppCompatActivity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private MapStatus.Builder builder;
    private boolean i=false;
    private boolean j=false;
    MyBookBean myBookBean = new MyBookBean();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        Intent intent = getIntent();//声明一个对象，并获得跳转过来的Intent对象
        final String historyName = intent.getStringExtra("historyName");
        final String destination = intent.getStringExtra("destination");
        final Double historyLatitude = intent.getDoubleExtra("historyLatitude",0);
        final Double historyLongitude = intent.getDoubleExtra("historyLongitude",0);
        final String type = intent.getStringExtra("type");

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
                    Integer travel_time = drivingRouteResult.getRouteLines().get(0).getDuration();
                    TextView travelTime = findViewById(R.id.travelTime);
                    travelTime.setText(travel_time/60+" minutes");
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

        final Button roadBook1 = findViewById(R.id.roadBook1);
        final ReserveThread reserveThread;
        if(type.equals("reserve")){
            reserveThread = new ReserveThread("reserve/"+historyLongitude+"/"+historyLatitude+"/"+type);}
        else {
            reserveThread = new ReserveThread("changeDisplay");
        }

        try {
            reserveThread.start();
            reserveThread.join();
        } catch (Exception e) {
            System.out.println("Exception from main");
        }

        //获取地图控件引用
        mMapView = findViewById(R.id.mapViewBook);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        builder = new MapStatus.Builder();
        builder.zoom(16.0f);
        final LatLng parkPosition = new LatLng(myBookBean.destinationLatitude,myBookBean.destinationLongitude);
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

        //定义Maker坐标点
        LatLng point = new LatLng(reserveThread.latitude, reserveThread.longitude);
        LatLng currentPoint = new LatLng(myBookBean.currentLatitude, myBookBean.currentLongitude);
        PlanNode stNode = PlanNode.withLocation(currentPoint);
        PlanNode enNode = PlanNode.withLocation(point);
        try {
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));
            mSearch.destroy();
        }catch (Exception e){
            Toast.makeText(Reserve.this,"The route cannot be planned.",Toast.LENGTH_SHORT);
        }

        TextView park = findViewById(R.id.park);
        park.setText(reserveThread.name);

        TextView parkingOccupancy = findViewById(R.id.parkingOccupancy);
        parkingOccupancy.setText(reserveThread.occupy+"/"+reserveThread.capacity);
        final String price_info = reserveThread.price_info;
        Button info = findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoIntent = new Intent(Reserve.this, PriceInfo.class);
                infoIntent.putExtra("price_info",price_info);
                startActivity(infoIntent);
            }
        });
        myBookBean.parkName = reserveThread.name;
        myBookBean.travelTime = reserveThread.time_use;
        myBookBean.parkingOccupancy = reserveThread.occupy+"/"+reserveThread.capacity;
        myBookBean.priceInfo = reserveThread.price_info;
        myBookBean.parkLatitude = reserveThread.latitude;
        myBookBean.parkLongitude = reserveThread.longitude;
        roadBook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LatLng startPoint = new LatLng(myBookBean.currentLatitude, myBookBean.currentLongitude);
                LatLng endPoint = new LatLng(myBookBean.parkLatitude, myBookBean.parkLongitude);
                RouteParaOption paraOption = new RouteParaOption()
                        .startPoint(startPoint)
                        .endPoint(endPoint);
                try {
                    BaiduMapRoutePlan.openBaiduMapDrivingRoute(paraOption, Reserve.this);
                    if(type.equals("reserve")){
                    initNotify(destination,historyLatitude,historyLongitude,historyName);}
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Reserve.this,"Please install the Baidu Map for navigation.",Toast.LENGTH_SHORT).show();
                }
                BaiduMapRoutePlan.finish(Reserve.this);


//                Intent i1 = new Intent();
//                j = true;
//                i1.setData(Uri.parse("baidumap://map/direction?region=shenzhen&origin="+myBookBean.currentLatitude+","+myBookBean.currentLongitude+"&destination="+"深圳市"+reserveThread.name+"&coord_type=bd09ll&mode=driving&src=andr.baidu.openAPIdemo"));
//                try {
//                    startActivity(i1);
//                    if(type.equals("reserve")){
//                    initNotify(destination,historyLatitude,historyLongitude,historyName);}
//                }catch (Exception e){
//                    Toast.makeText(Reserve.this,"Please install the Baidu Map for navigation.",Toast.LENGTH_SHORT).show();
//                }
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myBookBean.parkName = "";
                Intent intent = new Intent(Reserve.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"The cancellation was successful.",Toast.LENGTH_SHORT).show();
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



    private void initNotify(String destination,Double historyLatitude,Double historyLongitude,String historyName){
        Log.d("notify", "initNotify: ");
        String id = "8.0";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;

        Intent intent=new Intent(this, Reserve.class);

        intent.putExtra("destination",destination);
        intent.putExtra("historyName",historyName);
        intent.putExtra("historyLatitude",historyLatitude);
        intent.putExtra("historyLongitude",historyLongitude);
        intent.putExtra("type","changeDisplay");

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);




        final ReserveThread reserveThread1 = new ReserveThread("reserve/"+historyLongitude+"/"+historyLatitude+"/"+"change");
        try {
            reserveThread1.start();
            reserveThread1.join();
        } catch (Exception e) {
            System.out.println("Exception from main");
        }
        NotificationChannel mChannel = new NotificationChannel(id, "Tips", NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(mChannel);
        notification = new Notification.Builder(this,"default")
                .setChannelId(id)
                .setContentTitle("Better parking spot is detected:"+reserveThread1.name)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.hongyan)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);
    }

}
