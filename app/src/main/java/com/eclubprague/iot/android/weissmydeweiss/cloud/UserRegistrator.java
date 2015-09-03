package com.eclubprague.iot.android.weissmydeweiss.cloud;

import org.restlet.resource.Post;

/**
 * Created by Dat on 31.8.2015.
 */
public interface UserRegistrator {
    @Post("json")
    public TokenWrapper retrieveToken(NewTokenJson token);

    @Post("json")
    public TokenWrapper retrieveTokenByRegister(NewTokenByRegisterJson message);

    @Post("json")
    public String retrieveToken_3(NewTokenJson token);

    @Post
    public TokenWrapper retrieveToken_2(String token);
}
