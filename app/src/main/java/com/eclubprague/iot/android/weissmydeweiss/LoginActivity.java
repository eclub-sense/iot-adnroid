package com.eclubprague.iot.android.weissmydeweiss;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eclubprague.iot.android.weissmydeweiss.cloud.User;
import com.eclubprague.iot.android.weissmydeweiss.tasks.LoginTask;
import com.google.identitytoolkit.GitkitClient;
import com.google.identitytoolkit.GitkitUser;
import com.google.identitytoolkit.IdToken;

/**
 * Created by Dat on 13.8.2015.
 */
public class LoginActivity extends Activity /*implements LoginTask.TaskDelegate*/ {

    private EditText un,pw;
    private Button b_login;
    private Button b_register;

    private String username = "DAT";
    private String password = "567";

    private GitkitClient client;

    private String token = "";

    //-------------------------------------------------------------------------


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);

        /*un=(EditText)findViewById(R.id.et_un);
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

                new LoginTask(LoginActivity.this).execute(new User(username, password));
            }
        });

        b_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Not yet supported", Toast.LENGTH_SHORT).show();
            }
        });*/

        client = GitkitClient.newBuilder(this, new GitkitClient.SignInCallbacks() {
            // Implement the onSignIn method of GitkitClient.SignInCallbacks interface.
            // This method is called when the sign-in process succeeds. A Gitkit IdToken and the signed
            // in account information are passed to the callback.
            @Override
            public void onSignIn(IdToken idToken, GitkitUser user) {
                Log.i("LoginActivity", "Logged in as " + user.getDisplayName());
                token = idToken.getTokenString();
                Log.i("LoginActivity", "Token=" + idToken.getTokenString());

                //showProfilePage(idToken, user);
                // Now use the idToken to create a session for your user.
                // To do so, you should exchange the idToken for either a Session Token or Cookie
                // from your server.
                // Finally, save the Session Token or Cookie to maintain your user's session.
                startApplication();
            }

            // Implement the onSignInFailed method of GitkitClient.SignInCallbacks interface.
            // This method is called when the sign-in process fails.
            @Override
            public void onSignInFailed() {
                Toast.makeText(LoginActivity.this, "No such account", Toast.LENGTH_SHORT).show();
                return;
            }
        }).build();
    }


    public void signInWithGit(View view) {
        if (view.getId() == R.id.sign_in) {
            client.startSignIn();
        }
    }

    // Step 3: Override the onActivityResult method.
    // When a result is returned to this activity, it is maybe intended for GitkitClient. Call
    // GitkitClient.handleActivityResult to check the result. If the result is for GitkitClient,
    // the method returns true to indicate the result has been consumed.
    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (!client.handleActivityResult(requestCode, resultCode, intent)) {
            super.onActivityResult(requestCode, resultCode, intent);
        }


    }

    // Step 4: Override the onNewIntent method.
    // When the app is invoked with an intent, it is possible that the intent is for GitkitClient.
    // Call GitkitClient.handleIntent to check it. If the intent is for GitkitClient, the method
    // returns true to indicate the intent has been consumed.

    @Override
    protected void onNewIntent(Intent intent) {
        if (!client.handleIntent(intent)) {
            super.onNewIntent(intent);
        }
    }



    //-------------------------------------------------------------------
    //Task Delegates Overrides
    //-------------------------------------------------------------------

    /*@Override
    public void onLoginCompleted(boolean success) {
        if(!success) {
            Toast.makeText(this, "No such account", Toast.LENGTH_SHORT).show();
            return;
        }
        startApplication();
    }*/


    public void startApplication() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        intent.putExtra("token", token);
        startActivity(intent);
        this.finish();
    }

}