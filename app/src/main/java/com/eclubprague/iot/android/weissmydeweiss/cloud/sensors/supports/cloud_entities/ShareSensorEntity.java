package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities;

import com.google.gson.Gson;

/**
 * Created by Dat on 8.9.2015.
 */
public class ShareSensorEntity {

    private String uuid;
    private String email;
    private String access = "protected";
    private String permission = "read";

    public ShareSensorEntity(String uuid, String email) {
        this.uuid = uuid;
        if(email != null) {
            this.email = email;
        } else {
            this.email = "";
            this.access = "public";
            this.permission = "";
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
