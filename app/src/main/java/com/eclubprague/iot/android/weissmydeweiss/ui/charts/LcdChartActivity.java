package com.eclubprague.iot.android.weissmydeweiss.ui.charts;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.VirtualSensorCreator;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.ActionEntity;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.FieldEntity;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorAndData;
import com.eclubprague.iot.android.weissmydeweiss.tasks.GetSensorDataByIdTask;
import com.eclubprague.iot.android.weissmydeweiss.tasks.WriteToSensorTask;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.components.CustomMarkerView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dat on 21.9.2015.
 */
public class LcdChartActivity extends ActionBarActivity implements GetSensorDataByIdTask.TaskDelegate,
WriteToSensorTask.TaskDelegate {

    private String token;
    private String uuid;

    private TextView tv_owner;
    private TextView tv_desc;
    private TextView tv_access;

    private boolean firstTime = true;

    private Sensor sensor;

    private EditText et_text;

    private Button btn_connect;
    private Button btn_write;

    private ArrayList<GetSensorDataByIdTask.TaskDelegate> delegateRef = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.lcd_chart_2);

        tv_owner = (TextView) findViewById(R.id.tv_owner);
        tv_owner.setText(getIntent().getStringExtra("owner"));

        tv_desc = (TextView) findViewById(R.id.tv_description);
        tv_desc.setText(getIntent().getStringExtra("description"));

        tv_access = (TextView) findViewById(R.id.tv_access);
        tv_access.setText(getIntent().getStringExtra("access"));

        et_text = (EditText) findViewById(R.id.et_text);

        btn_connect = (Button) findViewById(R.id.btn_connect);
        btn_connect.setEnabled(false);
        btn_write = (Button) findViewById(R.id.btn_write);

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = et_text.getText().toString();
                ActionEntity tmp = null;
                for (ActionEntity a : sensor.getActions()) {
                    if (a.getName().equals("send-message")) {
                        tmp = a;
                        break;
                    }
                }

                if (tmp != null) {

                    for (FieldEntity field : tmp.getFields()) {
                        if (field.getName().equals("message")) {
                            field.setValue(text);
                            break;
                        }
                    }

                    Log.e("ACTION", "WRITE");
                    new WriteToSensorTask(LcdChartActivity.this, tmp).execute(uuid, token);
                }
            }
        });
        btn_write.setEnabled(false);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);

        token = getIntent().getStringExtra("token");
        uuid = getIntent().getStringExtra("uuid");

        actionBar.setTitle("LCD");

        delegateRef.add(this);
        new GetSensorDataByIdTask(delegateRef, token, uuid).execute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

    @Override
    public void onGetSensorDataByIdTaskCompleted(SensorAndData sData) {
        Log.e("ACTION", "GOT SOMETHING");
        sensor = VirtualSensorCreator.createSensorInstance(sData.getSensor());
        sensor.setActions(sData.getActions());

        if(sensor.getActions() != null && sensor.getActions().size() > 0) {
            Log.e("ACTION", "HELLO");
            for(final ActionEntity action : sensor.getActions()) {
                Log.e("ACTION", action.getName());
                if(action.getName().equals("connect")) {
                    btn_connect.setEnabled(true);
                    btn_connect.setText("Connect");
                    btn_connect.invalidate();
                    btn_connect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("ACTION", "CONNECT");
                            new WriteToSensorTask(LcdChartActivity.this, action).execute(uuid, token);
                        }
                    });
                    btn_write.setEnabled(false);

                } else if(action.getName().equals("disconnect")) {
                    btn_connect.setEnabled(true);
                    btn_connect.setText("Disconnect");
                    btn_connect.invalidate();
                    btn_connect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("ACTION", "DISCONNECT");
                            new WriteToSensorTask(LcdChartActivity.this, action).execute(uuid, token);
                        }
                    });

                    btn_write.setEnabled(true);
                } else if(action.getName().equals("send-message")) {
                }
            }

        } else {
            Log.e("WRITEABLEINIT", "empty actions");
            btn_connect.setEnabled(false);
            btn_write.setEnabled(false);
            Toast.makeText(this, "No actions available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWriteToSensorTaskCompleted() {
        new GetSensorDataByIdTask(delegateRef, token, uuid).execute();
    }
}
