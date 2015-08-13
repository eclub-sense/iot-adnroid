package com.eclubprague.iot.android.weissmydeweiss.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eclubprague.iot.android.weissmydeweiss.MainActivity;
import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.cloud.User;
import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.NameValuePair;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.RegisteredSensorsMessage;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.tasks.GetSensorDataByIdTask;
import com.eclubprague.iot.android.weissmydeweiss.tasks.GetSensorsDataTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dat on 11.8.2015.
 */
public class SensorDataDialog extends AlertDialog.Builder implements GetSensorDataByIdTask.TaskDelegate {

    private Context context;
    private Sensor sensor;
    private LinearLayout layout;

    private ArrayList<User> userRef;
    private ArrayList<GetSensorDataByIdTask.TaskDelegate> taskDelegate = new ArrayList<>();

    public SensorDataDialog(Context context, Sensor sensor, ArrayList<User> userRef) {
        super(context);
        this.context = context;
        this.sensor =  sensor;
        this.userRef = userRef;
        this.taskDelegate.add(this);


        layout = new LinearLayout(this.context);
        layout.setOrientation(LinearLayout.VERTICAL);

        for(int i = 0; i < sensor.getMeasured().size(); i++) {
            TextView tv = new TextView(this.context);
            tv.setText(sensor.getMeasured().get(i).getName() + " : " + sensor.getMeasured().get(i).getValue());
            tv.setPadding(5, 5, 5, 5);
            layout.addView(tv);
        }

        this.setView(layout);

        this.setTitle(SensorType.getStringSensorType(this.sensor.getType()));
        //this.setMessage(sensor.toString());

        this.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                stopTimerTask();
                dialog.dismiss();
            }
        });

        startTimer();
        this.create();
        this.show();
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

                        new GetSensorDataByIdTask(SensorDataDialog.this.taskDelegate, SensorDataDialog.this.userRef)
                                .execute(SensorDataDialog.this.sensor);

                        //TODO this goes to TaskDelegate
//                        layout = new LinearLayout(SensorDataDialog.this.context);
//                        layout.setOrientation(LinearLayout.VERTICAL);
//
//                        for(int i = 0; i < sensor.getMeasured().size(); i++) {
//                            TextView tv = new TextView(SensorDataDialog.this.context);
//                            tv.setText(sensor.getMeasured().get(i).getName() + " : " + sensor.getMeasured().get(i).getValue());
//                            tv.setPadding(5, 5, 5, 5);
//                            layout.addView(tv);
//                        }
//
//                        SensorDataDialog.this.setView(layout);
//                        layout.invalidate();
                    }
                });
            }
        };
    }

    @Override
    public void onGetSensorDataById(List<NameValuePair> measured) {
        if(measured == null) {
            Log.e("SENSORDATA", "NULL");
            return;
        }

        Log.d("SENSORDATA", "OK");

        for (int i = 0; i < measured.size(); i++) {
            ((TextView)(layout.getChildAt(i)))
                    .setText(measured.get(i).getName() + " : " + measured.get(i).getValue());
        }

        layout.invalidate();
    }
}
