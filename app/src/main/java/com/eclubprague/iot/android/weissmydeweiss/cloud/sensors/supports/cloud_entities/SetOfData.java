package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities;

import java.util.ArrayList;

/**
 * Created by Dat on 1.9.2015.
 */
public class SetOfData {

    private String name;
    private ArrayList<Data> items;

    public String getName() {
        return name;
    }

    public ArrayList<Data> getItems() {
        if(items == null) {
            return new ArrayList<>();
        }
        return items;
    }
}
