package com.eclubprague.iot.android.weissmydeweiss.cloud.gsonmods;

import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorDeserializer;
import com.google.gson.GsonBuilder;

import org.restlet.ext.gson.GsonRepresentation;
import org.restlet.representation.Representation;

/**
 * Created by paulos on 21. 7. 2015.
 */
public class GsonCustomRepresentation<T> extends GsonRepresentation<T> {
    /**
     * Creates a customized version of the GsonBuilder.
     * @return Created GsonBuilder instance
     */
    @Override
    protected GsonBuilder createBuilder() {
        return super.createBuilder()
                .registerTypeAdapter(Sensor.class, new SensorDeserializer());
    }

    public GsonCustomRepresentation(Representation representation, Class<T> objectClass) {
        super(representation, objectClass);
    }

    public GsonCustomRepresentation(T object) {
        super(object);
    }
}
