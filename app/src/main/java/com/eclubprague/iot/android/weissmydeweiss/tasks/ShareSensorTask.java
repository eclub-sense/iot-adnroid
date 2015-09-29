package com.eclubprague.iot.android.weissmydeweiss.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.eclubprague.iot.android.weissmydeweiss.cloud.RegisteredSensors;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.ShareSensorEntity;

import org.restlet.resource.ClientResource;

/**
 * Created by Dat on 8.9.2015.
 */
public class ShareSensorTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        try {
            ClientResource resource = new ClientResource("http://zettor.sin.cvut.cz:8080/share_sensor");
            resource.setQueryValue("access_token", params[2]);

            RegisteredSensors sr = resource.wrap(RegisteredSensors.class);
            ShareSensorEntity message = new ShareSensorEntity(params[1], params[0]);
            Log.e("SHARE", message.toString());
            sr.shareSensor(message);
        } catch (Exception e) {
            Log.e("ShareSensorTask", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

}
