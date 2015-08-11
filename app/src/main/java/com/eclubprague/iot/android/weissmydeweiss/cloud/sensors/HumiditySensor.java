package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;


import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.NameValuePair;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Dat on 6.8.2015.
 */
public class HumiditySensor extends Sensor {

    protected String unit = "%";
    protected float humidity = 0;

    public HumiditySensor() {
        super();
    }
    public HumiditySensor(String uuid, String secret, Hub hub) {
        super(uuid, SensorType.HUMIDITY, secret, hub);
    }

    @Override
    public void readPayload(byte[] payload) {
        //TODO
    }

    @Override
    public String printData() {
        return ("humidity = " + humidity + " %");
    }

    public float getHumidity() {
        return humidity;
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
        measured.add(new NameValuePair("humidity", Float.toString(humidity)));
        measured.add(new NameValuePair("unit", unit));
        return measured;
    }

    @Override
    public void setData(float[] values) {
        humidity = values[0];
    }
}