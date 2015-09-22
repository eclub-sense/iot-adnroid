package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.ActionEntity;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorEntity;

import java.util.ArrayList;

public class VirtualSensorCreator {

	public static Sensor createSensorInstance(String uuid, int type, String secret, Hub hub, String name) {
		switch (type) {
			case SensorType.THERMOMETER:
				return new ESCThermometer(uuid, secret, hub, name);
			case SensorType.LED:
				return new ESCLed(uuid, secret, hub, name, null);
			case SensorType.GPS:
				return new GPS(uuid, secret, hub, name);
			case SensorType.ACCELEROMETER:
				return new Accelerometer(uuid, secret, hub, name);
			case SensorType.LIGHT:
				return new LightSensor(uuid, secret, hub, name);
			case SensorType.PROXIMITY:
				return new ProximitySensor(uuid, secret, hub, name);
			case SensorType.MAGNETOMETER:
				return new Magnetometer(uuid, secret, hub, name);
			case SensorType.GYROSCOPE:
				return new Gyroscope(uuid, secret, hub, name);
			case SensorType.PRESSURE:
				return new Barometer(uuid, secret, hub, name);
			case SensorType.GRAVITY:
				return new GravitySensor(uuid, secret, hub, name);
			case SensorType.LINEAR_ACCELEROMETER:
				return new LinearAccelerometer(uuid, secret, hub, name);
			case SensorType.ROTATION:
				return new RotationSensor(uuid, secret, hub, name);
			case SensorType.HUMIDITY:
				return new HumiditySensor(uuid, secret, hub, name);
			case SensorType.AMBIENT_THERMOMETER:
				return new AmbientThermometer(uuid, secret, hub, name);
			case 2:
				return new Accelerometer(uuid, secret, hub, name);
			case SensorType.PIR:
				return new PirSensor(uuid, secret, hub, name);
			case SensorType.LCD:
				//ArrayList<ActionEntity> actions = new ArrayList<>();
				//TODO add actions
				return new LcdDisplay(uuid, secret, hub, name, null);
			default:
				//return new PublicSensor(uuid, secret, hub, type, name);
				return null;
		}
	}


	public static Sensor createSensorInstance(SensorEntity entity) {
		switch (entity.getType()) {
			case SensorType.THERMOMETER:
				return new ESCThermometer(entity);
			case SensorType.LED:
				return new ESCLed(entity);
			case SensorType.GPS:
				return new GPS(entity);
			case SensorType.ACCELEROMETER:
				return new Accelerometer(entity);
			case SensorType.LIGHT:
				return new LightSensor(entity);
			case SensorType.PROXIMITY:
				return new ProximitySensor(entity);
			case SensorType.MAGNETOMETER:
				return new Magnetometer(entity);
			case SensorType.GYROSCOPE:
				return new Gyroscope(entity);
			case SensorType.PRESSURE:
				return new Barometer(entity);
			case SensorType.GRAVITY:
				return new GravitySensor(entity);
			case SensorType.LINEAR_ACCELEROMETER:
				return new LinearAccelerometer(entity);
			case SensorType.ROTATION:
				return new RotationSensor(entity);
			case SensorType.HUMIDITY:
				return new HumiditySensor(entity);
			case SensorType.AMBIENT_THERMOMETER:
				return new AmbientThermometer(entity);
			case 2:
				return new Accelerometer(entity);
			case SensorType.PIR:
				return new PirSensor(entity);
			case SensorType.LCD:
				return new LcdDisplay(entity);
			default:
				//return new PublicSensor(entity);
				return null;
		}
	}
}
