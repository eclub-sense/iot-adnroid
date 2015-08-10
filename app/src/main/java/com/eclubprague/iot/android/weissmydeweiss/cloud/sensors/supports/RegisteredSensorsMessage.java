package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports;

import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dat on 10.8.2015.
 */
public class RegisteredSensorsMessage {

    private List<SensorDataWrapper> my;
    private List<SensorDataWrapper> borrowed;

    public List<SensorDataWrapper> getMy() {
        return my;
    }

    public List<SensorDataWrapper> getBorrowed() {
        return borrowed;
    }

    public List<Sensor> getMySensors() {
//        List<Sensor> mySensors = new ArrayList<>();
//        for(int i = 0; i < my.size(); i++) {
//            mySensors.add(new Sensor(my.get(i).getUuid(), my.get(i).getType(), "secret"));
//        }
        //TODO return list of Sensors
        return null;
    }

    public List<Sensor> getBorrowedSensors() {
        //TODO return list of Sensors
        return null;
    }
}
