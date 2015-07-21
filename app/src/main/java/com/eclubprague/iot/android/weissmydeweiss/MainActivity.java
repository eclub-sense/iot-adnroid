package com.eclubprague.iot.android.weissmydeweiss;

import android.content.Intent;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.eclubprague.iot.android.weissmydeweiss.cloud.PaginatedCollection;
import com.eclubprague.iot.android.weissmydeweiss.cloud.SensorRegistrator;
import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.SensorPaginatedCollection;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.SensorInstanceCreator;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.VirtualSensorCreator;
import com.eclubprague.iot.android.weissmydeweiss.tasks.RefreshSensorsTask;
import com.eclubprague.iot.android.weissmydeweiss.ui.BuiltInSensorInfoDialog;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorListViewAdapter;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorsExpandableListViewAdapter;

import org.restlet.engine.Engine;
import org.restlet.ext.gson.GsonConverter;
import org.restlet.resource.ClientResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        RefreshSensorsTask.RefreshSensorsCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager(); // For AppCompat use getSupportFragmentManager
        switch(position) {
            default:
            case 0:
                fragment = SensorsListFragment.newInstance();
                break;
            case 1:
                fragment = HubsListFragment.newInstance();
                break;
            case 2:
                fragment = BuiltInSensorsListFragment.newInstance();
        }

        // update the main content by replacing fragments
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshSensorsList(View view) {
        RefreshSensorsTask task = new RefreshSensorsTask(this);
        task.execute("hub1");
    }

    /**
     * Launch a QR code scanner.
     */
    public void launchQRScanner(View view) {
        Intent myIntent = new Intent(MainActivity.this, CameraActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        MainActivity.this.startActivityForResult(myIntent, CameraActivity.REQUEST_SCAN_QR_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CameraActivity.REQUEST_SCAN_QR_CODE) {
            if(resultCode == RESULT_OK) {
                try {
                    final String qrcode = data.getStringExtra(CameraActivity.RESULT_BARCODE);
                    // send the code to the audience
                    String[] qrCodeSplit = qrcode.split(";");
                    final int sensorId = Integer.parseInt(qrCodeSplit[0]);
                    final int sensorType = Integer.parseInt(qrCodeSplit[1]);
                    final String sensorSecret = qrCodeSplit[2];

                    Thread thr = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Engine.getInstance().getRegisteredConverters().add(new GsonConverter());

                                // try connection
                                ClientResource cr = new ClientResource("http://192.168.201.222:8080/sensor_registration");
                                SensorRegistrator sr = cr.wrap(SensorRegistrator.class);

                                Sensor sensor = VirtualSensorCreator.createSensorInstance(sensorId, SensorType.THERMOMETER, sensorSecret);
                                sr.store(sensor);

                            }catch(Throwable e) {
                                e.printStackTrace();
                                Toast t2 = Toast.makeText(MainActivity.this, ":-( Exception: " + e.getMessage(), Toast.LENGTH_LONG);
                                t2.show();
                            }
                        }
                    });
                    thr.start();
                }
                catch(NumberFormatException e) {
                    Toast t2 = Toast.makeText(this, "Exception: NaN", Toast.LENGTH_SHORT);
                    t2.show();
                }
            }
        }
    }

    @Override
    public void handleSensorsRefreshed(String hubId, SensorPaginatedCollection sensorsCollection) {
        Toast.makeText(this, "Refresh done", Toast.LENGTH_SHORT).show();

        ExpandableListView sensorsList = (ExpandableListView) findViewById(R.id.sensors_expList);
        Hub hub1 = new Hub(12456, "example", "that's so secret");
        List<Hub> hubs = new ArrayList<>();
        hubs.add(hub1);
        HashMap<Hub, List<Sensor>> hubSensors = new LinkedHashMap<>();
        hubSensors.put(hub1, sensorsCollection.getItems());
        SensorsExpandableListViewAdapter adapter = new SensorsExpandableListViewAdapter(
                this, hubs, hubSensors);

        sensorsList.setAdapter(adapter);
    }

    @Override
    public void handleSensorsRefreshFailed(String hubId) {
        Toast.makeText(this, "Refresh FAILED :-(", Toast.LENGTH_SHORT).show();
    }
}
