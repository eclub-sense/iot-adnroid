package com.eclubprague.iot.android.weissmydeweiss.ui.charts;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.Data;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorAndData;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SetOfData;
import com.eclubprague.iot.android.weissmydeweiss.tasks.GetSensorDataByIdTask;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.components.ChartItem;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.components.LineChartItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dat on 2.9.2015.
 */
public class ThermometerChartActivity extends ActionBarActivity implements GetSensorDataByIdTask.TaskDelegate {

    ArrayList<ThermometerChartActivity> activityRef = new ArrayList<>();

    private String token;
    private String uuid;

    private ArrayList<GetSensorDataByIdTask.TaskDelegate> delegateRef = new ArrayList<>();

    ArrayList<ChartItem> chartItemsList = new ArrayList<>();

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_listview_chart);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);

        token = getIntent().getStringExtra("token");
        uuid = getIntent().getStringExtra("uuid");

        actionBar.setTitle("Thermometer: " + uuid);

        activityRef.add(this);

        lv = (ListView) findViewById(R.id.chart_listView);

        //float upperBound = getIntent().getFloatExtra("upperBound", 100);

        float[] upperBounds = { 45, 110000, 100};

        String[] descriptions = {"Temperature", "Pressure", "Humidity"};

        // 30 items
        for (int i = 0; i < 3; i++) {
                chartItemsList.add(new LineChartItem(getApplicationContext(), activityRef,
                        descriptions[i], upperBounds[i]));
        }

        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), chartItemsList);
        lv.setAdapter(cda);

        delegateRef.add(this);

        startTimer();
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

    private void clearAllCharts() {
        for (int i = 0; i < lv.getAdapter().getCount(); i++) {
            ((LineChartItem) lv.getAdapter().getItem(i)).clearChart();
        }
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
                clearAllCharts();
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

            clearAllCharts();

            for(int i = 0; i < measured.size(); i++) {

                if(measured.get(i).getItems().size() <= 0) {
                    continue;
                }

                int increment = measured.get(i).getItems().size() / 100;
                if(increment == 0) increment = 1;

                if(measured.get(i).getName().contains("temperature")) {

                    for(int j = 0; j < measured.get(i).getItems().size(); j+=increment) {
                        ((LineChartItem) lv.getAdapter().getItem(0)).addEntry(
                                Float.parseFloat(measured.get(i).getItems().get(j).getValue()),
                                measured.get(i).getItems().get(j).getTime(),
                                0, measured.get(i).getName(), 0, true
                        );
                    }
                    //((LineChartItem) lv.getAdapter().getItem(0)).invalidate();

                } else if(measured.get(i).getName().contains("pressure")) {

                    for(int j = 0; j < measured.get(i).getItems().size(); j+=increment) {
                        ((LineChartItem) lv.getAdapter().getItem(1)).addEntry(
                                Float.parseFloat(measured.get(i).getItems().get(j).getValue()),
                                measured.get(i).getItems().get(j).getTime(),
                                0, measured.get(i).getName(), 1, true
                        );
                    }

                } else if(measured.get(i).getName().contains("humudity")) {

                    for(int j = 0; j < measured.get(i).getItems().size(); j+=increment) {
                        ((LineChartItem) lv.getAdapter().getItem(2)).addEntry(
                                Float.parseFloat(measured.get(i).getItems().get(j).getValue()),
                                measured.get(i).getItems().get(j).getTime(),
                                0, measured.get(i).getName(), 2, true
                        );
                    }

                }
            }

            Log.e("FillHist", "HIST");
            return;

        }

        //Realtime run

        //int listViewIndex = 0;

        for(int i = 0; i < measured.size(); i++) {

            int lastIndex = measured.get(i).getItems().size() - 1;
            if (lastIndex < 0) {
                continue;
            }

            Data lastData = measured.get(i).getItems().get(lastIndex);

            DateFormat format = new SimpleDateFormat("MMM d, yyyy HH:mm:ss aaa");
            try {

                Date now = new Date();
                Date date = format.parse(lastData.getTime());

                if (now.getTime() > date.getTime() + 30000) continue;

            } catch (ParseException e) {
                Log.e("DATE", e.toString());
            }

            if (measured.get(i).getName().contains("temperature")) {

                ((LineChartItem) lv.getAdapter().getItem(0)).addEntry(
                        Float.parseFloat(lastData.getValue()),
                        lastData.getTime(),
                        0, measured.get(i).getName(), 0, false);

            } else if (measured.get(i).getName().contains("pressure")) {

                ((LineChartItem) lv.getAdapter().getItem(1)).addEntry(
                        Float.parseFloat(lastData.getValue()),
                        lastData.getTime(),
                        0, measured.get(i).getName(), 1, false);

            } else if (measured.get(i).getName().contains("humudity")) {

                ((LineChartItem) lv.getAdapter().getItem(2)).addEntry(
                        Float.parseFloat(lastData.getValue()),
                        lastData.getTime(),
                        0, measured.get(i).getName(), 2, false);

            }
        }



    }

    @Override
    public void onGetSensorDataByIdTaskWithFlageCompleted(SensorAndData sData) {

    }

    /** adapter that supports 3 different item types */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 1; // we have 3 different item-types
        }
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
