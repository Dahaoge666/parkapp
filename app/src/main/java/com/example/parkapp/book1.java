package com.example.parkapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
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
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
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
import com.example.parkapp.Bean.NormalBean;
import com.example.parkapp.Thread.NormalThread;
import com.example.parkapp.Thread.ReserveThread;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.log4j.chainsaw.Main;

import java.util.ArrayList;
import java.util.List;

import overlayutil.DrivingRouteOverlay;

public class book1 extends AppCompatActivity {
    private TextView shortBookBack;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private MapStatus.Builder builder;
    private boolean i=false;
    private boolean j=false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book1);

        Intent intent = getIntent();//声明一个对象，并获得跳转过来的Intent对象
        final String currentLatitude = intent.getStringExtra("latitude");//从intent对象中获得数据
        final String currentLongtitude = intent.getStringExtra("longtitude");//从intent对象中获得数据

        ReserveThread reserveThread = new ReserveThread();
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
        final LatLng parkPosition = new LatLng(22.524663,113.936272);
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
//
//        TextView travelTime = findViewById(R.id.travelTime);
//        travelTime.setText(reserveThread.time_use);
//        TextView parkingOccupancy = findViewById(R.id.parkingOccupancy);
//        parkingOccupancy.setText(reserveThread.occupy+"/"+reserveThread.capacity);
        Button info = findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoIntent = new Intent(book1.this,priceinfo.class);
                startActivity(infoIntent);
            }
        });
        final Button roadBook1 = findViewById(R.id.roadBook1);
        roadBook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent();
                j = true;
                i1.setData(Uri.parse("baidumap://map/direction?region=shenzhen&origin=22.534088,113.919806&destination="+"深圳市文心一路"+"&coord_type=bd09ll&mode=driving&src=andr.baidu.openAPIdemo"));
                try {
                    startActivity(i1);
                    initNotify();
                    roadBook1.setText("变更路线");
                    roadBook1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            changeNotify();

                        }
                    });
                }catch (Exception e){
                    Toast.makeText(book1.this,"请安装百度地图",Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(book1.this,MainActivity.class);
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
                PlanNode stNode = PlanNode.withCityNameAndPlaceName("深圳", "文心一路");
                PlanNode enNode = PlanNode.withCityNameAndPlaceName("深圳", "文心二路");
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
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    private void changeNotify(){
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_NEGATIVE:
                        final Button roadBook1 = findViewById(R.id.roadBook1);
                        roadBook1.setText("开始导航");
                        roadBook1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i2=new Intent().setData(Uri.parse("baidumap://map/direction?region=shenzhen&origin=22.534088,113.919806&destination="+"深圳市文心二路"+"&coord_type=bd09ll&mode=driving&src=andr.baidu.openAPIdemo"));
                                try {
                                    startActivity(i2);
                                }catch(Exception e){
                                    Toast.makeText(book1.this,"请安装百度地图",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        if(j) {
                            i = true;
                            mBaiduMap.clear();
                            final BitmapDescriptor bitmap = BitmapDescriptorFactory
                                    .fromResource(R.drawable.icon_gcoding);
                            LatLng position = new LatLng(22.525269, 113.937374);
                            OverlayOptions markOption = new MarkerOptions()
                                    .position(position)
                                    .perspective(true)
                                    .icon(bitmap)
                                    .alpha(0.8f);
                            mBaiduMap.addOverlay(markOption);
                            builder = new MapStatus.Builder();
                            builder.zoom(20.0f);
                            builder.target(position);
                            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                            TextView park = findViewById(R.id.park);
                            park.setText("文心二路");
                        }
                        break;
                    case Dialog.BUTTON_NEUTRAL://综合
                        break;
                }
            }
        };
        //dialog参数设置

        AlertDialog.Builder builder = new AlertDialog.Builder(book1.this);  //先得到构造器
        builder.setMessage("检测到更优的车位是否变更行程？"); //设置内容
        builder.setTitle("车位变更"); //设置标题
        builder.setIcon(R.drawable.park);//设置图标，图片id即可
        builder.setNegativeButton("确定", dialogOnclicListener);
        builder.setNeutralButton("取消", dialogOnclicListener);
        builder.create().show();
    }



    private void initNotify(){
        Log.d("notify", "initNotify: ");
        String id = "8.0";
        String name="提示";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this)
                    .setChannelId(id)
                    .setContentTitle("Better parking spot is detected: 文心一路")
//                    .setContentText("Better parking spot is detected: 文心一路")
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.hongyan).build();
        } else {
            Notification.Builder builder = new Notification.Builder(this);

            builder.setSmallIcon(R.drawable.hongyan);
            builder.setAutoCancel(true);






            builder.setContentTitle("Better parking spot is detected: 文心一路");
//            .setContentText("Better parking spot is detected: 文心一路");

            notification = builder.build();
        }
        notificationManager.notify(0, notification);
    }

}
