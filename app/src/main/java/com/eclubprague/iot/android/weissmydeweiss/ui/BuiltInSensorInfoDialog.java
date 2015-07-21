package com.eclubprague.iot.android.weissmydeweiss.ui;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Dat on 20.7.2015.
 */
@TargetApi(20)
public class BuiltInSensorInfoDialog extends AlertDialog.Builder implements SensorEventListener {

    private Context context;
    private Sensor sensor;
    private LinearLayout layout;
    private TextView txt;

    private SensorManager senSensorManager;

    public BuiltInSensorInfoDialog(Context context, Sensor sensor) {
        super(context);
        this.context = context;
        this.sensor = sensor;

        layout = new LinearLayout(this.context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10,10,10,10);

        TextView text1=new TextView(this.context);
        text1.setText("Max. range: " + Float.toString(sensor.getMaximumRange()));
        text1.setPadding(5, 5, 5, 5);

        TextView text2=new TextView(this.context);
        text2.setText("type: " + sensor.getStringType());
        text2.setPadding(5, 5, 5, 5);

        TextView text3=new TextView(this.context);
        text3.setText("delay: " + Integer.toString(sensor.getMinDelay()));
        text3.setPadding(5, 5, 5, 5);

        txt=new TextView(this.context);
        txt.setText("data: ");
        txt.setPadding(5, 5, 5, 5);

        layout.addView(text2);
        layout.addView(text1);
        layout.addView(text3);
        layout.addView(txt);
//        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            sensor.
//        }

        this.setView(layout);

        this.setTitle(sensor.getName());
        this.setMessage("INFO");

        this.setPositiveButton("Close",new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int id)
            {
                senSensorManager.unregisterListener(BuiltInSensorInfoDialog.this);
                dialog.dismiss();
            }
        });

        this.create();
        this.show();
        senSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        senSensorManager.registerListener(this, sensor , SensorManager.SENSOR_DELAY_NORMAL);

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                txt.setText("x: " + String.format("%.2f", event.values[0]) +
                        "  y: " + String.format("%.2f", event.values[1]) +
                        "  z: " + String.format("%.2f", event.values[2]));
                break;
            case Sensor.TYPE_PROXIMITY:
                txt.setText("proximity: " + String.format("%.2f", event.values[0]) + " cm");
                if(event.values[0] < sensor.getMaximumRange()) {
                    Toast t2 = Toast.makeText(context, "I SEE YOU", Toast.LENGTH_SHORT);
                    t2.show();
                }
                break;
            case Sensor.TYPE_LIGHT:
                txt.setText("illumination: " + String.format("%.2f", event.values[0]) + " lx");
                break;
            default:
                String text = "data: ";
                    for(int i = 0; i < event.values.length; i++) {
                        text+= String.format("%.2f", event.values[i]);
                        text += "   ";
                    }
                txt.setText(text);

        }
        layout.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
