package com.eclubprague.iot.android.weissmydeweiss;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.SensorType;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.VirtualSensorCreator;
import com.eclubprague.iot.android.weissmydeweiss.ui.SensorListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dat on 14.7.2015.
 */
public class SensorsListFragment extends Fragment {

    static ListView listView;

    public static SensorsListFragment newInstance() {
        SensorsListFragment fragment = new SensorsListFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sensors_list, container, false);

        listView = (ListView) rootView.findViewById(R.id.sensors_list);

        // Defined Array values to show in ListView
        List<Sensor> sensorList = new ArrayList<>();
        sensorList.add(VirtualSensorCreator.createSensorInstance(123, SensorType.THERMOMETER, "12345"));
        sensorList.add(VirtualSensorCreator.createSensorInstance(34345, SensorType.LED, "8878"));
        sensorList.add(VirtualSensorCreator.createSensorInstance(3677, SensorType.THERMOMETER, "33442"));

        // Define a new Adapter
        // Assign adapter to ListView
        SensorListViewAdapter adapter = new SensorListViewAdapter(getActivity(), R.layout.item_img_twolines, sensorList);
        listView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }
}
