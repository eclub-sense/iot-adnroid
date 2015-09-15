package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.registry.Identificable;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.NameValuePair;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.cloud_entities.SensorEntity;
import com.google.gson.Gson;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public abstract class Sensor implements Identificable {

	protected String uuid;
	protected String description;
	protected String hub_uuid;
	protected String secret;
	protected int type = SensorType.THERMOMETER;
	protected String s_type = "sensor";
	protected transient int incr;
	/*@Expose (deserialize = false) protected int battery;
    @Expose (deserialize = false) protected String hubID;*/
	protected transient WeakReference<Hub> hubRef;
	protected transient byte reserved[] = new byte[3];

	protected transient String owner;
	protected transient String access;

	protected transient List<NameValuePair> measured = new ArrayList<>();

	public abstract void readPayload(byte[] data);
	public abstract String printData();
	public abstract List<NameValuePair> getDataList();
	public abstract void setData(float values[]);

	protected Sensor() {
	}

	protected Sensor(SensorEntity entity) {
		try {
			this.hubRef = new WeakReference<Hub>(new Hub(entity.getHub().getUuid()));
			this.hub_uuid = hubRef.get().getUuid();
		} catch (Exception e) {
			Log.e("EXC", e.toString());
		}
		this.uuid = entity.getUuid();
		this.type = entity.getType();
		this.s_type = SensorType.getStringSensorType(type);
		this.secret = "secret";
		this.description = entity.getDescription();
		this.access = entity.getAccess();
		this.owner = entity.getOwnerEmail();
	}

	protected Sensor(String uuid, int type, String secret, Hub hub, String name) {
		this.hubRef = new WeakReference<Hub>(hub);
		this.hub_uuid = hubRef.get().getUuid();
		this.uuid = uuid;
		this.type = type;
		this.s_type = SensorType.getStringSensorType(type);
		this.secret = secret;
		this.description = name;
		this.access = "only me";
		this.owner = "me";
	}

	public void readPacket(String p) throws DecoderException {
		byte[] packet = decrypt(p);
		incr = (int)(packet[0]);
		//battery = (int)(packet[2]);
		reserved[0] = packet[3];
		reserved[1] = packet[4];
		reserved[2] = packet[5];
		readPayload(Arrays.copyOfRange(packet, 6, (p.length() / 2) + 1));
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
	public String getUuid() {
		return uuid;
	}

    /*public String getStringUuid() {
        return String.format("%08d", uuid);
    }*/

	public int getType() {
		return type;
	}

	public String getSecret() {
		return secret;
	}

	public int getIncr() {
		return incr;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setMeasured(List<NameValuePair> measured) {
		this.measured = measured;
	}

	public List<NameValuePair> getMeasured() {
		return measured;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

	public String getDescription() {
		return description;
	}

	public String getOwner() {
		return owner;
	}

	public String getAccess() {
		return access;
	}

	//	@Expose @SerializedName("@type") private String jsonType = "sensor";
//	@Expose protected String uuid;
//	@Expose protected int type;
//	@Expose (serialize = false) protected String secret;
//	protected int incr;
//	@Expose (deserialize = false) protected int battery;
//	@Expose (deserialize = false) protected String hubID;
//	protected Hub hub;
//	protected byte reserved[] = new byte[3];
//
//	public void readPayload(byte[] data) {
//
//	}
//
//	protected Sensor() {
//
//	}
//
//	protected Sensor(String uuid, int type, String secret) {
//		this.uuid = uuid;
//		this.type = type;
//		this.secret = secret;
//	}
//	protected Sensor(String uuid, SensorType type, String secret) {
//		this.uuid = uuid;
//		this.type = type.getCode();
//		this.secret = secret;
//	}
//
//	public void readPacket(String p) throws DecoderException {
//		byte[] packet = decrypt(p);
//		incr = (int)(packet[0]);
//		battery = (int)(packet[2]);
//		reserved[0] = packet[3];
//		reserved[1] = packet[4];
//		reserved[2] = packet[5];
//		readPayload(Arrays.copyOfRange(packet, 6, (p.length()/2)+1));
//	}
//
//	private byte[] decrypt(String encrypted) throws DecoderException {
//		int len = encrypted.length()/2;
//		byte[] secretBytes = Hex.decodeHex(secret.toCharArray());
//		byte[] encryptedBytes = Hex.decodeHex(encrypted.toCharArray());
//		for (int i = 0; i < len; i++) {
//			encryptedBytes[i] = (byte)(0xff & ((int)secretBytes[i] ^ (int)encryptedBytes[i]));
//		}
//		return encryptedBytes;
//	}
//
//	@Override
//	public String getUuid() {
//		return uuid;
//	}
//
//	public int getType() {
//		return type;
//	}
//
//	public String getSecret() {
//		return secret;
//	}
//
//	public int getIncr() {
//		return incr;
//	}
//
//	public int getBattery() {
//		return battery;
//	}
//
//	public Hub getHub() {
//		return hub;
//	}
//
//	public void setHub(Hub hub) {
//		this.hubID = hub.getUuid();
//		this.hub = hub;
//	}
//
//	public void setUuid(String uuid) {
//		this.uuid = uuid;
//	}
//
//	@Override
//	public String toString() {
//		return "Sensor [jsonType=" + jsonType + ", uuid=" + uuid + ", type=" + type + ", secret=" + secret + "]";
//	}
}
