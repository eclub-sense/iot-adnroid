package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import com.google.gson.annotations.Expose;

/**
 * Created by Dat on 27.7.2015.
 */
public class GPS extends Sensor {

    @Expose(deserialize = false) protected double latitude = -1;
    @Expose (deserialize = false) protected double longitude = -1;

    public GPS() {
        super();
    }
    public GPS(int uuid, String secret) {
        super(uuid, SensorType.GPS, secret);
    }

    @Override
    public void readPayload(byte[] data) {

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "GPS [latitude=" + latitude + ", longitude=" + longitude + ", uuid=" + uuid + ", type="
                + type + ", secret=" + secret + "]";
    }

    public void setCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
