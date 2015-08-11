package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;

public class VirtualSensorCreator {

	public static Sensor createSensorInstance(String uuid, int type, String secret, Hub hub) {
		switch (type) {
			case SensorType.THERMOMETER:
				return new ESCThermometer(uuid, secret, hub);
			case SensorType.LED:
				return new ESCLed(uuid, secret, hub);
			case SensorType.GPS:
				return new GPS(uuid, secret, hub);
			case SensorType.ACCELEROMETER:
				return new Accelerometer(uuid, secret, hub);
			case SensorType.LIGHT:
				return new LightSensor(uuid, secret, hub);
			case SensorType.PROXIMITY:
				return new ProximitySensor(uuid, secret, hub);
			case SensorType.MAGNETOMETER:
				return new Magnetometer(uuid, secret, hub);
			case SensorType.GYROSCOPE:
				return new Gyroscope(uuid, secret, hub);
			case SensorType.PRESSURE:
				return new Barometer(uuid, secret, hub);
			case SensorType.GRAVITY:
				return new GravitySensor(uuid, secret, hub);
			case SensorType.LINEAR_ACCELEROMETER:
				return new LinearAccelerometer(uuid, secret, hub);
			case SensorType.ROTATION:
				return new RotationSensor(uuid, secret, hub);
			case SensorType.HUMIDITY:
				return new HumiditySensor(uuid, secret, hub);
			case SensorType.AMBIENT_THERMOMETER:
				return new AmbientThermometer(uuid, secret, hub);
			case 2:
				return new Accelerometer(uuid, secret, hub);
			default:
				return null;
		}
	}
}
