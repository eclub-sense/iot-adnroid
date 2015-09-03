package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.VirtualSensorCreator;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dat on 24.8.2015.
 */
public class AllSensors {

    @Expose
    private List<SensorEntity> _my;
    @Expose private List<SensorAccessEntity> _borrowed;
    @Expose private List<SensorEntity> _public;

    public List<SensorEntity> getMy() {
        return _my;
    }
    public List<SensorAccessEntity> getBorrowed() {
        return _borrowed;
    }
    public List<SensorEntity> get_public() {
        return _public;
    }

    public List<Sensor> getMySensors() {
        List<Sensor> sensors = new ArrayList<>();

        if(_my == null) return sensors;

        for(int i = 0; i < _my.size(); i++) {
            SensorEntity entity = _my.get(i);
            sensors.add(VirtualSensorCreator.createSensorInstance(entity.getUuid(), entity.getType(),
                    "some_secret", new Hub("my")));
        }

        return sensors;
    }

    public List<Sensor> getBorrowedSensors() {
        List<Sensor> sensors = new ArrayList<>();

        if(_borrowed == null) return sensors;

        for(int i = 0; i < _borrowed.size(); i++) {
            SensorEntity entity = _borrowed.get(i).getSensor();
            sensors.add(VirtualSensorCreator.createSensorInstance(entity.getUuid(), entity.getType(),
                    "some_secret", new Hub("borrowed")));
        }

        return sensors;
    }

    public List<Sensor> getPublicSensors() {
        List<Sensor> sensors = new ArrayList<>();

        if(_public == null) return sensors;

        for(int i = 0; i < _public.size(); i++) {
            SensorEntity entity = _public.get(i);
            sensors.add(VirtualSensorCreator.createSensorInstance(entity.getUuid(), entity.getType(),
                    "some_secret", /*new Hub(entity.getHub().getUuid())*/ new Hub("public")));
        }

        return sensors;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
