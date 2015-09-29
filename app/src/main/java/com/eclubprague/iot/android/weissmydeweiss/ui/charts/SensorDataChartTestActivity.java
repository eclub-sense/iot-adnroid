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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dat on 1.9.2015.
 */
public class SensorDataChartTestActivity extends ActionBarActivity implements
        OnChartValueSelectedListener, GetSensorDataByIdTask.TaskDelegate {

    private LineChart mChart;

    private String title;

    private String[] datasetDesc;

    private String token;

    private String uuid;

    private float[] values;

    private ArrayList<LineDataSet> dataSets;

    private int no_vals;

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

        datasetDesc = getIntent().getStringArrayExtra("datasetDesc");
        no_vals = datasetDesc.length;

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
    private void addEntry(/*float[] vals, String[] timeStamps*/ ArrayList<Float> vals, ArrayList<String> timeStamps) {

        //System.out.println( sdf.format(cal.getTime()) );

        LineData data = mChart.getData();

        if (data != null) {

//            ArrayList<LineDataSet> tmp_sets = new ArrayList<>();
//            for(int i = 0; i < no_vals; i++)
//
//            for(int i = 0; i < no_vals; i++) {
//                if(data.getDataSetByIndex(i) == null) {
//                    Log.e("!DATASET", "null");
//                    data.addDataSet(dataSets.get(i));
//                }
//            }

            ArrayList<LineDataSet> datasets = new ArrayList<>();

            for(int i = 0; i < no_vals; i++) {
                LineDataSet set = data.getDataSetByIndex(i);
                if(set == null) {
                    set = createSet(i);
                    data.addDataSet(set);
                }

                datasets.add(set);
            }

            data.addXValue("x_val");

            for(int i = 0; i < no_vals; i++) {
//                Log.e("ADENTRY", i + " : " +  data.getDataSetByIndex(i).getEntryCount());
//                data.getDataSetByIndex(i).addEntry(new Entry(vals[i], data.getDataSetByIndex(i).getEntryCount(), timeStamps[i]));
//
//                data.addEntry(new Entry(vals[i], data.getDataSetByIndex(i).getEntryCount(), timeStamps[i]), i);

                //data.addEntry(new Entry(vals[i], datasets.get(i).getEntryCount(), timeStamps[i]), i);
                Log.e("Float", Float.toString(vals.get(i)));
                Log.e("xxx",Integer.toString(datasets.get(i).getEntryCount()));
                Log.e("yyy", timeStamps.get(i));
                data.addEntry(
                        new Entry(
                                vals.get(i),
                                datasets.get(i).getEntryCount(),
                                timeStamps.get(i)),
                        i);

            }

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRangeMaximum(10);
            mChart.moveViewToX(data.getXValCount() - 11);
            mChart.invalidate();
        }
    }

    int[] colors = {
            ColorTemplate.getHoloBlue(), Color.GREEN,
            Color.BLACK, Color.RED, Color.YELLOW,
            Color.GRAY, Color.CYAN
    };

    private ArrayList<LineDataSet> createSetList() {

        ArrayList<LineDataSet> sets = new ArrayList<>();

        for(int i = 0; i < no_vals; i++) {
            LineDataSet dataSet = new LineDataSet(null, datasetDesc[i]);
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            dataSet.setColor(colors[i]);
            dataSet.setCircleColor(Color.RED);
            dataSet.setLineWidth(2f);
            dataSet.setCircleSize(2f);
            dataSet.setFillAlpha(65);
            dataSet.setHighLightColor(Color.rgb(244, 117, 117));
            dataSet.setValueTextColor(Color.BLACK);
            dataSet.setValueTextSize(9f);
            dataSet.setDrawValues(false);

            sets.add(dataSet);

        }

        return sets;
    }

    private LineDataSet createSet(int index) {
        LineDataSet dataSet = new LineDataSet(null, datasetDesc[index]);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setColor(colors[index]);
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
                if(history == true) {
                    history = false;
                    clearChart();
                }
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
                    //stopTimerTask();
                    new GetSensorDataByIdTask(delegateRef, token, uuid, false).execute();
                } catch (Exception e) {
                    Log.e("History", e.toString());
                }

        }
        return true;
    }


    @Override
    public void onGetSensorDataByIdTaskCompleted(SensorAndData sData) {


        /*float[] vals = new float[no_vals];
        String[] timeStamps = new String[no_vals];*/
        ArrayList<Float> vals = new ArrayList<>();
        ArrayList<String> timeStamps = new ArrayList<>();
        List<SetOfData> measured;

        try {
            measured = sData.getMeasured();
        } catch (Exception e) {
            Log.e("measured", e.toString());
            measured = new ArrayList<>();
        }

        if(history == true) {
            stopTimerTask();
            clearChart();

            try {

                for(int i = 0; i < measured.size();) {

//                    for(int j = 0; j < no_vals; j++, i++) {
//                        /*vals[j] = */vals.add(Float.parseFloat(measured.get(i).getValue()));
//                        /*timeStamps[j] = */timeStamps.add(measured.get(i).getTime());
//                    }

                    for(int j = 0; j < measured.get(i).getItems().size(); i++) {

                        addEntry(vals, timeStamps);
                    }
                    vals.clear();
                    timeStamps.clear();

                }

            } catch (Exception e) {
                Log.e("FillHist", e.toString());
            }


            return;

        }


//        try {

        for(int i = 0; i< no_vals; i++) {
            Log.e("I", Integer.toString(i));
            Log.e("VAL", measured.get(i).getItems().get(
                    measured.get(i).getItems().size() - 1
            ).getValue());

            Log.e("TIME", measured.get(i).getItems().get(
                    measured.get(i).getItems().size() - 1
            ).getTime());

                /*vals[i] = */vals.add( Float.parseFloat(measured.get(i).getItems().get(
                    measured.get(i).getItems().size() - 1
            ).getValue()) );
                /*timeStamps[i] = */timeStamps.add(measured.get(i).getItems().get(
                    measured.get(i).getItems().size() - 1
            ).getTime());
            //Log.e("VALS", vals[i] + " : " + timeStamps[i]);
            Log.e("VALS", vals.get(i) + " : " + timeStamps.get(i));
        }

        addEntry(vals, timeStamps);
//        } catch (Exception e) {
//            Log.e("onTaskCompleted", e.toString());
//        }
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
