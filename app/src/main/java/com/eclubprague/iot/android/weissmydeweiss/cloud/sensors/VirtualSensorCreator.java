package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

public class VirtualSensorCreator {

	public static Sensor createSensorInstance(int uuid, SensorType type, String secret) {
		switch (type) {
			case THERMOMETER : return new ESCThermometer(uuid, secret);
			case LED : return new Led(uuid, secret);
		default : return null;
		}
	}

}
