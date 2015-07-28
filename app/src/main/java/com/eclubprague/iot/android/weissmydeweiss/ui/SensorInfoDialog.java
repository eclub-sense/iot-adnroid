package com.eclubprague.iot.android.weissmydeweiss.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.ESCLed;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.ESCThermometer;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.tasks.GetSensorTask;

import java.security.spec.ECField;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dat on 16.7.2015.
 */
public class SensorInfoDialog extends AlertDialog.Builder {

    private Context context;
    private Sensor sensor;
    private LinearLayout layout;

    private final Timer updateTimer;

    public SensorInfoDialog(Context context, final Sensor sensor) {
        super(context);
        this.context = context;
        this.sensor = sensor;

        layout = new LinearLayout(this.context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView text1=new TextView(this.context);
        text1.setPadding(5, 5, 5, 5);

        final TextView text2=new TextView(this.context);
        text2.setPadding(5, 5, 5, 5);

        if(sensor instanceof ESCThermometer) {
            text1.setText("Temperature: " + Integer.toString(((ESCThermometer) sensor).getTemperature()));
            text2.setText("Pressure: " + Integer.toString(((ESCThermometer) sensor).getPressure()));
        }
        else if(sensor instanceof ESCLed && ((ESCLed) sensor).getLed() != null) {
            text1.setText("LED " + (((ESCLed) sensor).getLed().toString()));
        }

        class RefreshSensorInfo extends TimerTask implements GetSensorTask.GetSensorCallbacks {
            @Override
            public void run() {
                GetSensorTask getSensorTask = new GetSensorTask(this);
                getSensorTask.execute(sensor.getIntUuid());
            }

            @Override
            public void handleGetSensorResult(GetSensorTask.GetSensorResult sensorResult) {
                if(sensorResult.getSensor() == null) {
                    text2.setText("Connection issues");
                    return;
                }

                Sensor sr = sensorResult.getSensor();

                if(sr instanceof ESCThermometer) {
                    text1.setText("Temperature: " + Integer.toString(((ESCThermometer) sr).getTemperature()));
                    text2.setText("Pressure: " + Integer.toString(((ESCThermometer) sr).getPressure()));
                }
                else if(sr instanceof ESCLed && ((ESCLed) sr).getLed() != null) {
                    text1.setText("LED " + (((ESCLed) sr).getLed().toString()));
                }
            }
        }

        updateTimer = new Timer();
        updateTimer.schedule(new RefreshSensorInfo(), 2000, 2000);

        layout.addView(text1);
        layout.addView(text2);

        this.setView(layout);

        this.setTitle(sensor.getType().getName());
        this.setMessage(sensor.toString());

        this.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        this.setNegativeButton("Unregister", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(SensorInfoDialog.this.context, "Sensor unregistered", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        this.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                updateTimer.cancel();
                updateTimer.purge();
            }
        });
        this.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                updateTimer.cancel();
                updateTimer.purge();
            }
        });

        this.create();
        this.show();
    }




}
