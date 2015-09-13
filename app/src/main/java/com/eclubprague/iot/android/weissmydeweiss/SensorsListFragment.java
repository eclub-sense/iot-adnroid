package com.eclubprague.iot.android.weissmydeweiss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.tasks.ShareSensorTask;
import com.eclubprague.iot.android.weissmydeweiss.tasks.UnregisterSensorTask;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorMenuDialog;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorShareDialog;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorsExpandableListViewAdapter;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.PirChartActivity;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.SensorDataChartActivity;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.ThermChartActivity;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.ThermometerChartActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dat on 14.7.2015.
 */
public class SensorsListFragment extends Fragment implements SensorMenuDialog.DialogDelegate,
SensorShareDialog.DialogDelegate{

    static ExpandableListView expListView;
    SensorsExpandableListViewAdapter adapter;
    List<Hub> groupItems;
    HashMap<Hub, List<Sensor>> childItems;
    View rootView;
    ArrayList<MainActivity> activityRef;

    public static SensorsListFragment newInstance() {
        SensorsListFragment fragment = new SensorsListFragment();

        return fragment;
    }

    public void setMainActivityRef(ArrayList<MainActivity> activityRef) {
        this.activityRef = activityRef;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_sensors_list, container, false);

        // get the listview
        expListView = (ExpandableListView) rootView.findViewById(R.id.sensors_expList);

        // preparing list data
        prepareListData();

        adapter = new SensorsExpandableListViewAdapter(rootView.getContext(), groupItems, childItems);

        // setting list adapter
        expListView.setAdapter(adapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Sensor sensor = (Sensor) parent.getExpandableListAdapter().
                        getChild(groupPosition, childPosition);
                new SensorMenuDialog(SensorsListFragment.this, sensor);
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }

    private void prepareListData() {
        groupItems = new ArrayList<Hub>();
        childItems = new HashMap<Hub, List<Sensor>>();
    }

    @Override
    public void onSensorChartRequested(Sensor sensor) {

        String token = ((MainActivity) SensorsListFragment.this.getActivity()).getToken().getAccess_token();

        Intent intent = new Intent(SensorsListFragment.this.getActivity(), SensorDataChartActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("uuid", sensor.getUuid());
        intent.putExtra("title", SensorType.getStringSensorType(sensor.getType()));

        switch (sensor.getType()) {

//            case SensorType.GPS:
//                String[] datasetDesc1 = {"longitude", "latitude"};
//                intent.putExtra("datasetDesc", datasetDesc1);
//                intent.putExtra("upperBound", 180f);
//                startActivity(intent);
//                break;
            case SensorType.THERMOMETER:
                Intent intent2 = new Intent(SensorsListFragment.this.getActivity(), ThermChartActivity.class);
                intent2.putExtra("token", token);
                intent2.putExtra("uuid", sensor.getUuid());
                startActivity(intent2);
                //String[] datasetDesc2 = {"increment", "pressure", "temperature", "vbat", "rssi"};
                //intent.putExtra("datasetDesc", datasetDesc2);
                //intent.putExtra("upperBound", 45f);
                break;
//            case SensorType.ACCELEROMETER:
//                String[] datasetDesc3 = {"x", "y", "z"};
//                intent.putExtra("datasetDesc", datasetDesc3);
//                intent.putExtra("upperBound", 15f);
//                startActivity(intent);
//                break;
//            case SensorType.LIGHT:
//                String[] datasetDesc4 = {"illumination"};
//                intent.putExtra("datasetDesc", datasetDesc4);
//                intent.putExtra("upperBound", 400f);
//                startActivity(intent);
//                break;
//            case SensorType.PROXIMITY:
//                String[] datasetDesc5 = {"proximity"};
//                intent.putExtra("datasetDesc", datasetDesc5);
//                intent.putExtra("upperBound", 10f);
//                startActivity(intent);
//                break;
//            case SensorType.MAGNETOMETER:
//                String[] datasetDesc6 = {"x", "y", "z"};
//                intent.putExtra("datasetDesc", datasetDesc6);
//                intent.putExtra("upperBound", 300f);
//                startActivity(intent);
//                break;
//            case SensorType.GYROSCOPE:
//                String[] datasetDesc7 = {"x", "y", "z"};
//                intent.putExtra("datasetDesc", datasetDesc7);
//                intent.putExtra("upperBound", 10f);
//                startActivity(intent);
//                break;
//            case SensorType.PRESSURE:
//                String[] datasetDesc8 = {"pressure"};
//                intent.putExtra("datasetDesc", datasetDesc8);
//                intent.putExtra("upperBound", 1100f);
//                startActivity(intent);
//                break;
//            case SensorType.GRAVITY:
//                String[] datasetDesc9 = {"x", "y", "z"};
//                intent.putExtra("datasetDesc", datasetDesc9);
//                intent.putExtra("upperBound", 300f);
//                startActivity(intent);
//                break;
//            case SensorType.LINEAR_ACCELEROMETER:
//                String[] datasetDesc10 = {"x", "y", "z"};
//                intent.putExtra("datasetDesc", datasetDesc10);
//                intent.putExtra("upperBound", 15f);
//                startActivity(intent);
//                break;
//            case SensorType.ROTATION:
//                String[] datasetDesc11 = {"x", "y", "z"};
//                intent.putExtra("datasetDesc", datasetDesc11);
//                intent.putExtra("upperBound", 15f);
//                startActivity(intent);
//                break;
//            case SensorType.HUMIDITY:
//                String[] datasetDesc12 = {"humidity"};
//                intent.putExtra("datasetDesc", datasetDesc12);
//                intent.putExtra("upperBound", 110f);
//                startActivity(intent);
//                break;
//            case SensorType.AMBIENT_THERMOMETER:
//                String[] datasetDesc13 = {"temperature"};
//                intent.putExtra("datasetDesc", datasetDesc13);
//                intent.putExtra("upperBound", 45f);
//                startActivity(intent);
//                break;
            case SensorType.PIR:
                Intent intent3 = new Intent(SensorsListFragment.this.getActivity(), PirChartActivity.class);
                intent3.putExtra("token", token);
                intent3.putExtra("uuid", sensor.getUuid());
                startActivity(intent3);
                break;
            default:
                //TODO CHARTS FOR THE REST
                return;
        }

        //startActivity(intent);

        getActivity()
                .overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }

    @Override
    public void onSensorShareRequested(String uuid) {
        new SensorShareDialog(this, uuid);
    }

    @Override
    public void onSensorUnregisterRequested(String uuid) {
        new UnregisterSensorTask().execute(uuid,
                ((MainActivity) SensorsListFragment.this.getActivity()).getToken().getAccess_token());
    }

    @Override
    public void onSensorShareDialogSubmitted(String email, String uuid) {
        new ShareSensorTask().execute(email, uuid,
                ((MainActivity) SensorsListFragment.this.getActivity()).getToken().getAccess_token());
    }

    @Override
    public void onSensorShareDialogSubmitted(String uuid) {
        new ShareSensorTask().execute(null, uuid,
                ((MainActivity) SensorsListFragment.this.getActivity()).getToken().getAccess_token());
    }
}
