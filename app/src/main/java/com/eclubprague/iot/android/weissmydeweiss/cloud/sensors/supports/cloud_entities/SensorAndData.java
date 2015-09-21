package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dat on 24.8.2015.
 */
public class SensorAndData {
    @Expose
    private SensorEntity sensor;
    @Expose private List<SetOfData> measured;
    private ArrayList<ActionEntity> actions;

    public SensorEntity getSensor() {
        return sensor;
    }
    public void setSensor(SensorEntity sensor) {
        this.sensor = sensor;
    }

    public List<SetOfData> getMeasured() {
        if(measured == null) {
            return new ArrayList<>();
        }
        return measured;
    }

    public ArrayList<ActionEntity> getActions() {
        return actions;
    }

    public void setActions(ArrayList<ActionEntity> actions) {
        this.actions = actions;
    }

    public void setMeasured(List<SetOfData> measured) {
        this.measured = measured;
    }
}
