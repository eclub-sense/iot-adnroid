package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

/**
 * Created by Dat on 27.7.2015.
 */
public class BuiltInSensor extends Sensor {

    public BuiltInSensor() {
        super();
    }
    public BuiltInSensor(int uuid, String secret) {
        super(uuid, SensorType.GPS, secret);
    }


    @Override
    public void readPayload(byte[] data) {

    }
}
