package com.eclubprague.iot.android.weissmydeweiss;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

/**
 * Created by Dat on 21.7.2015.
 */
public class BLEDevicesListFragment extends Fragment {

    View rootView;

    private BluetoothAdapter mBluetoothAdapter;
    private Button scanButton;

    static ListView listView;

    private List<String> devices = new ArrayList<>();

    private int REQUEST_ENABLE_BT = 1;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(BLEDevicesListFragment.this.getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, devices);
                listView.setAdapter(adapter);

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devices.add(device.getName());
                //showToast("Found device " + device.getName());
            }
        }
    };

    public static BLEDevicesListFragment newInstance() {
        BLEDevicesListFragment fragment = new BLEDevicesListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_ble_devices_list, container, false);

        listView = (ListView) rootView.findViewById(R.id.devices_list);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast t = Toast.makeText(rootView.getContext(), "No Bluetooth adapter available", Toast.LENGTH_SHORT);
            t.show();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        scanButton = (Button) rootView.findViewById(R.id.scan_devices_button);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentFilter filter = new IntentFilter();

                filter.addAction(BluetoothDevice.ACTION_FOUND);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

                BLEDevicesListFragment.this.getActivity().registerReceiver(mReceiver, filter);
                mBluetoothAdapter.startDiscovery();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(4);
    }

    @Override
    public void onDetach() {
        try {
            BLEDevicesListFragment.this.getActivity().unregisterReceiver(mReceiver);
        } catch(Exception e) {

        }
        super.onDetach();
    }
}
