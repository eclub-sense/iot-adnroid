package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.NameValuePair;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.ActionEntity;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dat on 21.9.2015.
 */
public class LcdDisplay extends Sensor {

    public LcdDisplay(SensorEntity entity) {
        super(entity);
    }

    public LcdDisplay(String uuid, String secret, Hub hub, String name, ArrayList<ActionEntity> actions) {
        super(uuid, SensorType.LCD, secret, hub, name, actions);
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
