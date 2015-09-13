package com.eclubprague.iot.android.weissmydeweiss.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.SensorsListFragment;

/**
 * Created by Dat on 8.9.2015.
 */
public class SensorShareDialog extends AlertDialog.Builder {
    public interface DialogDelegate {
        void onSensorShareDialogSubmitted(String email, String uuid);
        void onSensorShareDialogSubmitted(String uuid);
    }

    private DialogDelegate delegate;
    private String uuid;

    public SensorShareDialog(DialogDelegate delegate, String uuid) {
        super(((SensorsListFragment)delegate).getActivity());
        this.delegate = delegate;
        this.uuid = uuid;

        this.setTitle("SHARE SENSOR");

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                this.getContext().LAYOUT_INFLATER_SERVICE
        );

        View view = inflater.inflate(R.layout.sensor_share_dialog,
                (ViewGroup) ((SensorsListFragment) this.delegate).getActivity().findViewById(R.id.sensor_share_dialog_layout));

        this.setView(view);

        //TextView txt_email = (TextView) view.findViewById(R.id.txt_shared_email);

        final EditText edit = (EditText) view.findViewById(R.id.edit_email);

        this.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        this.setPositiveButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SensorShareDialog.this.delegate.onSensorShareDialogSubmitted(edit.getText().toString(),
                        SensorShareDialog.this.uuid);
                dialog.dismiss();
            }
        });

        this.setNeutralButton("Make Public", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SensorShareDialog.this.delegate.onSensorShareDialogSubmitted(SensorShareDialog.this.uuid);
            }
        });

        this.create();
        this.show();

    }
}
