package com.eclubprague.iot.android.weissmydeweiss.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.eclubprague.iot.android.weissmydeweiss.MainActivity;
import com.eclubprague.iot.android.weissmydeweiss.cloud.RegisteredSensors;
import com.eclubprague.iot.android.weissmydeweiss.cloud.User;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.RegisteredSensorsMessage;

import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Dat on 10.8.2015.
 */
public class GetSensorsDataTask extends AsyncTask<Void, Void, RegisteredSensorsMessage> {

    public interface TaskDelegate {
        public void onGetSensorsDataTaskCompleted(RegisteredSensorsMessage message);
    }

    private ArrayList<TaskDelegate> delegateRef;
    private ArrayList<User> userRef;
    public GetSensorsDataTask(ArrayList<TaskDelegate> delegateRef, ArrayList<User> userRef) {
        this.delegateRef = delegateRef;
        this.userRef = userRef;

    }

    @Override
    protected RegisteredSensorsMessage doInBackground(Void... params) {

        try {
            ClientResource cr = new ClientResource("http://147.32.107.139:8080/registered_sensors");
            cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC,
                    userRef.get(0).getUsername(), userRef.get(0).getPassword());
            RegisteredSensors sr = cr.wrap(RegisteredSensors.class);
            return sr.retrieve_2();
        } catch (Exception e) {
            Log.e("RegedSensorsTask", e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(RegisteredSensorsMessage message) {
        if(message == null) {
            Log.e("MESSAGE","NULL");
            return;
        }
        delegateRef.get(0).onGetSensorsDataTaskCompleted(message);
    }
}
