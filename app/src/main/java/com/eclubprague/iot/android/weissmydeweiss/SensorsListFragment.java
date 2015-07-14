package com.eclubprague.iot.android.weissmydeweiss;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.SensorType;
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
        sensorList.add(new Sensor(123, SensorType.THERMOMETER, 12345));
        sensorList.add(new Sensor(34345, SensorType.LED, 8878));
        sensorList.add(new Sensor(3677, SensorType.THERMOMETER, 33442));

        // Define a new Adapter
        // Assign adapter to ListView
        listView.setAdapter(new SensorListViewAdapter(getActivity(), R.layout.item_img_twolines, sensorList));

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }
}
