package com.eclubprague.iot.android.weissmydeweiss.ui;


import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;

import java.util.ArrayList;

/**
 * Created by Dat on 13.9.2015.
 */
public enum GridViewItemWrapper {

    SHARE("Share", R.drawable.ic_share),
    UNREGISTER("Unregister", R.drawable.ic_unreg),
    CHARTS("Charts", R.drawable.ic_charts),
    WRITE("Actions", R.drawable.ic_write);

    private String desc;
    private int res;

    GridViewItemWrapper(String desc, int res) {
        this.desc = desc;
        this.res = res;
    }

    public String getDesc() {
        return desc;
    }

    public int getRes() {
        return res;
    }

    public static ArrayList<GridViewItemWrapper> getGridItems() {
        ArrayList<GridViewItemWrapper> list = new ArrayList<>();
        list.add(CHARTS);
        list.add(SHARE);
        list.add(UNREGISTER);
        return list;
    }

    public static ArrayList<GridViewItemWrapper> getGridItemsForWriteable() {
        ArrayList<GridViewItemWrapper> list = new ArrayList<>();
        list.add(WRITE);
        list.add(SHARE);
        list.add(UNREGISTER);
        return list;
    }
}
