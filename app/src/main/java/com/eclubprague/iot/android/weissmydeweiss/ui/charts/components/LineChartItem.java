package com.eclubprague.iot.android.weissmydeweiss.ui.charts.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.SensorDataChartActivity;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.ThermometerChartActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by Dat on 2.9.2015.
 */
public class LineChartItem extends ChartItem {

    private Typeface mTf;
    private ArrayList<ThermometerChartActivity> activityRef = new ArrayList<>();
    private float upperBound;
    private String description = "zero";

    public LineChartItem(Context c, ArrayList<ThermometerChartActivity> activityRef,
                         String description, float upperBound) {
        super();

        mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
        this.activityRef = activityRef;
        this.description = description;
        this.upperBound = upperBound;
    }

    @Override
    public int getItemType() {
        return TYPE_LINECHART;
    }

    private LineChart chart;

    @Override
    public View getView(int position, View convertView, Context c) {

        //ViewHolder holder;

        if (convertView == null) {

            //holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_linechart, null);
            Log.e("INITCHART", description);
            chart = (LineChart) convertView.findViewById(R.id.chart);

            convertView.setTag(chart);

        } else {
            chart = (LineChart) convertView.getTag();
        }


        //chart.setOnChartValueSelectedListener(this);

        // no description text
        chart.setDescription(description);
        chart.setNoDataTextDescription("No data available yet.");

        CustomMarkerView mv = new CustomMarkerView (activityRef.get(0), R.layout.tv_content_layout);
        chart.setMarkerView(mv);

        // enable value highlighting
        chart.setHighlightEnabled(true);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        // add empty data
        chart.setData(data);

        Typeface tf = Typeface.createFromAsset(activityRef.get(0).getAssets(), "OpenSans-Regular.ttf");

        // get the legend (only possible after setting data)
        Legend legend = chart.getLegend();

        // modify the legend ...
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setTypeface(tf);
        legend.setTextColor(Color.BLACK);

        XAxis xl = chart.getXAxis();
        xl.setTypeface(tf);
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setSpaceBetweenLabels(5);
        xl.setEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaxValue(upperBound);
        leftAxis.setAxisMinValue(-1 * upperBound);
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        return convertView;
    }

//    private static class ViewHolder {
//        LineChart chart;
//    }

    public void addEntry(float val, String timeStamp, int dataSetIndex, String dataSetName, int colorIndex, boolean history) {

        //System.out.println( sdf.format(cal.getTime()) );
        Log.e("FAILED", description);
        LineData data = chart.getData();

        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(dataSetIndex);
            if (set == null) {
                set = createSet(dataSetName, colorIndex);
                data.addDataSet(set);
            }

            data.addXValue(timeStamp);
            data.addEntry(new Entry(val, set.getEntryCount(), timeStamp), dataSetIndex);

            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(10);
            if(!history)
                chart.moveViewToX(data.getXValCount() - 11);
            //mChart.invalidate();
        }
    }

    int[] colors = {
            ColorTemplate.getHoloBlue(), Color.GREEN,
            Color.BLACK, Color.RED, Color.YELLOW,
            Color.GRAY, Color.CYAN
    };

    private LineDataSet createSet(String dataSetName, int colorIndex) {
        LineDataSet dataSet = new LineDataSet(null, dataSetName);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setColor(colors[colorIndex]);
        dataSet.setCircleColor(Color.RED);
        dataSet.setLineWidth(2f);
        dataSet.setCircleSize(2f);
        dataSet.setFillAlpha(65);
        dataSet.setHighLightColor(Color.rgb(244, 117, 117));
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(9f);
        dataSet.setDrawValues(false);
        return dataSet;
    }


    public void clearChart() {
        Log.e("Cleaning", "");
        if(chart != null && chart.getData() != null) {
            chart.getData().clearValues();
            chart.invalidate();
        }
    }

    public void invalidate() {
        chart.invalidate();
    }

}

