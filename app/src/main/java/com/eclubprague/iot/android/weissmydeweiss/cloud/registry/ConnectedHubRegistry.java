package com.eclubprague.iot.android.weissmydeweiss.cloud.registry;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;

public class ConnectedHubRegistry extends ConnectedDeviceList<Hub> {

	private static ConnectedHubRegistry list = new ConnectedHubRegistry();
	
	public static ConnectedHubRegistry getInstance() {
		return list;
	}

}
