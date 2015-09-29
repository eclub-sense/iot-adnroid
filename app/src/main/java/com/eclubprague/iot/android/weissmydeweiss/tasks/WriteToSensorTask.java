package com.eclubprague.iot.android.weissmydeweiss.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.eclubprague.iot.android.weissmydeweiss.cloud.RegisteredSensors;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.ActionEntity;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorAndData;

import org.restlet.resource.ClientResource;

import java.util.ArrayList;

/**
 * Created by Dat on 21.9.2015.
 */
public class WriteToSensorTask extends AsyncTask<String, Void, Void> {

    private ActionEntity action;

    public interface TaskDelegate {
        public void onWriteToSensorTaskCompleted();
    }

    private TaskDelegate delegate;

    public WriteToSensorTask(TaskDelegate delegate, ActionEntity action) {
        this.action = action;
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        delegate.onWriteToSensorTaskCompleted();
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            ClientResource resource = new ClientResource("http://zettor.sin.cvut.cz:8080/registered_sensors/" + params[0]);
            resource.setQueryValue("access_token", params[1]);

            RegisteredSensors sr = resource.wrap(RegisteredSensors.class);
            Log.e("ACTION", action.toString());
            sr.write(action);
        } catch (Exception e) {
            Log.e("WriteToSensorTask", e.toString());
        }
        return null;
    }
}
