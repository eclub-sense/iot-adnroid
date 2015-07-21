package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

public interface WriteableSensor {

	public void writePacket(byte[] payload);
	public void createPayload();
	public byte[] encrypt(String data);
}
