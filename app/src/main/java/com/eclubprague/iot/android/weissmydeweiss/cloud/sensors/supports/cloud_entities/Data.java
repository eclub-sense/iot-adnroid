package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

/**
 * Created by Dat on 24.8.2015.
 */
public class Data {
    @Expose private String value;
    @Expose private String time;

    public Data() {
    }

    public Data(String value, String time) {
        this.value = value;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

