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
import android.widget.Toast;

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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dat on 26.8.2015.
 */
public class SensorDataChartActivity extends ActionBarActivity implements
        OnChartValueSelectedListener, GetSensorDataByIdTask.TaskDelegate {

    private LineChart mChart;

    private String title;

    //private String[] datasetDesc;

    private String token;

    private String uuid;

    //Activity ovveride
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_realtime_linechart);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);

        token = getIntent().getStringExtra("token");
        uuid = getIntent().getStringExtra("uuid");

        title = getIntent().getStringExtra("title");

        actionBar.setTitle(title);

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
        leftAxis.setAxisMaxValue(/*100f*/getIntent().getFloatExtra("upperBound", 100));
        leftAxis.setAxisMinValue(/*0f*/-1 * getIntent().getFloatExtra("upperBound", 100));
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        //dataSets = createSetList();

        delegateRef.add(this);

        //todo start timer task
        startTimer();

    }

    //Activity override

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO stop timer task
        stopTimerTask();
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
    private void addEntry(float val, String timeStamp, int dataSetIndex, String dataSetName) {

        //System.out.println( sdf.format(cal.getTime()) );

        LineData data = mChart.getData();

        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(dataSetIndex);
            if (set == null) {
                set = createSet(dataSetIndex, dataSetName);
                data.addDataSet(set);
            }

            data.addXValue(timeStamp);
            data.addEntry(new Entry(val, set.getEntryCount(), timeStamp), dataSetIndex);

            mChart.notifyDataSetChanged();
            mChart.setVisibleXRangeMaximum(10);
            if(!history)
            mChart.moveViewToX(data.getXValCount() - 11);
            //mChart.invalidate();
        }
    }

    int[] colors = {
            ColorTemplate.getHoloBlue(), Color.GREEN,
            Color.BLACK, Color.RED, Color.YELLOW,
            Color.GRAY, Color.CYAN
    };

    private LineDataSet createSet(int dataSetIndex, String dataSetName) {
        LineDataSet dataSet = new LineDataSet(null, dataSetName);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setColor(colors[dataSetIndex]);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chart_menu, menu);
        return true;
    }


    private boolean history = false;

    private void clearChart() {
        Log.e("Cleaning", "");
        mChart.getData().clearValues();
        mChart.invalidate();
    }

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
                    history = false;
                    clearChart();
                try {
                    //TODO start timer task
                    startTimer();
                } catch (Exception e) {
                    Log.e("REG", e.toString());
                }
                break;
            case R.id.action_history:
                history = true;
                try {
                    stopTimerTask();
                    new GetSensorDataByIdTask(delegateRef, token, uuid, false).execute();
                } catch (Exception e) {
                    Log.e("History", e.toString());
                }

        }
        return true;
    }


    @Override
    public void onGetSensorDataByIdTaskCompleted(SensorAndData sData) {

        List<SetOfData> measured;

        measured = sData.getMeasured();
        if(measured.size() == 0) {
            Log.e("measured", "nothing in here");
            return;
        }

        //History requested
        if(history == true) {
            //stopTimerTask();
            clearChart();


            for(int i = 0; i < measured.size(); i++) {
                Log.e("Index", Integer.toString(i) + " : " + measured.get(i).getName() + ", size : " + measured.get(i).getItems().size());

                int increment = measured.get(i).getItems().size() / 100;
                if(increment == 0) increment = 1;

                for(int j = 0; j < measured.get(i).getItems().size(); j+=increment) {
                    addEntry(
                            Float.parseFloat(measured.get(i).getItems().get(j).getValue()),
                            measured.get(i).getItems().get(j).getTime(),
                            i, measured.get(i).getName());
                }
            }
            mChart.invalidate();

            Log.e("FillHist", "HIST");
            return;

        }

        //Realtime run
        for(int i = 0; i < measured.size(); i++) {
            int lastIndex = measured.get(i).getItems().size() - 1;
            if(lastIndex < 0) continue;
            Data lastData = measured.get(i).getItems().get(lastIndex);

            DateFormat format = new SimpleDateFormat("MMM d, yyyy HH:mm:ss aaa");
            try {

                Date now = new Date();
                Date date = format.parse(lastData.getTime());

                if(now.getTime() > date.getTime() + 10000) continue;

            } catch (ParseException e) {
                Log.e("DATE", e.toString());
            }

            addEntry(
                    Float.parseFloat(lastData.getValue()),
                    lastData.getTime(),
                    i, measured.get(i).getName());
        }

    }

    @Override
    public void onGetSensorDataByIdTaskWithFlageCompleted(SensorAndData sData) {

    }

    //----------------------------------------------------------------
    // TIMER TASK
    // DO SOME WORKS PERIODICALLY
    //----------------------------------------------------------------

    private Timer timer;
    private TimerTask timerTask;
    final Handler handler = new Handler();


    public void startTimer() {
        if(timer != null) return;
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, after the first 3000ms the TimerTask will run every 2000ms
        timer.schedule(timerTask, 3000, 2000); //
    }

    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private ArrayList<GetSensorDataByIdTask.TaskDelegate> delegateRef = new ArrayList<>();

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
