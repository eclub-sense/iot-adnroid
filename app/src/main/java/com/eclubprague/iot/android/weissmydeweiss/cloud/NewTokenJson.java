package com.eclubprague.iot.android.weissmydeweiss.cloud;

import com.google.gson.Gson;

/**
 * Created by Dat on 31.8.2015.
 */
public class NewTokenJson {

    private String code = "code";
    private String grant_type = "authorization_code";
    private String client_id = "dat";

    public NewTokenJson(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
