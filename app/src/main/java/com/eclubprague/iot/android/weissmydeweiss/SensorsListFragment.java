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
import android.widget.Toast;

import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.SensorType;
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

    public static SensorsListFragment newInstance() {
        SensorsListFragment fragment = new SensorsListFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sensors_list, container, false);

        // get the listview
        expListView = (ExpandableListView) rootView.findViewById(R.id.sensors_expList);

        // preparing list data
        prepareListData();

        adapter = new SensorsExpandableListViewAdapter(rootView.getContext(), groupItems, childItems);

        // setting list adapter
        expListView.setAdapter(adapter);




//        listView = (ListView) rootView.findViewById(R.id.sensors_list);
//
//        // Defined Array values to show in ListView
//        List<Sensor> sensorList = new ArrayList<>();
//        sensorList.add(new Sensor(123, SensorType.THERMOMETER, "12345"));
//        sensorList.add(new Sensor(34345, SensorType.LED, "8878"));
//        sensorList.add(new Sensor(3677, SensorType.THERMOMETER, "33442"));
//
//        // Define a new Adapter
//        // Assign adapter to ListView
//        listView.setAdapter(new SensorListViewAdapter(getActivity(), R.layout.item_img_twolines, sensorList));

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

        groupItems.add(new Hub(1, "living room", "26594"));
        groupItems.add(new Hub(2, "kitchen", "15879"));
        groupItems.add(new Hub(3, "bathroom", "77777"));

        List<Sensor> livingRoom = new ArrayList<>();
        livingRoom.add(new Sensor(123, SensorType.THERMOMETER, "12345"));
        livingRoom.add(new Sensor(34345, SensorType.LED, "8878"));
        livingRoom.add(new Sensor(3677, SensorType.THERMOMETER, "33442"));

        List<Sensor> kitchen = new ArrayList<>();
        kitchen.add(new Sensor(7888, SensorType.THERMOMETER, "8888"));
        kitchen.add(new Sensor(2015, SensorType.LED, "70021"));

        List<Sensor> bathroom = new ArrayList<>();
        bathroom.add(new Sensor(7001, SensorType.THERMOMETER, "0124"));
        bathroom.add(new Sensor(1991, SensorType.LED, "12345"));

        childItems.put(groupItems.get(0), livingRoom); // Header, Child data
        childItems.put(groupItems.get(1), kitchen);
        childItems.put(groupItems.get(2), bathroom);
    }

}
