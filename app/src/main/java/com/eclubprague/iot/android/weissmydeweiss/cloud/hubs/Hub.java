package com.eclubprague.iot.android.weissmydeweiss.cloud.hubs;

/**
 * Created by Dat on 16.7.2015.
 */
public class Hub {

    private int uuid;
    private String secret;
    private String name;

    public Hub(int uuid, String name, String secret) {
        this.uuid = uuid;
        this.name = name;
        this.secret = secret;
    }

    public int getUuid() {
        return uuid;
    }

    public String getName() {return name; }

    @Override
    public String toString() {
        return "Hub [uuid=" + uuid + ", name=" + name + "]";
    }
}
