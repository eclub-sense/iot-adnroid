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

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    private ArrayList<GetSensorDataByIdTask.TaskDelegate> delegateRef = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.therm_chart);
        tv_temperature = (TextView)findViewById(R.id.tv_temperature);
        tv_time = (TextView)findViewById(R.id.tv_time);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);

        token = getIntent().getStringExtra("token");
        uuid = getIntent().getStringExtra("uuid");

        actionBar.setTitle("Thermometer");

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
        leftAxis.setAxisMaxValue(10);
        leftAxis.setAxisMinValue(0);
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

        LineDataSet set = new LineDataSet(null, "vbat (V)");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.RED);
        set.setLineWidth(4f);
        set.setCircleSize(4f);
        set.setFillAlpha(65);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private void addEntry(float val, String timeStamp) {

        LineData data = mChart.getData();

        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            // add a new x-value first
            data.addXValue(timeStamp);
            set.addEntry(new Entry(val, set.getEntryCount(), timeStamp));
            //data.addEntry(new Entry(val, set.getEntryCount(), timeStamp), 0);

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(15);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            if(!history)
                mChart.moveViewToX(data.getXValCount() - 16);

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

    private boolean history = false;

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
                mChart.clear();
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
                    new GetSensorDataByIdTask(delegateRef, token, uuid).execute();
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
        if (measured.size() == 0) {
            Log.e("measured", "nothing in here");
            return;
        }

        //History requested
        if (history == true) {

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
                } //TODO draw temperature history

//                Log.e("FillHist", "HIST");
//                return;

            }

            //Realtime run

            //int listViewIndex = 0;

        } else {
            for (int i = 0; i < measured.size(); i++) {

                if (!measured.get(i).getName().contains("temperature") && !measured.get(i).getName().contains("vbat"))
                    continue;

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

                //TODO count people
                if(measured.get(i).getName().contains("temperature")) {
                    Log.e("TEMP", lastData.getValue());
                    tv_temperature.setText(lastData.getValue());
                    tv_temperature.invalidate();
                    tv_time.setText(lastData.getTime());
                    tv_time.invalidate();
                } else {
                    Log.e("vbat", lastData.getValue());
                    addEntry(Float.parseFloat(lastData.getValue()), lastData.getTime());
                }
            }
        }
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
        timer.schedule(timerTask, 3000, 10000); //
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
