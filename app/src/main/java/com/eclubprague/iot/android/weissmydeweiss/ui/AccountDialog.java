package com.eclubprague.iot.android.weissmydeweiss.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.eclubprague.iot.android.weissmydeweiss.MainActivity;
import com.eclubprague.iot.android.weissmydeweiss.R;

import java.lang.ref.WeakReference;

/**
 * Created by Dat on 10.8.2015.
 */
public class AccountDialog extends AlertDialog.Builder {

    TextView txt_username;
    TextView txt_password;
    EditText edit_username;
    EditText edit_password;
    View view;
    WeakReference<MainActivity> activityRef;
    WeakReference<MainActivity.Account> accountRef;

    public AccountDialog(MainActivity activity, WeakReference<MainActivity.Account> accountRef) {
        super(activity);
        activityRef = new WeakReference<>(activity);
        activityRef.get().stopTimerTask();
        this.accountRef = accountRef;



        this.setTitle("ACCOUNT");

        LayoutInflater inflater = (LayoutInflater) activityRef.get().getSystemService(
                activityRef.get().LAYOUT_INFLATER_SERVICE
        );

        view = inflater.inflate(R.layout.account_layout,
                (ViewGroup) activityRef.get().findViewById(R.id.account_relative_layout));

        txt_username = (TextView) view.findViewById(R.id.txt_username);
        txt_password = (TextView) view.findViewById(R.id.txt_password);
        edit_username = (EditText) view.findViewById(R.id.edit_username);
        edit_password = (EditText) view.findViewById(R.id.edit_password);

        this.setView(view);

        this.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        this.setPositiveButton("Log in", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AccountDialog.this.
                        accountRef.get().
                        setAccount(edit_username.getText().toString(), edit_password.getText().toString());
                AccountDialog.this.activityRef.get().startTimer();
                dialog.dismiss();
            }
        });


        this.create();
        this.show();
    }

}
