package com.eclubprague.iot.android.weissmydeweiss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.eclubprague.iot.android.weissmydeweiss.cloud.User;
import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.VirtualSensorCreator;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorDataDialog;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorInfoDialog;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorsExpandableListViewAdapter;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.MagneticFieldChart;
import com.eclubprague.iot.android.weissmydeweiss.ui.charts.OneValueChartActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dat on 14.7.2015.
 */
public class SensorsListFragment extends Fragment {

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
                String token = ((MainActivity)SensorsListFragment.this.getActivity()).getToken();

                Intent intent = new Intent(SensorsListFragment.this.getActivity(), OneValueChartActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("uuid", sensor.getUuid());
                intent.putExtra("title", SensorType.getStringSensorType(sensor.getType()));
                intent.putExtra("datasetDesc", "todo");
                startActivity(intent);

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

}
