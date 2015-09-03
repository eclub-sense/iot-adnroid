package com.eclubprague.iot.android.weissmydeweiss.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.eclubprague.iot.android.weissmydeweiss.cloud.RegisteredSensors;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorAndData;

import org.restlet.resource.ClientResource;

import java.util.ArrayList;

/**
 * Created by Dat on 13.8.2015.
 */
public class GetSensorDataByIdTask extends AsyncTask<Void, Void, SensorAndData> {

    public interface TaskDelegate {
        public void onGetSensorDataByIdTaskCompleted(SensorAndData sData);
    }

    private ArrayList<TaskDelegate> delegateRef;
    private String token;
    private String uuid;

    public GetSensorDataByIdTask(ArrayList<TaskDelegate> delegateRef, String token, String uuid) {
        this.delegateRef = delegateRef;
        this.token = token;
        this.uuid = uuid;
    }

    @Override
    protected SensorAndData doInBackground(Void ... params) {
        try {
            ClientResource resource = new ClientResource("http://mlha-139.sin.cvut.cz:8080/registered_sensors/" + uuid);
            resource.setQueryValue("access_token", token);

            RegisteredSensors sr = resource.wrap(RegisteredSensors.class);
            return sr.retrieve_SensorAndData();
        } catch (Exception e) {
            Log.e("RegedSensorsTask", e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(SensorAndData sData) {

        if(sData == null) {
            Log.e("DATA","NULL");
            return;
        }
        delegateRef.get(0).onGetSensorDataByIdTaskCompleted(sData);
    }
}
