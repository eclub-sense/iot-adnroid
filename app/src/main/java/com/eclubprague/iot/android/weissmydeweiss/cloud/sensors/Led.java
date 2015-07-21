package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

public class Led extends Sensor {

	public Led(int uuid, String secret) {
		super(uuid, SensorType.LED, secret);
	}

	public Led() {
	}

	@Override

	public void setPayload(byte[] data) {
		// TODO Auto-generated method stub
		
	}
}
