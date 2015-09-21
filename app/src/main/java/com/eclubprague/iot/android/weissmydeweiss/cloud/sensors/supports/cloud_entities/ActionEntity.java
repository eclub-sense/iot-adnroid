package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Dat on 21.9.2015.
 */
public class ActionEntity {

    private String name;
    private ArrayList<FieldEntity> fields;

    public ActionEntity(String name, ArrayList<FieldEntity> fields) {
        this.name = name;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public ArrayList<FieldEntity> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
