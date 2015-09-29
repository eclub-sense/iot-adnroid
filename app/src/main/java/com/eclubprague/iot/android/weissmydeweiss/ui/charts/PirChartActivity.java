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

    private int chartMode = VBAT;
    private static final int VBAT = 0;
    private static final int PIR = 1;

    private ImageView iv_vbat_mode;
    private ImageView iv_pir_mode;

    private int range = 15;

    private ArrayList<GetSensorDataByIdTask.TaskDelegate> delegateRef = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.pir_chart_2);
        counter = (TextView) findViewById(R.id.tv_counter);

        tv_owner = (TextView) findViewById(R.id.tv_owner);
        tv_owner.setText(getIntent().getStringExtra("owner"));

        tv_desc = (TextView) findViewById(R.id.tv_description);
        tv_desc.setText(getIntent().getStringExtra("description"));

        tv_access = (TextView) findViewById(R.id.tv_access);
        tv_access.setText(getIntent().getStringExtra("access"));

        iv_vbat_mode = (ImageView) findViewById(R.id.iv_vbat_mode);
        iv_pir_mode = (ImageView) findViewById(R.id.iv_pir_mode);

        iv_vbat_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartMode = VBAT;
                //stopTimerTask();
                drawChart();
                new GetSensorDataByIdTask(delegateRef, token, uuid, true).execute();
                //startTimer();
            }
        });

        iv_pir_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartMode = PIR;
                //stopTimerTask();
                drawChart();
                new GetSensorDataByIdTask(delegateRef, token, uuid, true).execute();
                //startTimer();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);

        token = getIntent().getStringExtra("token");
        uuid = getIntent().getStringExtra("uuid");

        actionBar.setTitle("Pir sensor");

        drawChart();

        delegateRef.add(this);
        //todo start timer task
        startTimer();

    }

    public void drawChart() {
        if (mChart != null) {
            mChart.clear();
        }
        mChart = null;
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
            case PIR:
                leftAxis.setAxisMaxValue(2f);
                leftAxis.setAxisMinValue(-1f);
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
            default:
                set = new LineDataSet(null, "Pir");
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

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            data.addXValue(timeStamp);
            data.addEntry(new Entry(val, set.getEntryCount(), timeStamp), 0);
            mChart.notifyDataSetChanged();
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
        }
        return true;
    }


    private int lastPirValue = 0;

    //private int seconds = 10;

    @Override
    public void onGetSensorDataByIdTaskCompleted(SensorAndData sData) {

        List<SetOfData> measured;

        measured = sData.getMeasured();
        if (measured.size() == 0) {
            Log.e("measured", "nothing in here");
            return;
        }

        for (int i = 0; i < measured.size(); i++) {

            if (measured.get(i).getName().contains("pir")) {

                int lastIndex = measured.get(i).getItems().size() - 1;
                if (lastIndex < 0) {
                    continue;
                }

                Data lastData = measured.get(i).getItems().get(lastIndex);

//                DateFormat format = new SimpleDateFormat("MMM d, yyyy HH:mm:ss aaa");
//                try {
//
//                    Date now = new Date();
//                    Date date = format.parse(lastData.getTime());
//
//                    if (now.getTime() > date.getTime() + 120000) continue;
//
//                } catch (ParseException e) {
//                    Log.e("DATE", e.toString());
//                }

                Log.e("PIR", lastData.getValue());
                if (Integer.parseInt(lastData.getValue()) != lastPirValue && lastPirValue == 0) {
                    numOfPersons++;
                    counter.setText(Integer.toString(numOfPersons));
                    counter.invalidate();
                }
                lastPirValue = Integer.parseInt(lastData.getValue());
            }
        }
    }

    @Override
    public void onGetSensorDataByIdTaskWithFlageCompleted(SensorAndData sData) {

        List<SetOfData> measured;

        measured = sData.getMeasured();
        if (measured.size() == 0) {
            Log.e("measured", "nothing in here");
            return;
        }

        for (int i = 0; i < measured.size(); i++) {

            if (measured.get(i).getName().contains("pir") && chartMode == PIR) {
                fillChart(measured.get(i).getItems());
            } else if (measured.get(i).getName().contains("vbat") && chartMode == VBAT) {
                fillChart(measured.get(i).getItems());
            }
        }
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
        new GetSensorDataByIdTask(delegateRef, token, uuid, true).execute();
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
