package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.data.ESCThermometerData;

public class ESCThermometer extends Sensor {

	private ESCThermometerData data;
	
	public ESCThermometer(int uuid, int secret) {
		super(uuid, SensorType.THERMOMETER, secret);
	}
}
