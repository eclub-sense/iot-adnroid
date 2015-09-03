package com.eclubprague.iot.android.weissmydeweiss.tasks;

import android.os.AsyncTask;

import com.eclubprague.iot.android.weissmydeweiss.cloud.SensorRegistrator;
import com.eclubprague.iot.android.weissmydeweiss.cloud.gsonmods.GsonCustomConverter;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;

import org.restlet.engine.Engine;
import org.restlet.ext.gson.GsonConverter;
import org.restlet.resource.ClientResource;

import java.lang.ref.WeakReference;

/**
 * Refreshes sensor list from the server based on hub ID.
 */
public class AddSensorTask extends AsyncTask<Sensor, Void, AddSensorTask.AddSensorResult> {
    private final WeakReference<AddSensorCallbacks> callbacks;

    public AddSensorTask(AddSensorCallbacks callbacks) {
        this.callbacks = new WeakReference<AddSensorCallbacks>(callbacks);
    }

    public interface AddSensorCallbacks {
        void handleSensorAdded(AddSensorResult sensorResult);
    }

    public class AddSensorResult {
        public AddSensorResult(boolean success, Sensor sensor) {
            this.success = success;
            this.sensor = sensor;
        }

        boolean success;
        Sensor sensor;

        public boolean isSuccess() {
            return success;
        }

        public Sensor getSensor() {
            return sensor;
        }
    }

    @Override
    protected AddSensorResult doInBackground(Sensor... sensors) {
        Engine.getInstance().getRegisteredConverters().add(new GsonCustomConverter());

        try {
            // try connection
            ClientResource cr = new ClientResource("http://mlha-139.sin.cvut.cz:8080/sensors_registrator");
            SensorRegistrator rs = cr.wrap(SensorRegistrator.class);

            rs.store(sensors[0]);
        } catch(Throwable thr) {
            thr.printStackTrace();
        }

        return new AddSensorResult(false, sensors[0]);
    }

    @Override
    protected void onPostExecute(AddSensorResult sensorResult) {
        super.onPostExecute(sensorResult);

        AddSensorCallbacks rsc = callbacks.get();
        if(rsc != null) {
            rsc.handleSensorAdded(sensorResult);
        }
    }
}
