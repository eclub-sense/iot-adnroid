package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Dat on 24.8.2015.
 */
public class SensorAndData {
    @Expose
    private SensorEntity sensor;
    @Expose private List<Data> measured;

    public SensorEntity getSensor() {
        return sensor;
    }
    public void setSensor(SensorEntity sensor) {
        this.sensor = sensor;
    }
    public List<Data> getMeasured() {
        return measured;
    }
    public void setMeasured(List<Data> measured) {
        this.measured = measured;
    }
}
