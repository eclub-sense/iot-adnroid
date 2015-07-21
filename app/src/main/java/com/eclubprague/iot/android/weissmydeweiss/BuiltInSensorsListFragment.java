package com.eclubprague.iot.android.weissmydeweiss;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.eclubprague.iot.android.weissmydeweiss.tasks.GetSensorTask;
import com.eclubprague.iot.android.weissmydeweiss.ui.BuiltInSensorInfoDialog;
import com.eclubprague.iot.android.weissmydeweiss.ui.BuiltInSensorsListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dat on 20.7.2015.
 */
public class BuiltInSensorsListFragment extends Fragment {

    static ListView listView;
    private SensorManager mSensorManager;
    private Context context;

    public static BuiltInSensorsListFragment newInstance() {
        BuiltInSensorsListFragment fragment = new BuiltInSensorsListFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_hubs_list, container, false);

        listView = (ListView) rootView.findViewById(R.id.hubs_list);

        // Defined Array values to show in ListView
        mSensorManager = (SensorManager) this.getActivity().getSystemService(this.getActivity().SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(	android.hardware.Sensor.TYPE_ALL);

//        List<String>values = new ArrayList<>();
//        for(int i = 0; i < deviceSensors.size(); i++) {
//            values.add(deviceSensors.get(i).getName());
//        }

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
//                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        BuiltInSensorsListViewAdapter adapter = new BuiltInSensorsListViewAdapter(this.getActivity(),
                android.R.layout.simple_list_item_1, deviceSensors);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new BuiltInSensorInfoDialog(rootView.getContext(), (Sensor) parent.getAdapter().getItem(position));
            }
        });

        context = rootView.getContext();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(3);
    }
}

