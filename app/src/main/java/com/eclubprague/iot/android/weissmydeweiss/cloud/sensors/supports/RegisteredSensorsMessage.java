package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.VirtualSensorCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dat on 10.8.2015.
 */
public class RegisteredSensorsMessage {

    private List<SensorDataWrapper> _my;
    private List<SensorDataWrapper> _public;

    public List<SensorDataWrapper> getMy() {
        return _my;
    }

    public List<SensorDataWrapper> getBorrowed() {
        return _public;
    }

    public List<Sensor> getMySensors() {
        List<Sensor> mySensors = new ArrayList<>();
        if(_my != null) {
            for (int i = 0; i < _my.size(); i++) {
                Sensor sensor = VirtualSensorCreator.
                        createSensorInstance(_my.get(i).getUuid(), _my.get(i).getType(), "secret", new Hub("my"));
                sensor.setMeasured(_my.get(i).getMeasured());
                mySensors.add(sensor);
            }
        }
        return mySensors;
    }

    public List<Sensor> getBorrowedSensors() {
        List<Sensor> borrowedSensors = new ArrayList<>();
        if(_public != null) {
            for (int i = 0; i < _public.size(); i++) {
                Sensor sensor = VirtualSensorCreator.
                        createSensorInstance(_public.get(i).getUuid(), _public.get(i).getType(), "secret", new Hub("borrowed"));
                sensor.setMeasured(_public.get(i).getMeasured());
                borrowedSensors.add(sensor);
            }
        }
        return borrowedSensors;
    }

    public List<NameValuePair> getSensorData(String uuid) {
        if(_my != null) {
            for (int i = 0; i < _my.size(); i++) {
                if(_my.get(i).getUuid().equals(uuid)) return _my.get(i).getMeasured();
            }
        }
        if(_public != null) {
            for (int i = 0; i < _public.size(); i++) {
                if(_public.get(i).getUuid().equals(uuid)) return _public.get(i).getMeasured();
            }
        }
        return null;
    }
}
