package com.example.parkapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
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

public class book2 extends AppCompatActivity {
    private TextView longBookBack;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private MapStatus.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book2);

        Intent intent = getIntent();//声明一个对象，并获得跳转过来的Intent对象
        final String name=intent.getExtras().getString("name");
        final Double latitude=intent.getExtras().getDouble("latitude");
        final Double longtitude=intent.getExtras().getDouble("longtitude");
        final Double capacity=intent.getExtras().getDouble("capacity");
        final Double distance=intent.getExtras().getDouble("distance");
        final String time=intent.getExtras().getString("time");
//        longBookBack=findViewById(R.id.longBookBack);

        //获取地图控件引用
        mMapView = findViewById(R.id.mapViewLongBook);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        builder = new MapStatus.Builder();
        builder.zoom(14.0f);
        LatLng parkPosition = new LatLng(latitude,longtitude);
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

//        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
////        longBookBack.setTypeface(font);
////        longBookBack.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Intent intent = new Intent(book2.this,MainActivity.class);
////                startActivity(intent);
////            }
////        });


        TextView parkName = findViewById(R.id.parkName);
        parkName.setText(name);

        TextView parkCapacity = findViewById(R.id.parkCapacity);
        parkCapacity.setText(capacity.intValue()+"");
        TextView parkDistance = findViewById(R.id.parkDistance);
        parkDistance.setText(distance+"米");

        findViewById(R.id.info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(book2.this,priceinfo.class);
                startActivity(intent);
            }
        });

        final Button roadBook1 = findViewById(R.id.roadBook1);
        roadBook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_POSITIVE://开始导航
                                roadBook1.setText("返回主页");
                                Toast.makeText(book2.this,"预约成功！可前往我的预约中查看订单。",Toast.LENGTH_LONG).show();

                                roadBook1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(book2.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                break;
                            case Dialog.BUTTON_NEUTRAL://返回主页
//                                Intent intent = new Intent(book2.this, MainActivity.class);
//                                startActivity(intent);
                                break;
                        }
                    }
                };
                //dialog参数设置
                AlertDialog.Builder builder = new AlertDialog.Builder(book2.this);  //先得到构造器
                builder.setTitle(name); //设置标题
                builder.setMessage("即将为您预约"+time+"的停车位，请确认信息。");
                builder.setIcon(R.drawable.park);//设置图标，图片id即可
                builder.setPositiveButton("确认预约", dialogOnclicListener);
                builder.setNeutralButton("返回", dialogOnclicListener);
                builder.create().show();

            }
        });
        String nameStr=name.replaceAll("辅道","");

        String[] name1 = nameStr.split("\\)");
        String[] name2 = name1[0].split("\\(");
        nameStr = name2[0];
        Log.d("nameStr", nameStr);


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

                if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(book2.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                }
                if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    // result.getSuggestAddrInfo()
                    return;
                }
                if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {


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
        final String finalNameStr = nameStr;
        road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlanNode stNode = PlanNode.withCityNameAndPlaceName("深圳", "文心二路");
                PlanNode enNode = PlanNode.withCityNameAndPlaceName("深圳", finalNameStr);
                mSearch.drivingSearch((new DrivingRoutePlanOption())
                        .from(stNode)
                        .to(enNode));
                mSearch.destroy();
                //设置缩放
                //builder = new MapStatus.Builder();
                //builder.zoom(12.0f);
                //mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
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
