package com.eclubprague.iot.android.weissmydeweiss.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.eclubprague.iot.android.weissmydeweiss.MainActivity;
import com.eclubprague.iot.android.weissmydeweiss.cloud.RegisteredSensors;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.RegisteredSensorsMessage;

import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;

import java.lang.ref.WeakReference;

/**
 * Created by Dat on 10.8.2015.
 */
public class GetSensorsDataTask extends AsyncTask<MainActivity.Account, Void, RegisteredSensorsMessage> {

    public interface TaskDelegate {
        public void onGetSensorsDataTaskCompleted(RegisteredSensorsMessage message);
    }

    private WeakReference<TaskDelegate> delegateRef;

    public GetSensorsDataTask(TaskDelegate delegate) {
        delegateRef = new WeakReference<TaskDelegate>(delegate);
    }

    @Override
    protected RegisteredSensorsMessage doInBackground(MainActivity.Account... accounts) {

        if(accounts.length == 0) return null;
        try {
            ClientResource cr = new ClientResource("http://192.168.201.222:8080/registered_sensors");
            cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC,
                    accounts[0].USERNAME, accounts[0].PASSWORD);
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
        delegateRef.get().onGetSensorsDataTaskCompleted(message);
    }
}
