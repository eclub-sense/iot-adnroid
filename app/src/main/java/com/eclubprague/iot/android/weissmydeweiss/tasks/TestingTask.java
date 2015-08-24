package com.eclubprague.iot.android.weissmydeweiss.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.eclubprague.iot.android.weissmydeweiss.cloud.RegisteredSensors;
import com.eclubprague.iot.android.weissmydeweiss.cloud.User;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.RegisteredSensorsMessage;

import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;

import java.util.ArrayList;

/**
 * Created by Dat on 22.8.2015.
 */
public class TestingTask extends AsyncTask<String, Void, String> {

    public interface TaskDelegate {
        public void onTestingTaskCompleted(String message);
    }

    //private ArrayList<TaskDelegate> delegateRef;
    private TaskDelegate delegate;
    public TestingTask(/*ArrayList<TaskDelegate> delegateRef*/ TaskDelegate delegate) {
        //this.delegateRef = delegateRef;
        this.delegate = delegate;

    }

    @Override
    protected String doInBackground(String... params) {

        try {
            ClientResource cr = new ClientResource("http://147.32.107.139:8080/registered_sensors");
            //?access_token="+ params[0]

            cr.setChallengeResponse(ChallengeScheme.HTTP_OAUTH_BEARER, params[0], null);

            RegisteredSensors sr = cr.wrap(RegisteredSensors.class);

            return sr.retrieve_test();
        } catch (Exception e) {
            Log.e("RegedSensorsTask", e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String message) {
        if(message == null) {
            Log.e("MESSAGE","NULL");
            return;
        }
        delegate.onTestingTaskCompleted(message);
    }
}
