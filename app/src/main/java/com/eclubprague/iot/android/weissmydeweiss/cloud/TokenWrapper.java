package com.eclubprague.iot.android.weissmydeweiss.cloud;

import com.google.gson.Gson;

/**
 * Created by Dat on 31.8.2015.
 */
public class TokenWrapper {

    private String access_token;
    private String refresh_token;
    private String token_type;
    private int expires_in;
    private String error = "";
    private String code = "";

    public static TokenWrapper getTokenWrapperInstance(String jsonToken) {
        return new Gson().fromJson(jsonToken, TokenWrapper.class);
    }

    public TokenWrapper(String access_token, String refresh_token, String token_type, int expires_in) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getError() {
        return error;
    }

    public String getCode() { return code;}

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
