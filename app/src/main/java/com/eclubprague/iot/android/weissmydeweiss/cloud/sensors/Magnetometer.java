package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.NameValuePair;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorEntity;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Dat on 6.8.2015.
 */
public class Magnetometer extends Sensor {

    protected String unit = "uT";
    protected float x = 0;
    protected float y = 0;
    protected float z = 0;

    public Magnetometer(SensorEntity entity) {
        super(entity);
    }

    public Magnetometer(String uuid, String secret, Hub hub, String name) {
        super(uuid, SensorType.MAGNETOMETER, secret, hub, name);
    }

    @Override
    public void readPayload(byte[] payload) {
        //TODO
    }

    @Override
    public String printData() {
        return ("x = " + x + ", y = " + y + ", z = " + z + "(uT)");
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public void setData(float values[]) {
        x = values[0];
        y = values[1];
        z = values[2];
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public List<NameValuePair> getDataList() {
        measured.clear();
        measured.add(new NameValuePair("x", Float.toString(x)));
        measured.add(new NameValuePair("y", Float.toString(y)));
        measured.add(new NameValuePair("z", Float.toString(z)));
        return measured;
    }
}