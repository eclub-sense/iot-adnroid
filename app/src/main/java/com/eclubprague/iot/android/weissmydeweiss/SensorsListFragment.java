package com.eclubprague.iot.android.weissmydeweiss;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.VirtualSensorCreator;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorInfoDialog;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorListViewAdapter;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorsExpandableListViewAdapter;

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

    public static SensorsListFragment newInstance() {
        SensorsListFragment fragment = new SensorsListFragment();

        return fragment;
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
                new SensorInfoDialog(rootView.getContext(),
                        (Sensor) parent.getExpandableListAdapter().
                                getChild(groupPosition, childPosition));
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

        groupItems.add(new Hub("1"));
        groupItems.add(new Hub("2"));
        groupItems.add(new Hub("3"));

        List<Sensor> livingRoom = new ArrayList<>();
        livingRoom.add(VirtualSensorCreator.createSensorInstance("123", SensorType.THERMOMETER, "12345"));
        livingRoom.add(VirtualSensorCreator.createSensorInstance("34345", SensorType.LED, "8878"));
        livingRoom.add(VirtualSensorCreator.createSensorInstance("3677", SensorType.THERMOMETER, "33442"));

        List<Sensor> kitchen = new ArrayList<>();
        kitchen.add(VirtualSensorCreator.createSensorInstance("7888", SensorType.THERMOMETER, "8888"));
        kitchen.add(VirtualSensorCreator.createSensorInstance("2015", SensorType.LED, "70021"));

        List<Sensor> bathroom = new ArrayList<>();
        bathroom.add(VirtualSensorCreator.createSensorInstance("7001", SensorType.THERMOMETER, "0124"));
        bathroom.add(VirtualSensorCreator.createSensorInstance("1991", SensorType.LED, "12345"));

        childItems.put(groupItems.get(0), livingRoom);
        childItems.put(groupItems.get(1), kitchen);
        childItems.put(groupItems.get(2), bathroom);
    }

}
