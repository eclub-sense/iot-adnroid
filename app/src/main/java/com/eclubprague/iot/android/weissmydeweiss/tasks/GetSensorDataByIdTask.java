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
        void onGetSensorDataByIdTaskCompleted(SensorAndData sData);
        void onGetSensorDataByIdTaskWithFlageCompleted(SensorAndData sData);
    }

    private ArrayList<TaskDelegate> delegateRef;
    private String token;
    private String uuid;

    private boolean flag;

    public GetSensorDataByIdTask(ArrayList<TaskDelegate> delegateRef, String token, String uuid, boolean flag) {
        this.delegateRef = delegateRef;
        this.token = token;
        this.uuid = uuid;
        this.flag = flag;
    }

    @Override
    protected SensorAndData doInBackground(Void ... params) {
        try {
            ClientResource resource = new ClientResource("http://zettor.sin.cvut.cz:8080/registered_sensors/" + uuid);
            resource.setQueryValue("limit", flag == true ? "1" : "100");
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
        if(!flag) {
            delegateRef.get(0).onGetSensorDataByIdTaskCompleted(sData);
        } else {
            delegateRef.get(0).onGetSensorDataByIdTaskWithFlageCompleted(sData);
        }
    }
}
