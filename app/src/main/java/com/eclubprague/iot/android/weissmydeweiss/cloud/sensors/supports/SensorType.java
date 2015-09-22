package com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports;


public class SensorType {
	public static final int GPS = 8;
	public static final int THERMOMETER = 0x80;
	public static final int LED = 3;
	public static final int ACCELEROMETER = 7;
	public static final int BUILTIN = 0;
	public static final int LIGHT = 9;
	public static final int PROXIMITY = 10;
	public static final int MAGNETOMETER = 11;
	public static final int GYROSCOPE = 12;
	public static final int PRESSURE = 13;
	public static final int GRAVITY = 14;
	public static final int LINEAR_ACCELEROMETER = 15;
	public static final int ROTATION = 16;
	public static final int HUMIDITY = 17;
	public static final int AMBIENT_THERMOMETER = 18;
	public static final int PIR = 129;
	public static final int LCD = 123;
	public static final int BEACON = 130;
	public static final int EASY = 200;
	public static final int ACCELEROMETER2 = 89;


	public static final String getStringSensorType(int type) {
		switch(type) {
			case GPS:
				return "GPS";
			case THERMOMETER:
				return "Thermometer";
			case LED:
				return "Led";
			case ACCELEROMETER:
				return "Accelerometer";
			case LIGHT:
				return "Light sensor";
			case PROXIMITY:
				return "Proximity sensor";
			case MAGNETOMETER:
				return "Magnetometer";
			case GYROSCOPE:
				return "Gyroscope";
			case PRESSURE:
				return "Barometer";
			case GRAVITY:
				return "Gravity sensor";
			case LINEAR_ACCELEROMETER:
				return "Linear accelerometer";
			case ROTATION:
				return "Rotation sensor";
			case HUMIDITY:
				return "Humidity sensor";
			case AMBIENT_THERMOMETER:
				return "Ambient thermometer";
			case PIR:
				return "Pir sensor";
			case LCD:
				return "LCD display";
			case BEACON:
				return "Beacon";
			case EASY:
				return "Easy Button";
			case ACCELEROMETER2:
				return "Accelerometer";
			default:
				return "Public sensor";
		}
	}
}
