package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

/**
 * Created by Dat on 24.8.2015.
 */
public class SensorAccessEntity {
    @Expose
    private UserEntity owner;
    private UserEntity user;
    @Expose private String permission;
    @Expose private SensorEntity sensor;

    public SensorAccessEntity() {

    }

    public SensorAccessEntity(UserEntity owner, UserEntity user, String permission, SensorEntity sensor) {
        this.sensor = sensor;
        this.permission = permission;
        this.owner = owner;
        this.user = user;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public UserEntity getOwener() {
        return owner;
    }

    public void setOwener(UserEntity owner) {
        this.owner = owner;
    }

    public SensorEntity getSensor() {
        return sensor;
    }

    public void setSensor(SensorEntity sensor) {
        this.sensor = sensor;
    }


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
