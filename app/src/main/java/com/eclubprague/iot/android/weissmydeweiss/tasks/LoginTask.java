package com.eclubprague.iot.android.weissmydeweiss.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.eclubprague.iot.android.weissmydeweiss.cloud.RegisteredSensors;
import com.eclubprague.iot.android.weissmydeweiss.cloud.User;

import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;

import java.lang.ref.WeakReference;

/**
 * Created by Dat on 14.8.2015.
 */
public class LoginTask extends AsyncTask<User, Void, Boolean> {

    public interface TaskDelegate {
        public void onLoginCompleted(boolean success);
    }

    private WeakReference<TaskDelegate> delegateRef;

    public LoginTask(TaskDelegate delegate) {
        delegateRef = new WeakReference<TaskDelegate>(delegate);
    }

    @Override
    protected Boolean doInBackground(User... users) {

        if(users.length == 0) return false;
        try {
            ClientResource cr = new ClientResource("http://147.32.107.139:8080/registered_sensors");
            cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC,
                    users[0].getUsername(), users[0].getPassword());
            RegisteredSensors sr = cr.wrap(RegisteredSensors.class);
            sr.getStringData();
        } catch (Exception e) {
            Log.e("RegedSensorsTask", e.toString());
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        delegateRef.get().onLoginCompleted(success);
    }
}

