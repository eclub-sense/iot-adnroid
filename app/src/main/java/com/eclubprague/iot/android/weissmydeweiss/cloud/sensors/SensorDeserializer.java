package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by paulos on 21. 7. 2015.
 */
public class SensorDeserializer implements JsonDeserializer<Sensor> {
    @Override
    public Sensor deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        String sensorTypeName = jo.get("type").getAsString();
        SensorType sensorType = SensorType.valueOf(sensorTypeName);

        if(sensorType != null) {
            if (sensorType.equals(SensorType.THERMOMETER)) {
                return context.deserialize(json, ESCThermometer.class);
            } else if (sensorType.equals(SensorType.LED)) {
                return context.deserialize(json, ESCLed.class);
            }
        }
        throw new JsonParseException("We can't deserialize this type of Sensor.");
    }
}
