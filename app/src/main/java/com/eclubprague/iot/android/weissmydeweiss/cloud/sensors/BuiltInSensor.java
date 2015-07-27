package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dat on 27.7.2015.
 */
public class BuiltInSensor extends Sensor {

    private List<Float> data = new ArrayList<>();

    public BuiltInSensor() {
        super();
    }
    public BuiltInSensor(int uuid, String secret) {
        super(uuid, SensorType.GPS, secret);
    }

    public void setData(List<Float> data) {
        this.data.clear();
        this.data.addAll(data);
    }

    public List<Float> getData() {
        return data;
    }

    @Override
    public void readPayload(byte[] data) {

    }
}
