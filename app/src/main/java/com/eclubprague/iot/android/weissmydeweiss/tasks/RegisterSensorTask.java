package com.eclubprague.iot.android.weissmydeweiss.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.eclubprague.iot.android.weissmydeweiss.MainActivity;
import com.eclubprague.iot.android.weissmydeweiss.cloud.RegisteredSensors;
import com.eclubprague.iot.android.weissmydeweiss.cloud.SensorRegistrator;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;

import org.restlet.resource.ClientResource;

import java.util.ArrayList;

/**
 * Created by Dat on 14.9.2015.
 */
public class RegisterSensorTask extends AsyncTask<String, Void, Void> {

    private Sensor sensor;
    private MainActivity activity;

    public RegisterSensorTask(Sensor sensor, MainActivity activity) {
        this.sensor = sensor;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            ClientResource cr = new ClientResource("http://mlha-139.sin.cvut.cz:8080/sensor_registration");
            cr.setQueryValue("access_token", params[0]);
            SensorRegistrator sr = cr.wrap(SensorRegistrator.class);
            Log.e("REGISTERING", sensor.toString());
            sr.store(sensor);
        } catch (Exception e) {
            Log.e("RegisterSensorTask", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        activity.getSensorsData();
    }

}