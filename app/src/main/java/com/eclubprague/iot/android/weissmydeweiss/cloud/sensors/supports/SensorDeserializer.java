package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports;

import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Accelerometer;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.AmbientThermometer;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Barometer;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.ESCLed;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.ESCThermometer;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.GPS;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.GravitySensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Gyroscope;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.HumiditySensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.LightSensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.LinearAccelerometer;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Magnetometer;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.ProximitySensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.RotationSensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
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
        int sensorType = jo.get("type").getAsInt();

        switch(sensorType) {
            case SensorType.THERMOMETER:
                return context.deserialize(json, ESCThermometer.class);
            case SensorType.LED:
                return context.deserialize(json, ESCLed.class);
            case SensorType.GPS:
                return context.deserialize(json, GPS.class);
            case SensorType.ACCELEROMETER:
                return context.deserialize(json, Accelerometer.class);
            case SensorType.LIGHT:
                return context.deserialize(json, LightSensor.class);
            case SensorType.PROXIMITY:
                return context.deserialize(json, ProximitySensor.class);
            case SensorType.MAGNETOMETER:
                return context.deserialize(json, Magnetometer.class);
            case SensorType.GYROSCOPE:
                return context.deserialize(json, Gyroscope.class);
            case SensorType.PRESSURE:
                return context.deserialize(json, Barometer.class);
            case SensorType.GRAVITY:
                return context.deserialize(json, GravitySensor.class);
            case SensorType.LINEAR_ACCELEROMETER:
                return context.deserialize(json, LinearAccelerometer.class);
            case SensorType.ROTATION:
                return context.deserialize(json, RotationSensor.class);
            case SensorType.HUMIDITY:
                return context.deserialize(json, HumiditySensor.class);
            case SensorType.AMBIENT_THERMOMETER:
                return context.deserialize(json, AmbientThermometer.class);
            default:
                throw new JsonParseException("We can't deserialize this type of Sensor.");
        }
    }
}
