package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.NameValuePair;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorEntity;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Dat on 22.9.2015.
 */
public class Accelerometer2 extends Sensor {

    protected float accX = 0;
    protected float accY = 0;
    protected float accZ = 0;

    public Accelerometer2(SensorEntity entity) {
        super(entity);
    }

    public Accelerometer2(String uuid, String secret, Hub hub, String name) {
        super(uuid, SensorType.ACCELEROMETER2, secret, hub, name, null);
    }

    @Override
    public void readPayload(byte[] payload) {
        //TODO
    }

    @Override
    public String printData() {
        return ("x = " + accX + ", y = " + accY + ", z = " + accZ + "(m/s^2)");
    }

    public float getAccX() {
        return accX;
    }

    public float getAccZ() {
        return accZ;
    }

    public float getAccY() {
        return accY;
    }

    @Override
    public void setData(float values[]) {
        accX = values[0];
        accZ = values[1];
        accY = values[2];
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public List<NameValuePair> getDataList() {
        measured.clear();
        measured.add(new NameValuePair("accX", Float.toString(accX)));
        measured.add(new NameValuePair("accY", Float.toString(accY)));
        measured.add(new NameValuePair("accZ", Float.toString(accZ)));
        return measured;
    }
}
