package com.eclubprague.iot.android.weissmydeweiss.cloud;

import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorPaginatedCollection;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.RegisteredSensorsMessage;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.AllSensors;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorAndData;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

/**
 * Created by paulos on 14. 7. 2015.
 */
public interface RegisteredSensors {
    @Get("json")
    public SensorPaginatedCollection retrieve();

    @Get("json")
    public RegisteredSensorsMessage retrieve_2();

    @Get("json")
    public AllSensors retrieve_AllSensors();

    @Get("json")
    public SensorAndData retrieve_SensorAndData();

    @Get("json")
    public String retrieve_test();

    @Get("json")
    public Sensor get(String uuid);

    @Get("json")
    public Sensor get();

    @Post("json")
    public void store(SensorPaginatedCollection collection);

    @Delete
    public void remove();

    @Get
    public String getStringData();
}
