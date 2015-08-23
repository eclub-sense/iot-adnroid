package com.eclubprague.iot.android.weissmydeweiss;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eclubprague.iot.android.weissmydeweiss.cloud.User;
import com.eclubprague.iot.android.weissmydeweiss.tasks.LoginTask;

/**
 * Created by Dat on 13.8.2015.
 */
public class LoginActivity extends Activity implements LoginTask.TaskDelegate {

    private EditText un,pw;
    private Button b_login;
    private Button b_register;

    private String username = "DAT";
    private String password = "567";

    //-------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        un=(EditText)findViewById(R.id.et_un);
        pw=(EditText)findViewById(R.id.et_pw);
        b_login =(Button)findViewById(R.id.btn_login);
        b_register =(Button)findViewById(R.id.btn_register);

        b_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                username = un.getText().toString();
                password = pw.getText().toString();

                if(username.length() < 3 || password.length() < 3) {
                    Toast.makeText(LoginActivity.this, "Credentials min. lenght: 3", Toast.LENGTH_SHORT).show();
                    return;
                }

                //new LoginTask(LoginActivity.this).execute(new User(username, password));
                startApp();
            }
        });

        b_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Not yet supported", Toast.LENGTH_SHORT).show();
            }
        });
    }






    //-------------------------------------------------------------------
    //Task Delegates Overrides
    //-------------------------------------------------------------------

    @Override
    public void onLoginCompleted(boolean success) {
        if(!success) {
            Toast.makeText(this, "No such account", Toast.LENGTH_SHORT).show();
            return;
        }
        startApp();
    }

    private void startApp() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        startActivity(intent);
        this.finish();
    }
}