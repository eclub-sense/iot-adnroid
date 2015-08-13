package com.eclubprague.iot.android.weissmydeweiss.cloud;

import com.google.gson.Gson;

/**
 * Created by Dat on 13.8.2015.
 */
public class User {
    protected String username;
    protected String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {return username;}

    public String getPassword() {return password;}

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}