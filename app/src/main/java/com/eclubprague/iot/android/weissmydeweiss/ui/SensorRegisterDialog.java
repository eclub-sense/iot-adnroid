package com.eclubprague.iot.android.weissmydeweiss.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.eclubprague.iot.android.weissmydeweiss.MainActivity;
import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.SensorsListFragment;
import com.eclubprague.iot.android.weissmydeweiss.cloud.hubs.Hub;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.Sensor;
import com.eclubprague.iot.android.weissmydeweiss.cloud.sensors.VirtualSensorCreator;

/**
 * Created by Dat on 14.9.2015.
 */
public class SensorRegisterDialog extends AlertDialog.Builder {
    public interface DialogDelegate {
        void onSensorRegisterDialogSubmitted(Sensor sensor);
    }

    private DialogDelegate delegate;
    private String sensorId;
    private int sensorType;
    private String sensorSecret;

    public SensorRegisterDialog(DialogDelegate delegate, String sensorId,
                                int sensorType, String sensorSecret) {
        super((MainActivity)delegate);
        this.delegate = delegate;
        this.sensorId = sensorId;
        this.sensorType = sensorType;
        this.sensorSecret = sensorSecret;

        this.setTitle("Enter description");

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                this.getContext().LAYOUT_INFLATER_SERVICE
        );

        View view = inflater.inflate(R.layout.sensor_register_dialog,
                (ViewGroup) ((MainActivity) this.delegate).findViewById(R.id.sensor_register_dialog_layout));

        this.setView(view);

        //TextView txt_email = (TextView) view.findViewById(R.id.txt_shared_email);

        final EditText edit = (EditText) view.findViewById(R.id.edit_desc);

        this.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        this.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SensorRegisterDialog.this.delegate.onSensorRegisterDialogSubmitted(
                        //edit.getText().toString()
                        VirtualSensorCreator.
                                createSensorInstance(SensorRegisterDialog.this.sensorId,
                                                SensorRegisterDialog.this.sensorType,
                                                SensorRegisterDialog.this.sensorSecret,
                                                new Hub("00000001"), edit.getText().toString())
                );
            }
        });

        this.create();
        this.show();

    }
}
