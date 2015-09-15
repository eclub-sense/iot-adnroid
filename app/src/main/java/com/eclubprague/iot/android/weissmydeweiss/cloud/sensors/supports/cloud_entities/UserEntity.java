package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Dat on 24.8.2015.
 */
public class UserEntity {

    private String identifier;
    private String email;
    @Expose
    private List<String> emails = new LinkedList<>();

    public UserEntity() {
    }

    public String getEmail() {
        return email;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String username) {
        this.identifier = username;
    }

    public void addEmail(String mail) {
        emails.add(mail);
    }


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
