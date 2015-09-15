package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.NameValuePair;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorEntity;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Dat on 28.7.2015.
 */
public class ProximitySensor extends Sensor {

        protected String unit = "cm";
        protected float proximity = 0;

    public ProximitySensor(SensorEntity entity) {
        super(entity);
    }

    public ProximitySensor(String uuid, String secret, Hub hub, String name) {
        super(uuid, SensorType.PROXIMITY, secret, hub, name);
    }

    @Override
    public void readPayload(byte[] payload) {
        //TODO
    }

    @Override
    public String printData() {
        return ("proximity = " + proximity + " cm");
    }

    public float getProximity() {
        return proximity;
    }

    public String getUnit() {
        return unit;
    }


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public List<NameValuePair> getDataList() {
        measured.clear();
        measured.add(new NameValuePair("proximity", Float.toString(proximity)));
        measured.add(new NameValuePair("unit", unit));
        return measured;
    }

    @Override
    public void setData(float[] values) {
        proximity = values[0];
    }
}