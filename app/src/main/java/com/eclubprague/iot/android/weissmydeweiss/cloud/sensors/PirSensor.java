package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.NameValuePair;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorEntity;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Dat on 7.9.2015.
 */
public class PirSensor extends Sensor {

    protected int pir = 0;
    protected int vbat = 0;
    protected int rssi = 0;

    public PirSensor(SensorEntity entity) {
        super(entity);
    }

    public PirSensor(String uuid, String secret, Hub hub, String name) {
        super(uuid, SensorType.PIR, secret, hub, name, null);
    }

    @Override
    public void readPayload(byte[] payload) {
        //TODO
    }

    @Override
    public String printData() {
        return ("pir = " + pir + ", vbat = " + vbat + ", rssi = " + rssi);
    }


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public List<NameValuePair> getDataList() {
        measured.clear();
        measured.add(new NameValuePair("pir", Integer.toString(pir)));
        measured.add(new NameValuePair("vbat", Integer.toString(vbat)));
        measured.add(new NameValuePair("rssi", Integer.toString(rssi)));
        return measured;
    }

    @Override
    public void setData(float[] values) {

    }

    public int getPir() {
        return pir;
    }

    public int getVbat() {
        return vbat;
    }

    public int getRssi() {
        return rssi;
    }
}

