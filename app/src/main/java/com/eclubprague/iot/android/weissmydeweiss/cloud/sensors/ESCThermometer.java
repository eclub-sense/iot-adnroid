package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.NameValuePair;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorEntity;
import com.google.gson.annotations.Expose;

import java.util.List;

public class ESCThermometer extends Sensor {
	
	@Expose (deserialize = false) protected int temperature;
	@Expose (deserialize = false) protected int pressure;
	
	public ESCThermometer(SensorEntity entity) {
		super(entity);
	}

	public ESCThermometer(String uuid, String secret, Hub hub, String name) {
		super(uuid, SensorType.THERMOMETER, secret, hub, name);
	}
	
	@Override
	public void readPayload(byte[] payload) {
		temperature = payload[0];
		pressure = payload[1];
	}

	@Override
	public String printData() {
		return "[temperature=" + temperature + ", pressure=" + pressure + "]";
	}

	@Override
	public List<NameValuePair> getDataList() {
		measured.clear();
		measured.add(new NameValuePair("temperature", Integer.toString(temperature)));
		measured.add(new NameValuePair("pressure", Integer.toString(pressure)));
		return measured;
	}

	@Override
	public void setData(float[] values) {

	}

	public int getTemperature() {
		return temperature;
	}

	public int getPressure() {
		return pressure;
	}

	@Override
	public String toString() {
		return "ESCThermometer [temperature=" + temperature + ", pressure=" + pressure + ", uuid=" + uuid + ", type="
				+ type + ", secret=" + secret + "]";
	}
}
