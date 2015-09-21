package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.NameValuePair;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorEntity;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Dat on 28.7.2015.
 */
public class GPS  extends Sensor {

    protected double latitude = -1;
    protected double longitude = -1;

    public GPS() {
        super();
    }
    public GPS(String uuid, String secret, Hub hub, String name) {
        super(uuid, SensorType.GPS, secret, hub, name, null);
    }

    public GPS(SensorEntity entity) {
        super(entity);
    }

    @Override
    public void readPayload(byte[] payload) {
        //TODO
    }

    @Override
    public String printData() {
        return ("longitude = " + longitude + ", latitude = " + latitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public List<NameValuePair> getDataList() {
        measured.clear();
        measured.add(new NameValuePair("latitude", Double.toString(latitude)));
        measured.add(new NameValuePair("longitude", Double.toString(longitude)));
        return measured;
    }

    @Override
    public void setData(float[] values) {
    }
}