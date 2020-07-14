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
import java.util.Calendar;
import java.util.List;

public class LineCharts {

    public LineCharts(LineChart mLineChart) {
        mLineChart.setDrawBorders(false);
        mLineChart.setDescription("");// 数据描述
        mLineChart.setNoDataTextDescription("暂无数据");
        mLineChart.setDrawGridBackground(false);
        mLineChart.setGridBackgroundColor(Color.CYAN);
        mLineChart.setTouchEnabled(true);
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setPinchZoom(false);
        mLineChart.setBackgroundColor(Color.WHITE);
        Legend mLegend = mLineChart.getLegend();
        mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
        mLegend.setForm(LegendForm.LINE);// 样式
        mLegend.setFormSize(20f);// 字体
        mLegend.setTextSize(20f);//设置文字大小
        mLegend.setTextColor(Color.parseColor("#000000"));// 颜色
        mLineChart.animateX(1000);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getXAxis().setPosition(XAxisPosition.BOTTOM); // 让x轴在下面
        mLineChart.getAxisRight().setDrawGridLines(false);

    }

    public LineData getLineData(String name, int capacity, int remain, List<Integer> predict) {
        // y轴的数据
        ArrayList<Entry> y_use = new ArrayList<Entry>();
        y_use.add(new Entry(remain, 0));
        int i=1;
        for (Integer val:predict) {
            i++;
            Entry entry = new Entry(val, i);
            y_use.add(entry);
        }

        LineDataSet mLineDataSet_use = new LineDataSet(y_use, "Num of vacant spots");
        mLineDataSet_use.setCircleColor(Color.parseColor("#00C0BF"));
        mLineDataSet_use.setColor(Color.parseColor("#00C0BF"));
        setLineStyle(mLineDataSet_use);
        ArrayList<LineDataSet> mLineDataSets = new ArrayList<LineDataSet>();
        mLineDataSets.add(mLineDataSet_use);
        LineData mLineData = new LineData(getXAxisValues(), mLineDataSets);
        return mLineData;
    }

    public void setLineStyle(LineDataSet mLineDataSet) {

        mLineDataSet.setLineWidth(2.0f);
        mLineDataSet.setCircleSize(5.0f);
        mLineDataSet.setCircleColor(Color.parseColor("#00C0BF"));
        mLineDataSet.setDrawHighlightIndicators(true);
        mLineDataSet.setHighLightColor(Color.CYAN);
        mLineDataSet.setValueTextSize(15.0f);
        mLineDataSet.setDrawCubic(true);
        mLineDataSet.setCircleColorHole(Color.WHITE);
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


    public ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<String>();
        Calendar time = Calendar.getInstance();
        xAxis.add(time.get(Calendar.HOUR)+":"+String.format("%02d",time.get(Calendar.MINUTE)));
        int i = 0;
        for(;i<12;i++){
            time.add(Calendar.MINUTE,5);
            xAxis.add(time.get(Calendar.HOUR)+":"+String.format("%02d",time.get(Calendar.MINUTE)));
        }
        return xAxis;
    }


}



