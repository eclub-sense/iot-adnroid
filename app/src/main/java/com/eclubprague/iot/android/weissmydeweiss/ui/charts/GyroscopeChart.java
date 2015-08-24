package com.eclubprague.iot.android.weissmydeweiss.ui.charts;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.components.CustomMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Dat on 24.8.2015.
 */
public class GyroscopeChart extends ActionBarActivity implements SensorEventListener,
        OnChartValueSelectedListener {

    private LineChart mChart;

    private SensorManager senSensorManager;
    private String sensorName;
    private Sensor sensor;

    //Activity ovveride
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //       WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_realtime_linechart);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);

        sensorName = getIntent().getStringExtra("sensorName");

        actionBar.setTitle(sensorName);

        senSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> deviceSensors = senSensorManager.getSensorList(android.hardware.Sensor.TYPE_ALL);

        for(int i = 0; i < deviceSensors.size(); i++) {
            if(deviceSensors.get(i).getName().equals(sensorName)) {
                sensor = deviceSensors.get(i);
                break;
            }
        }

        senSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("No data available yet.");

        CustomMarkerView mv = new CustomMarkerView (this, R.layout.tv_content_layout);
        mChart.setMarkerView(mv);

        // enable value highlighting
        mChart.setHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        // add empty data
        mChart.setData(data);

        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        // get the legend (only possible after setting data)
        Legend legend = mChart.getLegend();

        // modify the legend ...
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setTypeface(tf);
        legend.setTextColor(Color.BLACK);

        XAxis xl = mChart.getXAxis();
        xl.setTypeface(tf);
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setSpaceBetweenLabels(5);
        xl.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaxValue(/*100f*/sensor.getMaximumRange());
        leftAxis.setAxisMinValue(/*0f*/-1*sensor.getMaximumRange());
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

    }

    //Activity override

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        senSensorManager.unregisterListener(this);
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

    //onChartValueSelectedListener override

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Toast.makeText(this, "value : " + Float.toString(e.getVal()) + ", time : " +
                e.getData().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

    //------------------------------------------------------------------
    private void addEntry(float x_val, float y_val, float z_val) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        //System.out.println( sdf.format(cal.getTime()) );

        LineData data = mChart.getData();

        if (data != null) {

            LineDataSet x_set = data.getDataSetByIndex(0);
            LineDataSet y_set = data.getDataSetByIndex(1);
            LineDataSet z_set = data.getDataSetByIndex(2);
            // set.addEntry(...); // can be called as well

            if (x_set == null) {
                x_set = createSet("x");
                data.addDataSet(x_set);
            }

            if (y_set == null) {
                y_set = createSet("y");
                data.addDataSet(y_set);
            }

            if (z_set == null) {
                z_set = createSet("z");
                data.addDataSet(z_set);
            }

            String time = sdf.format(cal.getTime());

            // add a new x-value first
            data.addXValue(time);
            data.addEntry(new Entry(x_val, x_set.getEntryCount(), time), 0);
            data.addEntry(new Entry(y_val, y_set.getEntryCount(), time), 1);
            data.addEntry(new Entry(z_val, z_set.getEntryCount(), time), 2);

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(10);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getXValCount() - 11);

            // this automatically refreshes the chart (calls invalidate())
            // mChart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private LineDataSet createSet(String axis) {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        switch(axis) {
            case "x":
                set.setColor(ColorTemplate.getHoloBlue());
                set.setFillColor(ColorTemplate.getHoloBlue());
                set.setLabel("x axis");
                break;
            case "y":
                set.setColor(Color.GREEN);
                set.setFillColor(Color.GREEN);
                set.setLabel("y axis");
                break;
            case "z":
                set.setColor(Color.BLACK);
                set.setFillColor(Color.BLACK);
                set.setLabel("z axis");
        }
        set.setCircleColor(Color.RED);
        set.setLineWidth(2f);
        set.setCircleSize(2f);
        set.setFillAlpha(65);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        addEntry(event.values[0], event.values[1], event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.accelerometer_chart, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_pause:
                try {
                    senSensorManager.unregisterListener(this);
                } catch (Exception e) {
                    Log.e("UNREG", e.toString());
                }
                break;
            case R.id.action_continue:
                try {
                    senSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                } catch (Exception e) {
                    Log.e("REG", e.toString());
                }
        }
        return true;
    }
}
