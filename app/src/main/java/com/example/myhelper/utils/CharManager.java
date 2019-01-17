package com.example.myhelper.utils;

import android.content.Context;
import android.graphics.Color;

import com.example.myhelper.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Administrator on 2019/1/17.
 */

public class CharManager {

    private static CharManager instance;

    private PieChart mPieChart;
    private WeakReference<Context>  context;
    private PieDataSet mPieDataSet;

//    public static CharManager getInstance(Context context,PieChart mPieChart){
//        if (instance == null){
//            instance = new CharManager(context,mPieChart);
//        }
//        return instance;
//    }

    public CharManager(Context context,PieChart mPieChart) {
        this.context = new WeakReference<Context>(context);
        this.mPieChart = mPieChart;

        initChart();
    }

    private void initChart() {
        mPieChart.setUsePercentValues(false);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);
        mPieChart.setNoDataText("无更多数据");
        mPieChart.setDragDecelerationFrictionCoef(0.95f);
//        //设置中间文字
//        double profit = totalIncome - totalCost;
//        mPieChart.setCenterText("本月利润\n"+profit+"元");
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);
        mPieChart.setRotationEnabled(false);
    }

    public void setCenterText(String content){
        //设置中间文字
        mPieChart.setCenterText(content);
    }


    //设置数据
    public void setData(ArrayList<PieEntry> entries) {

        mPieDataSet = new PieDataSet(entries,"");

        mPieDataSet.setSliceSpace(3f);
        mPieDataSet.setSelectionShift(5f);

        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        mPieDataSet.setColors(colors);

        PieData data = new PieData(mPieDataSet);
//        data.setValueFormatter(new);

        data.setValueTextSize(12f);
        if (context.get() != null){
            data.setValueTextColor(context.get().getResources().getColor(R.color.text_color1));
        }

        mPieChart.setData(data);
        mPieChart.highlightValues(null);

        Legend legend=mPieChart.getLegend();//设置比例图
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
//        legend.setEnabled(false);//图例不显示
        //刷新
        mPieChart.invalidate();
    }
}
