package com.eclubprague.iot.android.weissmydeweiss.ui.charts;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.Data;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorAndData;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SetOfData;
import com.eclubprague.iot.android.weissmydeweiss.tasks.GetSensorDataByIdTask;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.components.CustomMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dat on 13.9.2015.
 */
public class ThermChartActivity extends ActionBarActivity implements GetSensorDataByIdTask.TaskDelegate {

    private LineChart mChart;

    private String token;
    private String uuid;

    private TextView tv_temperature;
    private TextView tv_time;
    private SeekBar sb_visible_range;

    private TextView tv_owner;
    private TextView tv_desc;
    private TextView tv_access;

    private ImageView iv_vbat_mode;
    private ImageView iv_temp_mode;
    private ImageView iv_pressure_mode;

    private int chartMode = VBAT;
    private static final int VBAT = 0;
    private static final int TEMPERATURE = 1;
    private static final int PRESSURE = 2;

    private ArrayList<GetSensorDataByIdTask.TaskDelegate> delegateRef = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.therm_chart_2);
        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_time = (TextView) findViewById(R.id.tv_time);

        tv_owner = (TextView) findViewById(R.id.tv_owner);
        tv_owner.setText(getIntent().getStringExtra("owner"));

        tv_desc = (TextView) findViewById(R.id.tv_description);
        tv_desc.setText(getIntent().getStringExtra("description"));

        tv_access = (TextView) findViewById(R.id.tv_access);
        tv_access.setText(getIntent().getStringExtra("access"));

        iv_vbat_mode = (ImageView) findViewById(R.id.iv_vbat_mode);
        iv_temp_mode = (ImageView) findViewById(R.id.iv_temp_mode);
        iv_pressure_mode = (ImageView) findViewById(R.id.iv_pressure_mode);

        iv_vbat_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartMode = VBAT;
                stopTimerTask();
                drawChart();
                startTimer();
            }
        });

        iv_temp_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartMode = TEMPERATURE;
                stopTimerTask();
                drawChart();
                startTimer();
            }
        });

        iv_pressure_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartMode = PRESSURE;
                stopTimerTask();
                drawChart();
                startTimer();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);

        token = getIntent().getStringExtra("token");
        uuid = getIntent().getStringExtra("uuid");

        actionBar.setTitle("Thermometer");

        drawChart();

        //dataSets = createSetList();

        delegateRef.add(this);
        //todo start timer task
        startTimer();

    }

    public void drawChart() {
        if(mChart != null) {
            mChart.clear();
        }
        mChart = null;
        mChart = (LineChart) findViewById(R.id.therm_chart);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("No data available yet.");

        CustomMarkerView mv = new CustomMarkerView(this, R.layout.tv_content_layout);
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
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
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
        switch (chartMode) {
            case VBAT:
                leftAxis.setAxisMaxValue(40f);
                leftAxis.setAxisMinValue(0f);
                break;
            case TEMPERATURE:
                leftAxis.setAxisMaxValue(40f);
                leftAxis.setAxisMinValue(-40f);
                break;
            case PRESSURE:
                leftAxis.setAxisMaxValue(1100f);
                leftAxis.setAxisMinValue(500f);
                break;
        }
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
        mChart.invalidate();
    }

    private LineDataSet createSet() {

        LineDataSet set;
        switch (chartMode) {
            case VBAT:
                set = new LineDataSet(null, "Battery (V)");
                break;
            case TEMPERATURE:
                set = new LineDataSet(null, "Temperature (C)");
                break;
            default:
                set = new LineDataSet(null, "Pressure (hPa)");
        }
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(2f);
        set.setCircleSize(0.5f);
        set.setFillAlpha(65);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private void addEntry(float val, String timeStamp) {

        LineData data = mChart.getData();
        //Log.e("NUM", "1");

        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            //Log.e("NUM", "2");
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
                //Log.e("NUM", "3");
            }

            // add a new x-value first
            data.addXValue(timeStamp);
            //Log.e("NUM", "4");
            //set.addEntry(new Entry(val, set.getEntryCount(), timeStamp));
            data.addEntry(new Entry(val, set.getEntryCount(), timeStamp), 0);
            //Log.e("NUM", "5");

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();
            //Log.e("NUM", "6");
            // limit the number of visible entries
            //mChart.setVisibleXRangeMaximum(15);
            //Log.e("NUM", "7");
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            //if(!history)
            // mChart.moveViewToX(data.getXValCount() - 16);
            //Log.e("NUM", "8");

            // this automatically refreshes the chart (calls invalidate())
            // mChart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO stop timer task
        stopTimerTask();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chart_menu, menu);
        return true;
    }

    //private boolean history = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_pause:
                try {
                    //TODO stop timer task
                    stopTimerTask();
                } catch (Exception e) {
                    Log.e("UNREG", e.toString());
                }
                break;
            case R.id.action_continue:
                //history = false;
                //mChart.clear();
                try {
                    //TODO start timer task
                    startTimer();
                } catch (Exception e) {
                    Log.e("REG", e.toString());
                }
                break;
            case R.id.action_history:
