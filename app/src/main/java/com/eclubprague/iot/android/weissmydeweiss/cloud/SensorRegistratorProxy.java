package com.eclubprague.iot.android.weissmydeweiss.cloud;

import org.restlet.resource.ClientProxy;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Result;

/**
 * Created by paulos on 13. 7. 2015.
 */
public interface SensorRegistratorProxy extends ClientProxy {
    @Get
    public void retrieve(Result<Object> callback);

    @Post
    public void store(Object sensor, Result<Void> callback);

    @Delete
    public void remove(Result<Void> callback);
}
