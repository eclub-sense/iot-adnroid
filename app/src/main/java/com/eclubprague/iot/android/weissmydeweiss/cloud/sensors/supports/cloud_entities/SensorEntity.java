package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

/**
 * Created by Dat on 24.8.2015.
 */
public class SensorEntity {
    @Expose
    private String uuid;
    @Expose private String access;
    @Expose private Integer type;
    private HubEntity hub;
    @Expose private UserEntity owner;

    public SensorEntity() {
        super();
    }

    public HubEntity getHub() {
        return hub;
    }

    public void setHub(HubEntity hub) {
        this.hub = hub;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getType() {
        return type;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setUser(UserEntity user) {
        this.owner = user;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
