package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

public class VirtualSensorCreator {

	public static Sensor createSensorInstance(String uuid, SensorType type, String secret) {
		switch (type) {
			case THERMOMETER : return new ESCThermometer(uuid, secret);
			case LED : return new ESCLed(uuid, secret);
		default : return null;
		}
	}

	public static Sensor createSensorInstance(String uuid, int type, String secret) {
		return new Sensor(uuid, type, secret);
	}

}
