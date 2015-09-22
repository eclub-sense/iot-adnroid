package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.NameValuePair;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorEntity;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Dat on 22.9.2015.
 */
public class Beacon extends Sensor {

    protected int rssi = 0;

    public Beacon(String uuid, String secret, Hub hub, String name) {
        super(uuid, SensorType.BEACON, secret, hub, name, null);
    }

    public Beacon(SensorEntity entity) {
        super(entity);
    }

    @Override
    public void readPayload(byte[] data) {

    }

    @Override
    public String printData() {
        return null;
    }

    @Override
    public List<NameValuePair> getDataList() {
        measured.clear();
        measured.add(new NameValuePair("rssi", Integer.toString(rssi)));
        return measured;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public void setData(float[] values) {

    }
}
