package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.NameValuePair;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorEntity;

import java.util.List;

/**
 * Created by Dat on 22.9.2015.
 */
public class EasyButton extends Sensor {

    public EasyButton(String uuid, String secret, Hub hub, String name) {
        super(uuid, SensorType.EASY, secret, hub, name, null);
    }

    public EasyButton(SensorEntity entity) {
        super(entity);
    }

    @Override
    public void readPayload(byte[] data) {

    }

    @Override
    public String printData() {
        return null;
    }

    @Override
    public List<NameValuePair> getDataList() {
        return null;
    }

    @Override
    public void setData(float[] values) {

    }
}
