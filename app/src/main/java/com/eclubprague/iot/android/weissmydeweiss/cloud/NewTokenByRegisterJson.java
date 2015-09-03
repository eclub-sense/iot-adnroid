package com.eclubprague.iot.android.weissmydeweiss.cloud;

import com.google.gson.Gson;

/**
 * Created by Dat on 31.8.2015.
 */
public class NewTokenByRegisterJson {

    private String password;
    private String code;
    private String client_id = "dat";
    private String grant_type = "authorization_code";

    public NewTokenByRegisterJson(String code, String password) {
        this.password = password;
        this.code = code;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
