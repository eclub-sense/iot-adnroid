package com.eclubprague.iot.android.weissmydeweiss.cloud;

import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.SensorPaginatedCollection;
import com.google.gson.JsonElement;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

/**
 * Created by paulos on 14. 7. 2015.
 */
public interface RegisteredSensors {
    @Get("json")
    public SensorPaginatedCollection retrieve();

    @Post("json")
    public void store(SensorPaginatedCollection collection);

    @Delete
    public void remove();
}
