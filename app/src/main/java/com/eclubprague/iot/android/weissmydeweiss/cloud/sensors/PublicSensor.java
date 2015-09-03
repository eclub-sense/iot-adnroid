package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.NameValuePair;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Dat on 25.8.2015.
 */
public class PublicSensor extends Sensor {

    public PublicSensor(String uuid, String secret, Hub hub, int type) {
        super(uuid, type, secret, hub);
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
        return null;
    }

    @Override
    public void setData(float[] values) {

    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
