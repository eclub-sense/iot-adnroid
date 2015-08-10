package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports;

import java.util.List;

/**
 * Created by Dat on 10.8.2015.
 */
public class SensorDataWrapper {
    private String uuid;
    private int type;
    private List<NameValuePair> measured;

    public SensorDataWrapper(String uuid, int type, List<NameValuePair> measured) {
        this. uuid = uuid;
        this.type = type;
        this.measured = measured;
    }

    public List<NameValuePair> getMeasured() {
        return measured;
    }

    public int getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }
}
