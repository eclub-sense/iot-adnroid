package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import java.util.Arrays;
import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.registry.Identificable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;


public class Sensor implements Identificable {
	
	@Expose @SerializedName("@type") private String jsonType = "sensor";
	@Expose protected int uuid;
	@Expose protected SensorType type = SensorType.THERMOMETER;
	@Expose (serialize = false) protected String secret;
	protected int incr;
	@Expose (deserialize = false) protected int battery;
	@Expose (deserialize = false) protected int hubID;
	protected Hub hub;
	protected byte reserved[] = new byte[3];

	@Expose (deserialize = false) protected int temperature;
	@Expose (deserialize = false) protected int pressure;

	public int getTemperature() {
		return temperature;
	}

	public int getPressure() {
		return pressure;
	}

	public Sensor() {
		super();
	}

	public Sensor(int uuid, SensorType type, String secret) {
		this.uuid = uuid;
		this.type = type;
		this.secret = secret;
	}

	public void setPayload(byte[] data) {

	}
	
	public void setMessageParts(String p) throws DecoderException {
		byte[] packet = decrypt(p);
		incr = (int)(packet[0]);
		battery = (int)(packet[2]);
		reserved[0] = packet[3];
		reserved[1] = packet[4];
		reserved[2] = packet[5];
		setPayload(Arrays.copyOfRange(packet, 6, (p.length()/2)+1));
	}
	
	private byte[] decrypt(String encrypted) throws DecoderException {
		int len = encrypted.length()/2;
		byte[] secretBytes = Hex.decodeHex(secret.toCharArray());
		byte[] encryptedBytes = Hex.decodeHex(encrypted.toCharArray());
		for (int i = 0; i < len; i++) {
			encryptedBytes[i] = (byte)(0xff & ((int)secretBytes[i] ^ (int)encryptedBytes[i]));
		}
		return encryptedBytes;
	}
	
	@Override
	public int getIntUuid() {
		return uuid;
	}

	public String getStringUuid() {
		return String.format("%08d", uuid);
	}
	
	public SensorType getType() {
		return type;
	}

	public String getSecret() {
		return secret;
	}

	public int getIncr() {
		return incr;
	}

	public int getBattery() {
		return battery;
	}

	public Hub getHub() {
		return hub;
	}

	public void setHub(Hub hub) {
		this.hubID = hub.getIntUuid();
		this.hub = hub;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "Sensor [jsonType=" + jsonType + ", uuid=" + uuid + ", type=" + type + ", secret=" + secret + "]";
	}
}
