package com.eclubprague.iot.android.weissmydeweiss.cloud.hubs;

import com.eclubprague.iot.android.weissmydeweiss.cloud.registry.Identificable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hub implements Identificable {

	@Expose @SerializedName("@type") private String jsonType = "hub";
	@Expose protected String uuid;
	@Expose (deserialize = false) protected String status = "connected";
	
	public Hub(String uuid) {
		this.uuid = uuid;
	}
	
	@Override
	public String getUuid() {
		return uuid;
	}
	
	public String getStringUuid() {
		return String.format("%08d", uuid);
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Hub [uuid=" + uuid + "]";
	}
}
