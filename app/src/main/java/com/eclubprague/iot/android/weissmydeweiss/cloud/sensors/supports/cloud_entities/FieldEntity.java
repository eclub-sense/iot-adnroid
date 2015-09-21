package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities;

/**
 * Created by Dat on 21.9.2015.
 */
public class FieldEntity {

    private String name;
    private String type;
    private String value;

    public FieldEntity(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
