package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by Dat on 24.8.2015.
 */
public class SensorEntity {
    @Expose
    private String uuid;
    @Expose private String access;
    @Expose private int type;
    private String description;
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

    public int getType() {
        return type;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public String getOwnerEmail() {
        return owner.getEmail();
    }

    public void setUser(UserEntity user) {
        this.owner = user;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        if(description == null) return "";
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