//                history = true;
//                try {
//                    stopTimerTask();
//                    new GetSensorDataByIdTask(delegateRef, token, uuid).execute();
//                } catch (Exception e) {
//                    Log.e("History", e.toString());
//                }

        }
        return true;
    }


    @Override
    public void onGetSensorDataByIdTaskCompleted(SensorAndData sData) {

        List<SetOfData> measured;

        measured = sData.getMeasured();
        if (measured.size() == 0) {
            Log.e("measured", "nothing in here");
            return;
        }

        for (int i = 0; i < measured.size(); i++) {

//            if (!measured.get(i).getName().contains("temperature") && !measured.get(i).getName().contains("vbat"))
//                continue;

            if (measured.get(i).getName().contains("temperature")) {

                int lastIndex = measured.get(i).getItems().size() - 1;
                if (lastIndex < 0) {
                    continue;
                }

                Data lastData = measured.get(i).getItems().get(lastIndex);

                Log.e("TEMP", lastData.getValue());
                float val = Float.parseFloat(lastData.getValue());
                int i_val = (val - (int) val >= 0.5 ? (int) val + 1 : (int) val);
                //int val = ()
                tv_temperature.setText(Integer.toString(i_val) + " \u00B0" + "C");
                tv_temperature.invalidate();
                tv_time.setText(lastData.getTime());
                tv_time.invalidate();

                if(chartMode == TEMPERATURE) {
                    fillChart(measured.get(i).getItems());
                }
            } else if (measured.get(i).getName().contains("vbat") && chartMode == VBAT) {
                fillChart(measured.get(i).getItems());

//                resetChart();
//
//                int increment = measured.get(i).getItems().size() / 100;
//                if (increment == 0) increment = 1;
//
//                for (int j = 0; j < measured.get(i).getItems().size(); j += increment) {
//                    float val = Float.parseFloat(measured.get(i).getItems().get(j).getValue()) / 1000f;
//                    Log.e("VAL", Float.toString(val));
//                    addEntry(val,
//                            measured.get(i).getItems().get(j).getTime());
//                }
//                mChart.setVisibleXRangeMaximum(mChart.getData().getXValCount());
//                mChart.invalidate();

//                    Log.e("vbat", lastData.getValue());
//                    addEntry(Float.parseFloat(lastData.getValue()), lastData.getTime());
            } else if (measured.get(i).getName().contains("pressure") && chartMode == PRESSURE) {
                fillChart(measured.get(i).getItems());
            }
        }
        //}
    }

    private void fillChart(ArrayList<Data> items) {
        resetChart();

        int increment = items.size() / 100;
        if (increment == 0) increment = 1;

        for (int j = 0; j < items.size(); j += increment) {
            float val = Float.parseFloat(items.get(j).getValue());
            if(chartMode == VBAT) {
                val = val / 1000f;
            }
            Log.e("VAL", Float.toString(val));
            addEntry(val,
                    items.get(j).getTime());
        }
        mChart.setVisibleXRangeMaximum(mChart.getData().getXValCount());
        mChart.invalidate();
    }

    private void resetChart() {
        mChart.clear();
        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        // add empty data
        mChart.setData(data);
    }


    private Timer timer;
    private TimerTask timerTask;
    final Handler handler = new Handler();


    public void startTimer() {
        if (timer != null) return;
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, after the first 3000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 1000, 10000); //
    }

    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        new GetSensorDataByIdTask(delegateRef, token, uuid).execute();
                    }
                });
            }
        };
    }
}
