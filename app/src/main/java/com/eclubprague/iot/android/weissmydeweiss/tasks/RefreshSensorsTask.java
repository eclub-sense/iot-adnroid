package com.eclubprague.iot.android.weissmydeweiss.tasks;

import android.os.AsyncTask;

import com.eclubprague.iot.android.weissmydeweiss.cloud.PaginatedCollection;
import com.eclubprague.iot.android.weissmydeweiss.cloud.RegisteredSensors;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;

import org.restlet.engine.Engine;
import org.restlet.ext.gson.GsonConverter;
import org.restlet.resource.ClientResource;

import java.lang.ref.WeakReference;

/**
 * Refreshes sensor list from the server based on hub ID.
 */
public class RefreshSensorsTask extends AsyncTask<String, Void, PaginatedCollection<Sensor>> {
    private final WeakReference<RefreshSensorsCallbacks> callbacks;

    public RefreshSensorsTask(RefreshSensorsCallbacks callbacks) {
        this.callbacks = new WeakReference<RefreshSensorsCallbacks>(callbacks);
    }

    public interface RefreshSensorsCallbacks {
        void handleSensorsRefreshed(String hubId, PaginatedCollection<Sensor> sensorsCollection);
        void handleSensorsRefreshFailed(String hubId);
    }

    @Override
    protected PaginatedCollection<Sensor> doInBackground(String... strings) {
        // TODO: actually use the hub ID

        Engine.getInstance().getRegisteredConverters().add(new GsonConverter());

        try {
            // try connection
            ClientResource cr = new ClientResource("http://192.168.201.222:8080/registered_sensors");
            RegisteredSensors rs = cr.wrap(RegisteredSensors.class);

            return rs.retrieve();
        } catch(Throwable thr) {
            thr.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(PaginatedCollection<Sensor> sensorPaginatedCollection) {
        super.onPostExecute(sensorPaginatedCollection);

        RefreshSensorsCallbacks rsc = callbacks.get();
        if(rsc != null) {
            if(sensorPaginatedCollection != null)
                rsc.handleSensorsRefreshed(null, sensorPaginatedCollection);
            else
                rsc.handleSensorsRefreshFailed(null);
        }
    }
}
