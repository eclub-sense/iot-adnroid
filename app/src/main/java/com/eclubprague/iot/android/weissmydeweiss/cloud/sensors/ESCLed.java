package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import java.util.Arrays;
import java.util.List;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.NameValuePair;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.Switch;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.WriteableSensor;
import com.google.gson.annotations.Expose;

public class ESCLed extends Sensor implements WriteableSensor {

	@Expose (deserialize = false) protected Switch led;

	public ESCLed(String uuid, String secret, Hub hub) {
		super(uuid, SensorType.LED, secret, hub);
	}

	public void readPayload(byte[] data) {
		led = (data[0] < 0) ? Switch.OFF : Switch.ON;
		
	}

	@Override
	public String printData() {
		return ( (led == Switch.OFF) ? "OFF" : "ON" );
	}

	@Override
	public List<NameValuePair> getDataList() {
		measured.clear();
		return measured;
	}

	@Override
	public void setData(float[] values) {

	}

	@Override
	public void writePacket(byte[] payload) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createPayload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] encrypt(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	public Switch getLed() {
		return led;
	}

	@Override
	public String toString() {
		return "ESCLed [led=" + led + ", uuid=" + uuid + ", type=" + type + ", secret=" + secret + ", incr=" + incr
				+  ", hubID=" + hub_uuid + ", hub=" + hubRef.get() + ", reserved="
				+ Arrays.toString(reserved) + "]";
	}
}
