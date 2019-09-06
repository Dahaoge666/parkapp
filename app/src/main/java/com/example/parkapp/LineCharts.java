package com.example.parkapp;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class LineCharts {
    /**
     * 设置chart显示的样式
     * 基本的样式信息，根据需求进行更改
     * 这里时间显示不全面不知道怎么回事
     * @param mLineChart
     */
    public LineCharts(LineChart mLineChart) {
        // 是否在折线图上添加边框
        mLineChart.setDrawBorders(false);
        mLineChart.setDescription("");// 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        mLineChart.setNoDataTextDescription("暂无数据");
        // 是否绘制背景颜色。
        // 如果mLineChart.setDrawGridBackground(false)，
        // 那么mLineChart.setGridBackgroundColor(Color.CYAN)将失效;
        mLineChart.setDrawGridBackground(false);
        mLineChart.setGridBackgroundColor(Color.CYAN);
        // 触摸
        mLineChart.setTouchEnabled(true);
        // 拖拽
        mLineChart.setDragEnabled(true);
        // 缩放
        mLineChart.setScaleEnabled(true);
        mLineChart.setPinchZoom(false);
        // 设置背景
        mLineChart.setBackgroundColor(Color.WHITE);
        // // 设置x,y轴的数据
        // mLineChart.setData(lineData);

        // 设置比例图标示，就是那个一组y的value的
        Legend mLegend = mLineChart.getLegend();
        mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
        mLegend.setForm(LegendForm.LINE);// 样式
        mLegend.setFormSize(18.0f);// 字体
        mLegend.setTextColor(Color.parseColor("#DEAD26"));// 颜色
        // 沿x轴动画，时间2000毫秒。
        mLineChart.animateX(1000);
        mLineChart.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴(true不隐藏)
        mLineChart.getXAxis().setPosition(XAxisPosition.BOTTOM); // 让x轴在下面
        // // 隐藏左边坐标轴横网格线
        // mLineChart.getAxisLeft().setDrawGridLines(false);
        // 隐藏右边坐标轴横网格线
        mLineChart.getAxisRight().setDrawGridLines(false);
        // 隐藏X轴竖网格线
        // mLineChart.getXAxis().setDrawGridLines(false);
        // enable / disable grid lines
        // mLineChart.setDrawVerticalGrid(false); // 是否显示水平的表格线
        // mChart.setDrawHorizontalGrid(false);/ 让x轴在下面
    }
    /**
     * @param count
     *            数据点的数量。
     * @return
     */
    public LineData getLineData(int count,int hour,int min,int num) {
        // ArrayList<String> x = new ArrayList<String>();
        // for (int i = 0; i < count; i++) {
        // // x轴显示的数据
        // x.add("周" + i);
        // }

        // y轴的数据
        ArrayList<Entry> y_use = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * num);
            Entry entry = new Entry(val, i);
            y_use.add(entry);
        }

        // y轴数据集
        LineDataSet mLineDataSet_use = new LineDataSet(y_use, "剩余车位数");

        // 折线的颜色
        mLineDataSet_use.setColor(Color.parseColor("#00C0BF"));
        //这里需要调用后面的折线样式设置
        setLineStyle(mLineDataSet_use);

        //返回折线集
        ArrayList<LineDataSet> mLineDataSets = new ArrayList<LineDataSet>();
        mLineDataSets.add(mLineDataSet_use);

        LineData mLineData = new LineData(getXAxisValues(hour,min), mLineDataSets);

        return mLineData;
    }
    /**
     * 设置折线样式，除了颜色（颜色单独设置）
     *
     * @param mLineDataSet
     */
    public void setLineStyle(LineDataSet mLineDataSet) {
        // 用y轴的集合来设置参数
        // 线宽
        mLineDataSet.setLineWidth(2.0f);
        // 显示的圆形大小
        mLineDataSet.setCircleSize(5.0f);
        // // 折线的颜色，在getLineData中设置了
        // mLineDataSet.setColor(Color.DKGRAY);
        // 圆球的颜色
        mLineDataSet.setCircleColor(Color.parseColor("#00C0BF"));
        // 设置mLineDataSet.setDrawHighlightIndicators(false)后，
        // Highlight的十字交叉的纵横线将不会显示，
        // 同时，mLineDataSet.setHighLightColor(Color.CYAN)失效。
        mLineDataSet.setDrawHighlightIndicators(true);
        // 按击后，十字交叉线的颜色
        mLineDataSet.setHighLightColor(Color.CYAN);
        // 设置这项上显示的数据点的字体大小。
        mLineDataSet.setValueTextSize(15.0f);
        // mLineDataSet.setDrawCircleHole(true);
        // 改变折线样式，用曲线。
        mLineDataSet.setDrawCubic(true);
        // 默认是直线
        // 曲线的平滑度，值越大越平滑。
        // mLineDataSet.setCubicIntensity(0.2f);
        // 填充曲线下方的区域，红色，半透明。
        // mLineDataSet.setDrawFilled(true);
        // mLineDataSet.setFillAlpha(128);
        // mLineDataSet.setFillColor(Color.RED);

        // 填充折线上数据点、圆球里面包裹的中心空白处的颜色。
        mLineDataSet.setCircleColorHole(Color.WHITE);
        // 设置折线上显示数据的格式。如果不设置，将默认显示float数据格式。
        mLineDataSet.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value, Entry entry,
                                            int dataSetIndex, ViewPortHandler viewPortHandler) {
                int n = (int) value;
                String s = "" + n;
                return s;
            }
        });
    }

    /**
     * 设置坐标轴数据
     * 可能会设置一个中间参数传进去
     */
    public ArrayList<String> getXAxisValues(int hour,int min) {
        ArrayList<String> xAxis = new ArrayList<String>();
        if (min <10) {
            if ((hour -3) < 0){
                xAxis.add(String.valueOf(hour - 3 + 24) + ":0" + String.valueOf(min));
            }
            else {
                xAxis.add(String.valueOf(hour - 3) + ":0" + String.valueOf(min));
            }
            if ((hour -2) < 0){
                xAxis.add(String.valueOf(hour - 2 + 24) + ":0" + String.valueOf(min));
            }
            else {
                xAxis.add(String.valueOf(hour - 2) + ":0" + String.valueOf(min));
            }
            if ((hour -1) < 0){
                xAxis.add(String.valueOf(hour - 1 + 24) + ":0" + String.valueOf(min));
            }
            else {
                xAxis.add(String.valueOf(hour - 1) + ":0" + String.valueOf(min));
            }

            xAxis.add(String.valueOf(hour) + ":0" + String.valueOf(min));

            if ((hour + 1) > 24){
                xAxis.add(String.valueOf(hour + 1 - 24) + ":0" + String.valueOf(min));
            }
            else {
                xAxis.add(String.valueOf(hour + 1) + ":0" + String.valueOf(min));
            }
            if ((hour + 2) > 24){
                xAxis.add(String.valueOf(hour + 2 - 24) + ":0" + String.valueOf(min));
            }
            else {
                xAxis.add(String.valueOf(hour + 2) + ":0" + String.valueOf(min));
            }
            if ((hour + 3) > 24){
                xAxis.add(String.valueOf(hour + 3 - 24) + ":0" + String.valueOf(min));
            }
            else {
                xAxis.add(String.valueOf(hour + 3) + ":0" + String.valueOf(min));
            }

        }
        else{
            if ((hour -3) < 0){
                xAxis.add(String.valueOf(hour - 3 + 24) + ":" + String.valueOf(min));
            }
            else {
                xAxis.add(String.valueOf(hour - 3) + ":" + String.valueOf(min));
            }
            if ((hour -2) < 0){
                xAxis.add(String.valueOf(hour - 2 + 24) + ":" + String.valueOf(min));
            }
            else {
                xAxis.add(String.valueOf(hour - 2) + ":" + String.valueOf(min));
            }
            if ((hour -1) < 0){
                xAxis.add(String.valueOf(hour - 1 + 24) + ":" + String.valueOf(min));
            }
            else {
                xAxis.add(String.valueOf(hour - 1) + ":" + String.valueOf(min));
            }

            xAxis.add(String.valueOf(hour) + ":" + String.valueOf(min));

            if ((hour + 1) < 24){
                xAxis.add(String.valueOf(hour + 1) + ":" + String.valueOf(min));
            }
            else {
                xAxis.add(String.valueOf(hour + 1 - 24) + ":" + String.valueOf(min));
            }
            if ((hour + 2) < 24){
                xAxis.add(String.valueOf(hour + 2) + ":" + String.valueOf(min));
            }
            else {
                xAxis.add(String.valueOf(hour + 2 - 24) + ":" + String.valueOf(min));
            }
            if ((hour + 3) < 24){
                xAxis.add(String.valueOf(hour + 3) + ":" + String.valueOf(min));
            }
            else {
                xAxis.add(String.valueOf(hour + 3 - 24) + ":" + String.valueOf(min));
            }

        }
        return xAxis;
    }
}



