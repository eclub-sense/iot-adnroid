package com.eclubprague.iot.android.weissmydeweiss.ui.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.eclubprague.iot.android.weissmydeweiss.LoginActivityTest;
import com.eclubprague.iot.android.weissmydeweiss.R;
import com.eclubprague.iot.android.weissmydeweiss.cloud.TokenWrapper;

/**
 * Created by Dat on 31.8.2015.
 */
public class UserRegistrationDialog extends AlertDialog.Builder {

    public interface TaskDelegate {
        public void onUserRegistrationDialogSubmitted(String password);
    }

    private TaskDelegate delegate;

    public UserRegistrationDialog(TaskDelegate delegate, String email) {
        super((LoginActivityTest)delegate);
        this.delegate = delegate;

        this.setTitle("REGISTER");

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
                this.getContext().LAYOUT_INFLATER_SERVICE
        );

        View view = inflater.inflate(R.layout.user_register_popup,
                (ViewGroup) ((LoginActivityTest)this.delegate).findViewById(R.id.user_register_popup_layout));

        this.setView(view);

        TextView txt_email = (TextView) view.findViewById(R.id.txt_email);
        txt_email.setText(email);

        final EditText edit_password = (EditText) view.findViewById(R.id.edit_password);

        this.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        this.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserRegistrationDialog.this.delegate.onUserRegistrationDialogSubmitted(edit_password.getText().toString());
                dialog.dismiss();
            }
        });

        this.create();
        this.show();

    }

}
