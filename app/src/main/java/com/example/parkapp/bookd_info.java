package com.example.parkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

import overlayutil.DrivingRouteOverlay;

public class bookd_info extends Activity {

    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private MapStatus.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookd_info);

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
        final Intent intent = getIntent();//声明一个对象，并获得跳转过来的Intent对象
        final String name=intent.getExtras().getString("name");
        final String time=intent.getExtras().getString("time");
        TextView book_name = findViewById(R.id.book_name);
        TextView book_time = findViewById(R.id.book_time);
        book_name.setText(name);
        book_time.setText(""+time);
        Button change = findViewById(R.id.change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeIntent=new Intent(bookd_info.this,book_list.class);
                changeIntent.putExtra("name","深圳公园");
                changeIntent.putExtra("latitude",22.53945);
                changeIntent.putExtra("longtitude",113.942651);
                startActivity(changeIntent);
            }
        });
        Button cancelButton = findViewById(R.id.cancelButon);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_POSITIVE://再次预定
                                Intent changeIntent=new Intent(bookd_info.this,book_list.class);
                                changeIntent.putExtra("name","深圳公园");
                                changeIntent.putExtra("latitude",22.53945);
                                changeIntent.putExtra("longtitude",113.942651);
                                startActivity(changeIntent);
                                break;
                            case Dialog.BUTTON_NEUTRAL://返回主页
                                Intent intent1 = new Intent(bookd_info.this, mybook.class);
                                intent1.putExtra("name",2);
                                startActivity(intent1);
                                break;
                        }
                    }
                };
                //dialog参数设置
                AlertDialog.Builder builder = new AlertDialog.Builder(bookd_info.this);  //先得到构造器
                builder.setTitle("取消订单"); //设置标题
                builder.setMessage("已为您取消订单，您可再次预定或返回。");
                builder.setIcon(R.drawable.park);//设置图标，图片id即可
                builder.setPositiveButton("再次预定", dialogOnclicListener);
                builder.setNeutralButton("返回", dialogOnclicListener);
                builder.create().show();

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
                PlanNode stNode = PlanNode.withCityNameAndPlaceName("深圳", "文心二路");
                PlanNode enNode = PlanNode.withCityNameAndPlaceName("深圳", "东角头");
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
