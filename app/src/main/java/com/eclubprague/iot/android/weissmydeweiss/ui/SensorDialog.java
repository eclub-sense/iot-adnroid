package com.eclubprague.iot.android.weissmydeweiss.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.SensorsListFragment;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.supports.SensorType;

/**
 * Created by Dat on 8.9.2015.
 */
public class SensorDialog extends AlertDialog.Builder {


    public interface DialogDelegate {
        void onSensorChartRequested(Sensor sensor);
        void onSensorShareRequested(String uuid);
    }

    private DialogDelegate delegate;
    private Sensor sensor;

    public SensorDialog(DialogDelegate delegate, Sensor sensor) {
        super(((SensorsListFragment) delegate).getActivity());
        this.delegate = delegate;
        this.sensor = sensor;

        this.setTitle(SensorType.getStringSensorType(sensor.getType()));

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                this.getContext().LAYOUT_INFLATER_SERVICE
        );

        View view = inflater.inflate(R.layout.sensor_dialog,
                (ViewGroup) ((SensorsListFragment) this.delegate).getActivity().findViewById(R.id.sensor_dialog_layout));

        this.setView(view);

        TextView txt_uuid = (TextView) view.findViewById(R.id.txt_uuid);
        txt_uuid.setText(this.sensor.getUuid());


        this.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        this.setPositiveButton("Show chart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SensorDialog.this.delegate.onSensorChartRequested(SensorDialog.this.sensor);
            }
        });

        this.setNeutralButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SensorDialog.this.delegate.onSensorShareRequested(SensorDialog.this.sensor.getUuid());
            }
        });

        this.create();
        this.show();
    }

}
