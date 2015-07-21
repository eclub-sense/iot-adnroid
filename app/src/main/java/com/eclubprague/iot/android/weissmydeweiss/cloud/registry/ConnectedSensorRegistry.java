package com.eclubprague.iot.android.weissmydeweiss.cloud.registry;

import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;

public class ConnectedSensorRegistry extends ConnectedDeviceList<Sensor> {

	private static ConnectedSensorRegistry list = new ConnectedSensorRegistry();
	
	public static ConnectedSensorRegistry getInstance() {
		return list;
	}
}
