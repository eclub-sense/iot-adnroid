package com.eclubprague.iot.android.weissmydeweiss.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.BuiltInSensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.GPS;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
/**
 * Created by Dat on 27.7.2015.
 */
public class BuiltInSensorsProviderService extends Service implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener, SensorEventListener {

    private GoogleApiClient mGoogleApiClient;

    private Context context;

    private final IBinder binder = new LocationListenerBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocationListenerBinder extends Binder {

        public BuiltInSensorsProviderService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BuiltInSensorsProviderService.this;
        }

        public void setServiceContext(Context context) {
            BuiltInSensorsProviderService.this.context = context;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        mGoogleApiClient.connect();
        return binder;
    }

    //Build GoogleApiClient to request GPS
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildGoogleApiClient();
        createLocationRequest();
        initBuiltInSensorsCollection();
        //Run the service in background thread
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public boolean onUnbind(Intent intent) {
        stopLocationUpdates();
        unregisterSensorListeners();
        return super.onUnbind(intent);
    }

    private Location mLastLocation;
    private double longitude = -1;
    private double latitude = -1;

    private boolean connected = false;

    @Override
    public void onConnected(Bundle connectionHint) {
        connected = true;
        Toast.makeText(this, "Connection connected", Toast.LENGTH_SHORT).show();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"Connection Suspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"Connection Failed",Toast.LENGTH_SHORT).show();
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public boolean getConnected() {
        return connected;
    }

    //------------------------------------------------------

    private LocationRequest mLocationRequest;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        if(mLocationRequest != null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } else {
            Toast.makeText(this,"mLocationRequest is null", Toast.LENGTH_SHORT).show();
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    private String mLastUpdateTime;

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            if(builtInSensors.containsKey(gpsKey)) {
                ((GPS) (builtInSensors.get(gpsKey))).setCoordinates(latitude, longitude);
            }
        }
    }

    //-----------------------------------------------------------
    //SensorEventListener
    //-----------------------------------------------------------

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()) {

            default:
                List<Float> data = new ArrayList<>();
                for(int i = 0; i < event.values.length; i++) {
                    data.add(event.values[i]);
                }
                ( (BuiltInSensor)(builtInSensors.get(event.sensor.getName())) ).setData(data);
        }
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
    }

    //-----------------------------------------------------------
    //SensorManagement
    //-----------------------------------------------------------

    private SensorManager mSensorManager;
    private List<android.hardware.Sensor> deviceSensors;

    private HashMap<String, Sensor> builtInSensors = new HashMap<>();

    private String gpsKey = "GPS_phamtdat";

    private void initBuiltInSensorsCollection() {
        mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        deviceSensors = mSensorManager.getSensorList(android.hardware.Sensor.TYPE_ALL);

        builtInSensors.put(gpsKey, new GPS(12346, "gps_secret"));

        for(int i = 0; i < deviceSensors.size(); i++) {
            android.hardware.Sensor sensor = deviceSensors.get(i);
            builtInSensors.put(sensor.getName(),
                    new BuiltInSensor(i, sensor.getName()));
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void unregisterSensorListeners() {
            mSensorManager.unregisterListener(this);
    }
}

