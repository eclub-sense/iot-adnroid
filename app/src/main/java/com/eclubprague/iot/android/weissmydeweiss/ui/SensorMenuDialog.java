package com.eclubprague.iot.android.weissmydeweiss.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.SensorsListFragment;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dat on 9.9.2015.
 */
public class SensorMenuDialog extends AlertDialog.Builder {


    public interface DialogDelegate {
        void onSensorChartRequested(Sensor sensor);
        void onSensorShareRequested(String uuid);
        void onSensorUnregisterRequested(String uuid);
    }

    private DialogDelegate delegate;
    private Sensor sensor;
    private AlertDialog dialog;

//    private Button btn_chart;
//    private Button btn_share;
//    private Button btn_unregister;

    GridView gridView;

    public SensorMenuDialog(DialogDelegate delegate, Sensor sensor) {
        super(((SensorsListFragment) delegate).getActivity());
        this.delegate = delegate;
        this.sensor = sensor;

        this.setTitle(SensorType.getStringSensorType(sensor.getType()));

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                this.getContext().LAYOUT_INFLATER_SERVICE
        );

//        View view = inflater.inflate(R.layout.sensor_menu,
//                (ViewGroup) ((SensorsListFragment) this.delegate).getActivity().findViewById(R.id.sensor_menu_layout));

        View view = inflater.inflate(R.layout.sensor_grid_menu,
                (ViewGroup) ((SensorsListFragment) this.delegate).getActivity().findViewById(R.id.sensor_grid_menu_layout));

        gridView = (GridView) view.findViewById(R.id.sensor_menu_grid_view);

        CustomGridViewAdapter adapter = new CustomGridViewAdapter(this.getContext(),
                android.R.layout.simple_list_item_1, GridViewItemWrapper.getGridItems());

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridViewItemWrapper item = (GridViewItemWrapper) parent.getAdapter().getItem(position);
                dialog.dismiss();
                switch (item) {
                    case CHARTS:
                        SensorMenuDialog.this.delegate.onSensorChartRequested(SensorMenuDialog.this.sensor);
                        break;
                    case SHARE:
                        SensorMenuDialog.this.delegate.onSensorShareRequested(SensorMenuDialog.this.sensor.getUuid());
                        break;
                    case UNREGISTER:
                        SensorMenuDialog.this.delegate.onSensorUnregisterRequested(SensorMenuDialog.this.sensor.getUuid());
                        break;
                }
            }
        });

        this.setView(view);

//        btn_chart = (Button) view.findViewById(R.id.btn_chart);
//        btn_chart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                SensorMenuDialog.this.delegate.onSensorChartRequested(SensorMenuDialog.this.sensor);
//            }
//        });
//
//        btn_share = (Button) view.findViewById(R.id.btn_share);
//        btn_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                SensorMenuDialog.this.delegate.onSensorShareRequested(SensorMenuDialog.this.sensor.getUuid());
//            }
//        });
//
//        btn_unregister = (Button) view.findViewById(R.id.btn_unregister);
//        btn_unregister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                SensorMenuDialog.this.delegate.onSensorUnregisterRequested(SensorMenuDialog.this.sensor.getUuid());
//            }
//        });

        dialog = this.create();
        dialog.show();
    }

}
