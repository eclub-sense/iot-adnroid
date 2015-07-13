package com.eclubprague.iot.android.weissmydeweiss.cloud;

import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Result;

/**
 * Created by paulos on 13. 7. 2015.
 */
public interface SensorRegistrator {
    @Get
    public Sensor retrieve();

    @Post
    public void store(Sensor sensor);

    @Delete
    public void remove();
}
