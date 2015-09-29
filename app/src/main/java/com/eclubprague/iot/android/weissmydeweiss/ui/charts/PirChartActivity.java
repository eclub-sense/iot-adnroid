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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.Data;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorAndData;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SetOfData;
import com.eclubprague.iot.android.weissmydeweiss.tasks.GetSensorDataByIdTask;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.components.CustomMarkerView;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.components.LineChartItem;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dat on 8.9.2015.
 */
public class PirChartActivity extends ActionBarActivity implements GetSensorDataByIdTask.TaskDelegate {

    private LineChart mChart;

    private String token;
    private String uuid;

    private TextView counter;
    private SeekBar sb_visible_range;
    private int numOfPersons = 0;

    private TextView tv_owner;
    private TextView tv_desc;
    private TextView tv_access;

    private int range = 15;

    private ArrayList<GetSensorDataByIdTask.TaskDelegate> delegateRef = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.pir_chart_2);
        counter = (TextView)findViewById(R.id.tv_counter);

        tv_owner = (TextView) findViewById(R.id.tv_owner);
        tv_owner.setText(getIntent().getStringExtra("owner"));

        tv_desc = (TextView) findViewById(R.id.tv_description);
        tv_desc.setText(getIntent().getStringExtra("description"));

        tv_access = (TextView) findViewById(R.id.tv_access);
        tv_access.setText(getIntent().getStringExtra("access"));

//        sb_visible_range = (SeekBar) findViewById(R.id.sb_visible_range);
//        sb_visible_range.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            int progress;
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if(mChart == null) return;
//                this.progress = progress;
//                //mChart.setVisibleXRangeMaximum((float)progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(PirChartActivity.this, "YRange: " + Integer.toString(progress), Toast.LENGTH_SHORT).show();
//                mChart.getAxisLeft().setAxisMaxValue((float) progress);
//                mChart.refreshDrawableState();
//                mChart.invalidate();
//            }
//        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);

        token = getIntent().getStringExtra("token");
        uuid = getIntent().getStringExtra("uuid");

        actionBar.setTitle("Pir sensor");

        mChart = (LineChart) findViewById(R.id.pir_chart);

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
        //legend.setEnabled(false);

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
        leftAxis.setAxisMaxValue(15f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        //dataSets = createSetList();

        delegateRef.add(this);
        //todo start timer task
        startTimer();

    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Battery (V)");
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
                //Log.e("NUM", "3");
                set = createSet();
                //Log.e("NUM", "4");
                data.addDataSet(set);
            }
            //Log.e("NUM", "5");

            // add a new x-value first
            data.addXValue(timeStamp);
            //Log.e("NUM", "6");
            data.addEntry(new Entry(val, set.getEntryCount(), timeStamp), 0);
            //Log.e("NUM", "7");
            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();
            //Log.e("NUM", "8");
            // limit the number of visible entries
            //mChart.setVisibleXRangeMaximum(data.getXValCount());
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);
            //Log.e("NUM", "9");
            // move to the latest entry
            //if(!history)
                //mChart.moveViewToX(data.getXValCount() - 16);
            //mChart.invalidate();
            //Log.e("NUM", "10");
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
//                history = false;
//                mChart.clear();
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



    private int lastPirValue = 0;

    private int seconds = 10;

    @Override
    public void onGetSensorDataByIdTaskCompleted(SensorAndData sData) {

        List<SetOfData> measured;

        measured = sData.getMeasured();
        if (measured.size() == 0) {
            Log.e("measured", "nothing in here");
            return;
        }

        //History requested
        /*if (history == true) {

            mChart.clear();

            for (int i = 0; i < measured.size(); i++) {
                Log.e("NAME", measured.get(i).getName());

                if (measured.get(i).getItems().size() <= 0) {
                    continue;
                }

                int increment = measured.get(i).getItems().size() / 100;
                if (increment == 0) increment = 1;

                if (measured.get(i).getName().contains("vbat")) {

                    for (int j = 0; j < measured.get(i).getItems().size(); j += increment) {
                        Log.e("Entry", Float.toString(Float.parseFloat(measured.get(i).getItems().get(j).getValue()) / 100f));
                        addEntry(Float.parseFloat(measured.get(i).getItems().get(j).getValue()) / 100f,
                                measured.get(i).getItems().get(j).getTime());
                    }
                    Log.e("FillHist", "HISTo");
                    return;
                }
                *TODO draw pir history

            }
        } else {*/
            for (int i = 0; i < measured.size(); i++) {

                if (!measured.get(i).getName().contains("pir") && !measured.get(i).getName().contains("vbat"))
                    continue;

                //TODO count people
                if(measured.get(i).getName().contains("pir")) {

                    int lastIndex = measured.get(i).getItems().size() - 1;
                    if (lastIndex < 0) {
                        continue;
                    }

                    Data lastData = measured.get(i).getItems().get(lastIndex);

                    DateFormat format = new SimpleDateFormat("MMM d, yyyy HH:mm:ss aaa");
                    try {

                        Date now = new Date();
                        Date date = format.parse(lastData.getTime());

                        if (now.getTime() > date.getTime() + 120000) continue;

                    } catch (ParseException e) {
                        Log.e("DATE", e.toString());
                    }

                    Log.e("PIR", lastData.getValue());
                    if (Integer.parseInt(lastData.getValue()) != lastPirValue && lastPirValue == 0) {
                        numOfPersons++;
                        counter.setText(Integer.toString(numOfPersons));
                        counter.invalidate();
                    }
                    lastPirValue = Integer.parseInt(lastData.getValue());
                } else {

                    if(seconds < 10) {
                        seconds++;
                    } else {
                        seconds = 0;
                        //mChart.clear();
                        resetChart();
                        int increment = measured.get(i).getItems().size() / 100;
                        if (increment == 0) increment = 1;

                        for(int j = 0; j < measured.get(i).getItems().size(); j+=increment) {
                            float val = Float.parseFloat(measured.get(i).getItems().get(j).getValue()) / 1000f;
                            Log.e("VAL", Float.toString(val));
                            addEntry(val,
                                    measured.get(i).getItems().get(j).getTime());
                        }
                        mChart.setVisibleXRangeMaximum(mChart.getData().getXValCount());
                        mChart.invalidate();
                    }

//                    Log.e("vbat", lastData.getValue());
//                    addEntry(Float.parseFloat(lastData.getValue()), lastData.getTime());
                }
            }
        //}
    }

    @Override
    public void onGetSensorDataByIdTaskWithFlageCompleted(SensorAndData sData) {

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
        //schedule the timer, after the first 3000ms the TimerTask will run every 2000ms
        timer.schedule(timerTask, 1000, 1000); //
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

                        new GetSensorDataByIdTask(delegateRef, token, uuid, false).execute();
                    }
                });
            }
        };
    }
}
