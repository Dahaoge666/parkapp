package com.example.parkapp;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.RequiresApi;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.parkapp.Bean.ParkdataBean;
import com.example.parkapp.Thread.*;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    public LocationClient mLocationClient = null;;
    private TextView text1, text2;
    Calendar c = Calendar.getInstance();
    public SQLiteDatabase db;
    public double currentLatitude;
    public double currentLongtitude;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private PoiSearch mPoiSearch;
    private List<PoiInfo> allAddr;
    private MapStatus.Builder builder;
    private boolean isFirstLocation = true;
    public double historyLatitude;
    public double historyLongtitude;
    int m_year = c.get(Calendar.YEAR);
    int m_month = c.get(Calendar.MONTH);
    int m_day = c.get(Calendar.DAY_OF_MONTH);
    int m_hour = c.get(Calendar.HOUR);
    int m_minute = c.get(Calendar.MINUTE);
    private String text_hour,text_minute;
    private boolean k=false;
    private String historyName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
        initMap();
        initSQL();
        initEvent();
        Log.d("time", m_year+"年"+m_month+"月"+m_day+"日"+m_hour+"时"+m_minute+"分");
        Integer month_true=m_month+1;
        text1 = findViewById(R.id.text1);
        text1.setText(month_true + "月" + m_day + "日");
        text2 = findViewById(R.id.text2);
        if(m_hour<10){
            text_hour="0"+m_hour;
        }else {
            text_hour=""+m_hour;
        }
        if (m_minute<10){
            text_minute="0"+m_minute;
        }else{
            text_minute=""+m_minute;
        }
        text2.setText(text_hour + ":" + text_minute);
    }

    //初始化
    private void initMap(){
        //获取地图控件引用
        mMapView = (MapView)findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMaxAndMinZoomLevel(20,15);
        mLocationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        mLocationClient.start();

        mPoiSearch = PoiSearch.newInstance();
        OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult res) {

                if (res == null || res.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    Toast.makeText(MainActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
                    return;
                }
                if (res.error == SearchResult.ERRORNO.NO_ERROR) {

                    k=true;
                    allAddr = res.getAllPoi();
                    for (PoiInfo p: allAddr) {
                        Log.d("MainActivity", "p.name--->" + p.name +"p.phoneNum" + p.phoneNum +" -->p.address:" + p.address + "p.location" + p.location);
                    }
                    historyLatitude=allAddr.get(0).location.latitude;
                    historyLongtitude=allAddr.get(0).location.longitude;
                    historyName=allAddr.get(0).name;
                    mBaiduMap.clear();
                    LatLng point = new LatLng(allAddr.get(0).location.latitude, allAddr.get(0).location.longitude);
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_en);
                    OverlayOptions option = new MarkerOptions()
                            .position(point)
                            .icon(bitmap);
                    mBaiduMap.addOverlay(option);
                    builder = new MapStatus.Builder();
                    builder.zoom(19.0f);
                    LatLng currentPosition = new LatLng(allAddr.get(0).location.latitude,allAddr.get(0).location.longitude);
                    builder.target(currentPosition);
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                    markSingleHandler();
                }
            }
            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

            }
            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
            //废弃
            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        };
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
    }

    private void initSQL(){
        DataBaseUtil util = new DataBaseUtil(this);
        if (!util.checkDataBase()) {
            try {
                util.copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        db=SQLiteDatabase.openOrCreateDatabase("/data/data/com.example.parkapp/databases/parkingdb.db",null);

        markSingleHandler();
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMarkerClick(Marker marker) {

                final String name = marker.getExtraInfo().getString("name");
                PredictThread predictThread = new PredictThread();
                try {
                    predictThread.start();
                    predictThread.join();
                } catch (Exception e) {
                    System.out.println("Exception from main");
                }

                if (name!=""){
                    showPopupWindow(name, predictThread.capacity, predictThread.occupy, predictThread.remain, predictThread.predict);
                }
                return false;
            }
        });
    }

    private void initEvent() {
        setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        //font设置
        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        TextView mine = findViewById(R.id.mine);
        mine.setTypeface(font);

        findViewById(R.id.text1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Integer month_true=month+1;
                        text1 = findViewById(R.id.text1);
                        text1.setText(month_true + "月" + day + "日");
                        m_month = month;
                        m_day = day;
                    }
                }, m_year, m_month, m_day).show();
            }
        });

        findViewById(R.id.text2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        text2 = findViewById(R.id.text2);
                        m_hour=hour;
                        m_minute=minute;
                        if(m_hour<10){
                            text_hour="0"+m_hour;
                        }else {
                            text_hour=""+m_hour;
                        }
                        if (m_minute<10){
                            text_minute="0"+m_minute;
                        }else{
                            text_minute=""+m_minute;
                        }
                        text2.setText(text_hour + ":" + text_minute);
                        mBaiduMap.clear();
                        markSingleHandler();
                    }
                }, m_hour, m_minute, true).show();
            }
        });

        findViewById(R.id.shortbook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText search = findViewById(R.id.search);
                Log.d("search", "onClick: "+search.getText().toString());
                if (TextUtils.isEmpty(search.getText())){
                    Toast.makeText(getApplicationContext(),"Please enter your destination",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, Reserve.class);

                intent.putExtra("destination",search.getText());
                intent.putExtra("name","文心二路");
                intent.putExtra("latitude",22.525269);
                intent.putExtra("longtitude",113.937374);
                startActivity(intent);
            }
        });

        findViewById(R.id.longbook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText search = findViewById(R.id.search);
                Log.d("search", "onClick: "+search.getText().toString());
                if (TextUtils.isEmpty(search.getText())){
                    Toast.makeText(getApplicationContext(),"Please enter your destination",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, Normal.class);
                intent.putExtra("name",historyName);
                intent.putExtra("latitude",historyLatitude);
                intent.putExtra("longtitude",historyLongtitude);
                startActivity(intent);
            }
        });
        findViewById(R.id.mine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Mine.class);
                startActivity(intent);
            }
        });

        final EditText search = findViewById(R.id.search);
        final List<String> lists = new ArrayList<String>();
        lists.add("文心公园");
        lists.add("深圳大学");
        lists.add("深圳动漫园");
        final ListPopupWindow mListPop = new ListPopupWindow(this);
        mListPop.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lists));
        mListPop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mListPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mListPop.setAnchorView(search);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        mListPop.setModal(true);//设置是否是模式
        mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                search.setText(lists.get(position));
                RelativeLayout parkingSelect = findViewById(R.id.parkingSelect);
                parkingSelect.setVisibility(View.VISIBLE);
                mPoiSearch.searchInCity(new PoiCitySearchOption()
                        .keyword(lists.get(position))
                        .city("深圳"));
                mListPop.dismiss();
                switch (position){
                    case 0:
                        historyLongtitude=113.936811;
                        historyLatitude=22.526892;
                    case 1:
                        historyLongtitude=113.942651;
                        historyLatitude=22.53945;
                    case 2:
                        historyLongtitude=113.930257;
                        historyLatitude=22.515024;
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListPop.show();
            }
        });
    }

    public void markSingleHandler(){
        ParkDataThread parkDataThread = new ParkDataThread();
        try {
            parkDataThread.start();
            parkDataThread.join();
        } catch (Exception e) {
            System.out.println("Exception from main");
        }

        ParkdataBean[] parkDataBean = parkDataThread.parkDataBean;
        for (ParkdataBean parkData:parkDataBean){
            String name = parkData.getName();
            Double longtitude = Double.valueOf(parkData.getAtitude().split(",")[0]);
            Double latitude = Double.valueOf(parkData.getAtitude().split(",")[1]);
            int capacity = parkData.getCapacity();
            int occupy = parkData.getOccupy();
            int remain = parkData.getRemain();
            Double info = Double.valueOf(occupy)/capacity;
            if(info<0.34){markSingle(latitude,longtitude,name,capacity,"little",occupy);}
            else if (info<0.67){markSingle(latitude,longtitude,name,capacity,"middle",occupy);}
            else{markSingle(latitude,longtitude,name,capacity,"much",occupy);}
        }
    }
    //标记函数
    private void markSingle(double latitude,double longtitude,String name,Integer capacity,String Info,Integer occupation) {    //地图标注

        LatLng point = new LatLng(latitude, longtitude);
        BitmapDescriptor bitmap_much = BitmapDescriptorFactory.fromResource(R.drawable.red);
        BitmapDescriptor bitmap_middle = BitmapDescriptorFactory.fromResource(R.drawable.yellow);
        BitmapDescriptor bitmap_little = BitmapDescriptorFactory.fromResource(R.drawable.green);
        Bundle mBundle = new Bundle();
        mBundle.putString("name",name);
        mBundle.putDouble("latitude",latitude);
        mBundle.putDouble("longtitude",longtitude);
        mBundle.putInt("capacity",capacity);

        OverlayOptions markOption;
        if(Info=="much") {
            markOption = new MarkerOptions()
                    .position(point)
                    .perspective(true)
                    .icon(bitmap_much)
                    .alpha(5)
                    .extraInfo(mBundle);
        }else if (Info=="middle"){
            markOption = new MarkerOptions()
                    .position(point)
                    .perspective(true)
                    .icon(bitmap_middle)
                    .alpha(5)
                    .extraInfo(mBundle);
        }else{
            markOption = new MarkerOptions()
                    .position(point)
                    .perspective(true)
                    .icon(bitmap_little)
                    .alpha(5)
                    .extraInfo(mBundle);
        }
        OverlayOptions textOption = new TextOptions()
                .text(occupation+"/"+capacity+"")
                .bgColor(0xFF1890FF)
                .fontSize(30)
                .fontColor(0xAAFFFFFF)
                .position(point)
                .extraInfo(mBundle);

        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        options.add(markOption);
        options.add(textOption);
        mBaiduMap.addOverlays(options);

    }
    //弹出框函数
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showPopupWindow(String name, int capacity, int occupy, int remain, List<Integer> predict) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vPopupWindow = inflater.inflate(R.layout.activity_mainpopupwindow, null, false);
        PopupWindow popupWindow = new PopupWindow(vPopupWindow, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        View parentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_mainpopupwindow, null);
        popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);


        TextView parkingName=popupWindow.getContentView().findViewById(R.id.parkingName);
        TextView parkingCapacity=popupWindow.getContentView().findViewById(R.id.parkingCapacity);
        parkingName.setText(name);
        parkingCapacity.setText("Total："+capacity+"        Remaining："+remain);

        //默认折线图,即直接调用下面四句话
        final LineChart chart = (LineChart) popupWindow.getContentView().findViewById(R.id.bar_chart); //获取画布
        final LineCharts lineCharts = new LineCharts(chart); //创建折线图对象
        LineData mLineData = lineCharts.getLineData(name, capacity, remain, predict); //初始化折线图数据，数据有几个
        chart.setData(mLineData); //显示数据
    }
    //权限申请函数
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO request success
                }
                break;
        }
    }




    //地图函数
    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        mPoiSearch.destroy();
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
    public class MyLocationListener extends BDAbstractLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location){
            if (location == null || mMapView == null){
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            currentLatitude = location.getLatitude();
            currentLongtitude = location.getLongitude();

            if (isFirstLocation) {
                isFirstLocation = false;
                builder = new MapStatus.Builder();
                builder.zoom(18.0f);
                LatLng currentPosition = null;
//                BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.circle);
//                MyLocationConfiguration mLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,mCurrentMarker,)

                if (currentLatitude>22.27&&currentLatitude<22.52&&currentLongtitude>113.46&&currentLongtitude<114.37){
                    currentPosition = new LatLng(currentLatitude,currentLongtitude);
                }else{
                    currentPosition = new LatLng(22.525269,113.938);


                        Log.d("location111", "sure: ");
                        LatLng point = new LatLng(22.525269,113.938);
                        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.circle);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("name","");
                    mBundle.putDouble("latitude",0.0);
                    mBundle.putDouble("longtitude",0.0);
                    mBundle.putInt("capacity",0);
                        OverlayOptions markOption = new MarkerOptions()
                                .position(point)
                                .perspective(true)
                                .alpha(10)
                                .extraInfo(mBundle)
                                .icon(bitmap);
                        mBaiduMap.addOverlay(markOption);

                }
                builder.target(currentPosition);
//                mBaiduMap.setMyLocationConfiguration(mLocationConfiguration)
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }


        }
    }
}

