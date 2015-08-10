package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports;

/**
 * Created by Dat on 10.8.2015.
 */
public class NameValuePair {

    private String name;
    private String value;

    public NameValuePair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
