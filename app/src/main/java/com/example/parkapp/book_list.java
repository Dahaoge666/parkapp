package com.example.parkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class book_list extends Activity {
    private ListPopupWindow mListPop;
    private List<String> lists = new ArrayList<String>();
    List<List<String>> dbData = new ArrayList<List<String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        final Button time = findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //时间选择器
                TimePickerView pvTime = new TimePickerBuilder(book_list.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        time.setText(getTime(date));
                    }
                })
                        .setType(new boolean[]{true, true, true, true, true, false})
                        .setDate(Calendar.getInstance())
                        .setLabel("年", "月", "日", "时", "分", "")//默认设置为年月日时分秒
                        .setSubmitText("确定")//确定按钮文字
                        .setCancelText("取消")//取消按钮文字
                        .setTitleText("请选择")//标题
                        .build();
                // pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.show();
            }
        });



        Intent intent = getIntent();//声明一个对象，并获得跳转过来的Intent对象
        final String name=intent.getExtras().getString("name");
        final Double latitude=intent.getExtras().getDouble("latitude");
        final Double longtitude=intent.getExtras().getDouble("longtitude");
        LatLng atitude = new LatLng(latitude,longtitude);
        SQLiteDatabase db= SQLiteDatabase.openOrCreateDatabase("/data/data/com.example.parkapp/databases/parkingdb.db",null);
        Cursor cursor = db.query("parking",new String[]{"name","longtitude","latitude","capacity"},null,null,null,null,null);
        while (cursor.moveToNext()){
            int nameindex=cursor.getColumnIndex("name");
            String dbname=cursor.getString(nameindex);
            int longtitudeindex=cursor.getColumnIndex("longtitude");
            double dblongtitude=cursor.getDouble(longtitudeindex);
            int latitudeindex=cursor.getColumnIndex("latitude");
            double dblatitude=cursor.getDouble(latitudeindex);
            LatLng dbatitude = new LatLng(dblatitude,dblongtitude);
            int capacityindexindex=cursor.getColumnIndex("capacity");
            final int capacity=cursor.getInt(capacityindexindex);
            Log.d("list", dbname+capacity+String.valueOf(DistanceUtil.getDistance(atitude, dbatitude)));

            try{
                if(DistanceUtil.getDistance(atitude, dbatitude)<1000) {
                    List<String> list = new ArrayList<String>();
                    list.add(dbname);
                    list.add(String.valueOf(dblatitude));
                    list.add(String.valueOf(dblongtitude));
                    list.add(String.valueOf(capacity));
                    list.add(String.format("%.2f", DistanceUtil.getDistance(atitude, dbatitude)));

                    dbData.add(list);
                    Log.d("list1", "1");
                }
            }catch (Exception e){
                Log.e("list1", "onCreate: ", e);
            }

        }
        TextView destination=findViewById(R.id.destination);
        destination.setText(name);


        lists.add("综合排序");
        lists.add("价格优先");
        lists.add("距离优先");
        mListPop = new ListPopupWindow(this);
        mListPop.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lists));
        final Button select = findViewById(R.id.select);
        mListPop.setWidth(ActionBar.LayoutParams.WRAP_CONTENT);
        mListPop.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        mListPop.setAnchorView(select);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        mListPop.setModal(true);//设置是否是模式
        mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                select.setText(lists.get(position));

                if (lists.get(position)=="距离优先"){
                    getList();
                }
                mListPop.dismiss();
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListPop.show();
            }
        });


        setPage();


    }


    private void intentPage(final Integer dataNum){
        Intent intent = new Intent(book_list.this, book2.class);
        intent.putExtra("longtitude",Double.parseDouble(dbData.get(dataNum).get(2)));
        intent.putExtra("latitude",Double.parseDouble(dbData.get(dataNum).get(1)));
        intent.putExtra("capacity",Double.parseDouble(dbData.get(dataNum).get(3)));
        intent.putExtra("distance",Double.parseDouble(dbData.get(dataNum).get(4)));
        intent.putExtra("name",dbData.get(dataNum).get(0));
        Button time = findViewById(R.id.time);
        String timeData=time.getText().toString();
        Log.d("timeData", timeData);
        if (timeData.equals("预约时间")){
            Toast.makeText(book_list.this,"请选择预约时间",Toast.LENGTH_LONG).show();
            return;
        }
        intent.putExtra("time",timeData);
        startActivity(intent);

//        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case Dialog.BUTTON_POSITIVE://确认支付
//                        Intent intent = new Intent(book_list.this, book2.class);
//                        intent.putExtra("longtitude",Double.parseDouble(dbData.get(dataNum).get(2)));
//                        intent.putExtra("latitude",Double.parseDouble(dbData.get(dataNum).get(1)));
//                        intent.putExtra("name",dbData.get(dataNum).get(0));
//                        startActivity(intent);
//                        break;
//                    case Dialog.BUTTON_NEUTRAL://取消预定
//                        break;
//                }
//            }
//        };
//        //dialog参数设置
//        AlertDialog.Builder builder = new AlertDialog.Builder(book_list.this);  //先得到构造器
//        builder.setTitle("预定价格"); //设置标题
//        builder.setMessage("预定费用：20元，请确认是否预定。"); //设置内容
//        builder.setIcon(R.drawable.park);//设置图标，图片id即可
//        builder.setPositiveButton("确认预定", dialogOnclicListener);
//        builder.setNeutralButton("取消预定", dialogOnclicListener);
//        builder.create().show();
    }

    private void getList(){

        for (Integer i=0;i<dbData.size();i++){
            for (Integer j=i;j<dbData.size();j++) {
                if (Double.parseDouble(dbData.get(j).get(4)) < Double.parseDouble(dbData.get(i).get(4))) {
                    Collections.swap(dbData, j, i);
                }
            }
        }

        setPage();
    }

    private void setPage(){
        Integer dbNum = dbData.size();
        if (dbNum==0){return;}
        if (dbNum>0){
            TextView parkingName1=findViewById(R.id.parkingName1);
            parkingName1.setText(dbData.get(0).get(0)+"停车场");
//            TextView parkingCapacity1=findViewById(R.id.parkingCapacity1);
//            parkingCapacity1.setText("推荐指数："+dbData.get(0).get(3)+"  距离："+dbData.get(0).get(4)+"米\n当前均价：5元/小时");
//            parkingCapacity1.setText("总车位："+dbData.get(0).get(3)+"  距离："+dbData.get(0).get(4)+"米\n当前均价：5元/小时");
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
//            TextView parkingCapacity2 = findViewById(R.id.parkingCapacity2);
            parkingName2.setText(dbData.get(1).get(0) + "停车场");
//            parkingCapacity2.setText("总车位：" + dbData.get(1).get(3) + "  距离：" + dbData.get(1).get(4) + "米\n当前均价：5元/小时");
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
//            TextView parkingCapacity3 = findViewById(R.id.parkingCapacity3);
            parkingName3.setText(dbData.get(2).get(0) + "停车场");
//            parkingCapacity3.setText("总车位：" + dbData.get(2).get(3) + "  距离：" + dbData.get(2).get(4) + "米\n当前均价：5元/小时");
            LinearLayout linear3 =findViewById(R.id.linear3);
            linear3.setVisibility(View.VISIBLE);
            Button parking3=findViewById(R.id.parking3);
            parking3.setVisibility(View.VISIBLE);
            parking3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentPage(2);
                }
            });
        }
    }



    private String getTime(Date date){//可根据需要自行截取数据显示
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return format.format(date);
    }
}
