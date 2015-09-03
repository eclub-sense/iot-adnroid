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
        if(items == null || items.size() == 0) {
            ArrayList<Data> res = new ArrayList<>();
            res.add(new Data("-1000", "Sep 1, 2000 1:00:00 PM"));
            return res;
        }
        return items;
    }
}
