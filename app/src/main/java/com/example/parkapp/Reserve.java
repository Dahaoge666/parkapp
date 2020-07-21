package com.example.parkapp;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.example.parkapp.Thread.ReserveThread;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import overlayutil.DrivingRouteOverlay;

public class Reserve extends AppCompatActivity {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private MapStatus.Builder builder;
    private boolean i=false;
    private boolean j=false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        Intent intent = getIntent();//声明一个对象，并获得跳转过来的Intent对象
        final Double latitude = intent.getDoubleExtra("latitude",0);
        final Double longitude = intent.getDoubleExtra("longitude",0);
        final String currentName = intent.getStringExtra("name");
        final String destination = intent.getStringExtra("destination");


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

        final Button roadBook1 = findViewById(R.id.roadBook1);


        final ReserveThread reserveThread = new ReserveThread("");
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
        final LatLng parkPosition = new LatLng(longitude,latitude);
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



        //按钮触发器
        Button road = findViewById(R.id.road);
        road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlanNode stNode = PlanNode.withCityNameAndPlaceName("深圳", currentName);
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


        TextView travelTime = findViewById(R.id.travelTime);
        travelTime.setText(reserveThread.time_use);
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


        roadBook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent();
                j = true;
                i1.setData(Uri.parse("baidumap://map/direction?region=shenzhen&origin=22.534088,113.919806&destination="+"深圳市"+reserveThread.name+"&coord_type=bd09ll&mode=driving&src=andr.baidu.openAPIdemo"));
                try {
                    startActivity(i1);
                    initNotify(reserveThread.name,destination);
                }catch (Exception e){
                    Toast.makeText(Reserve.this,"请安装百度地图",Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Reserve.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"取消成功",Toast.LENGTH_SHORT).show();
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

    private void changeNotify(final RoutePlanSearch mSearch, final String currentName, final String destination){
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_NEGATIVE:
                        final Button roadBook1 = findViewById(R.id.roadBook1);
                        roadBook1.setText("Navigation");
                        roadBook1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i2=new Intent().setData(Uri.parse("baidumap://map/direction?region=shenzhen&origin=22.534088,113.919806&destination="+"深圳市文心三路"+"&coord_type=bd09ll&mode=driving&src=andr.baidu.openAPIdemo"));
                                try {
                                    startActivity(i2);
                                }catch(Exception e){
                                    Toast.makeText(Reserve.this,"请安装百度地图",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        if(j) {
                            i = true;
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
                                    Intent infoIntent = new Intent(Reserve.this, PriceInfo.class);
                                    infoIntent.putExtra("price_info",price_info);
                                    startActivity(infoIntent);
                                }
                            });

                            Button road = findViewById(R.id.road);
                            road.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    PlanNode stNode = PlanNode.withCityNameAndPlaceName("深圳", currentName);
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
                            initNotify(reserveThread.name,destination);

                        }
                        break;
                    case Dialog.BUTTON_NEUTRAL://综合
                        break;
                }
            }
        };
        //dialog参数设置

        AlertDialog.Builder builder = new AlertDialog.Builder(Reserve.this);  //先得到构造器
        builder.setMessage("Better parking has been detected,whether to change？"); //设置内容
        builder.setTitle("CHANGE PARK"); //设置标题
        builder.setIcon(R.drawable.park);//设置图标，图片id即可
        builder.setNegativeButton("ACCEPT", dialogOnclicListener);
        builder.setNeutralButton("CANCEL", dialogOnclicListener);
        builder.create().show();
    }



    private void initNotify(String name,String destination){
        Log.d("notify", "initNotify: ");
        String id = "8.0";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;

        Intent intent=new Intent(this, Parking.class);

        intent.putExtra("destination",destination);
        intent.putExtra("name","文心三路");
        intent.putExtra("latitude",22.525269);
        intent.putExtra("longitude",113.937374);
        intent.putExtra("type","change");

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationChannel mChannel = new NotificationChannel(id, "提示", NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(mChannel);
        notification = new Notification.Builder(this,"default")
                .setChannelId(id)
                .setContentTitle("Better parking spot is detected:"+name)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.hongyan)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);

//        roadBook1.setText("CHANGE");
//        roadBook1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                changeNotify(mSearch, currentName, destination);
//
//            }
//        });
//        }
    }

}
